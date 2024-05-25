package org.ethelred.util.collect;

import java.util.Optional;

/** Created by edward on 3/14/17. */
public class ValuedTypedObjectMap implements TypedObjectMap {
  private final TypedObjectMap delegate;
  private final TypedObjectKey<?> key;
  private final Object value;

  public <T> ValuedTypedObjectMap(TypedObjectMap delegate, TypedObjectKey<T> key, T value) {
    this.delegate = delegate;
    this.key = key;
    this.value = value;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> Optional<T> get(TypedObjectKey<T> key) {
    if (this.key.equals(key)) {
      return Optional.ofNullable((T) value);
    }
    return delegate.get(key);
  }
}
