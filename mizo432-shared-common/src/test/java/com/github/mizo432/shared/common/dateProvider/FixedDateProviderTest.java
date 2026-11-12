package com.github.mizo432.shared.common.dateProvider;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("FixedDateProviderの単体テスト")
class FixedDateProviderTest {

  @AfterEach
  void tearDown() {
    FixedDateProvider.clear();
  }

  @Nested
  @DisplayName("initialize メソッドのテスト")
  class InitializeMethodTest {

    @Test
    @DisplayName("指定された日付が現在の日付として設定されること")
    void shouldSetSpecifiedDateAsCurrentDate() {
      // Arrange
      LocalDate fixedDate = LocalDate.of(2024, 6, 15);

      // Act
      FixedDateProvider.initialize(fixedDate);

      // Assert
      LocalDate actualDate = DateProvider.currentLocalDate();
      assertThat(actualDate).isEqualTo(fixedDate);
    }

    @Test
    @DisplayName("日付が設定された後、currentLocalDateTimeの日付部分が一致すること")
    void shouldHaveMatchingDatePartInLocalDateTime() {
      // Arrange
      LocalDate fixedDate = LocalDate.of(2024, 12, 25);

      // Act
      FixedDateProvider.initialize(fixedDate);

      // Assert
      LocalDateTime dateTime = DateProvider.currentLocalDateTime();
      assertThat(dateTime.toLocalDate()).isEqualTo(fixedDate);
    }
  }

  @Nested
  @DisplayName("clear メソッドのテスト")
  class ClearMethodTest {

    @Test
    @DisplayName("プロバイダーがデフォルト状態にリセットされること")
    void shouldResetProviderToDefault() {
      // Arrange
      FixedDateProvider.initialize(LocalDate.of(2024, 6, 15));
      assertThat(DateProvider.currentLocalDate()).isEqualTo(LocalDate.of(2024, 6, 15));

      // Act
      FixedDateProvider.clear();

      // Assert
      assertThat(DateProvider.currentLocalDate()).isEqualTo(LocalDate.now());
    }
  }

  @Nested
  @DisplayName("now メソッドのテスト")
  class NowMethodTest {

    @Test
    @DisplayName("固定された日付と現在の時間を組み合わせた日時が返されること")
    void shouldReturnFixedDateWithCurrentTime() {
      // Arrange
      LocalDate fixedDate = LocalDate.of(2024, 6, 15);
      FixedDateProvider.initialize(fixedDate);

      // Act
      LocalDateTime dateTime = DateProvider.currentLocalDateTime();

      // Assert
      assertThat(dateTime.toLocalDate()).isEqualTo(fixedDate);
      assertThat(dateTime.toLocalTime()).isNotNull();
    }
  }
}
