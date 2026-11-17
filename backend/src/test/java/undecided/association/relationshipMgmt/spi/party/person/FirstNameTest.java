package undecided.association.relationshipMgmt.spi.party.person;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
class FirstNameTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効な文字列を指定するとインスタンスが作成される")
    void shouldCreateInstanceWhenValidString() {
      FirstName firstName = FirstName.of("太郎");

      assertThat(firstName).isNotNull();
      assertThat(firstName.value()).isEqualTo("太郎");
    }

    @Test
    @DisplayName("半角文字も許容される")
    void shouldAcceptHalfWidthCharacters() {
      FirstName firstName = FirstName.of("Taro");

      assertThat(firstName).isNotNull();
      assertThat(firstName.value()).isEqualTo("Taro");
    }

    @Test
    @DisplayName("全角と半角の混合も許容される")
    void shouldAcceptMixedCharacters() {
      FirstName firstName = FirstName.of("田中Taro");

      assertThat(firstName).isNotNull();
      assertThat(firstName.value()).isEqualTo("田中Taro");
    }

    @Test
    @DisplayName("nullを指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenNull() {
      assertThatThrownBy(() -> FirstName.of(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("firstName must not be null");
    }

    @Test
    @DisplayName("空文字を指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenEmpty() {
      assertThatThrownBy(() -> FirstName.of(""))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("firstName must not be empty");
    }

    @Test
    @DisplayName("空白文字のみを指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenBlank() {
      assertThatThrownBy(() -> FirstName.of("   "))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("firstName must not be empty");
    }

    @Test
    @DisplayName("最大文字数を超える場合IllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenExceedsMaxLength() {
      String longName = "a".repeat(51);
      assertThatThrownBy(() -> FirstName.of(longName))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("firstName must be between");
    }

    @Test
    @DisplayName("空白はトリムされる")
    void shouldTrimWhitespace() {
      FirstName firstName = FirstName.of("  太郎  ");

      assertThat(firstName.value()).isEqualTo("太郎");
    }
  }

  @Nested
  @DisplayName("emptyメソッドのテスト")
  class EmptyMethodTest {

    @Test
    @DisplayName("空のインスタンスが作成される")
    void shouldCreateEmptyInstance() {
      FirstName firstName = FirstName.empty();

      assertThat(firstName).isNotNull();
      assertThat(firstName.value()).isNull();
    }
  }

  @Nested
  @DisplayName("デフォルトコンストラクタのテスト")
  class DefaultConstructorTest {

    @Test
    @DisplayName("nullの値を持つインスタンスが作成される")
    void shouldCreateInstanceWithNullValue() {
      FirstName firstName = new FirstName();

      assertThat(firstName).isNotNull();
      assertThat(firstName.value()).isNull();
    }
  }

  @Nested
  @DisplayName("valueメソッドのテスト")
  class ValueMethodTest {

    @Test
    @DisplayName("保持している値を返す")
    void shouldReturnHeldValue() {
      FirstName firstName = FirstName.of("花子");

      assertThat(firstName.value()).isEqualTo("花子");
    }

    @Test
    @DisplayName("空のインスタンスではnullを返す")
    void shouldReturnNullForEmptyInstance() {
      FirstName firstName = FirstName.empty();

      assertThat(firstName.value()).isNull();
    }
  }

  @Nested
  @DisplayName("isEmptyメソッドのテスト")
  class IsEmptyTest {

    @Test
    @DisplayName("空のインスタンスはtrueを返す")
    void shouldReturnTrueForEmptyInstance() {
      FirstName firstName = FirstName.empty();

      assertThat(firstName.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("値を持つインスタンスはfalseを返す")
    void shouldReturnFalseForNonEmptyInstance() {
      FirstName firstName = FirstName.of("太郎");

      assertThat(firstName.isEmpty()).isFalse();
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringTest {

    @Test
    @DisplayName("値をマスクして返す")
    void shouldReturnMaskedValue() {
      FirstName firstName = FirstName.of("太郎");

      assertThat(firstName.toString())
          .as("FirstNameのtoString()は機微な情報をマスクして返すこと")
          .isEqualTo("太*");
    }

    @Test
    @DisplayName("nullの場合はnullを返す")
    void shouldReturnNullWhenValueIsNull() {
      FirstName firstName = FirstName.empty();

      assertThat(firstName.toString())
          .as("空のFirstNameのtoString()はnullを返すこと")
          .isNull();
    }
  }

  @Nested
  @DisplayName("equalsとhashCodeのテスト")
  class EqualsAndHashCodeTest {

    @Test
    @DisplayName("同じ値を持つインスタンスは等しい")
    void shouldBeEqualWhenSameValue() {
      FirstName first = FirstName.of("太郎");
      FirstName second = FirstName.of("太郎");

      assertThat(first).isEqualTo(second);
      assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    @DisplayName("異なる値を持つインスタンスは等しくない")
    void shouldNotBeEqualWhenDifferentValue() {
      FirstName first = FirstName.of("太郎");
      FirstName second = FirstName.of("花子");

      assertThat(first).isNotEqualTo(second);
    }

    @Test
    @DisplayName("空のインスタンス同士は等しい")
    void shouldBeEqualWhenBothEmpty() {
      FirstName first = FirstName.empty();
      FirstName second = FirstName.empty();

      assertThat(first).isEqualTo(second);
    }
  }

  @Nested
  @DisplayName("FirstNameConverterのテスト")
  class ConverterTest {

    private final FirstName.FirstNameConverter converter = new FirstName.FirstNameConverter();

    @Nested
    @DisplayName("convertToDatabaseColumnメソッドのテスト")
    class ConvertToDatabaseColumnTest {

      @Test
      @DisplayName("属性からStringに変換する")
      void shouldConvertToDatabaseColumn() {
        FirstName firstName = FirstName.of("太郎");

        assertThat(converter.convertToDatabaseColumn(firstName)).isEqualTo("太郎");
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
        FirstName result = converter.convertToEntityAttribute("太郎");

        assertThat(result).isNotNull();
        assertThat(result.value()).isEqualTo("太郎");
      }

      @Test
      @DisplayName("nullデータベースデータは空のインスタンスを返す")
      void shouldReturnEmptyForNullDatabaseData() {
        FirstName result = converter.convertToEntityAttribute(null);

        assertThat(result).isNotNull();
        assertThat(result.value()).isNull();
      }
    }
  }
}
