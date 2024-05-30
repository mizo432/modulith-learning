package undecided.erp.shared.value;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NonEmptyStringTest {

    @Test
    public void WhenOfMethodIsCalledWithEmptyString_ThrowsIllegalArgumentException() {
        String emptyString = "";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> NonEmptyString.of(emptyString));
        assertEquals("Empty string", exception.getMessage());
    }

    @Test
    public void WhenOfMethodIsCalledWithNonEmptyString_ReturnsNonEmptyStringInstanceWithCorrectValue() {
        String testString = "Non Empty String";
        NonEmptyString nonEmptyString = NonEmptyString.of(testString);
        assertEquals(testString, nonEmptyString.value());
    }
}