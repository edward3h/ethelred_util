package org.ethelred.util.console;

import java.io.IOException;
import java.util.Arrays;

public class ConsoleHelper {
  private static final ConsoleHelper INSTANCE = new ConsoleHelper();

  private ConsoleHelper() {}

  public static Key readKey() throws ConsoleKeyException {
    return INSTANCE._readKey();
  }

  private Key _readKey() throws ConsoleKeyException {
    try {
      rawMode();
      int input = System.in.read();
      int available = System.in.available();
      byte[] subs = new byte[0];
      if (available > 0 && available < 6) {
        subs = new byte[available];
        System.in.read(subs);
      }
      return Key.from(input, subs);
    } catch (Exception e) {
      throw new ConsoleKeyException(e);
    } finally {
      cookedMode();
    }
  }

  private void cookedMode() {
    try {
      String[] cmd = {"/bin/sh", "-c", "stty cooked </dev/tty"};
      Runtime.getRuntime().exec(cmd).waitFor();
    } catch (InterruptedException | IOException e) {
      e.printStackTrace();
    }
  }

  private void rawMode() throws IOException, InterruptedException {
    String[] cmd = {"/bin/sh", "-c", "stty raw </dev/tty"};
    Runtime.getRuntime().exec(cmd).waitFor();
  }

  public static class Key {
    public static final Key CTRL_C = from(3);
    private final int input;
    private final byte[] subs;

    public Key(int input, byte[] subs) {
      this.input = input;
      this.subs = subs;
    }

    public char getChar() {
      return (char) input;
    }

    public boolean isChar() {
      if (subs.length > 0) {
        return false;
      }

      if (input == 13) {
        // enter
        return true;
      }

      // regular character
      return input >= 32 && input <= 126;
    }

    private static Key from(int input, byte... subs) {
      return new Key(input, subs);
    }

    @Override
    public String toString() {
      final StringBuilder sb = new StringBuilder("Key{");
      sb.append("input=").append(input);
      sb.append(", subs=").append(Arrays.toString(subs));
      sb.append('}');
      return sb.toString();
    }
  }

  public static class ConsoleKeyException extends Exception {
    public ConsoleKeyException(Throwable cause) {
      super(cause);
    }
  }
}
