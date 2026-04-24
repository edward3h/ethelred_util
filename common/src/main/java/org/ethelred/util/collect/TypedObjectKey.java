/* (C) 2024 */
package org.ethelred.util.collect;

import java.util.Objects;

/** */
public interface TypedObjectKey<T> {
    static <T> TypedObjectKey<T> identity() {
        return new IdentityTypedObjectKey<>();
    }

    static <V, T> TypedObjectKey<T> valued(V value) {
        return new ValuedTypedObjectKey<V, T>(value);
    }

    final class IdentityTypedObjectKey<TT> implements TypedObjectKey<TT> {
        // implementation not needed, Object hashCode and equals do what we want
    }

    final class ValuedTypedObjectKey<V, TT> implements TypedObjectKey<TT> {
        private final V value;

        ValuedTypedObjectKey(V value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + value.hashCode();
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            ValuedTypedObjectKey<?, ?> other = (ValuedTypedObjectKey<?, ?>) obj;
            return Objects.equals(value, other.value);
        }
    }
}
