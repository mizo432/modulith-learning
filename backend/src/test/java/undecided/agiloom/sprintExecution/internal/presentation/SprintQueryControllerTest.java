package undecided.agiloom.sprintExecution.internal.presentation;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;
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
@DisplayName("SprintQueryControllerのテスト")
class SprintQueryControllerTest {

  @Mock
  private SprintSettingsQueryService sprintSettingsQueryService;

  @InjectMocks
  private SprintQueryController controller;

  private SnowflakeId productId;

  @BeforeEach
  void setUp() {
    productId = SnowflakeId.of(100L);
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
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

      when(sprintSettingsQueryService.findByProductId(productId))
          .thenReturn(Optional.of(settings));

      // Act
      SprintSettingsResponse response = controller.getSprintSettings(productId.value().toString());

      // Assert
      assertThat(response).isNotNull();
      assertThat(response.getProductId()).isEqualTo(productId.value().toString());
      assertThat(response.getBusinessDays()).isEqualTo(10);
      assertThat(response.getEndDayOfWeek()).isEqualTo(EndDayOfWeek.FRIDAY);
    }

    @Test
    @DisplayName("設定が存在しない場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenSettingsNotFound() {
      // Arrange
      when(sprintSettingsQueryService.findByProductId(productId))
          .thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> controller.getSprintSettings(productId.value().toString()))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Sprint settings not found for product");
    }
  }
}
