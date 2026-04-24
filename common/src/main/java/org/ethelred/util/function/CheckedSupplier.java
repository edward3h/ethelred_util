/* (C) 2024 */
package org.ethelred.util.function;

import java.util.function.Supplier;

/**
 * Checked wrapper for a Supplier.
 *
 * @param <T> The type returned by the operation
 * @param <E> A checked exception type that is thrown by the operation
 */
@FunctionalInterface
public interface CheckedSupplier<T, E extends Exception> {
    T get() throws E;

    default Supplier<T> asUnchecked() {
        return () -> {
            try {
                return get();
            } catch (Exception e) {
                throw new WrappedCheckedException(e);
            }
        };
    }

    static <TT, EE extends Exception> Supplier<TT> unchecked(CheckedSupplier<TT, EE> supplier) {
        return supplier.asUnchecked();
    }
}
