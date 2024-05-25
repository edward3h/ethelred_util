import org.ethelred.util.function.CheckedConsumer
import org.ethelred.util.function.CheckedFunction
import org.ethelred.util.function.CheckedSupplier
import org.ethelred.util.function.WrappedCheckedException
import spock.lang.Specification

import java.util.stream.Collectors
import java.util.stream.Stream
import java.util.function.Supplier

class CheckedWrapperTest extends Specification {
    private static int half(int i) throws OddException {
        if (i % 2 != 0) {
            throw new OddException();
        }
        return i / 2;
    }

    static class OddException extends Exception {
        private static final long serialVersionUID = 4618053569704775903L;
    }

    def "CheckedFunction could throw WrappedCheckedException"() {
        when:
        def r = Stream.of(2, 3)
        .map(CheckedFunction.unchecked(CheckedWrapperTest::half))
        .collect(Collectors.toList())

        then:
        thrown(WrappedCheckedException)
    }

    def "CheckedConsumer could throw WrappedCheckedException"() {
        when:
        Stream.of(2, 3)
        .forEach(CheckedConsumer.unchecked(CheckedWrapperTest::half))

        then:
        thrown(WrappedCheckedException)
    }

    def "CheckedSupplier could throw WrappedCheckedException"() {
        when:
        Supplier<Integer> a = CheckedSupplier.unchecked(() -> half(2))

        then:
        a.get() == 1

        when:
        Supplier<Integer> b = CheckedSupplier.unchecked(() -> half(1))
        b.get()

        then:
        thrown(WrappedCheckedException)
    }
}