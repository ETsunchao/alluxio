/*
 * The Alluxio Open Foundation licenses this work under the Apache License, version 2.0 (the
 * “License”). You may not use this work except in compliance with the License, which is available
 * at www.apache.org/licenses/LICENSE-2.0
 *
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied, as more fully set forth in the License.
 *
 * See the NOTICE file distributed with this work for information regarding copyright ownership.
 */

package alluxio.underfs.jss;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.jcloud.jss.domain.ObjectSummary;
import com.jcloud.jss.domain.StorageError;
import com.jcloud.jss.domain.StorageObject;
import com.jcloud.jss.exception.StorageClientException;

import alluxio.AlluxioURI;
import alluxio.Configuration;
import alluxio.Constants;
import alluxio.underfs.UnderFileSystem;
import alluxio.util.io.PathUtils;

/**
 * JFS {@link UnderFileSystem} implementation.
 */
@ThreadSafe
public class JSSUnderFileSystem extends UnderFileSystem {
  private static final Logger LOG = LoggerFactory.getLogger(JSSUnderFileSystem.class);
  /**
   * Suffix for an empty file to flag it as a directory.
   */
  private static final String FOLDER_SUFFIX = "_..folder..";
  /**
   * Value used to indicate folder structure in JSS.
   */
  private static final String PATH_SEPARATOR = "/";
  private static Object sJSSOperatorLockObj = new Object();
  /**
   * Because JSSClient is thread-safe, so singleton. On another hand, this way can avoid the
   * excessive number of connections. Some servers are limited number of connections to prevent ddos
   * attacks.
   */
  private static JSSServiceOperator sJSSOperator = null;
  private final int DEL_NO_SUCH_KEY_ERRCODE = 404;
  /**
   * Bucket name of user's configured Alluxio bucket.
   */
  private final String mBucketName;

  /**
   * Prefix of the bucket, for example jss://my-bucket-name/ .
   */
  private final String mBucketPrefix;

  protected JSSUnderFileSystem(AlluxioURI uri, Configuration conf) {
    super(uri, conf);
    Preconditions.checkArgument(conf.containsKey(Constants.JSS_ACCESS_KEY),
        "Property " + Constants.JSS_ACCESS_KEY + " is required to connect to JSS");
    Preconditions.checkArgument(conf.containsKey(Constants.JSS_SECRET_KEY),
        "Property " + Constants.JSS_SECRET_KEY + " is required to connect to JSS");
    Preconditions.checkArgument(conf.containsKey(Constants.JSS_ENDPOINT),
        "Property " + Constants.JSS_ENDPOINT + " is required to connect to JSS");

    mBucketName = uri.getHost();
    mBucketPrefix = Constants.HEADER_JSS + mBucketName + PATH_SEPARATOR;
    if (null == sJSSOperator) {
      synchronized (sJSSOperatorLockObj) {
        if (null == sJSSOperator) {
          sJSSOperator = new JSSServiceOperator(conf);
        }
      }
    }

    sJSSOperator.addRef();
  }

  @Override
  public UnderFSType getUnderFSType() {
    return UnderFSType.JSS;
  }

  @Override
  public void connectFromMaster(Configuration conf, String hostname) throws IOException {
    // Authentication is taken care of in the constructor
  }

  @Override
  public void connectFromWorker(Configuration conf, String hostname) throws IOException {
    // Authentication is taken care of in the constructor
  }

  @Override
  public void close() throws IOException {
    sJSSOperator.release();
  }

  @Override
  public OutputStream create(String path) throws IOException {
    path = toURIPath(path);
    if (mkdirs(getParentKey(path), true)) {
      return new JSSOutputStream(mBucketName, stripPrefixIfPresent(path),
          sJSSOperator.getJSSClient());
    }
    return null;
  }

  @Override
  public OutputStream create(String path, int blockSizeByte) throws IOException {
    LOG.warn("blocksize is not supported with JSSUnderFileSystem. Block size will be ignored.");
    return create(path);
  }

  @Override
  public OutputStream create(String path, short replication, int blockSizeByte) throws IOException {
    LOG.warn(
        "blocksize and replication is not supported with JSSUnderFileSystem. Will be ignored.");
    return create(path);
  }

  @Override
  public boolean delete(String path, boolean recursive) throws IOException {
    if (!recursive) {
      if (isFolder(path) && listInternal(path, false).length != 0) {
        LOG.error("Unable to delete " + path + " because it is a non empty directory. Specify "
            + "recursive as true in order to delete non empty directories.");
        return false;
      }
      return deleteInternal(path);
    }
    // Get all relevant files
    String[] pathsToDelete = listInternal(path, true);
    for (String pathToDelete : pathsToDelete) {
      // If we fail to deleteInternal one file, stop
      if (!deleteInternal(PathUtils.concatPath(path, pathToDelete))) {
        LOG.error("Failed to delete path {}, aborting delete.", pathToDelete);
        return false;
      }
    }
    return deleteInternal(path);
  }

