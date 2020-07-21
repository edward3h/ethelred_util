package org.ethelred.util.function;

import java.util.function.Supplier;

@FunctionalInterface
public interface CheckedSupplier<T, E extends Throwable> {
    T get() throws E;

    default Supplier<T> asUnchecked() {
        return () -> {
            try {
            return get();
        } catch (Throwable e) {
            throw new WrappedCheckedException(e);
        }
    };
}
    static <TT, EE extends Throwable> Supplier<TT> unchecked(
        CheckedSupplier<TT, EE> supplier
    ) {
        return supplier.asUnchecked();
    }
}