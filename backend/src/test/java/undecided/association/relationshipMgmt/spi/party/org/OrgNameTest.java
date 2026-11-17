package undecided.association.relationshipMgmt.spi.party.org;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrgNameTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効な文字列を指定するとインスタンスが作成される")
    void shouldCreateInstanceWhenValidString() {
      OrgName orgName = OrgName.of("株式会社テスト");

      assertThat(orgName).isNotNull();
      assertThat(orgName.value()).isEqualTo("株式会社テスト");
    }

    @Test
    @DisplayName("半角文字も許容される")
    void shouldAcceptHalfWidthCharacters() {
      OrgName orgName = OrgName.of("Test Corp");

      assertThat(orgName).isNotNull();
      assertThat(orgName.value()).isEqualTo("Test Corp");
    }

    @Test
    @DisplayName("全角と半角の混合も許容される")
    void shouldAcceptMixedCharacters() {
      OrgName orgName = OrgName.of("株式会社Test");

      assertThat(orgName).isNotNull();
      assertThat(orgName.value()).isEqualTo("株式会社Test");
    }

    @Test
    @DisplayName("nullを指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenNull() {
      assertThatThrownBy(() -> OrgName.of(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("name must not be null");
    }

    @Test
    @DisplayName("空文字を指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenEmpty() {
      assertThatThrownBy(() -> OrgName.of(""))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("name must not be empty");
    }

    @Test
    @DisplayName("空白文字のみを指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenBlank() {
      assertThatThrownBy(() -> OrgName.of("   "))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("name must not be empty");
    }

    @Test
    @DisplayName("最大文字数を超える場合IllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenExceedsMaxLength() {
      String longName = "a".repeat(101);
      assertThatThrownBy(() -> OrgName.of(longName))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("name must be between");
    }

    @Test
    @DisplayName("空白はトリムされる")
    void shouldTrimWhitespace() {
      OrgName orgName = OrgName.of("  株式会社テスト  ");

      assertThat(orgName.value()).isEqualTo("株式会社テスト");
    }
  }

  @Nested
  @DisplayName("emptyメソッドのテスト")
  class EmptyMethodTest {

    @Test
    @DisplayName("空のインスタンスが作成される")
    void shouldCreateEmptyInstance() {
      OrgName orgName = OrgName.empty();

      assertThat(orgName).isNotNull();
      assertThat(orgName.value()).isNull();
    }
  }

  @Nested
  @DisplayName("デフォルトコンストラクタのテスト")
  class DefaultConstructorTest {

    @Test
    @DisplayName("nullの値を持つインスタンスが作成される")
    void shouldCreateInstanceWithNullValue() {
      OrgName orgName = new OrgName();

      assertThat(orgName).isNotNull();
      assertThat(orgName.value()).isNull();
    }
  }

  @Nested
  @DisplayName("valueメソッドのテスト")
  class ValueMethodTest {

    @Test
    @DisplayName("保持している値を返す")
    void shouldReturnHeldValue() {
      OrgName orgName = OrgName.of("株式会社テスト");

      assertThat(orgName.value()).isEqualTo("株式会社テスト");
    }

    @Test
    @DisplayName("空のインスタンスではnullを返す")
    void shouldReturnNullForEmptyInstance() {
      OrgName orgName = OrgName.empty();

      assertThat(orgName.value()).isNull();
    }
  }

  @Nested
  @DisplayName("isEmptyメソッドのテスト")
  class IsEmptyTest {

    @Test
    @DisplayName("空のインスタンスはtrueを返す")
    void shouldReturnTrueForEmptyInstance() {
      OrgName orgName = OrgName.empty();

      assertThat(orgName.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("値を持つインスタンスはfalseを返す")
    void shouldReturnFalseForNonEmptyInstance() {
      OrgName orgName = OrgName.of("株式会社テスト");

      assertThat(orgName.isEmpty()).isFalse();
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringTest {

    @Test
    @DisplayName("値を文字列として返す")
    void shouldReturnValueAsString() {
      OrgName orgName = OrgName.of("株式会社テスト");

      assertThat(orgName.toString()).isEqualTo("株式会社テスト");
    }

    @Test
    @DisplayName("nullの場合はnullを返す")
    void shouldReturnNullWhenValueIsNull() {
      OrgName orgName = OrgName.empty();

      assertThat(orgName.toString()).isNull();
    }
  }

  @Nested
  @DisplayName("equalsとhashCodeのテスト")
  class EqualsAndHashCodeTest {

    @Test
    @DisplayName("同じ値を持つインスタンスは等しい")
    void shouldBeEqualWhenSameValue() {
      OrgName first = OrgName.of("株式会社テスト");
      OrgName second = OrgName.of("株式会社テスト");

      assertThat(first).isEqualTo(second);
      assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    @DisplayName("異なる値を持つインスタンスは等しくない")
    void shouldNotBeEqualWhenDifferentValue() {
      OrgName first = OrgName.of("株式会社テスト");
      OrgName second = OrgName.of("株式会社サンプル");

      assertThat(first).isNotEqualTo(second);
    }

    @Test
    @DisplayName("空のインスタンス同士は等しい")
    void shouldBeEqualWhenBothEmpty() {
      OrgName first = OrgName.empty();
      OrgName second = OrgName.empty();

      assertThat(first).isEqualTo(second);
    }
  }

  @Nested
  @DisplayName("OrgNameConverterのテスト")
  class ConverterTest {

    private final OrgName.OrgNameConverter converter = new OrgName.OrgNameConverter();

    @Nested
    @DisplayName("convertToDatabaseColumnメソッドのテスト")
    class ConvertToDatabaseColumnTest {

      @Test
      @DisplayName("属性からStringに変換する")
      void shouldConvertToDatabaseColumn() {
        OrgName orgName = OrgName.of("株式会社テスト");

        assertThat(converter.convertToDatabaseColumn(orgName)).isEqualTo("株式会社テスト");
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
        OrgName result = converter.convertToEntityAttribute("株式会社テスト");

        assertThat(result).isNotNull();
        assertThat(result.value()).isEqualTo("株式会社テスト");
      }

      @Test
      @DisplayName("nullデータベースデータは空のインスタンスを返す")
      void shouldReturnEmptyForNullDatabaseData() {
        OrgName result = converter.convertToEntityAttribute(null);

        assertThat(result).isNotNull();
        assertThat(result.value()).isNull();
      }
    }
  }
}