  @Override
  public boolean exists(String path) throws IOException {
    // Root path always exists.
    return isRoot(path) || existInternal(path);
  }

  /**
   * Gets the block size in bytes. There is no concept of a block in JSS and the maximum size of one
   * put is 1TB, and the max size of a multi-part upload is no limit. This method defaults to the
   * default user block size in Alluxio.
   *
   * @param path the file name
   * @return the default Alluxio user block size
   * @throws IOException this implementation will not throw this exception, but subclasses may
   */
  @Override
  public long getBlockSizeByte(String path) throws IOException {
    return mConfiguration.getBytes(Constants.USER_BLOCK_SIZE_BYTES_DEFAULT);
  }

  // Not supported
  @Override
  public Object getConf() {
    LOG.warn("getConf is not supported when using JSSUnderFileSystem, returning null.");
    return null;
  }

  // Not supported
  @Override
  public void setConf(Object conf) {}

  // Not supported
  @Override
  public List<String> getFileLocations(String path) throws IOException {
    LOG.warn("getFileLocations is not supported when using JSSUnderFileSystem, returning null.");
    return null;
  }

  // Not supported
  @Override
  public List<String> getFileLocations(String path, long offset) throws IOException {
    LOG.warn("getFileLocations is not supported when using JSSUnderFileSystem, returning null.");
    return null;
  }

  @Override
  public long getFileSize(String path) throws IOException {
    StorageObject details = getObjectDetails(path);
    if (details != null) {
      return details.getContentLength();
    } else {
      throw new FileNotFoundException(path);
    }
  }

  @Override
  public long getModificationTimeMs(String path) throws IOException {
    StorageObject details = getObjectDetails(path);
    if (details != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
      try {
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = sdf.parse(details.getLastModified());
        return date.getTime();
      } catch (ParseException e) {
        throw new IOException(e);
      }
    } else {
      throw new FileNotFoundException(path);
    }
  }

  // This call is currently only used for the web ui, where a negative value implies unknown.
  @Override
  public long getSpace(String path, SpaceType type) throws IOException {
    return -1;
  }

  @Override
  public boolean isFile(String path) throws IOException {
    return exists(path) && !isFolder(path);
  }

  @Override
  public String[] list(String path) throws IOException {
    // if the path not exists, or it is a file, then should return null
    if (!exists(path) || isFile(path)) {
      return null;
    }
    // Non recursive list
    path = PathUtils.normalizePath(path, PATH_SEPARATOR);
    return listInternal(path, false);
  }

  public boolean mkdirs(String path, boolean createParent) throws IOException {
    if (path == null) {
      return false;
    }
    if (isFolder(path)) {
      return true;
    }
    if (exists(path)) {
      LOG.error("Cannot create directory {} because it is already a file.", path);
      return false;
    }
    if (!createParent) {
      if (parentExists(path)) {
        // Parent directory exists
        return mkdirsInternal(path);
      } else {
        LOG.error("Cannot create directory {} because parent does not exist", path);
        return false;
      }
    }
    // Parent directories should be created
    if (parentExists(path)) {
      // Parent directory exists
      return mkdirsInternal(path);
    } else {
      String parentKey = getParentKey(path);
      // Recursively make the parent folders
      return mkdirs(parentKey, true) && mkdirsInternal(path);
    }
  }

  @Override
  public InputStream open(String path) throws IOException {
    try {
      path = stripPrefixIfPresent(path);
      return new JSSInputStream(mBucketName, path, sJSSOperator.getJSSClient());
    } catch (Exception e) {
      LOG.error("Failed to open file: {}", path, e);
      return null;
    }
  }

  @Override
  public boolean rename(String src, String dst) throws IOException {
    if (!exists(src)) {
      LOG.error("Unable to rename {} to {} because source does not exist.", src, dst);
      return false;
    }
    if (exists(dst)) {
      LOG.error("Unable to rename {} to {} because destination already exists.", src, dst);
      return false;
    }
    // Source exists and destination does not exist
    if (isFolder(src)) {
      // Rename the source folder first
      if (!copy(convertToFolderName(src), convertToFolderName(dst))) {
        return false;
      }
      // Rename each child in the src folder to destination/child
      String[] children = list(src);
      for (String child : children) {
        if (!rename(PathUtils.concatPath(src, child), PathUtils.concatPath(dst, child))) {
          return false;
        }
      }
      // Delete src and everything under src
      return delete(src, true);
    }
    // Source is a file and Destination does not exist
    return copy(src, dst) && deleteInternal(src);
  }

