package org.ethelred.util.collect

import spock.lang.Specification

import java.time.LocalDate

class TypedObjectMapTest extends Specification {
    def "empty map"() {
        given:
        TypedObjectKey<String> ks = TypedObjectKey.identity()
        TypedObjectKey<LocalDate> kd = TypedObjectKey.identity()
        TypedObjectKey<Boolean> kflag = TypedObjectKey.identity()

        when:
        TypedObjectMap m = new EmptyTypedObjectMap()

        then:
        !m.get(ks)
        !m.get(kd)
        !m.isSet(kflag)

        expect:
        m.with(kd, LocalDate.of(2012,6, 10)) { map ->
            assert !map.get(ks)
            assert map.get(kd).get() == LocalDate.of(2012, 6, 10)
            assert !m.isSet(kflag)
        }
    }

    def "empty map with a valued key"() {
        given:
        TypedObjectKey<LocalDate> kdv = TypedObjectKey.valued("date1")
        TypedObjectMap mv = new EmptyTypedObjectMap()

        expect:
        !mv.get(kdv)

        mv.with(kdv, LocalDate.of(2012, 6, 10)) { map ->
            TypedObjectKey<LocalDate> kdv2 = TypedObjectKey.valued("date1")
            assert map.get(kdv2).get() == LocalDate.of(2012, 6, 10)
            try {
                TypedObjectKey<Integer> kiv2 = TypedObjectKey.valued("date1")
                Optional<Integer> x = map.get(kiv2)
                Integer y = x.get()
                assert y == 0 // should not get here
            } catch (ClassCastException e) {
                assert e
            }
        }
    }

    def "immutable map"() {
        given:
        TypedObjectKey<String> ks = TypedObjectKey.identity()
        TypedObjectKey<LocalDate> kd = TypedObjectKey.identity()
        TypedObjectKey<Boolean> kflag = TypedObjectKey.identity()

        when:
        TypedObjectMap m = ImmutableTypedObjectMap.builder()
        .put(ks, "foo")
        .put(kd, LocalDate.of(2013, 7, 11))
        .set(kflag)
        .build()

        then:
        m.get(ks).get() == "foo"
        m.get(kd).get() == LocalDate.of(2013, 7, 11)
        m.isSet(kflag)

        expect:
        m.with(kd, LocalDate.of(2014,8,12)) { map ->
            assert map.get(ks).get() == "foo"
            assert map.get(kd).get() == LocalDate.of(2014, 8, 12)
            assert map.isSet(kflag)
        }
    }
}
