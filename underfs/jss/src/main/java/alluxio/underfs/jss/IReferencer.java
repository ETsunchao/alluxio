/*
 * The Alluxio Open Foundation licenses this work under the Apache License, version 2.0 (the
 * "License"). You may not use this work except in compliance with the License, which is available
 * at www.apache.org/licenses/LICENSE-2.0
 *
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied, as more fully set forth in the License.
 *
 * See the NOTICE file distributed with this work for information regarding copyright ownership.
 */

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
