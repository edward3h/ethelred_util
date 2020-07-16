package org.ethelred.util;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import org.testng.annotations.Test;

public class UtilTest {

  @Test
  public void testSizeEmpty() throws IOException {
    Path testPath = null;
    try {
      testPath = Files.createTempDirectory("testSizeEmpty");
      assertEquals(Util.size(testPath), 0);
    } finally {
      if (testPath != null) {
        Util.deleteRecursive(testPath);
      }
    }
  }

  @Test
  public void testSizeFile() throws IOException {
    Path testPath = null;
    try {
      testPath = Files.createTempFile("testSizeFile", ".txt");
      writeTestFile(testPath, 1111);
      assertEquals(Util.size(testPath), 1111);
    } finally {
      Util.deleteRecursive(testPath);
    }
  }

  @Test
  public void testSizeDir() throws IOException {
    Path testPath = null;
    try {
      testPath = Files.createTempDirectory("testSizeEmpty");
      writeTestFile(testPath.resolve("a.txt"), 1234);
      Path nested = Files.createDirectory(testPath.resolve("nested"));
      writeTestFile(nested.resolve("b.txt"), 3210);
      assertEquals(Util.size(testPath), 4444);
    } finally {
      if (testPath != null) {
        Util.deleteRecursive(testPath);
      }
    }
  }

  private static final String LETTERS = "QWERTYUIOP";

  private void writeTestFile(Path path, int size) throws IOException {
    Random r = new Random(path.hashCode());
    StringBuilder buf = new StringBuilder();
    for (int i = 0; i < size; i++) {
      buf.append(LETTERS.charAt(r.nextInt(LETTERS.length())));
    }
    Files.writeString(path, buf.toString()); // default options
  }
}