  // Not supported
  @Override
  public void setOwner(String path, String user, String group) {}

  // Not supported
  @Override
  public void setPermission(String path, String posixPerm) throws IOException {}

  /**
   * Copies an object to another key.
   *
   * @param src the source key to copy
   * @param dst the destination key to copy to
   * @return true if the operation was successful, false otherwise
   */
  private boolean copy(String src, String dst) {
    try {
      src = stripPrefixIfPresent(src);
      dst = stripPrefixIfPresent(dst);
      LOG.info("Copying {} to {}", src, dst);
      StorageObject details = getObjectDetails(src);
      if ((null != details) && (0 != details.getContentLength())) {
        sJSSOperator.getJSSClient().bucket(mBucketName).object(dst).copyFrom(mBucketName, src)
            .copy();
      } else {
        sJSSOperator.getJSSClient().bucket(mBucketName).object(dst).put();
      }

      return true;
    } catch (Exception e) {
      LOG.error("Failed to rename file {} to {}", src, dst, e);
      return false;
    }
  }

  /**
   * Internal function to delete a key in JSS.
   *
   * @param key the key to delete
   * @return true if successful, false if an exception is thrown
   */
  private boolean deleteInternal(String key) {
    try {
      String delPath;
      if (isFolder(key)) {
        delPath = convertToFolderName(stripPrefixIfPresent(key));
      } else {
        delPath = stripPrefixIfPresent(key);
      }
      sJSSOperator.getJSSClient().bucket(mBucketName).object(delPath).delete();
    } catch (StorageClientException sce) {
      StorageError se = sce.getError();
      if (DEL_NO_SUCH_KEY_ERRCODE == se.getHttpStatusCode()) {
        return true;
      }
      LOG.error("Failed to delete {}, {}", key, se.getMessage());
      return false;
    } catch (Exception e) {
      LOG.error("Failed to delete {}", key, e);
      return false;
    }
    return true;
  }

