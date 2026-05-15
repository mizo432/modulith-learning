package undecided.shared.common.primitive;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link Strings2.IndexOfImpl}.
 * <p>
 * This class specifically tests the behavior of the {@code apply()} method in the
 * {@code IndexOfImpl} class, verifying its correctness under various input conditions.
 */
class IndexOfImplTest {

    @Nested
    @DisplayName("Tests for the apply method in IndexOfImpl")
    class ApplyTests {

        @Test
        @DisplayName("should return the index of a substring when it exists in the input string")
        void shouldReturnIndexWhenSubstringExists() {
            // Given
            Strings2.IndexOfImpl indexOf = new Strings2.IndexOfImpl();
            String str = "hello world";
            String subString = "world";

            // When
            int result = indexOf.apply(str, subString);

            // Then
            assertEquals(6, result, "Expected index of substring to be 6");
        }

        @Test
        @DisplayName("should return -1 when the substring does not exist in the input string")
        void shouldReturnMinusOneWhenSubstringNotExists() {
            // Given
            Strings2.IndexOfImpl indexOf = new Strings2.IndexOfImpl();
            String str = "hello world";
            String subString = "universe";

            // When
            int result = indexOf.apply(str, subString);

            // Then
            assertEquals(-1, result, "Expected -1 when substring does not exist");
        }

        @Test
        @DisplayName("should return 0 when the substring is empty")
        void shouldReturnZeroWhenSubstringIsEmpty() {
            // Given
            Strings2.IndexOfImpl indexOf = new Strings2.IndexOfImpl();
            String str = "hello world";
            String subString = "";

            // When
            int result = indexOf.apply(str, subString);

            // Then
            assertEquals(0, result, "Expected index to be 0 for empty substring");
        }

        @Test
        @DisplayName("should return -1 when the input string is null")
        void shouldReturnMinusOneWhenInputStringIsNull() {
            // Given
            Strings2.IndexOfImpl indexOf = new Strings2.IndexOfImpl();
            String str = null;
            String subString = "world";

            // When
            int result = indexOf.apply(str, subString);

            // Then
            assertEquals(-1, result, "Expected -1 when input string is null");
        }

        @Test
        @DisplayName("should return 0 when both the input string and substring are empty")
        void shouldReturnZeroWhenBothInputAndSubstringAreEmpty() {
            // Given
            Strings2.IndexOfImpl indexOf = new Strings2.IndexOfImpl();
            String str = "";
            String subString = "";

            // When
            int result = indexOf.apply(str, subString);

            // Then
            assertEquals(0, result, "Expected index to be 0 when both input and substring are empty");
        }

        @Test
        @DisplayName("should return -1 when the substring is null")
        void shouldReturnMinusOneWhenSubstringIsNull() {
            // Given
            Strings2.IndexOfImpl indexOf = new Strings2.IndexOfImpl();
            String str = "hello world";
            String subString = null;

            // When
            int result = indexOf.apply(str, subString);

            // Then
            assertEquals(-1, result, "Expected -1 when substring is null");
        }

        @Test
        @DisplayName("should return -1 when both the input string and substring are null")
        void shouldReturnMinusOneWhenBothInputAndSubstringAreNull() {
            // Given
            Strings2.IndexOfImpl indexOf = new Strings2.IndexOfImpl();
            String str = null;
            String subString = null;

            // When
            int result = indexOf.apply(str, subString);

            // Then
            assertEquals(-1, result, "Expected -1 when both input string and substring are null");
        }
    }
}