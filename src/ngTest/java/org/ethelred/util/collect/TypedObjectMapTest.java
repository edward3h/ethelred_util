package org.ethelred.util.collect;

import static com.github.npathai.hamcrestopt.OptionalMatchers.*;
import static org.junit.Assert.assertThat;
import static org.testng.Assert.*;

import java.time.LocalDate;
import java.util.Optional;
import org.testng.annotations.Test;

public class TypedObjectMapTest {
  @Test
  public void testEmpty() {
    TypedObjectKey<String> ks = TypedObjectKey.identity();
    TypedObjectKey<LocalDate> kd = TypedObjectKey.identity();
    TypedObjectKey<Boolean> kflag = TypedObjectKey.identity();
    TypedObjectMap m = new EmptyTypedObjectMap();
    assertThat(m.get(ks), isEmpty());
    assertThat(m.get(kd), isEmpty());
    assertFalse(m.isSet(kflag));

    m.with(
        kd,
        LocalDate.of(2012, 6, 10),
        (map) -> {
          assertThat(map.get(ks), isEmpty());
          assertThat(map.get(kd), isPresentAndIs(LocalDate.of(2012, 6, 10)));
          assertFalse(m.isSet(kflag));
        });
  }

  @Test
  public void testEmptyWithValuedKey() {
    TypedObjectKey<LocalDate> kdv = TypedObjectKey.valued("date1");
    TypedObjectMap mv = new EmptyTypedObjectMap();
    assertThat(mv.get(kdv), isEmpty());

    mv.with(
        kdv,
        LocalDate.of(2012, 6, 10),
        (map) -> {
          TypedObjectKey<LocalDate> kdv2 = TypedObjectKey.valued("date1");
          assertThat(map.get(kdv2), isPresentAndIs(LocalDate.of(2012, 6, 10)));
          assertThrows(
              ClassCastException.class,
              () -> {
                TypedObjectKey<Integer> kiv2 = TypedObjectKey.valued("date1");
                Optional<Integer> x = map.get(kiv2);
                Integer y = x.get();
              });
        });
  }

  @Test
  public void testImmutable() {
    TypedObjectKey<String> ks = TypedObjectKey.identity();
    TypedObjectKey<LocalDate> kd = TypedObjectKey.identity();
    TypedObjectKey<Boolean> kflag = TypedObjectKey.identity();
    TypedObjectMap m =
        ImmutableTypedObjectMap.builder()
            .put(ks, "foo")
            .put(kd, LocalDate.of(2013, 7, 11))
            .set(kflag)
            .build();
    assertThat(m.get(ks), isPresentAndIs("foo"));
    assertThat(m.get(kd), isPresentAndIs(LocalDate.of(2013, 7, 11)));
    assertTrue(m.isSet(kflag));

    m.with(
        kd,
        LocalDate.of(2014, 8, 12),
        (map) -> {
          assertThat(map.get(ks), isPresentAndIs("foo"));
          assertThat(map.get(kd), isPresentAndIs(LocalDate.of(2014, 8, 12)));
          assertTrue(map.isSet(kflag));
        });
  }
}
