package org.ethelred.util.stream;

import static org.testng.Assert.*;

import java.util.Comparator;
import java.util.stream.Stream;
import org.testng.annotations.Test;

/** Test SummaryStatistics */
public class SummaryStatisticsTest {
  @Test
  public void testEmpty() {
    SummaryStatistics<?> stats = Stream.<String>of().collect(SummaryStatistics.collector());
    assertEquals(stats.getCount(), 0);
    assertNull(stats.getMax());
    assertNull(stats.getMin());
  }

  @Test
  public void testSingle() {
    SummaryStatistics<String> stats = Stream.of("one").collect(SummaryStatistics.collector());
    assertEquals(stats.getCount(), 1);
    assertEquals(stats.getMax(), "one");
    assertEquals(stats.getMin(), "one");
  }

  @Test
  public void testMore() {
    SummaryStatistics<String> stats =
        Stream.of("one", "two", "three").collect(SummaryStatistics.collector());
    assertEquals(stats.getCount(), 3);
    assertEquals(stats.getMax(), "two");
    assertEquals(stats.getMin(), "one");
  }

  @Test
  public void testComparator() {
    SummaryStatistics<String> stats =
        Stream.of("one", "two", "three")
            .collect(SummaryStatistics.collector(Comparator.comparing(String::length)));
    assertEquals(stats.getCount(), 3);
    assertEquals(stats.getMax(), "three");
    assertEquals(stats.getMin(), "one");
  }
}
