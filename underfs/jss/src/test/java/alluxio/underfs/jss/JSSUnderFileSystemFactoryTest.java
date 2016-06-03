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
