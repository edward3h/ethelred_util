package org.ethelred.util.function

import spock.lang.Specification

import java.util.concurrent.atomic.AtomicInteger

class LazyTest extends Specification {
    def "lazy only gets once"() {
        given:
        def ai = new AtomicInteger(3)
        def lazy = Lazy.lazy(ai::incrementAndGet)

        when:        def a = lazy.get()
        def b = lazy.get()

        then:
        a == b
        a === b
    }

    class ClassWithLazy {
        private final Lazy<Integer> foo
        private final Lazy<Integer> bar = Lazy.lazy(this, cwl -> cwl.foo.get() * cwl.foo.get())

        ClassWithLazy(int initial) {
            foo = Lazy.lazy(() -> initial)
        }

        int getBar() {
            bar.get()
        }
    }

    def "chained references work"(int i, int result) {
        when:
        def obj = new ClassWithLazy(i)

        then:
        obj.bar == result

        where:
        i | result
        1 | 1
        2 | 4
        7 | 49
    }

    class ClassWithCycle {
        private final Lazy<Integer> foo = Lazy.lazy(this, cwc -> cwc.bar.get() + 1)
        private final Lazy<Integer> bar = Lazy.lazy(this, cwc -> cwc.foo.get() * 2)

        int getFoo() {
            foo.get()
        }
    }

    def "cycle will fail"() {
        given:
        def obj = new ClassWithCycle()

        when:
        def r = obj.foo

        then:
        thrown(IllegalStateException)
    }


    class ClassWithLazySupplier {
        private int initial
        private final Lazy<Integer> foo = Lazy.lazy(() -> initial)
        private final Lazy<Integer> bar = Lazy.lazy(foo, v -> v * v)

        ClassWithLazySupplier(int initial) {
            this.initial = initial
        }

        int getBar() {
            bar.get()
        }
    }

    def "chained supplier references work"(int i, int result) {
        when:
        def obj = new ClassWithLazySupplier(i)

        then:
        obj.bar == result

        where:
        i | result
        1 | 1
        2 | 4
        7 | 49
    }
}
