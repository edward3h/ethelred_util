package console;

import com.sun.jna.Library;
import com.sun.jna.Native;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;

/** Created by edward on 4/5/17. */
public class ConsoleUtil {
  private static final int ENOTTY = 25; // errno.h - TODO any better way to load this?

  private ConsoleUtil() {}

  interface CLibrary extends Library {
    CLibrary INSTANCE = (CLibrary) Native.loadLibrary("c", CLibrary.class);

    int isatty(int fd);
  }

  private static boolean _isATty(int fileDescriptor) throws TtyStateException {
    int result = CLibrary.INSTANCE.isatty(fileDescriptor);
    if (result == 1) {
      return true;
    }
    int errno = Native.getLastError();
    if (errno == ENOTTY) {
      // not a tty - expected
      return false;
    }
    throw new TtyStateException(fileDescriptor, errno);
  }

  private static class TtyStateException extends IOException {
    public TtyStateException(int fileDescriptor, int errno) {
      super("Unexpected tty result: fd=" + fileDescriptor + " errno=" + errno);
    }
  }

  public static boolean isATty(InputStream inputStream) throws IOException {
    return _isATty(_fileDescriptor(inputStream));
  }

  public static boolean isATty(OutputStream outputStream) throws IOException {
    return _isATty(_fileDescriptor(outputStream));
  }

  private static int _fileDescriptor(OutputStream outputStream) throws IOException {
    if (outputStream instanceof FileOutputStream) {
      return _fileDescriptor(((FileOutputStream) outputStream).getFD());
    }

    if (outputStream instanceof FilterOutputStream) {
      return _fileDescriptor(_unwrapFilterOutputStream((FilterOutputStream) outputStream));
    }
    throw new IOException("Cannot find file descriptor of " + outputStream);
  }

  private static Field filterOutputStreamInnerField =
      _lookupPrivateField(FilterOutputStream.class, "out");

  private static OutputStream _unwrapFilterOutputStream(FilterOutputStream outputStream)
      throws IOException {
    try {
      return (OutputStream) filterOutputStreamInnerField.get(outputStream);
    } catch (IllegalAccessException e) {
      throw new IOException("Could not access wrapped output stream", e);
    }
  }

  private static int _fileDescriptor(InputStream inputStream) throws IOException {
    if (inputStream instanceof FileInputStream) {
      return _fileDescriptor(((FileInputStream) inputStream).getFD());
    }

    if (inputStream instanceof FilterInputStream) {
      return _fileDescriptor(_unwrapFilterInputStream((FilterInputStream) inputStream));
    }
    throw new IOException("Cannot find file descriptor of " + inputStream);
  }

  private static Field filterInputStreamInnerField =
      _lookupPrivateField(FilterInputStream.class, "in");

  private static InputStream _unwrapFilterInputStream(FilterInputStream inputStream)
      throws IOException {
    try {
      return (InputStream) filterInputStreamInnerField.get(inputStream);
    } catch (IllegalAccessException e) {
      throw new IOException("Could not access wrapped input stream", e);
    }
  }

  private static Field fileDescriptorIntField = _lookupPrivateField(FileDescriptor.class, "fd");

  private static Field _lookupPrivateField(Class<?> klass, String name) {
    Field field = null;
    try {
      field = klass.getDeclaredField(name);
    } catch (NoSuchFieldException e) {
      // not expected
      throw new RuntimeException(e);
    }
    if (field != null) {
      field.setAccessible(true);
    }
    return field;
  }

  private static int _fileDescriptor(FileDescriptor fd) throws IOException {
    try {
      return (int) fileDescriptorIntField.get(fd);
    } catch (IllegalAccessException e) {
      throw new IOException("Could not read file descriptor", e);
    }
  }
}
