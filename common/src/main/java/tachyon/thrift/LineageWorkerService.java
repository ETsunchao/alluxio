/**
 * Autogenerated by Thrift Compiler (0.9.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package tachyon.thrift;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.2)", date = "2015-9-16")
public class LineageWorkerService {

  public interface Iface {

    public void persistFile(List<Long> blockIds, long fileId, String filePath) throws org.apache.thrift.TException;

  }

  public interface AsyncIface {

    public void persistFile(List<Long> blockIds, long fileId, String filePath, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException;

  }

  public static class Client extends org.apache.thrift.TServiceClient implements Iface {
    public static class Factory implements org.apache.thrift.TServiceClientFactory<Client> {
      public Factory() {}
      public Client getClient(org.apache.thrift.protocol.TProtocol prot) {
        return new Client(prot);
      }
      public Client getClient(org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot) {
        return new Client(iprot, oprot);
      }
    }

    public Client(org.apache.thrift.protocol.TProtocol prot)
    {
      super(prot, prot);
    }

    public Client(org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot) {
      super(iprot, oprot);
    }

    public void persistFile(List<Long> blockIds, long fileId, String filePath) throws org.apache.thrift.TException
    {
      send_persistFile(blockIds, fileId, filePath);
      recv_persistFile();
    }

    public void send_persistFile(List<Long> blockIds, long fileId, String filePath) throws org.apache.thrift.TException
    {
      persistFile_args args = new persistFile_args();
      args.setBlockIds(blockIds);
      args.setFileId(fileId);
      args.setFilePath(filePath);
      sendBase("persistFile", args);
    }

    public void recv_persistFile() throws org.apache.thrift.TException
    {
      persistFile_result result = new persistFile_result();
      receiveBase(result, "persistFile");
      return;
    }

  }
  public static class AsyncClient extends org.apache.thrift.async.TAsyncClient implements AsyncIface {
    public static class Factory implements org.apache.thrift.async.TAsyncClientFactory<AsyncClient> {
      private org.apache.thrift.async.TAsyncClientManager clientManager;
      private org.apache.thrift.protocol.TProtocolFactory protocolFactory;
      public Factory(org.apache.thrift.async.TAsyncClientManager clientManager, org.apache.thrift.protocol.TProtocolFactory protocolFactory) {
        this.clientManager = clientManager;
        this.protocolFactory = protocolFactory;
      }
      public AsyncClient getAsyncClient(org.apache.thrift.transport.TNonblockingTransport transport) {
        return new AsyncClient(protocolFactory, clientManager, transport);
      }
    }

    public AsyncClient(org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.async.TAsyncClientManager clientManager, org.apache.thrift.transport.TNonblockingTransport transport) {
      super(protocolFactory, clientManager, transport);
    }

    public void persistFile(List<Long> blockIds, long fileId, String filePath, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException {
      checkReady();
      persistFile_call method_call = new persistFile_call(blockIds, fileId, filePath, resultHandler, this, ___protocolFactory, ___transport);
      this.___currentMethod = method_call;
      ___manager.call(method_call);
    }

    public static class persistFile_call extends org.apache.thrift.async.TAsyncMethodCall {
      private List<Long> blockIds;
      private long fileId;
      private String filePath;
      public persistFile_call(List<Long> blockIds, long fileId, String filePath, org.apache.thrift.async.AsyncMethodCallback resultHandler, org.apache.thrift.async.TAsyncClient client, org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
        super(client, protocolFactory, transport, resultHandler, false);
        this.blockIds = blockIds;
        this.fileId = fileId;
        this.filePath = filePath;
      }

      public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
        prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("persistFile", org.apache.thrift.protocol.TMessageType.CALL, 0));
        persistFile_args args = new persistFile_args();
        args.setBlockIds(blockIds);
        args.setFileId(fileId);
        args.setFilePath(filePath);
        args.write(prot);
        prot.writeMessageEnd();
      }

      public void getResult() throws org.apache.thrift.TException {
        if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {
          throw new IllegalStateException("Method call not finished!");
        }
        org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());
        org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
        (new Client(prot)).recv_persistFile();
      }
    }

  }

  public static class Processor<I extends Iface> extends org.apache.thrift.TBaseProcessor<I> implements org.apache.thrift.TProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class.getName());
    public Processor(I iface) {
      super(iface, getProcessMap(new HashMap<String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>>()));
    }

    protected Processor(I iface, Map<String,  org.apache.thrift.ProcessFunction<I, ? extends  org.apache.thrift.TBase>> processMap) {
      super(iface, getProcessMap(processMap));
    }

    private static <I extends Iface> Map<String,  org.apache.thrift.ProcessFunction<I, ? extends  org.apache.thrift.TBase>> getProcessMap(Map<String,  org.apache.thrift.ProcessFunction<I, ? extends  org.apache.thrift.TBase>> processMap) {
      processMap.put("persistFile", new persistFile());
      return processMap;
    }

    public static class persistFile<I extends Iface> extends org.apache.thrift.ProcessFunction<I, persistFile_args> {
      public persistFile() {
        super("persistFile");
      }

      public persistFile_args getEmptyArgsInstance() {
        return new persistFile_args();
      }

      protected boolean isOneway() {
        return false;
      }

      public persistFile_result getResult(I iface, persistFile_args args) throws org.apache.thrift.TException {
        persistFile_result result = new persistFile_result();
        iface.persistFile(args.blockIds, args.fileId, args.filePath);
        return result;
      }
    }

  }

  public static class AsyncProcessor<I extends AsyncIface> extends org.apache.thrift.TBaseAsyncProcessor<I> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncProcessor.class.getName());
    public AsyncProcessor(I iface) {
      super(iface, getProcessMap(new HashMap<String, org.apache.thrift.AsyncProcessFunction<I, ? extends org.apache.thrift.TBase, ?>>()));
    }

    protected AsyncProcessor(I iface, Map<String,  org.apache.thrift.AsyncProcessFunction<I, ? extends  org.apache.thrift.TBase, ?>> processMap) {
      super(iface, getProcessMap(processMap));
    }

    private static <I extends AsyncIface> Map<String,  org.apache.thrift.AsyncProcessFunction<I, ? extends  org.apache.thrift.TBase,?>> getProcessMap(Map<String,  org.apache.thrift.AsyncProcessFunction<I, ? extends  org.apache.thrift.TBase, ?>> processMap) {
      processMap.put("persistFile", new persistFile());
      return processMap;
    }

    public static class persistFile<I extends AsyncIface> extends org.apache.thrift.AsyncProcessFunction<I, persistFile_args, Void> {
      public persistFile() {
        super("persistFile");
      }

      public persistFile_args getEmptyArgsInstance() {
        return new persistFile_args();
      }

      public AsyncMethodCallback<Void> getResultHandler(final AsyncFrameBuffer fb, final int seqid) {
        final org.apache.thrift.AsyncProcessFunction fcall = this;
        return new AsyncMethodCallback<Void>() { 
          public void onComplete(Void o) {
            persistFile_result result = new persistFile_result();
            try {
              fcall.sendResponse(fb,result, org.apache.thrift.protocol.TMessageType.REPLY,seqid);
              return;
            } catch (Exception e) {
              LOGGER.error("Exception writing to internal frame buffer", e);
            }
            fb.close();
          }
          public void onError(Exception e) {
            byte msgType = org.apache.thrift.protocol.TMessageType.REPLY;
            org.apache.thrift.TBase msg;
            persistFile_result result = new persistFile_result();
            {
              msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;
              msg = (org.apache.thrift.TBase)new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.INTERNAL_ERROR, e.getMessage());
            }
            try {
              fcall.sendResponse(fb,msg,msgType,seqid);
              return;
            } catch (Exception ex) {
              LOGGER.error("Exception writing to internal frame buffer", ex);
            }
            fb.close();
          }
        };
      }

      protected boolean isOneway() {
        return false;
      }

      public void start(I iface, persistFile_args args, org.apache.thrift.async.AsyncMethodCallback<Void> resultHandler) throws TException {
        iface.persistFile(args.blockIds, args.fileId, args.filePath,resultHandler);
      }
    }

  }

  public static class persistFile_args implements org.apache.thrift.TBase<persistFile_args, persistFile_args._Fields>, java.io.Serializable, Cloneable, Comparable<persistFile_args>   {
    private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("persistFile_args");

    private static final org.apache.thrift.protocol.TField BLOCK_IDS_FIELD_DESC = new org.apache.thrift.protocol.TField("blockIds", org.apache.thrift.protocol.TType.LIST, (short)1);
    private static final org.apache.thrift.protocol.TField FILE_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("fileId", org.apache.thrift.protocol.TType.I64, (short)2);
    private static final org.apache.thrift.protocol.TField FILE_PATH_FIELD_DESC = new org.apache.thrift.protocol.TField("filePath", org.apache.thrift.protocol.TType.STRING, (short)3);

    private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
    static {
      schemes.put(StandardScheme.class, new persistFile_argsStandardSchemeFactory());
      schemes.put(TupleScheme.class, new persistFile_argsTupleSchemeFactory());
    }

    public List<Long> blockIds; // required
    public long fileId; // required
    public String filePath; // required

    /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
    public enum _Fields implements org.apache.thrift.TFieldIdEnum {
      BLOCK_IDS((short)1, "blockIds"),
      FILE_ID((short)2, "fileId"),
      FILE_PATH((short)3, "filePath");

      private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

      static {
        for (_Fields field : EnumSet.allOf(_Fields.class)) {
          byName.put(field.getFieldName(), field);
        }
      }

      /**
       * Find the _Fields constant that matches fieldId, or null if its not found.
       */
      public static _Fields findByThriftId(int fieldId) {
        switch(fieldId) {
          case 1: // BLOCK_IDS
            return BLOCK_IDS;
          case 2: // FILE_ID
            return FILE_ID;
          case 3: // FILE_PATH
            return FILE_PATH;
          default:
            return null;
        }
      }

      /**
       * Find the _Fields constant that matches fieldId, throwing an exception
       * if it is not found.
       */
      public static _Fields findByThriftIdOrThrow(int fieldId) {
        _Fields fields = findByThriftId(fieldId);
        if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
        return fields;
      }

      /**
       * Find the _Fields constant that matches name, or null if its not found.
       */
      public static _Fields findByName(String name) {
        return byName.get(name);
      }

      private final short _thriftId;
      private final String _fieldName;

      _Fields(short thriftId, String fieldName) {
        _thriftId = thriftId;
        _fieldName = fieldName;
      }

      public short getThriftFieldId() {
        return _thriftId;
      }

      public String getFieldName() {
        return _fieldName;
      }
    }

    // isset id assignments
    private static final int __FILEID_ISSET_ID = 0;
    private byte __isset_bitfield = 0;
    public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
    static {
      Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
      tmpMap.put(_Fields.BLOCK_IDS, new org.apache.thrift.meta_data.FieldMetaData("blockIds", org.apache.thrift.TFieldRequirementType.DEFAULT, 
          new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
              new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64))));
      tmpMap.put(_Fields.FILE_ID, new org.apache.thrift.meta_data.FieldMetaData("fileId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
          new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
      tmpMap.put(_Fields.FILE_PATH, new org.apache.thrift.meta_data.FieldMetaData("filePath", org.apache.thrift.TFieldRequirementType.DEFAULT, 
          new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
      metaDataMap = Collections.unmodifiableMap(tmpMap);
      org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(persistFile_args.class, metaDataMap);
    }

    public persistFile_args() {
    }

    public persistFile_args(
      List<Long> blockIds,
      long fileId,
      String filePath)
    {
      this();
      this.blockIds = blockIds;
      this.fileId = fileId;
      setFileIdIsSet(true);
      this.filePath = filePath;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public persistFile_args(persistFile_args other) {
      __isset_bitfield = other.__isset_bitfield;
      if (other.isSetBlockIds()) {
        List<Long> __this__blockIds = new ArrayList<Long>(other.blockIds);
        this.blockIds = __this__blockIds;
      }
      this.fileId = other.fileId;
      if (other.isSetFilePath()) {
        this.filePath = other.filePath;
      }
    }

    public persistFile_args deepCopy() {
      return new persistFile_args(this);
    }

    @Override
    public void clear() {
      this.blockIds = null;
      setFileIdIsSet(false);
      this.fileId = 0;
      this.filePath = null;
    }

    public int getBlockIdsSize() {
      return (this.blockIds == null) ? 0 : this.blockIds.size();
    }

    public java.util.Iterator<Long> getBlockIdsIterator() {
      return (this.blockIds == null) ? null : this.blockIds.iterator();
    }

    public void addToBlockIds(long elem) {
      if (this.blockIds == null) {
        this.blockIds = new ArrayList<Long>();
      }
      this.blockIds.add(elem);
    }

    public List<Long> getBlockIds() {
      return this.blockIds;
    }

    public persistFile_args setBlockIds(List<Long> blockIds) {
      this.blockIds = blockIds;
      return this;
    }

    public void unsetBlockIds() {
      this.blockIds = null;
    }

    /** Returns true if field blockIds is set (has been assigned a value) and false otherwise */
    public boolean isSetBlockIds() {
      return this.blockIds != null;
    }

    public void setBlockIdsIsSet(boolean value) {
      if (!value) {
        this.blockIds = null;
      }
    }

    public long getFileId() {
      return this.fileId;
    }

    public persistFile_args setFileId(long fileId) {
      this.fileId = fileId;
      setFileIdIsSet(true);
      return this;
    }

    public void unsetFileId() {
      __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __FILEID_ISSET_ID);
    }

    /** Returns true if field fileId is set (has been assigned a value) and false otherwise */
    public boolean isSetFileId() {
      return EncodingUtils.testBit(__isset_bitfield, __FILEID_ISSET_ID);
    }

    public void setFileIdIsSet(boolean value) {
      __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __FILEID_ISSET_ID, value);
    }

    public String getFilePath() {
      return this.filePath;
    }

    public persistFile_args setFilePath(String filePath) {
      this.filePath = filePath;
      return this;
    }

    public void unsetFilePath() {
      this.filePath = null;
    }

    /** Returns true if field filePath is set (has been assigned a value) and false otherwise */
    public boolean isSetFilePath() {
      return this.filePath != null;
    }

    public void setFilePathIsSet(boolean value) {
      if (!value) {
        this.filePath = null;
      }
    }

    public void setFieldValue(_Fields field, Object value) {
      switch (field) {
      case BLOCK_IDS:
        if (value == null) {
          unsetBlockIds();
        } else {
          setBlockIds((List<Long>)value);
        }
        break;

      case FILE_ID:
        if (value == null) {
          unsetFileId();
        } else {
          setFileId((Long)value);
        }
        break;

      case FILE_PATH:
        if (value == null) {
          unsetFilePath();
        } else {
          setFilePath((String)value);
        }
        break;

      }
    }

    public Object getFieldValue(_Fields field) {
      switch (field) {
      case BLOCK_IDS:
        return getBlockIds();

      case FILE_ID:
        return Long.valueOf(getFileId());

      case FILE_PATH:
        return getFilePath();

      }
      throw new IllegalStateException();
    }

    /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
    public boolean isSet(_Fields field) {
      if (field == null) {
        throw new IllegalArgumentException();
      }

      switch (field) {
      case BLOCK_IDS:
        return isSetBlockIds();
      case FILE_ID:
        return isSetFileId();
      case FILE_PATH:
        return isSetFilePath();
      }
      throw new IllegalStateException();
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof persistFile_args)
        return this.equals((persistFile_args)that);
      return false;
    }

    public boolean equals(persistFile_args that) {
      if (that == null)
        return false;

      boolean this_present_blockIds = true && this.isSetBlockIds();
      boolean that_present_blockIds = true && that.isSetBlockIds();
      if (this_present_blockIds || that_present_blockIds) {
        if (!(this_present_blockIds && that_present_blockIds))
          return false;
        if (!this.blockIds.equals(that.blockIds))
          return false;
      }

      boolean this_present_fileId = true;
      boolean that_present_fileId = true;
      if (this_present_fileId || that_present_fileId) {
        if (!(this_present_fileId && that_present_fileId))
          return false;
        if (this.fileId != that.fileId)
          return false;
      }

      boolean this_present_filePath = true && this.isSetFilePath();
      boolean that_present_filePath = true && that.isSetFilePath();
      if (this_present_filePath || that_present_filePath) {
        if (!(this_present_filePath && that_present_filePath))
          return false;
        if (!this.filePath.equals(that.filePath))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      List<Object> list = new ArrayList<Object>();

      boolean present_blockIds = true && (isSetBlockIds());
      list.add(present_blockIds);
      if (present_blockIds)
        list.add(blockIds);

      boolean present_fileId = true;
      list.add(present_fileId);
      if (present_fileId)
        list.add(fileId);

      boolean present_filePath = true && (isSetFilePath());
      list.add(present_filePath);
      if (present_filePath)
        list.add(filePath);

      return list.hashCode();
    }

    @Override
    public int compareTo(persistFile_args other) {
      if (!getClass().equals(other.getClass())) {
        return getClass().getName().compareTo(other.getClass().getName());
      }

      int lastComparison = 0;

      lastComparison = Boolean.valueOf(isSetBlockIds()).compareTo(other.isSetBlockIds());
      if (lastComparison != 0) {
        return lastComparison;
      }
      if (isSetBlockIds()) {
        lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.blockIds, other.blockIds);
        if (lastComparison != 0) {
          return lastComparison;
        }
      }
      lastComparison = Boolean.valueOf(isSetFileId()).compareTo(other.isSetFileId());
      if (lastComparison != 0) {
        return lastComparison;
      }
      if (isSetFileId()) {
        lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.fileId, other.fileId);
        if (lastComparison != 0) {
          return lastComparison;
        }
      }
      lastComparison = Boolean.valueOf(isSetFilePath()).compareTo(other.isSetFilePath());
      if (lastComparison != 0) {
        return lastComparison;
      }
      if (isSetFilePath()) {
        lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.filePath, other.filePath);
        if (lastComparison != 0) {
          return lastComparison;
        }
      }
      return 0;
    }

    public _Fields fieldForId(int fieldId) {
      return _Fields.findByThriftId(fieldId);
    }

    public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
      schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
      schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("persistFile_args(");
      boolean first = true;

      sb.append("blockIds:");
      if (this.blockIds == null) {
        sb.append("null");
      } else {
        sb.append(this.blockIds);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("fileId:");
      sb.append(this.fileId);
      first = false;
      if (!first) sb.append(", ");
      sb.append("filePath:");
      if (this.filePath == null) {
        sb.append("null");
      } else {
        sb.append(this.filePath);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws org.apache.thrift.TException {
      // check for required fields
      // check for sub-struct validity
    }

    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
      try {
        write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
      } catch (org.apache.thrift.TException te) {
        throw new java.io.IOException(te);
      }
    }

    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
      try {
        // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
        __isset_bitfield = 0;
        read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
      } catch (org.apache.thrift.TException te) {
        throw new java.io.IOException(te);
      }
    }

    private static class persistFile_argsStandardSchemeFactory implements SchemeFactory {
      public persistFile_argsStandardScheme getScheme() {
        return new persistFile_argsStandardScheme();
      }
    }

    private static class persistFile_argsStandardScheme extends StandardScheme<persistFile_args> {

      public void read(org.apache.thrift.protocol.TProtocol iprot, persistFile_args struct) throws org.apache.thrift.TException {
        org.apache.thrift.protocol.TField schemeField;
        iprot.readStructBegin();
        while (true)
        {
          schemeField = iprot.readFieldBegin();
          if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
            break;
          }
          switch (schemeField.id) {
            case 1: // BLOCK_IDS
              if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
                {
                  org.apache.thrift.protocol.TList _list204 = iprot.readListBegin();
                  struct.blockIds = new ArrayList<Long>(_list204.size);
                  long _elem205;
                  for (int _i206 = 0; _i206 < _list204.size; ++_i206)
                  {
                    _elem205 = iprot.readI64();
                    struct.blockIds.add(_elem205);
                  }
                  iprot.readListEnd();
                }
                struct.setBlockIdsIsSet(true);
              } else { 
                org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
              }
              break;
            case 2: // FILE_ID
              if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
                struct.fileId = iprot.readI64();
                struct.setFileIdIsSet(true);
              } else { 
                org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
              }
              break;
            case 3: // FILE_PATH
              if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
                struct.filePath = iprot.readString();
                struct.setFilePathIsSet(true);
              } else { 
                org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
              }
              break;
            default:
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
          }
          iprot.readFieldEnd();
        }
        iprot.readStructEnd();

        // check for required fields of primitive type, which can't be checked in the validate method
        struct.validate();
      }

      public void write(org.apache.thrift.protocol.TProtocol oprot, persistFile_args struct) throws org.apache.thrift.TException {
        struct.validate();

        oprot.writeStructBegin(STRUCT_DESC);
        if (struct.blockIds != null) {
          oprot.writeFieldBegin(BLOCK_IDS_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.I64, struct.blockIds.size()));
            for (long _iter207 : struct.blockIds)
            {
              oprot.writeI64(_iter207);
            }
            oprot.writeListEnd();
          }
          oprot.writeFieldEnd();
        }
        oprot.writeFieldBegin(FILE_ID_FIELD_DESC);
        oprot.writeI64(struct.fileId);
        oprot.writeFieldEnd();
        if (struct.filePath != null) {
          oprot.writeFieldBegin(FILE_PATH_FIELD_DESC);
          oprot.writeString(struct.filePath);
          oprot.writeFieldEnd();
        }
        oprot.writeFieldStop();
        oprot.writeStructEnd();
      }

    }

    private static class persistFile_argsTupleSchemeFactory implements SchemeFactory {
      public persistFile_argsTupleScheme getScheme() {
        return new persistFile_argsTupleScheme();
      }
    }

    private static class persistFile_argsTupleScheme extends TupleScheme<persistFile_args> {

      @Override
      public void write(org.apache.thrift.protocol.TProtocol prot, persistFile_args struct) throws org.apache.thrift.TException {
        TTupleProtocol oprot = (TTupleProtocol) prot;
        BitSet optionals = new BitSet();
        if (struct.isSetBlockIds()) {
          optionals.set(0);
        }
        if (struct.isSetFileId()) {
          optionals.set(1);
        }
        if (struct.isSetFilePath()) {
          optionals.set(2);
        }
        oprot.writeBitSet(optionals, 3);
        if (struct.isSetBlockIds()) {
          {
            oprot.writeI32(struct.blockIds.size());
            for (long _iter208 : struct.blockIds)
            {
              oprot.writeI64(_iter208);
            }
          }
        }
        if (struct.isSetFileId()) {
          oprot.writeI64(struct.fileId);
        }
        if (struct.isSetFilePath()) {
          oprot.writeString(struct.filePath);
        }
      }

      @Override
      public void read(org.apache.thrift.protocol.TProtocol prot, persistFile_args struct) throws org.apache.thrift.TException {
        TTupleProtocol iprot = (TTupleProtocol) prot;
        BitSet incoming = iprot.readBitSet(3);
        if (incoming.get(0)) {
          {
            org.apache.thrift.protocol.TList _list209 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.I64, iprot.readI32());
            struct.blockIds = new ArrayList<Long>(_list209.size);
            long _elem210;
            for (int _i211 = 0; _i211 < _list209.size; ++_i211)
            {
              _elem210 = iprot.readI64();
              struct.blockIds.add(_elem210);
            }
          }
          struct.setBlockIdsIsSet(true);
        }
        if (incoming.get(1)) {
          struct.fileId = iprot.readI64();
          struct.setFileIdIsSet(true);
        }
        if (incoming.get(2)) {
          struct.filePath = iprot.readString();
          struct.setFilePathIsSet(true);
        }
      }
    }

  }

  public static class persistFile_result implements org.apache.thrift.TBase<persistFile_result, persistFile_result._Fields>, java.io.Serializable, Cloneable, Comparable<persistFile_result>   {
    private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("persistFile_result");


    private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
    static {
      schemes.put(StandardScheme.class, new persistFile_resultStandardSchemeFactory());
      schemes.put(TupleScheme.class, new persistFile_resultTupleSchemeFactory());
    }


    /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
    public enum _Fields implements org.apache.thrift.TFieldIdEnum {
;

      private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

      static {
        for (_Fields field : EnumSet.allOf(_Fields.class)) {
          byName.put(field.getFieldName(), field);
        }
      }

      /**
       * Find the _Fields constant that matches fieldId, or null if its not found.
       */
      public static _Fields findByThriftId(int fieldId) {
        switch(fieldId) {
          default:
            return null;
        }
      }

      /**
       * Find the _Fields constant that matches fieldId, throwing an exception
       * if it is not found.
       */
      public static _Fields findByThriftIdOrThrow(int fieldId) {
        _Fields fields = findByThriftId(fieldId);
        if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
        return fields;
      }

      /**
       * Find the _Fields constant that matches name, or null if its not found.
       */
      public static _Fields findByName(String name) {
        return byName.get(name);
      }

      private final short _thriftId;
      private final String _fieldName;

      _Fields(short thriftId, String fieldName) {
        _thriftId = thriftId;
        _fieldName = fieldName;
      }

      public short getThriftFieldId() {
        return _thriftId;
      }

      public String getFieldName() {
        return _fieldName;
      }
    }
    public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
    static {
      Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
      metaDataMap = Collections.unmodifiableMap(tmpMap);
      org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(persistFile_result.class, metaDataMap);
    }

    public persistFile_result() {
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public persistFile_result(persistFile_result other) {
    }

    public persistFile_result deepCopy() {
      return new persistFile_result(this);
    }

    @Override
    public void clear() {
    }

    public void setFieldValue(_Fields field, Object value) {
      switch (field) {
      }
    }

    public Object getFieldValue(_Fields field) {
      switch (field) {
      }
      throw new IllegalStateException();
    }

    /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
    public boolean isSet(_Fields field) {
      if (field == null) {
        throw new IllegalArgumentException();
      }

      switch (field) {
      }
      throw new IllegalStateException();
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof persistFile_result)
        return this.equals((persistFile_result)that);
      return false;
    }

    public boolean equals(persistFile_result that) {
      if (that == null)
        return false;

      return true;
    }

    @Override
    public int hashCode() {
      List<Object> list = new ArrayList<Object>();

      return list.hashCode();
    }

    @Override
    public int compareTo(persistFile_result other) {
      if (!getClass().equals(other.getClass())) {
        return getClass().getName().compareTo(other.getClass().getName());
      }

      int lastComparison = 0;

      return 0;
    }

    public _Fields fieldForId(int fieldId) {
      return _Fields.findByThriftId(fieldId);
    }

    public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
      schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
      schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
      }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("persistFile_result(");
      boolean first = true;

      sb.append(")");
      return sb.toString();
    }

    public void validate() throws org.apache.thrift.TException {
      // check for required fields
      // check for sub-struct validity
    }

    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
      try {
        write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
      } catch (org.apache.thrift.TException te) {
        throw new java.io.IOException(te);
      }
    }

    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
      try {
        read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
      } catch (org.apache.thrift.TException te) {
        throw new java.io.IOException(te);
      }
    }

    private static class persistFile_resultStandardSchemeFactory implements SchemeFactory {
      public persistFile_resultStandardScheme getScheme() {
        return new persistFile_resultStandardScheme();
      }
    }

    private static class persistFile_resultStandardScheme extends StandardScheme<persistFile_result> {

      public void read(org.apache.thrift.protocol.TProtocol iprot, persistFile_result struct) throws org.apache.thrift.TException {
        org.apache.thrift.protocol.TField schemeField;
        iprot.readStructBegin();
        while (true)
        {
          schemeField = iprot.readFieldBegin();
          if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
            break;
          }
          switch (schemeField.id) {
            default:
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
          }
          iprot.readFieldEnd();
        }
        iprot.readStructEnd();

        // check for required fields of primitive type, which can't be checked in the validate method
        struct.validate();
      }

      public void write(org.apache.thrift.protocol.TProtocol oprot, persistFile_result struct) throws org.apache.thrift.TException {
        struct.validate();

        oprot.writeStructBegin(STRUCT_DESC);
        oprot.writeFieldStop();
        oprot.writeStructEnd();
      }

    }

    private static class persistFile_resultTupleSchemeFactory implements SchemeFactory {
      public persistFile_resultTupleScheme getScheme() {
        return new persistFile_resultTupleScheme();
      }
    }

    private static class persistFile_resultTupleScheme extends TupleScheme<persistFile_result> {

      @Override
      public void write(org.apache.thrift.protocol.TProtocol prot, persistFile_result struct) throws org.apache.thrift.TException {
        TTupleProtocol oprot = (TTupleProtocol) prot;
      }

      @Override
      public void read(org.apache.thrift.protocol.TProtocol prot, persistFile_result struct) throws org.apache.thrift.TException {
        TTupleProtocol iprot = (TTupleProtocol) prot;
      }
    }

  }

}
