package undecided.association.relationshipMgmt.spi.party.partyRelationship;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class WhenAassociatedTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効な日時を指定するとインスタンスが作成される")
    void shouldCreateInstanceWhenValidDateTime() {
      LocalDateTime dateTime = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
      WhenAassociated whenAassociated = WhenAassociated.of(dateTime);

      assertThat(whenAassociated).isNotNull();
      assertThat(whenAassociated.value()).isEqualTo(dateTime);
    }

    @Test
    @DisplayName("nullを指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenNull() {
      assertThatThrownBy(() -> WhenAassociated.of(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("dateTime must not be null");
    }
  }

  @Nested
  @DisplayName("currentメソッドのテスト")
  class CurrentMethodTest {

    @Test
    @DisplayName("現在の日時を持つインスタンスが作成される")
    void shouldCreateInstanceWithCurrentDateTime() {
      WhenAassociated whenAassociated = WhenAassociated.current();

      assertThat(whenAassociated).isNotNull();
      assertThat(whenAassociated.value()).isNotNull();
    }
  }

  @Nested
  @DisplayName("emptyメソッドのテスト")
  class EmptyMethodTest {

    @Test
    @DisplayName("nullの値を持つインスタンスが作成される")
    void shouldCreateEmptyInstance() {
      WhenAassociated whenAassociated = WhenAassociated.empty();

      assertThat(whenAassociated).isNotNull();
      assertThat(whenAassociated.value()).isNull();
    }
  }

  @Nested
  @DisplayName("デフォルトコンストラクタのテスト")
  class DefaultConstructorTest {

    @Test
    @DisplayName("nullの値を持つインスタンスが作成される")
    void shouldCreateInstanceWithNullValue() {
      WhenAassociated whenAassociated = new WhenAassociated();

      assertThat(whenAassociated).isNotNull();
      assertThat(whenAassociated.value()).isNull();
    }
  }

  @Nested
  @DisplayName("valueメソッドのテスト")
  class ValueMethodTest {

    @Test
    @DisplayName("保持している日時を返す")
    void shouldReturnHeldDateTime() {
      LocalDateTime dateTime = LocalDateTime.of(2024, 6, 20, 14, 45, 0);
      WhenAassociated whenAassociated = WhenAassociated.of(dateTime);

      assertThat(whenAassociated.value()).isEqualTo(dateTime);
    }

    @Test
    @DisplayName("空のインスタンスではnullを返す")
    void shouldReturnNullForEmptyInstance() {
      WhenAassociated whenAassociated = WhenAassociated.empty();

      assertThat(whenAassociated.value()).isNull();
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringTest {

    @Test
    @DisplayName("日時を文字列として返す")
    void shouldReturnDateTimeAsString() {
      LocalDateTime dateTime = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
      WhenAassociated whenAassociated = WhenAassociated.of(dateTime);

      assertThat(whenAassociated.toString()).isEqualTo(dateTime.toString());
    }

    @Test
    @DisplayName("nullの場合はnull文字列を返す")
    void shouldReturnNullStringWhenValueIsNull() {
      WhenAassociated whenAassociated = WhenAassociated.empty();

      assertThat(whenAassociated.toString()).isEqualTo("null");
    }
  }

  @Nested
  @DisplayName("equalsとhashCodeのテスト")
  class EqualsAndHashCodeTest {

    @Test
    @DisplayName("同じ値を持つインスタンスは等しい")
    void shouldBeEqualWhenSameValue() {
      LocalDateTime dateTime = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
      WhenAassociated first = WhenAassociated.of(dateTime);
      WhenAassociated second = WhenAassociated.of(dateTime);

      assertThat(first).isEqualTo(second);
      assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    @DisplayName("異なる値を持つインスタンスは等しくない")
    void shouldNotBeEqualWhenDifferentValue() {
      WhenAassociated first = WhenAassociated.of(LocalDateTime.of(2024, 1, 15, 10, 30, 0));
      WhenAassociated second = WhenAassociated.of(LocalDateTime.of(2024, 2, 20, 11, 45, 0));

      assertThat(first).isNotEqualTo(second);
    }

    @Test
    @DisplayName("空のインスタンス同士は等しい")
    void shouldBeEqualWhenBothEmpty() {
      WhenAassociated first = WhenAassociated.empty();
      WhenAassociated second = WhenAassociated.empty();

      assertThat(first).isEqualTo(second);
    }
  }

  @Nested
  @DisplayName("WhenAassociatedConverterのテスト")
  class ConverterTest {

    private final WhenAassociated.WhenAassociatedConverter converter =
        new WhenAassociated.WhenAassociatedConverter();

    @Nested
    @DisplayName("convertToDatabaseColumnメソッドのテスト")
    class ConvertToDatabaseColumnTest {

      @Test
      @DisplayName("属性からLocalDateTimeに変換する")
      void shouldConvertToDatabaseColumn() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
        WhenAassociated whenAassociated = WhenAassociated.of(dateTime);

        assertThat(converter.convertToDatabaseColumn(whenAassociated)).isEqualTo(dateTime);
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
      @DisplayName("LocalDateTimeから属性に変換する")
      void shouldConvertToEntityAttribute() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 15, 10, 30, 0);

        WhenAassociated result = converter.convertToEntityAttribute(dateTime);

        assertThat(result).isNotNull();
        assertThat(result.value()).isEqualTo(dateTime);
      }

      @Test
      @DisplayName("nullデータベースデータは空のインスタンスを返す")
      void shouldReturnEmptyForNullDatabaseData() {
        WhenAassociated result = converter.convertToEntityAttribute(null);

        assertThat(result).isNotNull();
        assertThat(result.value()).isNull();
      }
    }
  }
}
