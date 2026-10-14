package undecided.shared.value.datetime;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class WhenOccuredTest {

  @Nested
  @DisplayName("currentメソッドのテスト")
  class CurrentMethodTest {

    @Test
    @DisplayName("現在の日時を持つインスタンスが作成される")
    void shouldCreateInstanceWithCurrentDateTime() {
      WhenOccured whenOccured = WhenOccured.current();

      assertThat(whenOccured).isNotNull();
      assertThat(whenOccured.value()).isNotNull();
    }
  }

  @Nested
  @DisplayName("emptyメソッドのテスト")
  class EmptyMethodTest {

    @Test
    @DisplayName("nullの値を持つインスタンスが作成される")
    void shouldCreateEmptyInstance() {
      WhenOccured whenOccured = WhenOccured.empty();

      assertThat(whenOccured).isNotNull();
      assertThat(whenOccured.value()).isNull();
    }
  }

  @Nested
  @DisplayName("デフォルトコンストラクタのテスト")
  class DefaultConstructorTest {

    @Test
    @DisplayName("nullの値を持つインスタンスが作成される")
    void shouldCreateInstanceWithNullValue() {
      WhenOccured whenOccured = new WhenOccured();

      assertThat(whenOccured).isNotNull();
      assertThat(whenOccured.value()).isNull();
    }
  }

  @Nested
  @DisplayName("valueメソッドのテスト")
  class ValueMethodTest {

    @Test
    @DisplayName("空のインスタンスではnullを返す")
    void shouldReturnNullForEmptyInstance() {
      WhenOccured whenOccured = WhenOccured.empty();

      assertThat(whenOccured.value()).isNull();
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringTest {

    @Test
    @DisplayName("nullの場合はnull文字列を返す")
    void shouldReturnNullStringWhenValueIsNull() {
      WhenOccured whenOccured = WhenOccured.empty();

      assertThat(whenOccured.toString()).isEqualTo("null");
    }
  }

  @Nested
  @DisplayName("WhenOccuredConverterのテスト")
  class ConverterTest {

    private final WhenOccured.WhenOccuredConverter converter =
        new WhenOccured.WhenOccuredConverter();

    @Nested
    @DisplayName("convertToDatabaseColumnメソッドのテスト")
    class ConvertToDatabaseColumnTest {

      @Test
      @DisplayName("null属性はnullを返す")
      void shouldReturnNullForNullAttribute() {
        assertThat(converter.convertToDatabaseColumn(null)).isNull();
      }

      @Test
      @DisplayName("空の属性はnullを返す")
      void shouldReturnNullForEmptyAttribute() {
        WhenOccured whenOccured = WhenOccured.empty();

        assertThat(converter.convertToDatabaseColumn(whenOccured)).isNull();
      }
    }

    @Nested
    @DisplayName("convertToEntityAttributeメソッドのテスト")
    class ConvertToEntityAttributeTest {

      @Test
      @DisplayName("LocalDateTimeから属性に変換する")
      void shouldConvertToEntityAttribute() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 15, 10, 30, 0);

        WhenOccured result = converter.convertToEntityAttribute(dateTime);

        assertThat(result).isNotNull();
        assertThat(result.value()).isEqualTo(dateTime);
      }

      @Test
      @DisplayName("nullデータベースデータは空のインスタンスを返す")
      void shouldReturnEmptyForNullDatabaseData() {
        WhenOccured result = converter.convertToEntityAttribute(null);

        assertThat(result).isNotNull();
        assertThat(result.value()).isNull();
      }
    }
  }
}
