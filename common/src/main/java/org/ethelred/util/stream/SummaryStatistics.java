package org.ethelred.util.stream;

import java.util.Comparator;
import java.util.stream.Collector;
import javax.annotation.CheckForNull;

/** Stream / Collector for generic stats */
public class SummaryStatistics<T> {
  private final Comparator<T> comparator;
  private int count;
  private T min;
  private T max;

  private SummaryStatistics(Comparator<T> comparator) {
    this.comparator = comparator;
  }

  public static <A extends Comparable<? super A>>
      Collector<A, ?, SummaryStatistics<A>> collector() {
    return collector(Comparator.<A>naturalOrder());
  }

  public static <B> Collector<B, ?, SummaryStatistics<B>> collector(Comparator<B> comparator) {
    return Collector.of(
        () -> new SummaryStatistics<>(comparator),
        SummaryStatistics::add,
        SummaryStatistics::combine);
  }

  private void add(T t) {
    count++;
    _min(t);
    _max(t);
  }

  private void _max(T t) {
    if (max == null || comparator.compare(t, max) > 0) {
      max = t;
    }
  }

  private void _min(T t) {
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

  @CheckForNull
  public T getMin() {
    return min;
  }

  @CheckForNull
  public T getMax() {
    return max;
  }

  public int getCount() {
    return count;
  }
}
