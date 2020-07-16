package org.ethelred.util.collect;

import com.google.common.collect.ImmutableMap;
import java.util.Optional;

/** Created by edward on 3/14/17. */
@SuppressWarnings("unchecked")
public class ImmutableTypedObjectMap implements TypedObjectMap {
  private final ImmutableMap<TypedObjectKey<?>, Object> map;

  private ImmutableTypedObjectMap(ImmutableMap<TypedObjectKey<?>, Object> map) {
    this.map = map;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private final ImmutableMap.Builder<TypedObjectKey<?>, Object> innerBuilder =
        ImmutableMap.builder();

    public <T> Builder put(TypedObjectKey<T> key, T value) {
      innerBuilder.put(key, value);
      return this;
    }

    public Builder set(TypedObjectKey<Boolean> flag) {
      return put(flag, true);
    }

    public ImmutableTypedObjectMap build() {
      return new ImmutableTypedObjectMap(innerBuilder.build());
    }
  }

  @Override
  public <T> Optional<T> get(TypedObjectKey<T> key) {
    return Optional.ofNullable((T) map.get(key));
  }
}
