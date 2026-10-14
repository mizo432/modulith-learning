package undecided.association.relationshipMgmt.spi.party;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class GovtAssignedIdTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効な政府発行IDを指定するとインスタンスが作成される")
    void shouldCreateInstanceWhenValidGovtAssignedId() {
      GovtAssignedId govtAssignedId = GovtAssignedId.of("1234567890123");

      assertThat(govtAssignedId).isNotNull();
      assertThat(govtAssignedId.value()).isEqualTo("1234567890123");
    }

    @Test
    @DisplayName("nullを指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenNull() {
      assertThatThrownBy(() -> GovtAssignedId.of(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("govtAssignedId must not be null");
    }

    @Test
    @DisplayName("空文字を指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenEmpty() {
      assertThatThrownBy(() -> GovtAssignedId.of(""))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("govtAssignedId must not be empty");
    }

    @Test
    @DisplayName("空白文字を指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenBlank() {
      assertThatThrownBy(() -> GovtAssignedId.of("   "))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("govtAssignedId must not be empty");
    }

    @Test
    @DisplayName("桁数未満の値を指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenTooShort() {
      assertThatThrownBy(() -> GovtAssignedId.of("")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("桁数超過の値を指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenTooLong() {
      String longString = "1".repeat(51);
      assertThatThrownBy(() -> GovtAssignedId.of(longString))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("govtAssignedId must be between");
    }

    @Test
    @DisplayName("半角数字以外の文字を含む値を指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenNonDigit() {
      assertThatThrownBy(() -> GovtAssignedId.of("123-456-789"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("govtAssignedId must contain only digits");
    }

    @Test
    @DisplayName("空白をトリムしてインスタンスが作成される")
    void shouldTrimWhitespace() {
      GovtAssignedId govtAssignedId = GovtAssignedId.of("  1234567890123  ");

      assertThat(govtAssignedId.value()).isEqualTo("1234567890123");
    }
  }

  @Nested
  @DisplayName("emptyメソッドのテスト")
  class EmptyMethodTest {

    @Test
    @DisplayName("空のインスタンスが作成される")
    void shouldCreateEmptyInstance() {
      GovtAssignedId govtAssignedId = GovtAssignedId.empty();

      assertThat(govtAssignedId).isNotNull();
      assertThat(govtAssignedId.value()).isNull();
      assertThat(govtAssignedId.isEmpty()).isTrue();
    }
  }

  @Nested
  @DisplayName("デフォルトコンストラクタのテスト")
  class DefaultConstructorTest {

    @Test
    @DisplayName("nullの値を持つインスタンスが作成される")
    void shouldCreateInstanceWithNullValue() {
      GovtAssignedId govtAssignedId = new GovtAssignedId();

      assertThat(govtAssignedId).isNotNull();
      assertThat(govtAssignedId.value()).isNull();
    }
  }

  @Nested
  @DisplayName("valueメソッドのテスト")
  class ValueMethodTest {

    @Test
    @DisplayName("保持している値を返す")
    void shouldReturnHeldValue() {
      GovtAssignedId govtAssignedId = GovtAssignedId.of("9876543210987");

      assertThat(govtAssignedId.value()).isEqualTo("9876543210987");
    }

    @Test
    @DisplayName("空のインスタンスではnullを返す")
    void shouldReturnNullForEmptyInstance() {
      GovtAssignedId govtAssignedId = GovtAssignedId.empty();

      assertThat(govtAssignedId.value()).isNull();
    }
  }

  @Nested
  @DisplayName("isEmptyメソッドのテスト")
  class IsEmptyTest {

    @Test
    @DisplayName("空のインスタンスではtrueを返す")
    void shouldReturnTrueForEmptyInstance() {
      GovtAssignedId govtAssignedId = GovtAssignedId.empty();

      assertThat(govtAssignedId.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("値を持つインスタンスではfalseを返す")
    void shouldReturnFalseForNonEmptyInstance() {
      GovtAssignedId govtAssignedId = GovtAssignedId.of("1234567890123");

      assertThat(govtAssignedId.isEmpty()).isFalse();
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringTest {

    @Test
    @DisplayName("値を文字列として返す")
    void shouldReturnValueAsString() {
      GovtAssignedId govtAssignedId = GovtAssignedId.of("1234567890123");

      assertThat(govtAssignedId.toString()).isEqualTo("1234567890123");
    }

    @Test
    @DisplayName("nullの場合はnullを返す")
    void shouldReturnNullWhenValueIsNull() {
      GovtAssignedId govtAssignedId = GovtAssignedId.empty();

      assertThat(govtAssignedId.toString()).isNull();
    }
  }

  @Nested
  @DisplayName("equalsとhashCodeのテスト")
  class EqualsAndHashCodeTest {

    @Test
    @DisplayName("同じ値を持つインスタンスは等しい")
    void shouldBeEqualWhenSameValue() {
      GovtAssignedId first = GovtAssignedId.of("1234567890123");
      GovtAssignedId second = GovtAssignedId.of("1234567890123");

      assertThat(first).isEqualTo(second);
      assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    @DisplayName("異なる値を持つインスタンスは等しくない")
    void shouldNotBeEqualWhenDifferentValue() {
      GovtAssignedId first = GovtAssignedId.of("1234567890123");
      GovtAssignedId second = GovtAssignedId.of("9876543210987");

      assertThat(first).isNotEqualTo(second);
    }

    @Test
    @DisplayName("空のインスタンス同士は等しい")
    void shouldBeEqualWhenBothEmpty() {
      GovtAssignedId first = GovtAssignedId.empty();
      GovtAssignedId second = GovtAssignedId.empty();

      assertThat(first).isEqualTo(second);
    }
  }

  @Nested
  @DisplayName("GovtAssignedIdConverterのテスト")
  class ConverterTest {

    private final GovtAssignedId.GovtAssignedIdConverter converter =
        new GovtAssignedId.GovtAssignedIdConverter();

    @Nested
    @DisplayName("convertToDatabaseColumnメソッドのテスト")
    class ConvertToDatabaseColumnTest {

      @Test
      @DisplayName("属性から文字列に変換する")
      void shouldConvertToDatabaseColumn() {
        GovtAssignedId govtAssignedId = GovtAssignedId.of("1234567890123");

        assertThat(converter.convertToDatabaseColumn(govtAssignedId)).isEqualTo("1234567890123");
      }

      @Test
      @DisplayName("null属性はnullを返す")
      void shouldReturnNullForNullAttribute() {
        assertThat(converter.convertToDatabaseColumn(null)).isNull();
      }
    }

    @Nested
    @DisplayName("convertToEntityAttributeメソッドのテスト")
    class ConvertToEntityAttributeTest {

      @Test
      @DisplayName("文字列から属性に変換する")
      void shouldConvertToEntityAttribute() {
        GovtAssignedId result = converter.convertToEntityAttribute("1234567890123");

        assertThat(result).isNotNull();
        assertThat(result.value()).isEqualTo("1234567890123");
      }

      @Test
      @DisplayName("nullデータベースデータは空のインスタンスを返す")
      void shouldReturnEmptyForNullDatabaseData() {
        GovtAssignedId result = converter.convertToEntityAttribute(null);

        assertThat(result).isNotNull();
        assertThat(result.value()).isNull();
      }
    }
  }
}
