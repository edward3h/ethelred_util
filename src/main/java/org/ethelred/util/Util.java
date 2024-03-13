package org.ethelred.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicLong;

/** Utility methods */
class Util {
  /** utility class */
  private Util() {}

  /**
   * Attempts to calculate the size of a file or directory.
   *
   * <p>Since the operation is non-atomic, the returned value may be inaccurate. However, this
   * method is quick and does its best.
   *
   * <p>From https://stackoverflow.com/questions/2149785/get-size-of-folder-or-file
   */
  public static long size(Path path) {

    final AtomicLong size = new AtomicLong(0);

    try {
      Files.walkFileTree(
          path,
          new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {

              size.addAndGet(attrs.size());
              return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {

              System.out.println("skipped: " + file + " (" + exc + ")");
              // Skip folders that can't be traversed
              return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) {

              if (exc != null)
                System.out.println("had trouble traversing: " + dir + " (" + exc + ")");
              // Ignore errors traversing a folder
              return FileVisitResult.CONTINUE;
            }
          });
    } catch (IOException e) {
      throw new AssertionError(
          "walkFileTree will not throw IOException if the FileVisitor does not");
    }

    return size.get();
  }

  public static void deleteRecursive(Path path) throws IOException {
    Files.walk(path).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
  }

  /**
   * Converts a standard POSIX Shell globbing pattern into a regular expression pattern. The result
   * can be used with the standard {@link java.util.regex} API to recognize strings which match the
   * glob pattern.
   *
   * <p>See also, the POSIX Shell language:
   * http://pubs.opengroup.org/onlinepubs/009695399/utilities/xcu_chap02.html#tag_02_13_01
   *
   * @param pattern A glob pattern.
   * @return A regex pattern to recognize the given glob pattern.
   *     <p>From:
   *     https://stackoverflow.com/questions/1247772/is-there-an-equivalent-of-java-util-regex-for-glob-type-patterns
   */
  public static String convertGlobToRegex(String pattern) {
    StringBuilder sb = new StringBuilder(pattern.length());
    int inGroup = 0;
    int inClass = 0;
    int firstIndexInClass = -1;
    char[] arr = pattern.toCharArray();
    for (int i = 0; i < arr.length; i++) {
      char ch = arr[i];
      switch (ch) {
        case '\\':
          if (++i >= arr.length) {
            sb.append('\\');
          } else {
            char next = arr[i];
            switch (next) {
              case ',':
                // escape not needed
                break;
              case 'Q':
              case 'E':
                // extra escape needed
                sb.append('\\');
              default:
                sb.append('\\');
            }
            sb.append(next);
          }
          break;
        case '*':
          if (inClass == 0) sb.append(".*");
          else sb.append('*');
          break;
        case '?':
          if (inClass == 0) sb.append('.');
          else sb.append('?');
          break;
        case '[':
          inClass++;
          firstIndexInClass = i + 1;
          sb.append('[');
          break;
        case ']':
          inClass--;
          sb.append(']');
          break;
        case '.':
        case '(':
        case ')':
        case '+':
        case '|':
        case '^':
        case '$':
        case '@':
        case '%':
          if (inClass == 0 || (firstIndexInClass == i && ch == '^')) sb.append('\\');
          sb.append(ch);
          break;
        case '!':
          if (firstIndexInClass == i) sb.append('^');
          else sb.append('!');
          break;
        case '{':
          inGroup++;
          sb.append('(');
          break;
        case '}':
          inGroup--;
          sb.append(')');
          break;
        case ',':
          if (inGroup > 0) sb.append('|');
          else sb.append(',');
          break;
        default:
          sb.append(ch);
      }
    }
    return sb.toString();
  }

  public static boolean isBlank(String s) {
    return s == null || s.isBlank();
  }
}
