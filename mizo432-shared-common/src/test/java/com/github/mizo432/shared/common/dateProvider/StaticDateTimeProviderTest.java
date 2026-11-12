package com.github.mizo432.shared.common.dateProvider;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("StaticDateTimeProviderの単体テスト")
class StaticDateTimeProviderTest {

  @AfterEach
  void tearDown() {
    StaticDateTimeProvider.clear();
  }

  @Nested
  @DisplayName("initialize メソッドのテスト")
  class InitializeMethodTest {

    @Test
    @DisplayName("指定された日時が現在の日時として設定されること")
    void shouldSetSpecifiedDateTimeAsCurrent() {
      // Arrange
      LocalDateTime fixedDateTime = LocalDateTime.of(2024, 6, 15, 10, 30, 0);

      // Act
      StaticDateTimeProvider.initialize(fixedDateTime);

      // Assert
      LocalDateTime actualDateTime = DateProvider.currentLocalDateTime();
      assertThat(actualDateTime).isEqualTo(fixedDateTime);
    }

    @Test
    @DisplayName("複数回呼び出しても同じ日時が返されること")
    void shouldReturnSameDateTimeOnMultipleCalls() {
      // Arrange
      LocalDateTime fixedDateTime = LocalDateTime.of(2024, 6, 15, 10, 30, 0);
      StaticDateTimeProvider.initialize(fixedDateTime);

      // Act
      LocalDateTime dateTime1 = DateProvider.currentLocalDateTime();
      LocalDateTime dateTime2 = DateProvider.currentLocalDateTime();

      // Assert
      assertThat(dateTime1).isEqualTo(dateTime2).isEqualTo(fixedDateTime);
    }
  }

  @Nested
  @DisplayName("clear メソッドのテスト")
  class ClearMethodTest {

    @Test
    @DisplayName("プロバイダーがデフォルト状態にリセットされること")
    void shouldResetProviderToDefault() {
      // Arrange
      LocalDateTime fixedDateTime = LocalDateTime.of(2024, 6, 15, 10, 30, 0);
      StaticDateTimeProvider.initialize(fixedDateTime);
      assertThat(DateProvider.currentLocalDateTime()).isEqualTo(fixedDateTime);

      // Act
      StaticDateTimeProvider.clear();

      // Assert
      assertThat(DateProvider.currentLocalDateTime()).isNotEqualTo(fixedDateTime);
    }
  }
}
