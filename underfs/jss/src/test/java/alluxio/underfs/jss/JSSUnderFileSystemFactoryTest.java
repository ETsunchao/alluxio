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

import org.junit.Assert;
import org.junit.Test;

import alluxio.Configuration;
import alluxio.underfs.UnderFileSystemFactory;
import alluxio.underfs.UnderFileSystemRegistry;

/**
 * Unit tests for the {@link JSSUnderFileSystemFactory}.
 */
public class JSSUnderFileSystemFactoryTest {
  /**
   * Tests that the JSS UFS module correctly accepts paths that begin with jss://.
   */
  @Test
  public void factoryTest() {
    Configuration conf = new Configuration();

    UnderFileSystemFactory factory = UnderFileSystemRegistry.find("jss://test-bucket/path", conf);

    Assert.assertNotNull(
        "A UnderFileSystemFactory should exist for jss paths when using this module", factory);
  }
}
