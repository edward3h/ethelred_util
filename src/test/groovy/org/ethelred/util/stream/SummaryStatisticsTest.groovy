package org.ethelred.util.stream

import spock.lang.Specification
import java.util.stream.Stream

class SummaryStatisticsTest extends Specification {
    def "empty stream"() {
        when:
        def stats = Stream.of().collect(SummaryStatistics.collector())

        then:
        stats.count == 0
        stats.max == null
        stats.min == null
    }

    def "single item stream"() {
        when:
        def stats = Stream.of("one").collect(SummaryStatistics.collector())

        then:
        stats.count == 1
        stats.max == 'one'
        stats.min == 'one'
    }

    def "a few items stream"() {
        when:
        def stats = Stream.of("one", "two", "three").collect(SummaryStatistics.collector())

        then:
        stats.count == 3
        stats.max == 'two'
        stats.min == 'one'
    }

    def "a few items stream with custom comparator"() {
        when:
        def stats = Stream.of("one", "two", "three").collect(SummaryStatistics.collector(Comparator.comparing(String::length)))

        then:
        stats.count == 3
        stats.max == 'three'
        stats.min == 'one'
    }
}