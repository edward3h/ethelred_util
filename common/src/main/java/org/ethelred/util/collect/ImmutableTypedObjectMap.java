/* (C) 2024 */
package org.ethelred.util.collect;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;

/** Created by edward on 3/14/17. */
@SuppressWarnings("unchecked")
@NullMarked
public class ImmutableTypedObjectMap implements TypedObjectMap {
    private final Map<TypedObjectKey<?>, Object> map;

    private ImmutableTypedObjectMap(Map<TypedObjectKey<?>, Object> map) {
        this.map = Map.copyOf(map);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Map<TypedObjectKey<?>, Object> innerBuilder = new LinkedHashMap<>();

        public <T> Builder put(TypedObjectKey<T> key, T value) {
            innerBuilder.put(key, value);
            return this;
        }

        public Builder set(TypedObjectKey<Boolean> flag) {
            return put(flag, true);
        }

        public ImmutableTypedObjectMap build() {
            return new ImmutableTypedObjectMap(innerBuilder);
        }
    }

    @Override
    public <T> Optional<T> get(TypedObjectKey<T> key) {
        return Optional.ofNullable((T) map.get(key));
    }
}
