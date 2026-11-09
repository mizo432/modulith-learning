package undecided.association.relationshipMgmt.spi.party.partyRelationship;

import com.github.mizo432.shared.exception.BusinessException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class WhenAssociatedTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効な日時を指定するとインスタンスが作成される")
    void shouldCreateInstanceWhenValidDateTime() {
      LocalDateTime dateTime = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
      WhenAssociated whenAssociated = WhenAssociated.of(dateTime);

      assertThat(whenAssociated).isNotNull();
      assertThat(whenAssociated.value()).isEqualTo(dateTime);
    }

    @Test
    @DisplayName("nullを指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenNull() {
      assertThatThrownBy(() -> WhenAssociated.of(null))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("dateTime must not be null");
    }
  }

  @Nested
  @DisplayName("currentメソッドのテスト")
  class CurrentMethodTest {

    @Test
    @DisplayName("現在の日時を持つインスタンスが作成される")
    void shouldCreateInstanceWithCurrentDateTime() {
      WhenAssociated whenAssociated = WhenAssociated.current();

      assertThat(whenAssociated).isNotNull();
      assertThat(whenAssociated.value()).isNotNull();
    }
  }

  @Nested
  @DisplayName("emptyメソッドのテスト")
  class EmptyMethodTest {

    @Test
    @DisplayName("nullの値を持つインスタンスが作成される")
    void shouldCreateEmptyInstance() {
      WhenAssociated whenAssociated = WhenAssociated.empty();

      assertThat(whenAssociated).isNotNull();
      assertThat(whenAssociated.value()).isNull();
    }
  }

  @Nested
  @DisplayName("デフォルトコンストラクタのテスト")
  class DefaultConstructorTest {

    @Test
    @DisplayName("nullの値を持つインスタンスが作成される")
    void shouldCreateInstanceWithNullValue() {
      WhenAssociated whenAssociated = new WhenAssociated();

      assertThat(whenAssociated).isNotNull();
      assertThat(whenAssociated.value()).isNull();
    }
  }

  @Nested
  @DisplayName("valueメソッドのテスト")
  class ValueMethodTest {

    @Test
    @DisplayName("保持している日時を返す")
    void shouldReturnHeldDateTime() {
      LocalDateTime dateTime = LocalDateTime.of(2024, 6, 20, 14, 45, 0);
      WhenAssociated whenAssociated = WhenAssociated.of(dateTime);

      assertThat(whenAssociated.value()).isEqualTo(dateTime);
    }

    @Test
    @DisplayName("空のインスタンスではnullを返す")
    void shouldReturnNullForEmptyInstance() {
      WhenAssociated whenAssociated = WhenAssociated.empty();

      assertThat(whenAssociated.value()).isNull();
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringTest {

    @Test
    @DisplayName("日時を文字列として返す")
    void shouldReturnDateTimeAsString() {
      LocalDateTime dateTime = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
      WhenAssociated whenAssociated = WhenAssociated.of(dateTime);

      assertThat(whenAssociated.toString()).isEqualTo(dateTime.toString());
    }

    @Test
    @DisplayName("nullの場合はnull文字列を返す")
    void shouldReturnNullStringWhenValueIsNull() {
      WhenAssociated whenAssociated = WhenAssociated.empty();

      assertThat(whenAssociated.toString()).isEqualTo("null");
    }
  }

  @Nested
  @DisplayName("equalsとhashCodeのテスト")
  class EqualsAndHashCodeTest {

    @Test
    @DisplayName("同じ値を持つインスタンスは等しい")
    void shouldBeEqualWhenSameValue() {
      LocalDateTime dateTime = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
      WhenAssociated first = WhenAssociated.of(dateTime);
      WhenAssociated second = WhenAssociated.of(dateTime);

      assertThat(first).isEqualTo(second);
      assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    @DisplayName("異なる値を持つインスタンスは等しくない")
    void shouldNotBeEqualWhenDifferentValue() {
      WhenAssociated first = WhenAssociated.of(LocalDateTime.of(2024, 1, 15, 10, 30, 0));
      WhenAssociated second = WhenAssociated.of(LocalDateTime.of(2024, 2, 20, 11, 45, 0));

      assertThat(first).isNotEqualTo(second);
    }

    @Test
    @DisplayName("空のインスタンス同士は等しい")
    void shouldBeEqualWhenBothEmpty() {
      WhenAssociated first = WhenAssociated.empty();
      WhenAssociated second = WhenAssociated.empty();

      assertThat(first).isEqualTo(second);
    }
  }

  @Nested
  @DisplayName("WhenAssociatedConverterのテスト")
  class ConverterTest {

    private final WhenAssociated.WhenAssociatedConverter converter =
        new WhenAssociated.WhenAssociatedConverter();

    @Nested
    @DisplayName("convertToDatabaseColumnメソッドのテスト")
    class ConvertToDatabaseColumnTest {

      @Test
      @DisplayName("属性からLocalDateTimeに変換する")
      void shouldConvertToDatabaseColumn() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
        WhenAssociated whenAssociated = WhenAssociated.of(dateTime);

        assertThat(converter.convertToDatabaseColumn(whenAssociated)).isEqualTo(dateTime);
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

        WhenAssociated result = converter.convertToEntityAttribute(dateTime);

        assertThat(result).isNotNull();
        assertThat(result.value()).isEqualTo(dateTime);
      }

      @Test
      @DisplayName("nullデータベースデータは空のインスタンスを返す")
      void shouldReturnEmptyForNullDatabaseData() {
        WhenAssociated result = converter.convertToEntityAttribute(null);

        assertThat(result).isNotNull();
        assertThat(result.value()).isNull();
      }
    }
  }
}
