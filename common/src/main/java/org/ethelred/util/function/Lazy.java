package org.ethelred.util.function;

import java.util.*;
import java.util.function.*;

/**
 * NOT THREAD SAFE - intended to be used in single request scope
 *
 * @param <T> The type that will be created and supplied
 */
public class Lazy<T> implements Supplier<T> {
  private final Supplier<T> supplier;
  private boolean got = false;
  private boolean inGet = false;
  private T value;

  private Lazy(Supplier<T> supplier) {
    this.supplier = supplier;
  }

  public static <V> Lazy<V> lazy(Supplier<V> supplier) {
    return new Lazy<>(Objects.requireNonNull(supplier));
  }

  public static <V, A> Lazy<V> lazy(A argument, Function<A, V> initializer) {
    Objects.requireNonNull(initializer);
    return new Lazy<>(() -> initializer.apply(argument));
  }

  public static <V, A> Lazy<V> lazy(Supplier<A> argument, Function<A, V> initializer) {
    Objects.requireNonNull(argument);
    Objects.requireNonNull(initializer);
    return new Lazy<>(() -> initializer.apply(argument.get()));
  }

  public T get() {
    if (inGet) {
      throw new IllegalStateException("Cycle in Lazy.get()");
    }
    if (!got) {
      got = true;
      inGet = true;
      value = supplier.get();
      inGet = false;
    }
    return value;
  }
}
