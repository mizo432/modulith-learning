package com.github.mizo432.shared.common.value.datetime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("WhenOccuredの単体テスト")
class WhenOccuredTest {

  @AfterEach
  void tearDown() {
    com.github.mizo432.shared.common.dateProvider.StaticDateTimeProvider.clear();
  }

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    @Test
    @DisplayName("引数なしコンストラクタでnull値のインスタンスが作成されること")
    void shouldCreateInstanceWithNullValue() {
      // Act
      WhenOccured whenOccured = new WhenOccured();

      // Assert
      assertThat(whenOccured.value()).isNull();
    }
  }

  @Nested
  @DisplayName("current メソッドのテスト")
  class CurrentMethodTest {

    @Test
    @DisplayName("現在の日時を持つインスタンスが作成されること")
    void shouldCreateInstanceWithCurrentDateTime() {
      // Arrange
      LocalDateTime fixedDateTime = LocalDateTime.of(2024, 6, 15, 10, 30, 0);
      com.github.mizo432.shared.common.dateProvider.StaticDateTimeProvider.initialize(fixedDateTime);

      // Act
      WhenOccured whenOccured = WhenOccured.current();

      // Assert
      assertThat(whenOccured.value()).isEqualTo(fixedDateTime);
    }

    @Test
    @DisplayName("currentメソッドでnullではない値が返されること")
    void shouldReturnNonNullValue() {
      // Act
      WhenOccured whenOccured = WhenOccured.current();

      // Assert
      assertThat(whenOccured.value()).isNotNull();
    }
  }

  @Nested
  @DisplayName("empty メソッドのテスト")
  class EmptyMethodTest {

    @Test
    @DisplayName("null値を持つインスタンスが作成されること")
    void shouldCreateEmptyInstance() {
      // Act
      WhenOccured whenOccured = WhenOccured.empty();

      // Assert
      assertThat(whenOccured.value()).isNull();
    }
  }

  @Nested
  @DisplayName("value メソッドのテスト")
  class ValueMethodTest {

    @Test
    @DisplayName("保持している日時を正しく返すこと")
    void shouldReturnHeldDateTime() {
      // Arrange
      LocalDateTime expected = LocalDateTime.of(2024, 12, 25, 15, 45, 30);
      WhenOccured.WhenOccuredConverter converter = new WhenOccured.WhenOccuredConverter();
      WhenOccured whenOccured = converter.convertToEntityAttribute(expected);

      // Act & Assert
      assertThat(whenOccured.value()).isEqualTo(expected);
    }

    @Test
    @DisplayName("null値のインスタンスではnullを返すこと")
    void shouldReturnNullForEmptyInstance() {
      // Arrange
      WhenOccured whenOccured = WhenOccured.empty();

      // Act & Assert
      assertThat(whenOccured.value()).isNull();
    }
  }

  @Nested
  @DisplayName("toString メソッドのテスト")
  class ToStringMethodTest {

    @Test
    @DisplayName("日時を持つインスタンスの文字列表現が正しく返されること")
    void shouldReturnCorrectStringRepresentation() {
      // Arrange
      LocalDateTime dateTime = LocalDateTime.of(2024, 6, 15, 10, 30, 0);
      WhenOccured.WhenOccuredConverter converter = new WhenOccured.WhenOccuredConverter();
      WhenOccured whenOccured = converter.convertToEntityAttribute(dateTime);

      // Act & Assert
      assertThat(whenOccured.toString()).isEqualTo("2024-06-15T10:30");
    }

    @Test
    @DisplayName("null値のインスタンスの文字列表現は\"null\"を返すこと")
    void shouldReturnNullStringForEmptyInstance() {
      // Arrange
      WhenOccured whenOccured = WhenOccured.empty();

      // Act & Assert
      assertThat(whenOccured.toString()).isEqualTo("null");
    }
  }

  @Nested
  @DisplayName("WhenOccuredConverter のテスト")
  class ConverterTest {

    private final WhenOccured.WhenOccuredConverter converter =
        new WhenOccured.WhenOccuredConverter();

    @Nested
    @DisplayName("convertToDatabaseColumn メソッドのテスト")
    class ConvertToDatabaseColumnTest {

      @Test
      @DisplayName("正常にLocalDateTimeに変換されること")
      void shouldConvertToDatabaseColumnSuccessfully() {
        // Arrange
        LocalDateTime expected = LocalDateTime.of(2024, 6, 15, 10, 30, 0);
        WhenOccured whenOccured = converter.convertToEntityAttribute(expected);

        // Act
        LocalDateTime actual = converter.convertToDatabaseColumn(whenOccured);

        // Assert
        assertThat(actual).isEqualTo(expected);
      }

      @Test
      @DisplayName("null属性が渡された場合nullを返すこと")
      void shouldReturnNullWhenAttributeIsNull() {
        // Act & Assert
        assertThat(converter.convertToDatabaseColumn(null)).isNull();
      }

      @Test
      @DisplayName("null値のWhenOccuredが渡された場合nullを返すこと")
      void shouldReturnNullWhenAttributeHasNullValue() {
        // Arrange
        WhenOccured whenOccured = WhenOccured.empty();

        // Act & Assert
        assertThat(converter.convertToDatabaseColumn(whenOccured)).isNull();
      }
    }

    @Nested
    @DisplayName("convertToEntityAttribute メソッドのテスト")
    class ConvertToEntityAttributeTest {

      @Test
      @DisplayName("正常にWhenOccuredに変換されること")
      void shouldConvertToEntityAttributeSuccessfully() {
        // Arrange
        LocalDateTime dbData = LocalDateTime.of(2024, 6, 15, 10, 30, 0);

        // Act
        WhenOccured actual = converter.convertToEntityAttribute(dbData);

        // Assert
        assertThat(actual.value()).isEqualTo(dbData);
      }

      @Test
      @DisplayName("nullデータが渡された場合空のインスタンスを返すこと")
      void shouldReturnEmptyWhenDataIsNull() {
        // Act & Assert
        WhenOccured actual = converter.convertToEntityAttribute(null);
        assertThat(actual.value()).isNull();
      }
    }
  }
}
