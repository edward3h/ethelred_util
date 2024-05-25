package org.ethelred.util.function;

import java.util.function.Consumer;

/** Created by edward on 6/7/16. */
public interface CheckedConsumer<T, E extends Throwable> {
  void accept(T t) throws E;

  default Consumer<T> asUnchecked() {
    return t -> {
      try {
        accept(t);
      } catch (Throwable e) {
        throw new WrappedCheckedException(e);
      }
    };
  }

  static <TT, EE extends Throwable> Consumer<TT> unchecked(
      CheckedConsumer<TT, EE> checkedConsumer) {
    return checkedConsumer.asUnchecked();
  }
}
