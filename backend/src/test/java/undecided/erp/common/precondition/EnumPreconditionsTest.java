package undecided.erp.common.precondition;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.function.Supplier;

enum TestEnum {
    VALUE1, VALUE2
}

public class EnumPreconditionsTest {

    @Test
    public void testCheckContains_EnumPresent_ExpectNoException() {
        EnumSet<TestEnum> enumSet = EnumSet.of(TestEnum.VALUE1, TestEnum.VALUE2);
        Supplier<RuntimeException> supplier = () -> new RuntimeException("Enum not contained in set");

        Assertions.assertDoesNotThrow(() -> EnumPreconditions.checkContains(TestEnum.VALUE1, enumSet, supplier));
    }

    @Test
    public void testCheckContains_EnumNotPresent_ExpectException() {
        EnumSet<TestEnum> enumSet = EnumSet.of(TestEnum.VALUE1);
        Supplier<RuntimeException> supplier = () -> new RuntimeException("Enum not contained in set");

        Assertions.assertThrows(RuntimeException.class, () -> EnumPreconditions.checkContains(TestEnum.VALUE2, enumSet, supplier));
    }

}