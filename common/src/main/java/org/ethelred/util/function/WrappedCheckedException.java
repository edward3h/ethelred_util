/* (C) 2024 */
package org.ethelred.util.function;

/**
 * The exception thrown from the 'Checked' wrappers in this package.
 */
public class WrappedCheckedException extends RuntimeException {

    /** */
    private static final long serialVersionUID = 7075961307847517795L;

    public WrappedCheckedException(Throwable cause) {
        super(cause);
    }
}
