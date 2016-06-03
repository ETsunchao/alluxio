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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

/**
 * Unit tests for the private helper methods in {@link JSSUnderFileSystem} that do not require an
 * JSS backend.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(JSSUnderFileSystem.class)
public class JSSUnderFileSystemTest {
  private JSSUnderFileSystem mMockJSSUnderFileSystem;

  /**
   * Sets up the mock before a test runs.
   */
  @Before
  public final void before() {
    mMockJSSUnderFileSystem = Mockito.mock(JSSUnderFileSystem.class);
    Whitebox.setInternalState(mMockJSSUnderFileSystem, "mBucketName", "test-bucket");
    Whitebox.setInternalState(mMockJSSUnderFileSystem, "mBucketPrefix", "jss://test-bucket/");
  }

  /**
   * Tests the {@link JSSUnderFileSystem#convertToFolderName(String)} method.
   *
   * @throws Exception if the Whitebox fails
   */
  @Test
  public void convertToFolderNameTest() throws Exception {
    String input1 = "test";
    String result1 = Whitebox.invokeMethod(mMockJSSUnderFileSystem, "convertToFolderName", input1);

    Assert.assertEquals(result1, "test_$folder$");
  }

  /**
   * Tests the {@link JSSUnderFileSystem#getChildName(String, String)} method.
   *
   * @throws Exception if the Whitebox fails
   */
  @Test
  public void getChildNameTest() throws Exception {
    String input11 = "jss://test-bucket/child";
    String input12 = "jss://test-bucket/";
    String input21 = "jss://test-bucket/parent/child";
    String input22 = "jss://test-bucket/parent/";
    String input31 = "jss://test-bucket/child";
    String input32 = "jss://test-bucket/not-parent";
    String result1 =
        Whitebox.invokeMethod(mMockJSSUnderFileSystem, "getChildName", input11, input12);
    String result2 =
        Whitebox.invokeMethod(mMockJSSUnderFileSystem, "getChildName", input21, input22);
    String result3 =
        Whitebox.invokeMethod(mMockJSSUnderFileSystem, "getChildName", input31, input32);

    Assert.assertEquals("child", result1);
    Assert.assertEquals("child", result2);
    Assert.assertNull(result3);
  }

  /**
   * Tests the {@link JSSUnderFileSystem#getParentKey(String)} method.
   *
   * @throws Exception if the Whitebox fails
   */
  @Test
  public void getParentKeyTest() throws Exception {
    String input1 = "jss://test-bucket/parent-is-root";
    String input2 = "jss://test-bucket/";
    String input3 = "jss://test-bucket/parent/child";
    String input4 = "jss://test-bucket";
    String result1 = Whitebox.invokeMethod(mMockJSSUnderFileSystem, "getParentKey", input1);
    String result2 = Whitebox.invokeMethod(mMockJSSUnderFileSystem, "getParentKey", input2);
    String result3 = Whitebox.invokeMethod(mMockJSSUnderFileSystem, "getParentKey", input3);
    String result4 = Whitebox.invokeMethod(mMockJSSUnderFileSystem, "getParentKey", input4);

    Assert.assertEquals("jss://test-bucket", result1);
    Assert.assertNull(result2);
    Assert.assertEquals("jss://test-bucket/parent", result3);
    Assert.assertNull(result4);
  }

  /**
   * Tests the {@link JSSUnderFileSystem#isRoot(String)} method.
   *
   * @throws Exception if the Whitebox fails
   */
  @Test
  public void isRootTest() throws Exception {
    String input1 = "jss://";
    String input2 = "jss://test-bucket";
    String input3 = "jss://test-bucket/";
    String input4 = "jss://test-bucket/file";
    String input5 = "jss://test-bucket/dir/file";
    String input6 = "jss://test-bucket-wrong/";
    Boolean result1 = Whitebox.invokeMethod(mMockJSSUnderFileSystem, "isRoot", input1);
    Boolean result2 = Whitebox.invokeMethod(mMockJSSUnderFileSystem, "isRoot", input2);
    Boolean result3 = Whitebox.invokeMethod(mMockJSSUnderFileSystem, "isRoot", input3);
    Boolean result4 = Whitebox.invokeMethod(mMockJSSUnderFileSystem, "isRoot", input4);
    Boolean result5 = Whitebox.invokeMethod(mMockJSSUnderFileSystem, "isRoot", input5);
    Boolean result6 = Whitebox.invokeMethod(mMockJSSUnderFileSystem, "isRoot", input6);

    Assert.assertFalse(result1);
    Assert.assertTrue(result2);
    Assert.assertTrue(result3);
    Assert.assertFalse(result4);
    Assert.assertFalse(result5);
    Assert.assertFalse(result6);
  }

  /**
   * Tests the {@link JSSUnderFileSystem#stripFolderSuffixIfPresent(String)} method.
   *
   * @throws Exception if the Whitebox fails
   */
  @Test
  public void stripFolderSuffixIfPresentTest() throws Exception {
    String input1 = "jss://test-bucket/";
    String input2 = "jss://test-bucket/dir/file";
    String input3 = "jss://test-bucket/dir_$folder$";
    String result1 =
        Whitebox.invokeMethod(mMockJSSUnderFileSystem, "stripFolderSuffixIfPresent", input1);
    String result2 =
        Whitebox.invokeMethod(mMockJSSUnderFileSystem, "stripFolderSuffixIfPresent", input2);
    String result3 =
        Whitebox.invokeMethod(mMockJSSUnderFileSystem, "stripFolderSuffixIfPresent", input3);

    Assert.assertEquals("jss://test-bucket/", result1);
    Assert.assertEquals("jss://test-bucket/dir/file", result2);
    Assert.assertEquals("jss://test-bucket/dir", result3);
  }

  /**
   * Tests the {@link JSSUnderFileSystem#stripPrefixIfPresent(String)} method.
   *
   * @throws Exception if the Whitebox fails
   */
  @Test
  public void stripPrefixIfPresentTest() throws Exception {
    String[] inputs = new String[] {"jss://test-bucket", "jss://test-bucket/",
        "jss://test-bucket/file", "jss://test-bucket/dir/file", "jss://test-bucket-wrong/dir/file",
        "dir/file", "/dir/file",};
    String[] results = new String[] {"jss://test-bucket", "", "file", "dir/file",
        "jss://test-bucket-wrong/dir/file", "dir/file", "dir/file",};
    for (int i = 0; i < inputs.length; i ++) {
      Assert.assertEquals(results[i],
          Whitebox.invokeMethod(mMockJSSUnderFileSystem, "stripPrefixIfPresent", inputs[i]));
    }
  }
}
