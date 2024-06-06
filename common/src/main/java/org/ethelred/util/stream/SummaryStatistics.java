/* (C) 2024 */
package org.ethelred.util.stream;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collector;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * A stream Collector that captures basic statistics in a single result. Currently, count, max and min.
 */
@NullMarked
public class SummaryStatistics<T> {
    private final Comparator<T> comparator;
    private int count;

    @Nullable private T min;

    @Nullable private T max;

    private SummaryStatistics(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    /**
     * A Collector that uses the natural order to determine max and min.
     * @return Collector producing SummaryStatistics.
     * @param <A> The type of elements in the Stream
     */
    public static <A extends Comparable<? super A>> Collector<A, ?, SummaryStatistics<A>> collector() {
        return collector(Comparator.<A>naturalOrder());
    }

    /**
     * A Collector that uses an explicit Comparator to determine max and min.
     * @param comparator Ordering to use
     * @return Collector producing SummaryStatistics.
     * @param <B> The type of elements in the Stream
     */
    public static <B> Collector<B, ?, SummaryStatistics<B>> collector(Comparator<B> comparator) {
        return Collector.of(
                () -> new SummaryStatistics<>(comparator), SummaryStatistics::add, SummaryStatistics::combine);
    }

    private void add(T t) {
        count++;
        _min(t);
        _max(t);
    }

    private void _max(@Nullable T t) {
        if (t == null) {
            return;
        }
        if (max == null || comparator.compare(t, max) > 0) {
            max = t;
        }
    }

    private void _min(@Nullable T t) {
        if (t == null) {
            return;
        }
        if (min == null || comparator.compare(t, min) < 0) {
            min = t;
        }
    }

    private SummaryStatistics<T> combine(SummaryStatistics<T> other) {
        count += other.count;
        _min(other.min);
        _max(other.max);
        return this;
    }

    /**
     *
     * @return The minimum valued element from the Stream. Empty if the Stream was empty.
     */
    public Optional<T> getMin() {
        return Optional.ofNullable(min);
    }

    /**
     *
     * @return The maximum valued element from the Stream. Empty if the Stream was empty.
     */
    public Optional<T> getMax() {
        return Optional.ofNullable(max);
    }

    /**
     *
     * @return Count of elements from the Stream
     */
    public int getCount() {
        return count;
    }
}
