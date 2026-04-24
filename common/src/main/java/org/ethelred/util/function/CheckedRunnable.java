/* (C) 2024 */
package org.ethelred.util.function;

/**
 * Checked wrapper for a Runnable
 * @param <E> A checked exception type that is thrown by the operation
 */
@FunctionalInterface
public interface CheckedRunnable<E extends Exception> {
    void run() throws E;

    default Runnable asUnchecked() {
        return () -> {
            try {
                run();
            } catch (Exception e) {
                throw new WrappedCheckedException(e);
            }
        };
    }

    static <EE extends Exception> Runnable unchecked(CheckedRunnable<EE> runnable) {
        return runnable.asUnchecked();
    }
}
