package undecided.erp.common.primitive;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import undecided.erp.common.verifier.IndexedRuntimeException;

enum TestEnum {
  VALUE1,
  VALUE2,
  VALUE3
}

class EnumsTest {

  @Nested
  class GetValueByEnumOrdinalTest {

    @Test
    void shouldReturnValueForValidEnumAndValues() {
      TestEnum testEnum = TestEnum.VALUE2;

      String result = Enums.getValueByEnumOrdinal(testEnum,
          "value1", "value2", "value3");

      assertThat(result)
          .isEqualTo("value2");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForInvalidValues() {
      TestEnum testEnum = TestEnum.VALUE3;

      assertThatThrownBy(
          () -> Enums.getValueByEnumOrdinal(testEnum, "value1", "value2"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("values length must be at least 3");
    }

    @Test
    void shouldThrowNullPointerExceptionForNullEnum() {

      assertThatThrownBy(
          () -> Enums.getValueByEnumOrdinal(null, "value1", "value2", "value3"))
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerExceptionForNullValues() {
      TestEnum testEnum = TestEnum.VALUE2;

      assertThatThrownBy(
          () -> Enums.getValueByEnumOrdinal(testEnum, (String[]) null))
          .isInstanceOf(NullPointerException.class);
    }

  }

  @Nested
  class GetValueByEnumOrdinalFromSuppliersTest {

    @Test
    void shouldReturnValueForValidEnumAndSuppliers() {
      TestEnum testEnum = TestEnum.VALUE1;

      Integer result = Enums.getValueByEnumOrdinalFromSuppliers(
          testEnum, () -> 1, () -> 2, () -> 3);

      assertThat(result)
          .isEqualTo(1);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForInvalidSuppliers() {
      TestEnum testEnum = TestEnum.VALUE3;

      assertThatThrownBy(
          () -> Enums.getValueByEnumOrdinalFromSuppliers(
              testEnum, () -> 1, () -> 2))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("valueSuppliers length must be at least 3");
    }

    @Test
    void shouldThrowNullPointerExceptionForNullEnum() {

      assertThatThrownBy(
          () -> Enums.getValueByEnumOrdinalFromSuppliers(
              null, () -> 1, () -> 2, () -> 3))
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowIndexedRuntimeExceptionForNullSuppliers() {
      TestEnum testEnum = TestEnum.VALUE2;

      assertThatThrownBy(
          () -> Enums.getValueByEnumOrdinalFromSuppliers(
              testEnum, () -> 1, null, () -> 3))
          .isInstanceOf(IndexedRuntimeException.class)
          .hasMessage("Index:1 all value supplier must not null.");
    }
  }
}
