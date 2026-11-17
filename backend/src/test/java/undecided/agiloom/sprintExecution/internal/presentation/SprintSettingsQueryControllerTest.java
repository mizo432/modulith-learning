package undecided.agiloom.sprintExecution.internal.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.agiloom.sprintExecution.internal.query.SprintSettingsQueryService;
import undecided.agiloom.sprintExecution.spi.EndDayOfWeek;
import undecided.agiloom.sprintExecution.spi.SprintSettings;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@ExtendWith(MockitoExtension.class)
@DisplayName("SprintSettingsQueryControllerのテスト")
class SprintSettingsQueryControllerTest {

  @Mock
  private SprintSettingsQueryService sprintSettingsQueryService;

  @InjectMocks
  private SprintSettingsQueryController controller;

  private SnowflakeId productId;

  @BeforeEach
  void setUp() {
    productId = SnowflakeId.of(100L);
  }

  @Nested
  @DisplayName("calculateEndDateメソッドのテスト")
  class CalculateEndDateTest {

    @Test
    @DisplayName("正常に終了日を計算し、レスポンスを返すこと")
    void shouldReturnCalculatedEndDate() {
      // Arrange
      LocalDate startDate = LocalDate.of(2026, 11, 16);
      LocalDate expectedEndDate = LocalDate.of(2026, 12, 4);

      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(1L))
          .productId(productId)
          .businessDays(10)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .build();

      when(sprintSettingsQueryService.getSettingsOrDefault(productId)).thenReturn(settings);
      when(sprintSettingsQueryService.calculateEndDate(productId, startDate))
          .thenReturn(expectedEndDate);

      // Act
      CalculateEndDateResponse response = controller.calculateEndDate(productId.value().toString(), startDate);

      // Assert
      assertThat(response).isNotNull();
      assertThat(response.getStartDate()).isEqualTo(startDate);
      assertThat(response.getEndDate()).isEqualTo(expectedEndDate);
      assertThat(response.getBusinessDays()).isEqualTo(10);
      assertThat(response.getEndDayOfWeekLabel()).isEqualTo("FRIDAY");
      assertThat(response.getIsPastDate()).isFalse();
    }

    @Test
    @DisplayName("終了日が過去の日付の場合、isPastDateがtrueになること")
    void shouldMarkPastDateWhenEndDateIsInPast() {
      // Arrange
      LocalDate startDate = LocalDate.of(2020, 1, 1);
      LocalDate expectedEndDate = LocalDate.of(2020, 1, 10);

      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(2L))
          .productId(productId)
          .businessDays(5)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .build();

      when(sprintSettingsQueryService.getSettingsOrDefault(productId)).thenReturn(settings);
      when(sprintSettingsQueryService.calculateEndDate(productId, startDate))
          .thenReturn(expectedEndDate);

      // Act
      CalculateEndDateResponse response = controller.calculateEndDate(productId.value().toString(), startDate);

      // Assert
      assertThat(response.getIsPastDate()).isTrue();
    }

    @Test
    @DisplayName("終了曜日が水曜日の場合、正しいラベルが返ること")
    void shouldReturnWednesdayLabel() {
      // Arrange
      LocalDate startDate = LocalDate.of(2026, 11, 16);
      LocalDate expectedEndDate = LocalDate.of(2026, 11, 25);

      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(3L))
          .productId(productId)
          .businessDays(3)
          .endDayOfWeek(EndDayOfWeek.WEDNESDAY)
          .build();

      when(sprintSettingsQueryService.getSettingsOrDefault(productId)).thenReturn(settings);
      when(sprintSettingsQueryService.calculateEndDate(productId, startDate))
          .thenReturn(expectedEndDate);

      // Act
      CalculateEndDateResponse response = controller.calculateEndDate(productId.value().toString(), startDate);

      // Assert
      assertThat(response.getEndDayOfWeekLabel()).isEqualTo("WEDNESDAY");
      assertThat(response.getBusinessDays()).isEqualTo(3);
    }

    @Test
    @DisplayName("1営業日の設定で正しく計算されること")
    void shouldCalculateWithOneBusinessDay() {
      // Arrange
      LocalDate startDate = LocalDate.of(2026, 11, 16);
      LocalDate expectedEndDate = LocalDate.of(2026, 11, 20);

      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(4L))
          .productId(productId)
          .businessDays(1)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .build();

      when(sprintSettingsQueryService.getSettingsOrDefault(productId)).thenReturn(settings);
      when(sprintSettingsQueryService.calculateEndDate(productId, startDate))
          .thenReturn(expectedEndDate);

      // Act
      CalculateEndDateResponse response = controller.calculateEndDate(productId.value().toString(), startDate);

      // Assert
      assertThat(response.getEndDate()).isEqualTo(expectedEndDate);
      assertThat(response.getBusinessDays()).isEqualTo(1);
    }

    @Test
    @DisplayName("30営業日の設定で正しく計算されること")
    void shouldCalculateWithMaxBusinessDays() {
      // Arrange
      LocalDate startDate = LocalDate.of(2026, 11, 16);
      LocalDate expectedEndDate = LocalDate.of(2027, 1, 15);

      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(5L))
          .productId(productId)
          .businessDays(30)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .build();

      when(sprintSettingsQueryService.getSettingsOrDefault(productId)).thenReturn(settings);
      when(sprintSettingsQueryService.calculateEndDate(productId, startDate))
          .thenReturn(expectedEndDate);

      // Act
      CalculateEndDateResponse response = controller.calculateEndDate(productId.value().toString(), startDate);

      // Assert
      assertThat(response.getEndDate()).isEqualTo(expectedEndDate);
      assertThat(response.getBusinessDays()).isEqualTo(30);
    }

    @Test
    @DisplayName("Service層で例外が発生した場合、コントローラーはそれを伝播すること")
    void shouldPropagateExceptionFromService() {
      // Arrange
      LocalDate startDate = LocalDate.of(2026, 11, 16);

      when(sprintSettingsQueryService.getSettingsOrDefault(productId))
          .thenThrow(new IllegalArgumentException("Sprint settings not found"));

      // Act & Assert
      assertThatThrownBy(() -> controller.calculateEndDate(productId.value().toString(), startDate))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Sprint settings not found");
    }
  }

  @Nested
  @DisplayName("getSprintSettingsメソッドのテスト")
  class GetSprintSettingsTest {

    @Test
    @DisplayName("設定が存在する場合、SprintSettingsResponseを返すこと")
    void shouldReturnSprintSettingsResponseWhenSettingsExist() {
      // Arrange
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(10L))
          .productId(productId)
          .businessDays(10)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .build();

      when(sprintSettingsQueryService.getSettingsOrDefault(productId)).thenReturn(settings);

      // Act
      SprintSettingsResponse response = controller.getSprintSettings(productId.value().toString());

      // Assert
      assertThat(response).isNotNull();
      assertThat(response.getProductId()).isEqualTo(productId.value().toString());
      assertThat(response.getBusinessDays()).isEqualTo(10);
      assertThat(response.getEndDayOfWeek()).isEqualTo(EndDayOfWeek.FRIDAY);
    }
  }
}
