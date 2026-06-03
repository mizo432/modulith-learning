package undecided.shared.common.primitive;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("small")
@DisplayName("LowerCamelImpl - convertToStringメソッドのテスト")
class LowerCamelImplTest {

    @Test
    @DisplayName("convertToString - nullを入力すると空文字を返すこと")
    void shouldReturnEmptyStringForNullInput() {
        // Given
        Strings2.LowerCamelImpl lowerCamel = new Strings2.LowerCamelImpl();

        // When
        String result = lowerCamel.convertToString(null);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("convertToString - 空文字を入力すると空文字を返すこと")
    void shouldReturnEmptyStringForEmptyInput() {
        // Given
        Strings2.LowerCamelImpl lowerCamel = new Strings2.LowerCamelImpl();

        // When
        String result = lowerCamel.convertToString("");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("convertToString - 単文字を入力すると小文字を返すこと")
    void shouldReturnLowerCaseForSingleCharacterInput() {
        // Given
        Strings2.LowerCamelImpl lowerCamel = new Strings2.LowerCamelImpl();

        // When
        String result = lowerCamel.convertToString("A");

        // Then
        assertThat(result).isEqualTo("a");
    }

    @Test
    @DisplayName("convertToString - 全て大文字の文字列を入力するとlower camel caseへ変換すること")
    void shouldConvertFirstCharacterToLowerCaseForAllUpperCaseInput() {
        // Given
        Strings2.LowerCamelImpl lowerCamel = new Strings2.LowerCamelImpl();

        // When
        String result = lowerCamel.convertToString("HELLO");

        // Then
        assertThat(result).isEqualTo("hello");
    }

    @Test
    @DisplayName("convertToString - 最初の文字が小文字の場合はそのまま返すこと")
    void shouldReturnInputUnchangedIfFirstCharacterIsLowerCase() {
        // Given
        Strings2.LowerCamelImpl lowerCamel = new Strings2.LowerCamelImpl();

        // When
        String result = lowerCamel.convertToString("camelCase");

        // Then
        assertThat(result).isEqualTo("camelCase");
    }

    @Test
    @DisplayName("convertToString - 特殊文字文字列を入力するとそのまま返すこと")
    void shouldConvertFirstCharacterForSpecialCharacterInput() {
        // Given
        Strings2.LowerCamelImpl lowerCamel = new Strings2.LowerCamelImpl();

        // When
        String result = lowerCamel.convertToString("@Example");

        // Then
        assertThat(result).isEqualTo("@Example");
    }

    @Test
    @DisplayName("convertToString - 数字で始まる文字列をそのまま返すこと")
    void shouldReturnInputUnchangedIfStartsWithNumber() {
        // Given
        Strings2.LowerCamelImpl lowerCamel = new Strings2.LowerCamelImpl();

        // When
        String result = lowerCamel.convertToString("123abc");

        // Then
        assertThat(result).isEqualTo("123abc");
    }

    @Test
    @DisplayName("convertToString - LOWER_UNDERSCOREを入力すると先頭文字のみ小文字に変換すること")
    void shouldConvertLowerUnderscoreInput() {
        // Given
        Strings2.LowerCamelImpl lowerCamel = new Strings2.LowerCamelImpl();

        // When
        String result = lowerCamel.convertToString("LOWER_UNDERSCORE");

        // Then
        assertThat(result).isEqualTo("lowerUnderscore");
    }

    @Test
    @DisplayName("convertToString - UPPER_UNDERSCOREを入力すると先頭文字のみ小文字に変換すること")
    void shouldConvertUpperUnderscoreInput() {
        // Given
        Strings2.LowerCamelImpl lowerCamel = new Strings2.LowerCamelImpl();

        // When
        String result = lowerCamel.convertToString("UPPER_UNDERSCORE");

        // Then
        assertThat(result).isEqualTo("upperUnderscore");
    }
}