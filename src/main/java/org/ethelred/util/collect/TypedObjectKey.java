package org.ethelred.util.collect;

/** */
public interface TypedObjectKey<T> {
  public static <T> TypedObjectKey<T> identity() {
    return new IdentityTypedObjectKey<>();
  }

  public static <V, T> TypedObjectKey<T> valued(V value) {
    return new ValuedTypedObjectKey<V, T>(value);
  }

  static class IdentityTypedObjectKey<TT> implements TypedObjectKey<TT> {
    // implementation not needed, Object hashCode and equals do what we want
  }

  static class ValuedTypedObjectKey<V, TT> implements TypedObjectKey<TT> {
    private V value;

    ValuedTypedObjectKey(V value) {
      this.value = value;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((value == null) ? 0 : value.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;
      ValuedTypedObjectKey other = (ValuedTypedObjectKey) obj;
      if (value == null) {
        if (other.value != null) return false;
      } else if (!value.equals(other.value)) return false;
      return true;
    }
  }
}