  /**
   * @param path the key to get the object details of
   * @return whether the path is exist or not
   */
  private boolean existInternal(String path) {
    try {
      if (isFolder(path)) {
        return true;
      } else {
        return sJSSOperator.getJSSClient().bucket(mBucketName).object(stripPrefixIfPresent(path))
            .exist();
      }
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * @param key the key to get the object details of
   * @return {@link StorageObject} of the key, or null if the key does not exist
   */
  private StorageObject getObjectDetails(String key) {
    try {
      String accessKey;
      if (isFolder(key)) {
        accessKey = convertToFolderName(stripPrefixIfPresent(key));

      } else {
        accessKey = stripPrefixIfPresent(key);
      }
      return sJSSOperator.getJSSClient().bucket(mBucketName).object(accessKey).head();
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Determines if the key represents a folder. If false is returned, it is not guaranteed that the
   * path exists.
   *
   * @param key the key to check
   * @return true if the key exists and is a directory
   */
  private boolean isFolder(String key) {
    // Root is a folder
    if (isRoot(key)) {
      return true;
    }

    String keyAsFolder = convertToFolderName(stripPrefixIfPresent(key));
    return sJSSOperator.getJSSClient().bucket(mBucketName).object(keyAsFolder).exist();
  }

  /**
   * Lists the files in the given path, the paths will be their logical names and not contain the
   * folder suffix.
   *
   * @param path the key to list
   * @param recursive if true will list children directories as well
   * @return an array of the file and folder names in this directory
   * @throws IOException if an I/O error occurs
   */
  private String[] listInternal(String path, boolean recursive) throws IOException {
    try {
      path = stripPrefixIfPresent(path);
      path = PathUtils.normalizePath(path, PATH_SEPARATOR);
      path = path.equals(PATH_SEPARATOR) ? "" : path;
      // Gets all the objects under the path, because we have no idea if there are non Alluxio
      // managed "directories"
      List<ObjectSummary> objs = sJSSOperator.getJSSClient().bucket(mBucketName).prefix(path)
          .listObject().getObjectSummaries();
      if (recursive) {
        List<String> ret = new ArrayList<>();
        for (ObjectSummary obj : objs) {
          // Remove parent portion of the key
          String child = getChildName(obj.getKey(), path);
          // Prune the special folder suffix
          child = stripFolderSuffixIfPresent(child);
          // Only add if the path is not empty (removes results equal to the path)
          if (!child.isEmpty()) {
            ret.add(child);
          }
        }
        return ret.toArray(new String[ret.size()]);
      }
      // Non recursive list
      Set<String> children = new HashSet<>();
      for (ObjectSummary obj : objs) {
        // Remove parent portion of the key
        String child = getChildName(obj.getKey(), path);
        // Remove any portion after the path delimiter
        int childNameIndex = child.indexOf(PATH_SEPARATOR);
        child = childNameIndex != -1 ? child.substring(0, childNameIndex) : child;
        // Prune the special folder suffix
        child = stripFolderSuffixIfPresent(child);
        // Add to the set of children, the set will deduplicate.
        if (!child.isEmpty()) {
          children.add(child);
        }
      }
      return children.toArray(new String[children.size()]);
    } catch (Exception e) {
      LOG.error("Failed to list path {}", path, e);
      return null;
    }
  }

  /**
   * Appends the directory suffix to the key.
   *
   * @param key the key to convert
   * @return key as a directory path
   */
  private String convertToFolderName(String key) {
    // Strips the slash if it is the end of the key string. This is because the slash at
    // the end of the string is not part of the Object key in JSS.
    if (key.endsWith(PATH_SEPARATOR)) {
      key = key.substring(0, key.length() - PATH_SEPARATOR.length());
    }
    return key + FOLDER_SUFFIX;
  }

  /**
   * Gets the child name based on the parent name.
   *
   * @param child the key of the child
   * @param parent the key of the parent
   * @return the child key with the parent prefix removed, null if the parent prefix is invalid
   */
  private String getChildName(String child, String parent) {
    if (child.startsWith(parent)) {
      return child.substring(parent.length());
    }
    LOG.error("Attempted to get childname with an invalid parent argument. Parent: {} Child: {}",
        parent, child);
    return null;
  }

  /**
   * @param key the key to get the parent of
   * @return the parent key, or null if the parent does not exist
   */
  private String getParentKey(String key) {
    // Root does not have a parent.
    if (isRoot(key)) {
      return null;
    }
    int separatorIndex = key.lastIndexOf(PATH_SEPARATOR);
    if (separatorIndex < 0) {
      return null;
    }
    return key.substring(0, separatorIndex);
  }

  /**
   * Creates a directory flagged file with the key and folder suffix.
   *
   * @param key the key to create a folder
   * @return true if the operation was successful, false otherwise
   */
  private boolean mkdirsInternal(String key) {
    try {
      String keyAsFolder = convertToFolderName(stripPrefixIfPresent(key));
      sJSSOperator.getJSSClient().bucket(mBucketName).object(keyAsFolder).put();
      return true;
    } catch (Exception e) {
      LOG.error("Failed to create directory: {}", key, e);
      return false;
    }
  }

  /**
   * Treating JSS as a file system, checks if the parent directory exists.
   *
   * @param key the key to check
   * @return true if the parent exists or if the key is root, false otherwise
   */
  private boolean parentExists(String key) {
    // Assume root always has a parent
    if (isRoot(key)) {
      return true;
    }
    String parentKey = getParentKey(key);
    return parentKey != null && isFolder(parentKey);
  }

  /**
   * If the path passed to this filesystem is not an URI path, then add jss prefix.
   *
   * @param path the path to process
   * @return the path with jss prefix
   */
  private String toURIPath(String path) {
    if (!path.startsWith(Constants.HEADER_JSS)) {
      path = Constants.HEADER_JSS + mBucketName + path;
    }
    return path;
  }

  /**
   * Checks if the key is the root.
   *
   * @param key the key to check
   * @return true if the key is the root, false otherwise
   */
  private boolean isRoot(String key) {
    return PathUtils.normalizePath(key, PATH_SEPARATOR)
        .equals(PathUtils.normalizePath(Constants.HEADER_JSS + mBucketName, PATH_SEPARATOR));
  }

  /**
   * Strips the folder suffix if it exists. This is a string manipulation utility and does not
   * guarantee the existence of the folder. This method will leave keys without a suffix unaltered.
   *
   * @param key the key to strip the suffix from
   * @return the key with the suffix removed, or the key unaltered if the suffix is not present
   */
  private String stripFolderSuffixIfPresent(String key) {
    if (key.endsWith(FOLDER_SUFFIX)) {
      return key.substring(0, key.length() - FOLDER_SUFFIX.length());
    }
    return key;
  }

  /**
   * Strips the JSS bucket prefix or the preceding path separator from the key if it is present. For
   * example, for input key jss://my-bucket-name/my-path/file, the output would be my-path/file. If
   * key is an absolute path like /my-path/file, the output would be my-path/file. This method will
   * leave keys without a prefix unaltered, ie. my-path/file returns my-path/file.
   *
   * @param key the key to strip
   * @return the key without the jss bucket prefix
   */
  private String stripPrefixIfPresent(String key) {
    if (key.startsWith(mBucketPrefix)) {
      return key.substring(mBucketPrefix.length());
    }
    if (key.startsWith(PATH_SEPARATOR)) {
      return key.substring(PATH_SEPARATOR.length());
    }
    return key;
  }
}
