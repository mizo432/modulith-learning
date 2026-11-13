package undecided.association.relationshipMgmt.spi.party.org;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("OrgCode値オブジェクトのテスト")
class OrgCodeTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効なコードからOrgCodeを正常に作成すること")
    void shouldCreateOrgCodeFromValidString() {
      // Act
      OrgCode orgCode = OrgCode.of("ORG001");

      // Assert
      assertThat(orgCode).as("OrgCodeインスタンスが作成されること").isNotNull();
      assertThat(orgCode.value()).as("値が保持されること").isEqualTo("ORG001");
    }

    @Test
    @DisplayName("前後に空白があるコードをトリムして作成すること")
    void shouldCreateOrgCodeWithTrimmedValue() {
      // Act
      OrgCode orgCode = OrgCode.of(" ORG001 ");

      // Assert
      assertThat(orgCode.value()).as("前後の空白がトリムされること").isEqualTo("ORG001");
    }

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> OrgCode.of(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("null");
    }

    @Test
    @DisplayName("空文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsEmpty() {
      // Act & Assert
      assertThatThrownBy(() -> OrgCode.of(""))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("empty");
    }

    @Test
    @DisplayName("空白のみの文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsBlank() {
      // Act & Assert
      assertThatThrownBy(() -> OrgCode.of("   "))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("empty");
    }

    @Test
    @DisplayName("31文字のコードが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeIsTooLong() {
      // Arrange
      String tooLong = "1234567890123456789012345678901";

      // Act & Assert
      assertThatThrownBy(() -> OrgCode.of(tooLong))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("30");
    }

    @Test
    @DisplayName("特殊文字を含むコードが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCodeContainsSpecialCharacters() {
      // Act & Assert
      assertThatThrownBy(() -> OrgCode.of("ORG@001"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("alphanumeric");
    }

    @Test
    @DisplayName("英数字とアンダースコアのみのコードは作成できること")
    void shouldCreateOrgCodeWithAlphanumericAndUnderscore() {
      // Act
      OrgCode orgCode = OrgCode.of("ORG_001_test");

      // Assert
      assertThat(orgCode.value()).isEqualTo("ORG_001_test");
    }

    @Test
    @DisplayName("1文字のコードは作成できること")
    void shouldCreateOrgCodeWithSingleCharacter() {
      // Act
      OrgCode orgCode = OrgCode.of("A");

      // Assert
      assertThat(orgCode.value()).isEqualTo("A");
    }

    @Test
    @DisplayName("30文字のコードは作成できること")
    void shouldCreateOrgCodeWithMaxCharacters() {
      // Arrange
      String maxCode = "123456789012345678901234567890";

      // Act
      OrgCode orgCode = OrgCode.of(maxCode);

      // Assert
      assertThat(orgCode.value()).isEqualTo(maxCode);
    }
  }

  @Nested
  @DisplayName("emptyメソッドのテスト")
  class EmptyMethodTest {

    @Test
    @DisplayName("空のOrgCodeインスタンスを作成すること")
    void shouldCreateEmptyOrgCode() {
      // Act
      OrgCode orgCode = OrgCode.empty();

      // Assert
      assertThat(orgCode).isNotNull();
      assertThat(orgCode.isEmpty()).isTrue();
    }
  }

  @Nested
  @DisplayName("valueメソッドのテスト")
  class ValueMethodTest {

    @Test
    @DisplayName("内部の文字列値を返すこと")
    void shouldReturnInternalStringValue() {
      // Arrange
      OrgCode orgCode = OrgCode.of("ORG001");

      // Act
      String result = orgCode.value();

      // Assert
      assertThat(result).isEqualTo("ORG001");
    }
  }

  @Nested
  @DisplayName("isEmptyメソッドのテスト")
  class IsEmptyMethodTest {

    @Test
    @DisplayName("空のOrgCodeはtrueを返すこと")
    void shouldReturnTrueWhenEmpty() {
      // Arrange
      OrgCode orgCode = OrgCode.empty();

      // Assert
      assertThat(orgCode.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("値を持つOrgCodeはfalseを返すこと")
    void shouldReturnFalseWhenNotEmpty() {
      // Arrange
      OrgCode orgCode = OrgCode.of("ORG001");

      // Assert
      assertThat(orgCode.isEmpty()).isFalse();
    }
  }

  @Nested
  @DisplayName("等価性のテスト")
  class EqualityTest {

    @Test
    @DisplayName("同じ値のOrgCodeは等しいと判定されること")
    void shouldBeEqualWhenValuesAreSame() {
      // Arrange
      OrgCode orgCode1 = OrgCode.of("ORG001");
      OrgCode orgCode2 = OrgCode.of("ORG001");

      // Assert
      assertThat(orgCode1).isEqualTo(orgCode2);
    }

    @Test
    @DisplayName("異なる値のOrgCodeは等しくないこと")
    void shouldNotBeEqualWhenValuesDiffer() {
      // Arrange
      OrgCode orgCode1 = OrgCode.of("ORG001");
      OrgCode orgCode2 = OrgCode.of("ORG002");

      // Assert
      assertThat(orgCode1).isNotEqualTo(orgCode2);
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringMethodTest {

    @Test
    @DisplayName("toStringメソッドが値を含む文字列を返すこと")
    void shouldReturnStringRepresentationWithValue() {
      // Arrange
      OrgCode orgCode = OrgCode.of("ORG001");

      // Act
      String result = orgCode.toString();

      // Assert
      assertThat(result).contains("ORG001");
    }
  }

  @Nested
  @DisplayName("定数のテスト")
  class ConstantsTest {

    @Test
    @DisplayName("MIN_LENGTHは1であること")
    void shouldHaveMinLengthEqualToOne() {
      assertThat(OrgCode.MIN_LENGTH).isEqualTo(1);
    }

    @Test
    @DisplayName("MAX_LENGTHは30であること")
    void shouldHaveMaxLengthEqualToThirty() {
      assertThat(OrgCode.MAX_LENGTH).isEqualTo(30);
    }
  }

  @Nested
  @DisplayName("OrgCodeConverterのテスト")
  class OrgCodeConverterTest {

    private final OrgCode.OrgCodeConverter converter = new OrgCode.OrgCodeConverter();

    @Nested
    @DisplayName("convertToDatabaseColumnメソッドのテスト")
    class ConvertToDatabaseColumnTest {

      @Test
      @DisplayName("OrgCodeをデータベース用の文字列に変換すること")
      void shouldConvertOrgCodeToDatabaseString() {
        // Arrange
        OrgCode orgCode = OrgCode.of("ORG001");

        // Act
        String dbData = converter.convertToDatabaseColumn(orgCode);

        // Assert
        assertThat(dbData).as("データベース用の文字列に変換されること").isEqualTo("ORG001");
      }

      @Test
      @DisplayName("nullのOrgCodeはnullに変換すること")
      void shouldConvertNullOrgCodeToNull() {
        // Act
        String dbData = converter.convertToDatabaseColumn(null);

        // Assert
        assertThat(dbData).as("nullのOrgCodeはnullに変換されること").isNull();
      }

      @Test
      @DisplayName("空のOrgCodeはnullに変換すること")
      void shouldConvertEmptyOrgCodeToNull() {
        // Arrange
        OrgCode orgCode = OrgCode.empty();

        // Act
        String dbData = converter.convertToDatabaseColumn(orgCode);

        // Assert
        assertThat(dbData).as("空のOrgCodeはnullに変換されること").isNull();
      }
    }

    @Nested
    @DisplayName("convertToEntityAttributeメソッドのテスト")
    class ConvertToEntityAttributeTest {

      @Test
      @DisplayName("データベース用の文字列をOrgCodeに変換すること")
      void shouldConvertDatabaseStringToOrgCode() {
        // Arrange
        String dbData = "ORG001";

        // Act
        OrgCode orgCode = converter.convertToEntityAttribute(dbData);

        // Assert
        assertThat(orgCode).as("OrgCodeに変換されること").isNotNull();
        assertThat(orgCode.value()).as("値が保持されること").isEqualTo("ORG001");
      }

      @Test
      @DisplayName("nullのデータベースデータは空のOrgCodeに変換すること")
      void shouldConvertNullToEmptyOrgCode() {
        // Act
        OrgCode orgCode = converter.convertToEntityAttribute(null);

        // Assert
        assertThat(orgCode).as("OrgCodeインスタンスが作成されること").isNotNull();
        assertThat(orgCode.isEmpty()).as("空のOrgCodeであること").isTrue();
      }

      @Test
      @DisplayName("不正なデータベースデータは空のOrgCodeに変換すること")
      void shouldConvertInvalidDataToEmptyOrgCode() {
        // Arrange
        String invalidData = "ORG@001"; // 特殊文字を含む不正なデータ

        // Act
        OrgCode orgCode = converter.convertToEntityAttribute(invalidData);

        // Assert
        assertThat(orgCode).as("OrgCodeインスタンスが作成されること").isNotNull();
        assertThat(orgCode.isEmpty()).as("空のOrgCodeであること").isTrue();
      }

      @Test
      @DisplayName("空文字列のデータベースデータは空のOrgCodeに変換すること")
      void shouldConvertEmptyStringToEmptyOrgCode() {
        // Act
        OrgCode orgCode = converter.convertToEntityAttribute("");

        // Assert
        assertThat(orgCode).as("OrgCodeインスタンスが作成されること").isNotNull();
        assertThat(orgCode.isEmpty()).as("空のOrgCodeであること").isTrue();
      }

      @Test
      @DisplayName("文字数超過のデータベースデータは空のOrgCodeに変換すること")
      void shouldConvertTooLongDataToEmptyOrgCode() {
        // Arrange
        String tooLongData = "1234567890123456789012345678901"; // 31文字

        // Act
        OrgCode orgCode = converter.convertToEntityAttribute(tooLongData);

        // Assert
        assertThat(orgCode).as("OrgCodeインスタンスが作成されること").isNotNull();
        assertThat(orgCode.isEmpty()).as("空のOrgCodeであること").isTrue();
      }
    }
  }
}
