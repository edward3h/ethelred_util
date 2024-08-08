/* (C) 2024 */
package org.ethelred.util.collect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.IntFunction;

/**
 * Utility functions for Lists.
 */
public class Sequences {
    /**
     * Static utilities. Prevent instantiation
     */
    private Sequences() {}

    /**
     * Get all possible prefixes of an Iterable.
     * For example, if the parameter is a list [1,2,3], the result will be [[],[1],[1,2],[1,2,3]].
     * @param iterable The starting value
     * @return A list of lists
     * @param <T> the type of elements
     */
    public static <T> List<List<T>> prefixes(Iterable<T> iterable) {
        var acc = new ArrayList<T>();
        var r = new ArrayList<List<T>>();
        r.add(List.of());
        for (var item : iterable) {
            acc.add(item);
            r.add(List.copyOf(acc));
        }
        return r;
    }

    /**
     * Join two Collections into one.
     * @param a first collection
     * @param b second collection
     * @param collectionSupplier Function to construct the collection to return. The int parameter is the expected size of the resulting collection.
     * @return the combined collection
     * @param <T> the type of elements
     * @param <C> the type of Collection to return
     */
    public static <T, C extends Collection<T>> C concat(
            Collection<? extends T> a, Collection<? extends T> b, IntFunction<C> collectionSupplier) {
        var result = collectionSupplier.apply(a.size() + b.size());
        result.addAll(a);
        result.addAll(b);
        return result;
    }

    /**
     * Join two Lists into one. The order will be all elements of a followed by all elements of b.
     * @param a first list
     * @param b second list
     * @return Combined list
     * @param <T> the type of elements
     */
    public static <T> List<T> concat(List<? extends T> a, List<? extends T> b) {
        return concat(a, b, ArrayList::new);
    }

    /**
     * Join some items to the end of a List.
     * @param list original list
     * @param items items to add to the list
     * @return Combined list
     * @param <T> the type of elements
     */
    @SafeVarargs
    public static <T> List<T> concat(List<? extends T> list, T... items) {
        return concat(list, List.of(items));
    }

    /**
     * Retrieve items from the start of a List.
     * @param list list to draw from
     * @param items When positive, return that many items from the start. When negative, return items from the start of the list except for that number at the end. When 0, return an empty list, though I'm not sure why you would want to do that. If greater than the size of the list, the original list is returned.
     * @return resulting list
     * @param <T> the type of elements
     */
    public static <T> List<T> head(List<T> list, int items) {
        if (items == 0) {
            return List.of();
        }
        if (Math.abs(items) >= list.size()) {
            return list;
        }
        if (items > 0) {
            return list.subList(0, items);
        }
        // items < 0
        return list.subList(0, list.size() + items);
    }

    /**
     * Like {@link #head(List, int) head}, except it works backwards from the end of the list.
     */
    public static <T> List<T> tail(List<T> list, int items) {
        if (items == 0) {
            return List.of();
        }
        if (Math.abs(items) >= list.size()) {
            return list;
        }
        if (items > 0) {
            return list.subList(list.size() - items, list.size());
        }
        // items < 0
        return list.subList(-items, list.size());
    }
}
