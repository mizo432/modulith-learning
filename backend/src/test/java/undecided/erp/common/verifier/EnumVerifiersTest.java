package undecided.erp.common.verifier;

import java.util.EnumSet;
import java.util.function.Supplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

enum TestEnum {
  VALUE1, VALUE2
}

public class EnumVerifiersTest {

  @Test
  public void testVerifyContains_EnumPresent_ExpectNoException() {
    EnumSet<TestEnum> enumSet = EnumSet.of(TestEnum.VALUE1, TestEnum.VALUE2);
    Supplier<RuntimeException> supplier = () -> new RuntimeException("Enum not contained in set");

    Assertions.assertDoesNotThrow(
        () -> EnumVerifiers.verifyContains(TestEnum.VALUE1, enumSet, supplier));
  }

  @Test
  public void testVerifyContains_EnumNotPresent_ExpectException() {
    EnumSet<TestEnum> enumSet = EnumSet.of(TestEnum.VALUE1);
    Supplier<RuntimeException> supplier = () -> new RuntimeException("Enum not contained in set");

    Assertions.assertThrows(RuntimeException.class,
        () -> EnumVerifiers.verifyContains(TestEnum.VALUE2, enumSet, supplier));
  }

}