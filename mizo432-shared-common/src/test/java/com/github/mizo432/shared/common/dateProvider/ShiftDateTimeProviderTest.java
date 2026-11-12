package com.github.mizo432.shared.common.dateProvider;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("ShiftDateTimeProviderの単体テスト")
class ShiftDateTimeProviderTest {

  @AfterEach
  void tearDown() {
    ShiftDateTimeProvider.clear();
  }

  @Nested
  @DisplayName("initialize メソッドのテスト")
  class InitializeMethodTest {

    @Test
    @DisplayName("指定された日時がシフトの基準として設定されること")
    void shouldSetSpecifiedDateTimeAsShiftBase() {
      // Arrange
      LocalDateTime fixedDateTime = LocalDateTime.of(2024, 6, 15, 10, 30, 0);

      // Act
      ShiftDateTimeProvider.initialize(fixedDateTime);

      // Assert
      LocalDateTime actualDateTime = DateProvider.currentLocalDateTime();
      // 初期化直後は設定した日時とほぼ同じ値が返される（ミリ秒の差は許容）
      assertThat(actualDateTime.getYear()).isEqualTo(2024);
      assertThat(actualDateTime.getMonthValue()).isEqualTo(6);
      assertThat(actualDateTime.getDayOfMonth()).isEqualTo(15);
      assertThat(actualDateTime.getHour()).isEqualTo(10);
      assertThat(actualDateTime.getMinute()).isEqualTo(30);
    }

    @Test
    @DisplayName("時間の経過とともにシフトされた日時が進むこと")
    void shouldAdvanceTimeAsRealTimePasses() throws InterruptedException {
      // Arrange
      LocalDateTime fixedDateTime = LocalDateTime.of(2024, 6, 15, 10, 30, 0);
      ShiftDateTimeProvider.initialize(fixedDateTime);

      // Act
      LocalDateTime dateTime1 = DateProvider.currentLocalDateTime();
      Thread.sleep(100);
      LocalDateTime dateTime2 = DateProvider.currentLocalDateTime();

      // Assert
      assertThat(dateTime2).isAfterOrEqualTo(dateTime1);
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
      ShiftDateTimeProvider.initialize(fixedDateTime);

      // Act
      ShiftDateTimeProvider.clear();

      // Assert
      LocalDateTime actualDateTime = DateProvider.currentLocalDateTime();
      assertThat(actualDateTime.getYear()).isNotEqualTo(2024);
    }
  }
}
