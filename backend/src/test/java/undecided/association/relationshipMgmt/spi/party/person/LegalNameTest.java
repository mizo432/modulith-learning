package undecided.association.relationshipMgmt.spi.party.person;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
class LegalNameTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効な姓と名を指定するとインスタンスが作成される")
    void shouldCreateInstanceWhenValidStrings() {
      LegalName legalName = LegalName.of("田中", "太郎");

      assertThat(legalName).isNotNull();
      assertThat(legalName.lastName()).isNotNull();
      assertThat(legalName.lastName().value()).isEqualTo("田中");
      assertThat(legalName.firstName()).isNotNull();
      assertThat(legalName.firstName().value()).isEqualTo("太郎");
    }

    @Test
    @DisplayName("半角文字も許容される")
    void shouldAcceptHalfWidthCharacters() {
      LegalName legalName = LegalName.of("Tanaka", "Taro");

      assertThat(legalName).isNotNull();
      assertThat(legalName.lastName().value()).isEqualTo("Tanaka");
      assertThat(legalName.firstName().value()).isEqualTo("Taro");
    }

    @Test
    @DisplayName("全角と半角の混合も許容される")
    void shouldAcceptMixedCharacters() {
      LegalName legalName = LegalName.of("田中", "Taro");

      assertThat(legalName).isNotNull();
      assertThat(legalName.lastName().value()).isEqualTo("田中");
      assertThat(legalName.firstName().value()).isEqualTo("Taro");
    }

    @Test
    @DisplayName("姓がnullの場合IllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenLastNameNull() {
      assertThatThrownBy(() -> LegalName.of(null, "太郎"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("lastName must not be null");
    }

    @Test
    @DisplayName("名がnullの場合IllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenFirstNameNull() {
      assertThatThrownBy(() -> LegalName.of("田中", null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("firstName must not be null");
    }

    @Test
    @DisplayName("姓が空文字の場合IllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenLastNameEmpty() {
      assertThatThrownBy(() -> LegalName.of("", "太郎"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("lastName must not be empty");
    }

    @Test
    @DisplayName("名が空文字の場合IllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenFirstNameEmpty() {
      assertThatThrownBy(() -> LegalName.of("田中", ""))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("firstName must not be empty");
    }

    @Test
    @DisplayName("姓が空白文字のみの場合IllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenLastNameBlank() {
      assertThatThrownBy(() -> LegalName.of("   ", "太郎"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("lastName must not be empty");
    }

    @Test
    @DisplayName("名が空白文字のみの場合IllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenFirstNameBlank() {
      assertThatThrownBy(() -> LegalName.of("田中", "   "))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("firstName must not be empty");
    }

    @Test
    @DisplayName("姓が最大文字数を超える場合IllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenLastNameExceedsMaxLength() {
      String longName = "a".repeat(51);
      assertThatThrownBy(() -> LegalName.of(longName, "太郎"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("lastName must be between");
    }

    @Test
    @DisplayName("名が最大文字数を超える場合IllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenFirstNameExceedsMaxLength() {
      String longName = "a".repeat(51);
      assertThatThrownBy(() -> LegalName.of("田中", longName))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("firstName must be between");
    }

    @Test
    @DisplayName("空白はトリムされる")
    void shouldTrimWhitespace() {
      LegalName legalName = LegalName.of("  田中  ", "  太郎  ");

      assertThat(legalName.lastName().value()).isEqualTo("田中");
      assertThat(legalName.firstName().value()).isEqualTo("太郎");
    }
  }

  @Nested
  @DisplayName("emptyメソッドのテスト")
  class EmptyMethodTest {

    @Test
    @DisplayName("空のインスタンスが作成される")
    void shouldCreateEmptyInstance() {
      LegalName legalName = LegalName.empty();

      assertThat(legalName).isNotNull();
      assertThat(legalName.lastName()).isNull();
      assertThat(legalName.firstName()).isNull();
    }
  }

  @Nested
  @DisplayName("デフォルトコンストラクタのテスト")
  class DefaultConstructorTest {

    @Test
    @DisplayName("nullの値を持つインスタンスが作成される")
    void shouldCreateInstanceWithNullValue() {
      LegalName legalName = new LegalName();

      assertThat(legalName).isNotNull();
      assertThat(legalName.lastName()).isNull();
      assertThat(legalName.firstName()).isNull();
    }
  }

  @Nested
  @DisplayName("lastNameメソッドのテスト")
  class LastNameTest {

    @Test
    @DisplayName("姓の値オブジェクトを返す")
    void shouldReturnLastName() {
      LegalName legalName = LegalName.of("田中", "太郎");

      assertThat(legalName.lastName()).isNotNull();
      assertThat(legalName.lastName().value()).isEqualTo("田中");
    }

    @Test
    @DisplayName("空のインスタンスではnullを返す")
    void shouldReturnNullForEmptyInstance() {
      LegalName legalName = LegalName.empty();

      assertThat(legalName.lastName()).isNull();
    }
  }

  @Nested
  @DisplayName("firstNameメソッドのテスト")
  class FirstNameTest {

    @Test
    @DisplayName("名の値オブジェクトを返す")
    void shouldReturnFirstName() {
      LegalName legalName = LegalName.of("田中", "太郎");

      assertThat(legalName.firstName()).isNotNull();
      assertThat(legalName.firstName().value()).isEqualTo("太郎");
    }

    @Test
    @DisplayName("空のインスタンスではnullを返す")
    void shouldReturnNullForEmptyInstance() {
      LegalName legalName = LegalName.empty();

      assertThat(legalName.firstName()).isNull();
    }
  }

  @Nested
  @DisplayName("valueメソッドのテスト")
  class ValueMethodTest {

    @Test
    @DisplayName("「姓 名」の形式で値を返す")
    void shouldReturnHeldValue() {
      LegalName legalName = LegalName.of("山田", "花子");

      assertThat(legalName.value()).isEqualTo("山田 花子");
    }

    @Test
    @DisplayName("空のインスタンスではnullを返す")
    void shouldReturnNullForEmptyInstance() {
      LegalName legalName = LegalName.empty();

      assertThat(legalName.value()).isNull();
    }
  }

  @Nested
  @DisplayName("isEmptyメソッドのテスト")
  class IsEmptyTest {

    @Test
    @DisplayName("空のインスタンスはtrueを返す")
    void shouldReturnTrueForEmptyInstance() {
      LegalName legalName = LegalName.empty();

      assertThat(legalName.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("値を持つインスタンスはfalseを返す")
    void shouldReturnFalseForNonEmptyInstance() {
      LegalName legalName = LegalName.of("田中", "太郎");

      assertThat(legalName.isEmpty()).isFalse();
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringTest {

    @Test
    @DisplayName("値をマスクして返す")
    void shouldReturnMaskedValue() {
      LegalName legalName = LegalName.of("田中", "太郎");

      assertThat(legalName.toString())
          .as("LegalNameのtoString()は機微な情報をマスクして返すこと")
          .isEqualTo("田*");
    }

    @Test
    @DisplayName("nullの場合はnullを返す")
    void shouldReturnNullWhenValueIsNull() {
      LegalName legalName = LegalName.empty();

      assertThat(legalName.toString())
          .as("空のLegalNameのtoString()はnullを返すこと")
          .isNull();
    }
  }

  @Nested
  @DisplayName("equalsとhashCodeのテスト")
  class EqualsAndHashCodeTest {

    @Test
    @DisplayName("同じ値を持つインスタンスは等しい")
    void shouldBeEqualWhenSameValue() {
      LegalName first = LegalName.of("田中", "太郎");
      LegalName second = LegalName.of("田中", "太郎");

      assertThat(first).isEqualTo(second);
      assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    @DisplayName("異なる値を持つインスタンスは等しくない")
    void shouldNotBeEqualWhenDifferentValue() {
      LegalName first = LegalName.of("田中", "太郎");
      LegalName second = LegalName.of("山田", "花子");

      assertThat(first).isNotEqualTo(second);
    }

    @Test
    @DisplayName("空のインスタンス同士は等しい")
    void shouldBeEqualWhenBothEmpty() {
      LegalName first = LegalName.empty();
      LegalName second = LegalName.empty();

      assertThat(first).isEqualTo(second);
    }
  }

  @Nested
  @DisplayName("LegalNameConverterのテスト")
  class ConverterTest {

    private final LegalName.LegalNameConverter converter = new LegalName.LegalNameConverter();

    @Nested
    @DisplayName("convertToDatabaseColumnメソッドのテスト")
    class ConvertToDatabaseColumnTest {

      @Test
      @DisplayName("属性からStringに変換する")
      void shouldConvertToDatabaseColumn() {
        LegalName legalName = LegalName.of("田中", "太郎");

        assertThat(converter.convertToDatabaseColumn(legalName)).isEqualTo("田中 太郎");
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
        LegalName result = converter.convertToEntityAttribute("田中 太郎");

        assertThat(result).isNotNull();
        assertThat(result.lastName()).isNotNull();
        assertThat(result.lastName().value()).isEqualTo("田中");
        assertThat(result.firstName()).isNotNull();
        assertThat(result.firstName().value()).isEqualTo("太郎");
      }

      @Test
      @DisplayName("nullデータベースデータは空のインスタンスを返す")
      void shouldReturnEmptyForNullDatabaseData() {
        LegalName result = converter.convertToEntityAttribute(null);

        assertThat(result).isNotNull();
        assertThat(result.lastName()).isNull();
        assertThat(result.firstName()).isNull();
      }
    }
  }
}
