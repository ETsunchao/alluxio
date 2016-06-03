package alluxio.underfs.jss;

/**
 * Auto control a resource by reference count.
 */
public interface IReferencer {
  /**
   * count +1, if count is from 0 to 1, implementation class must new an only resource.
   */
  void addRef();

  /**
   * count -1, if count is 0, implementation class must destroy the only resource.
   */
  void release();
}
