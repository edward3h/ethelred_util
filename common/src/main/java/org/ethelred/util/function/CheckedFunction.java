package org.ethelred.util.function;

import java.util.function.Function;

/**
 * Checked wrapper for a Function.
 *
 * @param <T> The input type for the operation
 * @param <R> The return type for the operation
 * @param <E> A checked exception type that is thrown by the operation
 * */
public interface CheckedFunction<T, R, E extends Throwable> {
  R apply(T t) throws E;

  default Function<T, R> asUnchecked() {
    return t -> {
      try {
        return apply(t);
      } catch (Throwable e) {
        throw new WrappedCheckedException(e);
      }
    };
  }

  static <TT, RR, EE extends Throwable> Function<TT, RR> unchecked(
      CheckedFunction<TT, RR, EE> checkedFunction) {
    return checkedFunction.asUnchecked();
  }
}
