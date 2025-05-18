/* (C) 2025 */
package org.ethelred.util.collect;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Like {@link Map#of}, but can produce copies with elements added.
 * @param <K> key type
 * @param <V> value type
 */
public class ExpandableMap<K, V> extends AbstractMap<K, V> {
    @Override
    public boolean containsKey(Object key) {
        return delegate.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return delegate.entrySet();
    }

    @Override
    public V get(Object key) {
        return delegate.get(key);
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return delegate.equals(o);
    }

    @Override
    public Set<K> keySet() {
        return delegate.keySet();
    }

    public static <K1, V1> ExpandableMap<K1, V1> of() {
        return new ExpandableMap<>(Map.of());
    }

    public static <K1, V1> ExpandableMap<K1, V1> of(K1 k1, V1 v1) {
        return new ExpandableMap<>(Map.of(k1, v1));
    }

    public static <K1, V1> ExpandableMap<K1, V1> of(K1 k1, V1 v1, K1 k2, V1 v2) {
        return new ExpandableMap<>(Map.of(k1, v1, k2, v2));
    }

    public static <K1, V1> ExpandableMap<K1, V1> of(K1 k1, V1 v1, K1 k2, V1 v2, K1 k3, V1 v3) {
        return new ExpandableMap<>(Map.of(k1, v1, k2, v2, k3, v3));
    }

    public static <K1, V1> ExpandableMap<K1, V1> of(K1 k1, V1 v1, K1 k2, V1 v2, K1 k3, V1 v3, K1 k4, V1 v4) {
        return new ExpandableMap<>(Map.of(k1, v1, k2, v2, k3, v3, k4, v4));
    }

    public static <K1, V1> ExpandableMap<K1, V1> of(
            K1 k1, V1 v1, K1 k2, V1 v2, K1 k3, V1 v3, K1 k4, V1 v4, K1 k5, V1 v5) {
        return new ExpandableMap<>(Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
    }

    public static <K1, V1> ExpandableMap<K1, V1> of(
            K1 k1, V1 v1, K1 k2, V1 v2, K1 k3, V1 v3, K1 k4, V1 v4, K1 k5, V1 v5, K1 k6, V1 v6) {
        return new ExpandableMap<>(Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6));
    }

    public static <K1, V1> ExpandableMap<K1, V1> of(
            K1 k1, V1 v1, K1 k2, V1 v2, K1 k3, V1 v3, K1 k4, V1 v4, K1 k5, V1 v5, K1 k6, V1 v6, K1 k7, V1 v7) {
        return new ExpandableMap<>(Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7));
    }

    public static <K1, V1> ExpandableMap<K1, V1> of(
            K1 k1,
            V1 v1,
            K1 k2,
            V1 v2,
            K1 k3,
            V1 v3,
            K1 k4,
            V1 v4,
            K1 k5,
            V1 v5,
            K1 k6,
            V1 v6,
            K1 k7,
            V1 v7,
            K1 k8,
            V1 v8) {
        return new ExpandableMap<>(Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8));
    }

    public static <K1, V1> ExpandableMap<K1, V1> of(
            K1 k1,
            V1 v1,
            K1 k2,
            V1 v2,
            K1 k3,
            V1 v3,
            K1 k4,
            V1 v4,
            K1 k5,
            V1 v5,
            K1 k6,
            V1 v6,
            K1 k7,
            V1 v7,
            K1 k8,
            V1 v8,
            K1 k9,
            V1 v9) {
        return new ExpandableMap<>(Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9));
    }

    public static <K1, V1> ExpandableMap<K1, V1> of(
            K1 k1,
            V1 v1,
            K1 k2,
            V1 v2,
            K1 k3,
            V1 v3,
            K1 k4,
            V1 v4,
            K1 k5,
            V1 v5,
            K1 k6,
            V1 v6,
            K1 k7,
            V1 v7,
            K1 k8,
            V1 v8,
            K1 k9,
            V1 v9,
            K1 k10,
            V1 v10) {
        return new ExpandableMap<>(
                Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10));
    }

    @SafeVarargs
    public static <K1, V1> ExpandableMap<K1, V1> ofEntries(Entry<? extends K1, ? extends V1>... entries) {
        return new ExpandableMap<>(Map.ofEntries(entries));
    }

    public ExpandableMap<K, V> with(K key, V value, Object... more) {
        if (more.length % 2 != 0) {
            throw new IllegalArgumentException("Expected even number of arguments");
        }
        Map<K, V> mutable = new HashMap<>(delegate);
        mutable.put(key, value);
        for (int i = 0; i < more.length; i += 2) {
            mutable.put((K) more[i], (V) more[i + 1]);
        }
        return new ExpandableMap<>(Map.copyOf(mutable));
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public Collection<V> values() {
        return delegate.values();
    }

    private final Map<K, V> delegate;

    private ExpandableMap(Map<K, V> delegate) {
        this.delegate = delegate;
    }
}
