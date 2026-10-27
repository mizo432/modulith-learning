package undecided.shared.sample.jpa.entity.converter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("SampleCurrencyCodeの単体テスト")
class SampleCurrencyCodeTest {

  @Nested
  @DisplayName("ofファクトリメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("正常な値で通貨コードインスタンスが作成される")
    void shouldCreateCurrencyCodeWithValidValue() {
      // Act
      SampleCurrencyCode currencyCode = SampleCurrencyCode.of("JPY");

      // Assert
      assertThat(currencyCode).isNotNull();
      assertThat(currencyCode.getValue()).isEqualTo("JPY");
    }

    @Test
    @DisplayName("小文字は大文字に変換される")
    void shouldConvertToLowercase() {
      // Act
      SampleCurrencyCode currencyCode = SampleCurrencyCode.of("jpy");

      // Assert
      assertThat(currencyCode.getValue()).isEqualTo("JPY");
    }

    @Test
    @DisplayName("nullを渡すとEMPTYが返される")
    void shouldReturnEmptyWhenNull() {
      // Act
      SampleCurrencyCode currencyCode = SampleCurrencyCode.of(null);

      // Assert
      assertThat(currencyCode).isEqualTo(SampleCurrencyCode.EMPTY);
      assertThat(currencyCode.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("空文字列を渡すとEMPTYが返される")
    void shouldReturnEmptyWhenEmptyString() {
      // Act
      SampleCurrencyCode currencyCode = SampleCurrencyCode.of("");

      // Assert
      assertThat(currencyCode).isEqualTo(SampleCurrencyCode.EMPTY);
      assertThat(currencyCode.isEmpty()).isTrue();
    }
  }

  @Nested
  @DisplayName("emptyメソッドのテスト")
  class EmptyMethodTest {

    @Test
    @DisplayName("空のインスタンスが返される")
    void shouldReturnEmptyInstance() {
      // Act
      SampleCurrencyCode currencyCode = SampleCurrencyCode.empty();

      // Assert
      assertThat(currencyCode).isEqualTo(SampleCurrencyCode.EMPTY);
      assertThat(currencyCode.isEmpty()).isTrue();
    }
  }

  @Nested
  @DisplayName("getValueメソッドのテスト")
  class GetValueTest {

    @Test
    @DisplayName("保持している値を返す")
    void shouldReturnValue() {
      // Arrange
      SampleCurrencyCode currencyCode = SampleCurrencyCode.of("USD");

      // Act & Assert
      assertThat(currencyCode.getValue()).isEqualTo("USD");
    }

    @Test
    @DisplayName("空のインスタンスでは空文字列を返す")
    void shouldReturnEmptyStringForEmptyInstance() {
      // Arrange
      SampleCurrencyCode currencyCode = SampleCurrencyCode.empty();

      // Act & Assert
      assertThat(currencyCode.getValue()).isEmpty();
    }
  }

  @Nested
  @DisplayName("isEmptyメソッドのテスト")
  class IsEmptyTest {

    @Test
    @DisplayName("空のインスタンスではtrueを返す")
    void shouldReturnTrueForEmptyInstance() {
      // Arrange
      SampleCurrencyCode currencyCode = SampleCurrencyCode.empty();

      // Act & Assert
      assertThat(currencyCode.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("値を持つインスタンスではfalseを返す")
    void shouldReturnFalseForNonEmptyInstance() {
      // Arrange
      SampleCurrencyCode currencyCode = SampleCurrencyCode.of("EUR");

      // Act & Assert
      assertThat(currencyCode.isEmpty()).isFalse();
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringTest {

    @Test
    @DisplayName("値を文字列として返す")
    void shouldReturnValueAsString() {
      // Arrange
      SampleCurrencyCode currencyCode = SampleCurrencyCode.of("EUR");

      // Act & Assert
      assertThat(currencyCode.toString()).isEqualTo("EUR");
    }
  }

  @Nested
  @DisplayName("equalsとhashCodeのテスト")
  class EqualsAndHashCodeTest {

    @Test
    @DisplayName("同じ値を持つインスタンスは等しい")
    void shouldBeEqualWhenSameValue() {
      // Arrange
      SampleCurrencyCode first = SampleCurrencyCode.of("JPY");
      SampleCurrencyCode second = SampleCurrencyCode.of("JPY");

      // Assert
      assertThat(first).isEqualTo(second);
      assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    @DisplayName("異なる値を持つインスタンスは等しくない")
    void shouldNotBeEqualWhenDifferentValue() {
      // Arrange
      SampleCurrencyCode first = SampleCurrencyCode.of("JPY");
      SampleCurrencyCode second = SampleCurrencyCode.of("USD");

      // Assert
      assertThat(first).isNotEqualTo(second);
    }

    @Test
    @DisplayName("大文字小文字の違いは等しいと判定される")
    void shouldBeEqualWhenCaseDifference() {
      // Arrange
      SampleCurrencyCode first = SampleCurrencyCode.of("jpy");
      SampleCurrencyCode second = SampleCurrencyCode.of("JPY");

      // Assert
      assertThat(first).isEqualTo(second);
    }
  }

  @Nested
  @DisplayName("SampleCurrencyCodeConverterのテスト")
  class ConverterTest {

    private final SampleCurrencyCode.SampleCurrencyCodeConverter converter =
        new SampleCurrencyCode.SampleCurrencyCodeConverter();

    @Nested
    @DisplayName("convertToDatabaseColumnメソッドのテスト")
    class ConvertToDatabaseColumnTest {

      @Test
      @DisplayName("通貨コードからStringに変換する")
      void shouldConvertToDatabaseColumn() {
        // Arrange
        SampleCurrencyCode currencyCode = SampleCurrencyCode.of("JPY");

        // Act
        String result = converter.convertToDatabaseColumn(currencyCode);

        // Assert
        assertThat(result).isEqualTo("JPY");
      }

      @Test
      @DisplayName("null属性はnullを返す")
      void shouldReturnNullForNullAttribute() {
        // Act & Assert
        assertThat(converter.convertToDatabaseColumn(null)).isNull();
      }
    }

    @Nested
    @DisplayName("convertToEntityAttributeメソッドのテスト")
    class ConvertToEntityAttributeTest {

      @Test
      @DisplayName("Stringから通貨コードに変換する")
      void shouldConvertToEntityAttribute() {
        // Act
        SampleCurrencyCode result = converter.convertToEntityAttribute("USD");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getValue()).isEqualTo("USD");
      }

      @Test
      @DisplayName("nullデータベースデータは空のインスタンスを返す")
      void shouldReturnEmptyForNullDatabaseData() {
        // Act
        SampleCurrencyCode result = converter.convertToEntityAttribute(null);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isTrue();
      }
    }
  }
}
