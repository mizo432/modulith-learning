package undecided.association.relationshipMgmt.spi.party;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class SearchNameTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効な検索用名称を指定するとインスタンスが作成される")
    void shouldCreateInstanceWhenValidSearchName() {
      SearchName searchName = SearchName.of("山田太郎");

      assertThat(searchName).isNotNull();
      assertThat(searchName.value()).isEqualTo("山田太郎");
    }

    @Test
    @DisplayName("nullを指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenNull() {
      assertThatThrownBy(() -> SearchName.of(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("searchName must not be null");
    }

    @Test
    @DisplayName("空文字を指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenEmpty() {
      assertThatThrownBy(() -> SearchName.of(""))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("searchName must not be empty");
    }

    @Test
    @DisplayName("空白文字を指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenBlank() {
      assertThatThrownBy(() -> SearchName.of("   "))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("searchName must not be empty");
    }

    @Test
    @DisplayName("文字数超過の値を指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenTooLong() {
      String longString = "あ".repeat(51);
      assertThatThrownBy(() -> SearchName.of(longString))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("searchName must be between");
    }

    @Test
    @DisplayName("半角文字を含む値を指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenHalfWidth() {
      assertThatThrownBy(() -> SearchName.of("Yamada"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("searchName must contain only full-width characters");
    }

    @Test
    @DisplayName("空白をトリムしてインスタンスが作成される")
    void shouldTrimWhitespace() {
      SearchName searchName = SearchName.of("  山田太郎  ");

      assertThat(searchName.value()).isEqualTo("山田太郎");
    }

    @Test
    @DisplayName("ひらがなでインスタンスが作成される")
    void shouldCreateInstanceWithHiragana() {
      SearchName searchName = SearchName.of("やまだたろう");

      assertThat(searchName.value()).isEqualTo("やまだたろう");
    }

    @Test
    @DisplayName("カタカナでインスタンスが作成される")
    void shouldCreateInstanceWithKatakana() {
      SearchName searchName = SearchName.of("ヤマダタロウ");

      assertThat(searchName.value()).isEqualTo("ヤマダタロウ");
    }

    @Test
    @DisplayName("漢字とひらがなの混合でインスタンスが作成される")
    void shouldCreateInstanceWithMixedCharacters() {
      SearchName searchName = SearchName.of("山田たろう");

      assertThat(searchName.value()).isEqualTo("山田たろう");
    }
  }

  @Nested
  @DisplayName("emptyメソッドのテスト")
  class EmptyMethodTest {

    @Test
    @DisplayName("空のインスタンスが作成される")
    void shouldCreateEmptyInstance() {
      SearchName searchName = SearchName.empty();

      assertThat(searchName).isNotNull();
      assertThat(searchName.value()).isNull();
      assertThat(searchName.isEmpty()).isTrue();
    }
  }

  @Nested
  @DisplayName("デフォルトコンストラクタのテスト")
  class DefaultConstructorTest {

    @Test
    @DisplayName("nullの値を持つインスタンスが作成される")
    void shouldCreateInstanceWithNullValue() {
      SearchName searchName = new SearchName();

      assertThat(searchName).isNotNull();
      assertThat(searchName.value()).isNull();
    }
  }

  @Nested
  @DisplayName("valueメソッドのテスト")
  class ValueMethodTest {

    @Test
    @DisplayName("保持している値を返す")
    void shouldReturnHeldValue() {
      SearchName searchName = SearchName.of("鈴木一郎");

      assertThat(searchName.value()).isEqualTo("鈴木一郎");
    }

    @Test
    @DisplayName("空のインスタンスではnullを返す")
    void shouldReturnNullForEmptyInstance() {
      SearchName searchName = SearchName.empty();

      assertThat(searchName.value()).isNull();
    }
  }

  @Nested
  @DisplayName("isEmptyメソッドのテスト")
  class IsEmptyTest {

    @Test
    @DisplayName("空のインスタンスではtrueを返す")
    void shouldReturnTrueForEmptyInstance() {
      SearchName searchName = SearchName.empty();

      assertThat(searchName.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("値を持つインスタンスではfalseを返す")
    void shouldReturnFalseForNonEmptyInstance() {
      SearchName searchName = SearchName.of("山田太郎");

      assertThat(searchName.isEmpty()).isFalse();
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringTest {

    @Test
    @DisplayName("値を文字列として返す")
    void shouldReturnValueAsString() {
      SearchName searchName = SearchName.of("山田太郎");

      assertThat(searchName.toString()).isEqualTo("山田太郎");
    }

    @Test
    @DisplayName("nullの場合はnullを返す")
    void shouldReturnNullWhenValueIsNull() {
      SearchName searchName = SearchName.empty();

      assertThat(searchName.toString()).isNull();
    }
  }

  @Nested
  @DisplayName("equalsとhashCodeのテスト")
  class EqualsAndHashCodeTest {

    @Test
    @DisplayName("同じ値を持つインスタンスは等しい")
    void shouldBeEqualWhenSameValue() {
      SearchName first = SearchName.of("山田太郎");
      SearchName second = SearchName.of("山田太郎");

      assertThat(first).isEqualTo(second);
      assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    @DisplayName("異なる値を持つインスタンスは等しくない")
    void shouldNotBeEqualWhenDifferentValue() {
      SearchName first = SearchName.of("山田太郎");
      SearchName second = SearchName.of("鈴木一郎");

      assertThat(first).isNotEqualTo(second);
    }

    @Test
    @DisplayName("空のインスタンス同士は等しい")
    void shouldBeEqualWhenBothEmpty() {
      SearchName first = SearchName.empty();
      SearchName second = SearchName.empty();

      assertThat(first).isEqualTo(second);
    }
  }

  @Nested
  @DisplayName("SearchNameConverterのテスト")
  class ConverterTest {

    private final SearchName.SearchNameConverter converter = new SearchName.SearchNameConverter();

    @Nested
    @DisplayName("convertToDatabaseColumnメソッドのテスト")
    class ConvertToDatabaseColumnTest {

      @Test
      @DisplayName("属性から文字列に変換する")
      void shouldConvertToDatabaseColumn() {
        SearchName searchName = SearchName.of("山田太郎");

        assertThat(converter.convertToDatabaseColumn(searchName)).isEqualTo("山田太郎");
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
        SearchName result = converter.convertToEntityAttribute("山田太郎");

        assertThat(result).isNotNull();
        assertThat(result.value()).isEqualTo("山田太郎");
      }

      @Test
      @DisplayName("nullデータベースデータは空のインスタンスを返す")
      void shouldReturnEmptyForNullDatabaseData() {
        SearchName result = converter.convertToEntityAttribute(null);

        assertThat(result).isNotNull();
        assertThat(result.value()).isNull();
      }
    }
  }
}
