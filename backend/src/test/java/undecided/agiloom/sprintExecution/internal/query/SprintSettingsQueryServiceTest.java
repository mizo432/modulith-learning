package undecided.agiloom.sprintExecution.internal.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.agiloom.sprintExecution.internal.SprintSettingsRepository;
import undecided.agiloom.sprintExecution.internal.domain.SprintSettingsCalculator;
import undecided.agiloom.sprintExecution.spi.EndDayOfWeek;
import undecided.agiloom.sprintExecution.spi.SprintSettings;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@ExtendWith(MockitoExtension.class)
@DisplayName("SprintSettingsQueryServiceのテスト")
class SprintSettingsQueryServiceTest {

  @Mock
  private SprintSettingsRepository sprintSettingsRepository;

  @Mock
  private SprintSettingsCalculator sprintSettingsCalculator;

  @InjectMocks
  private SprintSettingsQueryService service;

  private UUID productId;

  @BeforeEach
  void setUp() {
    productId = UUID.randomUUID();
  }

  @Nested
  @DisplayName("findByProductIdメソッドのテスト")
  class FindByProductIdTest {

    @Test
    @DisplayName("設定が存在する場合、Optionalで返すこと")
    void shouldReturnSettingsWhenExists() {
      // Arrange
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(1L))
          .productId(productId)
          .businessDays(10)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .build();

      when(sprintSettingsRepository.findByProductId(productId)).thenReturn(Optional.of(settings));

      // Act
      Optional<SprintSettings> result = service.findByProductId(productId);

      // Assert
      assertThat(result).isPresent();
      assertThat(result.get().getProductId()).isEqualTo(productId);
      assertThat(result.get().getBusinessDays()).isEqualTo(10);
    }

    @Test
    @DisplayName("設定が存在しない場合、Optional.emptyを返すこと")
    void shouldReturnEmptyWhenNotFound() {
      // Arrange
      when(sprintSettingsRepository.findByProductId(productId)).thenReturn(Optional.empty());

      // Act
      Optional<SprintSettings> result = service.findByProductId(productId);

      // Assert
      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("productIdがnullの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenProductIdIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> service.findByProductId(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("productId must not be null");
    }
  }

  @Nested
  @DisplayName("calculateEndDateメソッドのテスト")
  class CalculateEndDateTest {

    @Test
    @DisplayName("設定が存在する場合、計算された終了日を返すこと")
    void shouldReturnCalculatedEndDateWhenSettingsExist() {
      // Arrange
      LocalDate startDate = LocalDate.of(2026, 11, 16);
      LocalDate expectedEndDate = LocalDate.of(2026, 12, 4);

      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(1L))
          .productId(productId)
          .businessDays(10)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .build();

      when(sprintSettingsRepository.findByProductId(productId)).thenReturn(Optional.of(settings));
      when(sprintSettingsCalculator.calculateEndDate(settings, startDate))
          .thenReturn(expectedEndDate);

      // Act
      LocalDate endDate = service.calculateEndDate(productId, startDate);

      // Assert
      assertThat(endDate).isEqualTo(expectedEndDate);
    }

    @Test
    @DisplayName("設定が存在しない場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenSettingsNotFound() {
      // Arrange
      LocalDate startDate = LocalDate.of(2026, 11, 16);

      when(sprintSettingsRepository.findByProductId(productId)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> service.calculateEndDate(productId, startDate))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Sprint settings not found for product");
    }

    @Test
    @DisplayName("異なる終了曜日の設定で正しく計算されること")
    void shouldCalculateWithDifferentEndDayOfWeek() {
      // Arrange
      LocalDate startDate = LocalDate.of(2026, 11, 16);
      LocalDate expectedEndDate = LocalDate.of(2026, 11, 25);

      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(2L))
          .productId(productId)
          .businessDays(3)
          .endDayOfWeek(EndDayOfWeek.WEDNESDAY)
          .build();

      when(sprintSettingsRepository.findByProductId(productId)).thenReturn(Optional.of(settings));
      when(sprintSettingsCalculator.calculateEndDate(settings, startDate))
          .thenReturn(expectedEndDate);

      // Act
      LocalDate endDate = service.calculateEndDate(productId, startDate);

      // Assert
      assertThat(endDate).isEqualTo(expectedEndDate);
    }

    @Test
    @DisplayName("1営業日の設定で正しく計算されること")
    void shouldCalculateWithOneBusinessDay() {
      // Arrange
      LocalDate startDate = LocalDate.of(2026, 11, 16);
      LocalDate expectedEndDate = LocalDate.of(2026, 11, 20);

      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(3L))
          .productId(productId)
          .businessDays(1)
          .endDayOfWeek(EndDayOfWeek.FRIDAY)
          .build();

      when(sprintSettingsRepository.findByProductId(productId)).thenReturn(Optional.of(settings));
      when(sprintSettingsCalculator.calculateEndDate(settings, startDate))
          .thenReturn(expectedEndDate);

      // Act
      LocalDate endDate = service.calculateEndDate(productId, startDate);

      // Assert
      assertThat(endDate).isEqualTo(expectedEndDate);
    }
  }

  @Nested
  @DisplayName("getSettingsOrDefaultメソッドのテスト")
  class GetSettingsOrDefaultTest {

    @Test
    @DisplayName("設定が存在する場合、既存の設定を返すこと")
    void shouldReturnExistingSettingsWhenExists() {
      // Arrange
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(1L))
          .productId(productId)
          .businessDays(15)
          .endDayOfWeek(EndDayOfWeek.THURSDAY)
          .build();

      when(sprintSettingsRepository.findByProductId(productId)).thenReturn(Optional.of(settings));

      // Act
      SprintSettings result = service.getSettingsOrDefault(productId);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getProductId()).isEqualTo(productId);
      assertThat(result.getBusinessDays()).isEqualTo(15);
      assertThat(result.getEndDayOfWeek()).isEqualTo(EndDayOfWeek.THURSDAY);
    }

    @Test
    @DisplayName("設定が存在しない場合、デフォルト値を返すこと")
    void shouldReturnDefaultSettingsWhenNotFound() {
      // Arrange
      when(sprintSettingsRepository.findByProductId(productId)).thenReturn(Optional.empty());

      // Act
      SprintSettings result = service.getSettingsOrDefault(productId);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getProductId()).isEqualTo(productId);
      assertThat(result.getBusinessDays()).isEqualTo(10);
      assertThat(result.getEndDayOfWeek()).isEqualTo(EndDayOfWeek.FRIDAY);
    }

    @Test
    @DisplayName("productIdがnullの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenProductIdIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> service.getSettingsOrDefault(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("productId must not be null");
    }

    @Test
    @DisplayName("カスタム設定が存在する場合、デフォルト値ではなくカスタム値を返すこと")
    void shouldReturnCustomSettingsInsteadOfDefaults() {
      // Arrange
      SprintSettings settings = SprintSettings.builder()
          .id(SnowflakeId.of(4L))
          .productId(productId)
          .businessDays(5)
          .endDayOfWeek(EndDayOfWeek.MONDAY)
          .build();

      when(sprintSettingsRepository.findByProductId(productId)).thenReturn(Optional.of(settings));

      // Act
      SprintSettings result = service.getSettingsOrDefault(productId);

      // Assert
      assertThat(result.getBusinessDays()).isEqualTo(5);
      assertThat(result.getEndDayOfWeek()).isEqualTo(EndDayOfWeek.MONDAY);
    }
  }
}
