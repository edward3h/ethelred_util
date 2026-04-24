package org.ethelred.util.collect

import spock.lang.Specification

class ExpandableMapTest extends Specification {

    def "empty map has size 0"() {
        when:
        def map = ExpandableMap.of()

        then:
        map.isEmpty()
        map.size() == 0
    }

    def "of(k, v) creates a single-entry map"() {
        when:
        def map = ExpandableMap.of("key", "value")

        then:
        map.size() == 1
        map["key"] == "value"
    }

    def "with returns a new map containing the added entry"() {
        given:
        def original = ExpandableMap.of("a", 1)

        when:
        def extended = original.with("b", 2)

        then:
        extended.size() == 2
        extended["a"] == 1
        extended["b"] == 2
    }

    def "with does not modify the original map"() {
        given:
        def original = ExpandableMap.of("a", 1)

        when:
        original.with("b", 2)

        then:
        original.size() == 1
        !original.containsKey("b")
    }

    def "with accepts additional key-value pairs"() {
        given:
        def original = ExpandableMap.<String, Integer> of()

        when:
        def extended = original.with("a", 1, "b", 2, "c", 3)

        then:
        extended.size() == 3
        extended["a"] == 1
        extended["b"] == 2
        extended["c"] == 3
    }

    def "with odd number of extra args throws IllegalArgumentException"() {
        given:
        def map = ExpandableMap.of()

        when:
        map.with("a", 1, "orphan")

        then:
        thrown(IllegalArgumentException)
    }

    def "map is immutable"() {
        given:
        def map = ExpandableMap.of("a", 1)

        when:
        map.put("b", 2)

        then:
        thrown(UnsupportedOperationException)
    }

    def "ofEntries creates a map from entries"() {
        when:
        def map = ExpandableMap.ofEntries(Map.entry("x", 10), Map.entry("y", 20))

        then:
        map.size() == 2
        map["x"] == 10
        map["y"] == 20
    }
}
