package org.ethelred.util.function;

import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class CheckedWrapperTest {

  @Test(expectedExceptions = WrappedCheckedException.class)
  public void testFunction() {
    var r =
        Stream.of(2, 3)
            .map(CheckedFunction.unchecked(CheckedWrapperTest::half))
            .collect(Collectors.toList());
  }

  @Test(expectedExceptions = WrappedCheckedException.class)
  public void testConsumer() {
    Stream.of(2, 3).forEach(CheckedConsumer.unchecked(CheckedWrapperTest::half));
  }

  @Test
  public void testSupplier() {
    Supplier<Integer> a = CheckedSupplier.unchecked(() -> half(2));
    Supplier<Integer> b = CheckedSupplier.unchecked(() -> half(1));
    assertEquals(a.get(), Integer.valueOf(1));
    assertThrows(WrappedCheckedException.class, () -> b.get());
  }

  private static int half(int i) throws OddException {
    if (i % 2 != 0) {
      throw new OddException();
    }
    return i / 2;
  }

  static class OddException extends Exception {

    private static final long serialVersionUID = 4618053569704775903L;
  }
}
