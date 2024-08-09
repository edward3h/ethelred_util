/* (C) 2024 */
package org.ethelred.util.collect;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * A bidirectional mapping. Does not implement Map because I don't find that useful now. May not contain nulls or
 * duplicate keys/values. Immutable.
 * @param <A> Type of one of the keys/values
 * @param <B> Type of the other keys/values
 */
public class BiMap<A, B> {
    private final Map<A, B> first;
    private final Map<B, A> second;

    /**
     * Construct a BiMap from the entries in map.
     * @param map entries to use
     * @throws NullPointerException when any key or value in map is null
     * @throws IllegalArgumentException when there are duplicate keys or values
     */
    public BiMap(Map<A, B> map) {
        this(
                map.entrySet().stream()
                        .map(mapEntry -> entry(mapEntry.getKey(), mapEntry.getValue()))
                        .collect(Collectors.toList()),
                map.size());
    }

    private BiMap(Iterable<Entry<A, B>> entries, int size) {
        first = new HashMap<>(size);
        second = new HashMap<>(size);
        entries.forEach(e -> {
            var a = e.a;
            var b = e.b;
            if (first.containsKey(Objects.requireNonNull(a)) || second.containsKey(Objects.requireNonNull(b))) {
                throw new IllegalArgumentException(String.format("Duplicate value in (%s, %s)", a, b));
            }
            first.put(a, b);
            second.put(b, a);
        });
    }

    public int size() {
        // The constructor invariants ensure that first and second have the same size
        return first.size();
    }

    /**
     * Get the B value corresponding to the A key.
     * @param key A key
     * @return Optional containing value, empty if not found
     */
    public Optional<B> getByA(A key) {
        return Optional.ofNullable(first.get(key));
    }

    /**
     * Get the A value corresponding to the B key.
     * @param key B key
     * @return Optional containing value, empty if not found
     */
    public Optional<A> getByB(B key) {
        return Optional.ofNullable(second.get(key));
    }

    /**
     * Construct an entry for building a BiMap.
     * @param a one of the keys/values
     * @param b the other key/value
     * @return an entry
     * @param <A> the type of the first key/value
     * @param <B> the type of the second key/value
     */
    public static <A, B> Entry<A, B> entry(A a, B b) {
        return new Entry<>(Objects.requireNonNull(a), Objects.requireNonNull(b));
    }

    /**
     * Construct a BiMap from given entries.
     * @param entries entries to use
     * @throws IllegalArgumentException when there are duplicate keys or values
     */
    public static <A, B> BiMap<A, B> ofEntries(Iterable<Entry<A, B>> entries) {
        return new BiMap<>(entries, 0);
    }

    /**
     * Construct a BiMap from given entries.
     * @param entries entries to use
     * @throws IllegalArgumentException when there are duplicate keys or values
     */
    public static <A, B> BiMap<A, B> ofEntries(Collection<Entry<A, B>> entries) {
        return new BiMap<>(entries, entries.size());
    }

    /**
     * Construct a BiMap from given entries.
     * @param entries entries to use
     * @throws IllegalArgumentException when there are duplicate keys or values
     */
    @SafeVarargs
    public static <A, B> BiMap<A, B> ofEntries(Entry<A, B>... entries) {
        return new BiMap<>(List.of(entries), entries.length);
    }

    /**
     *
     * @return An unmodifiable view of the mapping from A keys
     */
    public Map<A, B> mapByA() {
        return Collections.unmodifiableMap(first);
    }

    /**
     *
     * @return An unmodifiable view of the mapping from B keys
     */
    public Map<B, A> mapByB() {
        return Collections.unmodifiableMap(second);
    }

    /**
     *
     * @return An unmodifiable view of the set of A keys
     */
    public Set<A> keysA() {
        return Collections.unmodifiableSet(first.keySet());
    }

    /**
     *
     * @return An unmodifiable view of the set of B keys
     */
    public Set<B> keysB() {
        return Collections.unmodifiableSet(second.keySet());
    }

    /**
     *
     * @return Collect a stream of Entry into a BiMap
     * @param <A> Type of one of the keys/values
     * @param <B> Type of the other keys/values
     */
    public static <A, B> Collector<Entry<A, B>, ?, BiMap<A, B>> toBiMap() {
        return Collectors.collectingAndThen(Collectors.toList(), BiMap::ofEntries);
    }

    public static class Entry<A, B> {
        private final A a;
        private final B b;

        private Entry(A a, B b) {
            this.a = a;
            this.b = b;
        }
    }
}
