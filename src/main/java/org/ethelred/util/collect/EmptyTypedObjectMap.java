package org.ethelred.util.collect;

import java.util.Optional;

/** Created by edward on 3/14/17. */
public class EmptyTypedObjectMap implements TypedObjectMap {
  @Override
  public <T> Optional<T> get(TypedObjectKey<T> key) {
    return Optional.empty();
  }
}
