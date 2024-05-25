package org.ethelred.util.function;

import java.util.function.Function;

/** Created by edward on 6/7/16. */
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
