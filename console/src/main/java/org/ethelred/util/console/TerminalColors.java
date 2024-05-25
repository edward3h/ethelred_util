package console;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Helper class for printing colored output on terminals that support it.
 *
 * <p>refs: http://linux.101hacks.com/ps1-examples/prompt-color-using-tput/
 * https://linuxtidbits.wordpress.com/2008/08/11/output-color-on-bash-scripts/
 * http://unix.stackexchange.com/questions/269077/tput-setaf-color-table-how-to-determine-color-codes
 */
public class TerminalColors {
  private final Map<String, String> m_cmdCache = new ConcurrentHashMap<>();
  private final boolean m_enabled;
  private int m_supportedColors;
  private @Nullable String m_resetCmd;

  private OutputStream m_delegate;

  public TerminalColors() {
    this(System.out);
  }

  public TerminalColors(OutputStream delegate) {
    boolean enabled = false;
    try {
      String colorCountS = _executeTputCmd("colors");
      m_supportedColors = Integer.parseInt(colorCountS.trim());
      if (m_supportedColors > 1) {
        enabled = true;
        m_resetCmd = _executeTputCmd("sgr0");
      }

    } catch (IOException | NumberFormatException e) {
      System.err.print(e.getMessage());
      e.printStackTrace(System.err);
      enabled = false;
    }

    m_enabled = enabled;
    m_delegate = delegate;
  }

  private String _executeTputCmd(String cmd) throws IOException {
    String cached = m_cmdCache.get(cmd);
    if (cached != null) {
      return cached;
    }
    Process p = Runtime.getRuntime().exec(new String[] {"bash", "-c", "tput " + cmd});
    byte[] buf = new byte[1024];
    int read = p.getInputStream().read(buf);
    String result = new String(buf, 0, read);
    m_cmdCache.put(cmd, result);
    return result;
  }

  public TCPrintStream printStream() {
    return new TCPrintStream();
  }

  public class TCPrintStream extends PrintStream {
    String m_setupCmds = "";

    private TCPrintStream() {
      super(m_delegate);
    }

    public TCPrintStream foreground(int colorNum, int... backups) {
      if (!m_enabled) {
        return this;
      }
      int color = _firstValid(colorNum, backups);
      if (color >= 0) {
        try {
          m_setupCmds += _executeTputCmd("setaf " + color);
        } catch (IOException e) {
          // ignore TODO?
        }
      }
      return this;
    }

    public TCPrintStream background(int colorNum, int... backups) {
      if (!m_enabled) {
        return this;
      }
      int color = _firstValid(colorNum, backups);
      if (color >= 0) {
        try {
          m_setupCmds += _executeTputCmd("setab " + color);
        } catch (IOException e) {
          // ignore TODO?
        }
      }
      return this;
    }

    public TCPrintStream bold() {
      if (!m_enabled) {
        return this;
      }
      try {
        m_setupCmds += _executeTputCmd("bold");
      } catch (IOException e) {
        // ignore TODO?
      }
      return this;
    }

    public TCPrintStream underline() {
      if (!m_enabled) {
        return this;
      }
      try {
        m_setupCmds += _executeTputCmd("smul");
      } catch (IOException e) {
        // ignore TODO?
      }
      return this;
    }

    private void _printSetup() {
      if (m_enabled) {
        super.print(m_setupCmds);
      }
    }

    private void _printReset() {
      if (m_enabled) {
        super.print(m_resetCmd);
      }
    }

    private String _wrapFormat(String format) {
      if (m_enabled) {
        return m_setupCmds + format + m_resetCmd;
      } else {
        return format;
      }
    }

    @Override
    public void print(boolean b) {
      _printSetup();
      super.print(b);
      _printReset();
    }

    @Override
    public void print(char c) {
      _printSetup();
      super.print(c);
      _printReset();
    }

    @Override
    public void print(int i) {
      _printSetup();
      super.print(i);
      _printReset();
    }

    @Override
    public void print(long l) {
      _printSetup();
      super.print(l);
      _printReset();
    }

    @Override
    public void print(float f) {
      _printSetup();
      super.print(f);
      _printReset();
    }

    @Override
    public void print(double d) {
      _printSetup();
      super.print(d);
      _printReset();
    }

    @Override
    public void print(char[] s) {
      _printSetup();
      super.print(s);
      _printReset();
    }

    @Override
    public void print(String s) {
      _printSetup();
      super.print(s);
      _printReset();
    }

    @Override
    public void print(Object obj) {
      _printSetup();
      super.print(obj);
      _printReset();
    }

    @Override
    public void println(boolean x) {
      _printSetup();
      super.print(x);
      _printReset();
      super.println();
    }

    @Override
    public void println(char x) {
      _printSetup();
      super.print(x);
      _printReset();
      super.println();
    }

    @Override
    public void println(int x) {
      _printSetup();
      super.print(x);
      _printReset();
      super.println();
    }

    @Override
    public void println(long x) {
      _printSetup();
      super.print(x);
      _printReset();
      super.println();
    }

    @Override
    public void println(float x) {
      _printSetup();
      super.print(x);
      _printReset();
      super.println();
    }

    @Override
    public void println(double x) {
      _printSetup();
      super.print(x);
      _printReset();
      super.println();
    }

    @Override
    public void println(char[] x) {
      _printSetup();
      super.print(x);
      _printReset();
      super.println();
    }

    @Override
    public void println(String x) {
      _printSetup();
      super.print(x);
      _printReset();
      super.println();
    }

    @Override
    public void println(Object x) {
      _printSetup();
      super.print(x);
      _printReset();
      super.println();
    }

    @Override
    public PrintStream format(String format, Object... args) {
      return super.format(_wrapFormat(format), args);
    }

    @Override
    public PrintStream format(Locale l, String format, Object... args) {
      return super.format(l, _wrapFormat(format), args);
    }
  }

  private int _firstValid(int colorNum, int[] backups) {
    if (!m_enabled) {
      return -1;
    }
    if (colorNum < m_supportedColors) {
      return colorNum;
    }
    for (int n : backups) {
      if (n < m_supportedColors) {
        return n;
      }
    }
    return -1;
  }

  public static void main(String[] args) {
    TerminalColors terminalColors = new TerminalColors();
    if (terminalColors.m_enabled) {
      for (int i = 0; i < terminalColors.m_supportedColors; i++) {
        terminalColors.printStream().foreground(i).print(" " + i);
      }
      terminalColors.printStream().println();
    } else {
      System.out.println("Not enabled.");
    }
  }
}
