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
  public void testCheckContains_EnumPresent_ExpectNoException() {
    EnumSet<TestEnum> enumSet = EnumSet.of(TestEnum.VALUE1, TestEnum.VALUE2);
    Supplier<RuntimeException> supplier = () -> new RuntimeException("Enum not contained in set");

    Assertions.assertDoesNotThrow(
        () -> EnumVerifiers.checkContains(TestEnum.VALUE1, enumSet, supplier));
  }

  @Test
  public void testCheckContains_EnumNotPresent_ExpectException() {
    EnumSet<TestEnum> enumSet = EnumSet.of(TestEnum.VALUE1);
    Supplier<RuntimeException> supplier = () -> new RuntimeException("Enum not contained in set");

    Assertions.assertThrows(RuntimeException.class,
        () -> EnumVerifiers.checkContains(TestEnum.VALUE2, enumSet, supplier));
  }

}