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

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.concurrent.NotThreadSafe;

import com.jcloud.jss.JingdongStorageService;
import com.jcloud.jss.domain.StorageObject;

/**
 * A stream for reading a file from JSS. This input stream returns 0 when calling read with an empty
 * buffer.
 */
@NotThreadSafe
public class JSSInputStream extends InputStream {
  /** Bucket name of the Alluxio JSS bucket. */
  private final String mBucketName;

  /** Key of the file in JSS to read. */
  private final String mKey;

  /** The JetS3t client for JSS operations. */
  private final JingdongStorageService mClient;

  /** The storage object that will be updated on each large skip. */
  private StorageObject mObject;

  /** The storage object input stream */
  private InputStream mObjectInputStream;

  /** Position of the stream. */
  private long mPos = 0;

  /**
   * Creates a new instance of {@link JSSInputStream}.
   *
   * @param bucketName the name of the bucket
   * @param key the key of the file
   * @param client the client for JSS
   */
  JSSInputStream(String bucketName, String key, JingdongStorageService client) {
    mBucketName = bucketName;
    mKey = key;
    mClient = client;
    mObject = mClient.bucket(mBucketName).object(mKey).get();
    mObjectInputStream = mObject.getInputStream();
  }

  @Override
  public void close() throws IOException {
    mObjectInputStream.close();
    mObject.close();
  }

  @Override
  public int read() throws IOException {
    int ret = mObjectInputStream.read();
    if (ret != -1) {
      mPos ++;
    }
    return ret;
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    int ret = mObjectInputStream.read(b, off, len);
    if (ret != -1) {
      mPos += ret;
    }
    return ret;
  }

  /**
   * This method leverages the ability to open a stream from JSS from a given offset. When the
   * underlying stream has fewer bytes buffered than the skip request, the stream is closed, and a
   * new stream is opened starting at the requested offset.
   *
   * @param n number of bytes to skip
   * @return the number of bytes skipped
   * @throws IOException if an error occurs when requesting from JSS
   */
  @Override
  public long skip(long n) throws IOException {
    if (mObjectInputStream.available() >= n) {
      return mObjectInputStream.skip(n);
    }

    // The number of bytes to skip is possibly large, open a new stream from JSS.
    mObjectInputStream.close();
    mPos += n;
    try {
      mObject = mClient.bucket(mBucketName).object(mKey).range(mPos).get();
      mObjectInputStream = mObject.getInputStream();
    } catch (Exception e) {
      throw new IOException(e);
    }
    return n;
  }
}
