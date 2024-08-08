package org.ethelred.util.collect

import spock.lang.Specification

class SequencesTest extends Specification {
    List<Integer> LIST = List.copyOf(1..5)

    def heads(int index, List<Integer> expected) {
        expect:
            Sequences.head(LIST, index) == expected

        where:
        index | expected
        2 | 1..2
        -1 | 1..4
        -2 | 1..3
    }

    def tails(int index, List<Integer> expected) {
        expect:
        Sequences.tail(LIST, index) == expected

        where:
        index | expected
        2 | 4..5
        -1 | 2..5
        -2 | 3..5
    }

    def prefixes() {
        when:
        def prefixes = Sequences.prefixes(LIST)

        then:
        prefixes.size() == 6
        prefixes[0] == []
        prefixes[3] == 1..3
    }

    def concat() {
        expect:
        Sequences.concat([1, 2, 3], 4, 5) == [1, 2, 3, 4, 5]
    }
}
