package org.ethelred.util.stream

import spock.lang.Specification
import java.util.stream.Stream

class SummaryStatisticsTest extends Specification {
    def "empty stream"() {
        when:
        def stats = Stream.of().collect(SummaryStatistics.collector())

        then:
        stats.count == 0
        !stats.max
        !stats.min
    }

    def "single item stream"() {
        when:
        def stats = Stream.of("one").collect(SummaryStatistics.collector())

        then:
        stats.count == 1
        stats.max.get() == 'one'
        stats.min.get() == 'one'
    }

    def "a few items stream"() {
        when:
        def stats = Stream.of("one", "two", "three").collect(SummaryStatistics.collector())

        then:
        stats.count == 3
        stats.max.get() == 'two'
        stats.min.get() == 'one'
    }

    def "a few items stream with custom comparator"() {
        when:
        def stats = Stream.of("one", "two", "three").collect(SummaryStatistics.collector(Comparator.comparing(String::length)))

        then:
        stats.count == 3
        stats.max.get() == 'three'
        stats.min.get() == 'one'
    }
}