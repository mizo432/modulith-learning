package undecided.association.relationshipMgmt.spi.party.partyRelationship;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class WhenDisassociatedTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効な日時を指定するとインスタンスが作成される")
    void shouldCreateInstanceWhenValidDateTime() {
      LocalDateTime dateTime = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
      WhenDisassociated whenDisassociated = WhenDisassociated.of(dateTime);

      assertThat(whenDisassociated).isNotNull();
      assertThat(whenDisassociated.value()).isEqualTo(dateTime);
    }

    @Test
    @DisplayName("nullを指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenNull() {
      assertThatThrownBy(() -> WhenDisassociated.of(null))
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
      WhenDisassociated whenDisassociated = WhenDisassociated.current();

      assertThat(whenDisassociated).isNotNull();
      assertThat(whenDisassociated.value()).isNotNull();
    }
  }

  @Nested
  @DisplayName("emptyメソッドのテスト")
  class EmptyMethodTest {

    @Test
    @DisplayName("nullの値を持つインスタンスが作成される")
    void shouldCreateEmptyInstance() {
      WhenDisassociated whenDisassociated = WhenDisassociated.empty();

      assertThat(whenDisassociated).isNotNull();
      assertThat(whenDisassociated.value()).isNull();
    }
  }

  @Nested
  @DisplayName("デフォルトコンストラクタのテスト")
  class DefaultConstructorTest {

    @Test
    @DisplayName("nullの値を持つインスタンスが作成される")
    void shouldCreateInstanceWithNullValue() {
      WhenDisassociated whenDisassociated = new WhenDisassociated();

      assertThat(whenDisassociated).isNotNull();
      assertThat(whenDisassociated.value()).isNull();
    }
  }

  @Nested
  @DisplayName("valueメソッドのテスト")
  class ValueMethodTest {

    @Test
    @DisplayName("保持している日時を返す")
    void shouldReturnHeldDateTime() {
      LocalDateTime dateTime = LocalDateTime.of(2024, 6, 20, 14, 45, 0);
      WhenDisassociated whenDisassociated = WhenDisassociated.of(dateTime);

      assertThat(whenDisassociated.value()).isEqualTo(dateTime);
    }

    @Test
    @DisplayName("空のインスタンスではnullを返す")
    void shouldReturnNullForEmptyInstance() {
      WhenDisassociated whenDisassociated = WhenDisassociated.empty();

      assertThat(whenDisassociated.value()).isNull();
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringTest {

    @Test
    @DisplayName("日時を文字列として返す")
    void shouldReturnDateTimeAsString() {
      LocalDateTime dateTime = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
      WhenDisassociated whenDisassociated = WhenDisassociated.of(dateTime);

      assertThat(whenDisassociated.toString()).isEqualTo(dateTime.toString());
    }

    @Test
    @DisplayName("nullの場合はnull文字列を返す")
    void shouldReturnNullStringWhenValueIsNull() {
      WhenDisassociated whenDisassociated = WhenDisassociated.empty();

      assertThat(whenDisassociated.toString()).isEqualTo("null");
    }
  }

  @Nested
  @DisplayName("equalsとhashCodeのテスト")
  class EqualsAndHashCodeTest {

    @Test
    @DisplayName("同じ値を持つインスタンスは等しい")
    void shouldBeEqualWhenSameValue() {
      LocalDateTime dateTime = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
      WhenDisassociated first = WhenDisassociated.of(dateTime);
      WhenDisassociated second = WhenDisassociated.of(dateTime);

      assertThat(first).isEqualTo(second);
      assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    @DisplayName("異なる値を持つインスタンスは等しくない")
    void shouldNotBeEqualWhenDifferentValue() {
      WhenDisassociated first = WhenDisassociated.of(LocalDateTime.of(2024, 1, 15, 10, 30, 0));
      WhenDisassociated second = WhenDisassociated.of(LocalDateTime.of(2024, 2, 20, 11, 45, 0));

      assertThat(first).isNotEqualTo(second);
    }

    @Test
    @DisplayName("空のインスタンス同士は等しい")
    void shouldBeEqualWhenBothEmpty() {
      WhenDisassociated first = WhenDisassociated.empty();
      WhenDisassociated second = WhenDisassociated.empty();

      assertThat(first).isEqualTo(second);
    }
  }

  @Nested
  @DisplayName("WhenDisassociatedConverterのテスト")
  class ConverterTest {

    private final WhenDisassociated.WhenDisassociatedConverter converter =
        new WhenDisassociated.WhenDisassociatedConverter();

    @Nested
    @DisplayName("convertToDatabaseColumnメソッドのテスト")
    class ConvertToDatabaseColumnTest {

      @Test
      @DisplayName("属性からLocalDateTimeに変換する")
      void shouldConvertToDatabaseColumn() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
        WhenDisassociated whenDisassociated = WhenDisassociated.of(dateTime);

        assertThat(converter.convertToDatabaseColumn(whenDisassociated)).isEqualTo(dateTime);
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

        WhenDisassociated result = converter.convertToEntityAttribute(dateTime);

        assertThat(result).isNotNull();
        assertThat(result.value()).isEqualTo(dateTime);
      }

      @Test
      @DisplayName("nullデータベースデータは空のインスタンスを返す")
      void shouldReturnEmptyForNullDatabaseData() {
        WhenDisassociated result = converter.convertToEntityAttribute(null);

        assertThat(result).isNotNull();
        assertThat(result.value()).isNull();
      }
    }
  }
}
