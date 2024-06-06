/* (C) 2024 */
package org.ethelred.util.collect;

import java.util.Optional;
import java.util.function.Consumer;

/** Created by edward on 3/12/17. */
public interface TypedObjectMap {
    <T> Optional<T> get(TypedObjectKey<T> key);

    default boolean isSet(TypedObjectKey<Boolean> flag) {
        Optional<Boolean> value = get(flag);
        return value.isPresent() && value.get();
    }

    default <T> void with(TypedObjectKey<T> key, T value, Consumer<TypedObjectMap> consumer) {
        consumer.accept(new ValuedTypedObjectMap(this, key, value));
    }

    default void withFlag(TypedObjectKey<Boolean> flag, Consumer<TypedObjectMap> consumer) {
        with(flag, true, consumer);
    }
}
