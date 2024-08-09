package org.ethelred.util.collect

import spock.lang.Specification
import java.util.stream.Stream
import static org.ethelred.util.collect.BiMap.entry

class BiMapTest extends Specification {
    def "map constructor simple"() {
        when:
        BiMap test = new BiMap([1:'one', 2:'two', 3:'three'])

        then:
        test.size() == 3
        test.getByA(1).isPresent()
        test.getByA(1).get() == 'one'
        test.getByB('two').isPresent()
        test.getByB('two').get() == 2
        test.getByB(1).isEmpty()
        test.getByA(4).isEmpty()
        test.mapByA() == [1:'one', 2:'two', 3:'three']
        test.mapByB() == ['one':1, 'two':2, 'three':3]
        test.keysA() == Set.of(1, 2, 3)
        test.keysB() == Set.of('one', 'two', 'three')
    }

    def "entry constructor simple"() {
        when:
        BiMap test = BiMap.ofEntries(entry(1, 'one'), entry(2, 'two'), entry(3, 'three'))

        then:
        test.size() == 3
        test.getByA(1).isPresent()
        test.getByA(1).get() == 'one'
        test.getByB('two').isPresent()
        test.getByB('two').get() == 2
        test.getByB(1).isEmpty()
        test.getByA(4).isEmpty()
    }

    def "stream collector simple"() {
        when:
        BiMap test = Stream.of(entry(1, 'one'), entry(2, 'two'), entry(3, 'three')).collect(BiMap.toBiMap())

        then:
        test.size() == 3
        test.getByA(1).isPresent()
        test.getByA(1).get() == 'one'
        test.getByB('two').isPresent()
        test.getByB('two').get() == 2
        test.getByB(1).isEmpty()
        test.getByA(4).isEmpty()
    }

    def "dupe in A"() {
        when:
        BiMap.ofEntries(entry(1, 'one'), entry(2, 'two'), entry(1, 'other'))

        then:
        IllegalArgumentException e = thrown()
        e.message.contains('(1, other)')
    }

    def "dupe in B"() {
        when:
        new BiMap([1:'one', 2:'two', 3:'one'])

        then:
        IllegalArgumentException e = thrown()
        e.message.contains('(3, one)')
    }

    def "null in A"() {
        when:
        BiMap.ofEntries(entry(1, 'one'), entry(null, 'two'), entry(1, 'other'))

        then:
        thrown(NullPointerException)
    }

    def "null in B"() {
        when:
        new BiMap([1:'one', 2:null, 3:'one'])

        then:
        thrown(NullPointerException)
    }
}
