package undecided.erp.common.verifier;

import java.util.EnumSet;
import java.util.function.Supplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class EnumVerifiersTest {

  @Nested
  @DisplayName("Enumの存在確認テスト")
  class VerifyContainsTest {

    @Test
    @DisplayName("Enumが存在する場合、例外は発生しない")
    void enumPresentNoExceptionExpected() {
      EnumSet<TestEnum> enumSet = EnumSet.of(TestEnum.VALUE1, TestEnum.VALUE2);
      Supplier<RuntimeException> supplier = () -> new RuntimeException("Enum not contained in set");
      Assertions.assertDoesNotThrow(
          () -> EnumVerifiers.verifyContains(TestEnum.VALUE1, enumSet, supplier));
    }

    @Test
    @DisplayName("Enumが存在しない場合、例外が発生する")
    void enumNotPresentExceptionExpected() {
      EnumSet<TestEnum> enumSet = EnumSet.of(TestEnum.VALUE1);
      Supplier<RuntimeException> supplier = () -> new RuntimeException("Enum not contained in set");
      Assertions.assertThrows(RuntimeException.class,
          () -> EnumVerifiers.verifyContains(TestEnum.VALUE2, enumSet, supplier));
    }
  }

  @Nested
  @DisplayName("Enumの値の等価性確認テスト")
  class CheckNotEqualTest {

    @Test
    @DisplayName("値が等しくない場合、例外が発生しない")
    void valuesNotEqualNoExceptionExpected() {
      Supplier<RuntimeException> supplier = () -> new RuntimeException("Values are equal");
      Assertions.assertDoesNotThrow(
          () -> EnumVerifiers.verifyNotEqual(TestEnum.VALUE1, TestEnum.VALUE2, supplier));
    }

    @Test
    @DisplayName("値が等しい場合、例外が発生する")
    void valuesEqualExceptionExpected() {
      Supplier<RuntimeException> supplier = () -> new RuntimeException("Values are equal");
      Assertions.assertThrows(RuntimeException.class,
          () -> EnumVerifiers.verifyNotEqual(TestEnum.VALUE1, TestEnum.VALUE1, supplier));
    }
  }

  enum TestEnum {VALUE2, VALUE1}
}
