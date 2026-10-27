package undecided.association.relationshipMgmt.spi.party.person;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LastNameTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効な文字列を指定するとインスタンスが作成される")
    void shouldCreateInstanceWhenValidString() {
      LastName lastName = LastName.of("田中");

      assertThat(lastName).isNotNull();
      assertThat(lastName.value()).isEqualTo("田中");
    }

    @Test
    @DisplayName("半角文字も許容される")
    void shouldAcceptHalfWidthCharacters() {
      LastName lastName = LastName.of("Tanaka");

      assertThat(lastName).isNotNull();
      assertThat(lastName.value()).isEqualTo("Tanaka");
    }

    @Test
    @DisplayName("全角と半角の混合も許容される")
    void shouldAcceptMixedCharacters() {
      LastName lastName = LastName.of("田中Tanaka");

      assertThat(lastName).isNotNull();
      assertThat(lastName.value()).isEqualTo("田中Tanaka");
    }

    @Test
    @DisplayName("nullを指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenNull() {
      assertThatThrownBy(() -> LastName.of(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("lastName must not be null");
    }

    @Test
    @DisplayName("空文字を指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenEmpty() {
      assertThatThrownBy(() -> LastName.of(""))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("lastName must not be empty");
    }

    @Test
    @DisplayName("空白文字のみを指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenBlank() {
      assertThatThrownBy(() -> LastName.of("   "))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("lastName must not be empty");
    }

    @Test
    @DisplayName("最大文字数を超える場合IllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenExceedsMaxLength() {
      String longName = "a".repeat(51);
      assertThatThrownBy(() -> LastName.of(longName))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("lastName must be between");
    }

    @Test
    @DisplayName("空白はトリムされる")
    void shouldTrimWhitespace() {
      LastName lastName = LastName.of("  田中  ");

      assertThat(lastName.value()).isEqualTo("田中");
    }
  }

  @Nested
  @DisplayName("emptyメソッドのテスト")
  class EmptyMethodTest {

    @Test
    @DisplayName("空のインスタンスが作成される")
    void shouldCreateEmptyInstance() {
      LastName lastName = LastName.empty();

      assertThat(lastName).isNotNull();
      assertThat(lastName.value()).isNull();
    }
  }

  @Nested
  @DisplayName("デフォルトコンストラクタのテスト")
  class DefaultConstructorTest {

    @Test
    @DisplayName("nullの値を持つインスタンスが作成される")
    void shouldCreateInstanceWithNullValue() {
      LastName lastName = new LastName();

      assertThat(lastName).isNotNull();
      assertThat(lastName.value()).isNull();
    }
  }

  @Nested
  @DisplayName("valueメソッドのテスト")
  class ValueMethodTest {

    @Test
    @DisplayName("保持している値を返す")
    void shouldReturnHeldValue() {
      LastName lastName = LastName.of("山田");

      assertThat(lastName.value()).isEqualTo("山田");
    }

    @Test
    @DisplayName("空のインスタンスではnullを返す")
    void shouldReturnNullForEmptyInstance() {
      LastName lastName = LastName.empty();

      assertThat(lastName.value()).isNull();
    }
  }

  @Nested
  @DisplayName("isEmptyメソッドのテスト")
  class IsEmptyTest {

    @Test
    @DisplayName("空のインスタンスはtrueを返す")
    void shouldReturnTrueForEmptyInstance() {
      LastName lastName = LastName.empty();

      assertThat(lastName.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("値を持つインスタンスはfalseを返す")
    void shouldReturnFalseForNonEmptyInstance() {
      LastName lastName = LastName.of("田中");

      assertThat(lastName.isEmpty()).isFalse();
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringTest {

    @Test
    @DisplayName("値を文字列として返す")
    void shouldReturnValueAsString() {
      LastName lastName = LastName.of("田中");

      assertThat(lastName.toString()).isEqualTo("田中");
    }

    @Test
    @DisplayName("nullの場合はnullを返す")
    void shouldReturnNullWhenValueIsNull() {
      LastName lastName = LastName.empty();

      assertThat(lastName.toString()).isNull();
    }
  }

  @Nested
  @DisplayName("equalsとhashCodeのテスト")
  class EqualsAndHashCodeTest {

    @Test
    @DisplayName("同じ値を持つインスタンスは等しい")
    void shouldBeEqualWhenSameValue() {
      LastName first = LastName.of("田中");
      LastName second = LastName.of("田中");

      assertThat(first).isEqualTo(second);
      assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    @DisplayName("異なる値を持つインスタンスは等しくない")
    void shouldNotBeEqualWhenDifferentValue() {
      LastName first = LastName.of("田中");
      LastName second = LastName.of("山田");

      assertThat(first).isNotEqualTo(second);
    }

    @Test
    @DisplayName("空のインスタンス同士は等しい")
    void shouldBeEqualWhenBothEmpty() {
      LastName first = LastName.empty();
      LastName second = LastName.empty();

      assertThat(first).isEqualTo(second);
    }
  }

  @Nested
  @DisplayName("LastNameConverterのテスト")
  class ConverterTest {

    private final LastName.LastNameConverter converter = new LastName.LastNameConverter();

    @Nested
    @DisplayName("convertToDatabaseColumnメソッドのテスト")
    class ConvertToDatabaseColumnTest {

      @Test
      @DisplayName("属性からStringに変換する")
      void shouldConvertToDatabaseColumn() {
        LastName lastName = LastName.of("田中");

        assertThat(converter.convertToDatabaseColumn(lastName)).isEqualTo("田中");
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
      @DisplayName("Stringから属性に変換する")
      void shouldConvertToEntityAttribute() {
        LastName result = converter.convertToEntityAttribute("田中");

        assertThat(result).isNotNull();
        assertThat(result.value()).isEqualTo("田中");
      }

      @Test
      @DisplayName("nullデータベースデータは空のインスタンスを返す")
      void shouldReturnEmptyForNullDatabaseData() {
        LastName result = converter.convertToEntityAttribute(null);

        assertThat(result).isNotNull();
        assertThat(result.value()).isNull();
      }
    }
  }
}
