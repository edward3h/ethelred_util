/* (C) 2024 */
package org.ethelred.util.function;

import java.util.function.Consumer;

/**
 * Checked wrapper for a Consumer.
 *
 * @param <T> The consumed type
 * @param <E> A checked exception type that is thrown by the operation
 */
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

    static <TT, EE extends Throwable> Consumer<TT> unchecked(CheckedConsumer<TT, EE> checkedConsumer) {
        return checkedConsumer.asUnchecked();
    }
}
