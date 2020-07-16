package org.ethelred.util.function;

public class WrappedCheckedException extends RuntimeException {

  /** */
  private static final long serialVersionUID = 7075961307847517795L;

  public WrappedCheckedException(Throwable cause) {
    super(cause);
  }
}
