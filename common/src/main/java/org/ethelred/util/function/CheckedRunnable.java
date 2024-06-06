package org.ethelred.util.function;

/**
 * Checked wrapper for a Runnable
 * @param <E> A checked exception type that is thrown by the operation
 */
@FunctionalInterface
public interface CheckedRunnable<E extends Throwable> {
  void run() throws E;

  default Runnable asUnchecked() {
    return () -> {
      try {
        run();
      } catch (Throwable e) {
        throw new WrappedCheckedException(e);
      }
    };
  }

  static <EE extends Throwable> Runnable unchecked(CheckedRunnable<EE> runnable) {
    return runnable.asUnchecked();
  }
}
