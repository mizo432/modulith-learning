package undecided.agiloom.sprintExecution.internal.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.agiloom.sprintExecution.internal.query.SprintCapacity;
import undecided.agiloom.sprintExecution.internal.query.SprintCapacityQueryService;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@ExtendWith(MockitoExtension.class)
@DisplayName("SprintQueryControllerの容量取得のテスト")
class SprintQueryControllerCapacityTest {

  private static final SnowflakeId SPRINT_ID = SnowflakeId.of(3001L);

  @Mock private SprintCapacityQueryService sprintCapacityQueryService;

  @InjectMocks private SprintQueryController controller;

  @Test
  @DisplayName("容量が存在する場合、SprintCapacityResponseを返すこと")
  void shouldReturnCapacityResponse() {
    when(sprintCapacityQueryService.getCapacity(SPRINT_ID))
        .thenReturn(new SprintCapacity(SPRINT_ID, 10, 5.0, 50L, 55L, true));

    SprintCapacityResponse response = controller.getSprintCapacity(SPRINT_ID.value().toString());

    assertThat(response.getSprintId()).isEqualTo(SPRINT_ID.value().toString());
    assertThat(response.getBusinessDays()).isEqualTo(10);
    assertThat(response.getVelocityPerBusinessDay()).isEqualTo(5.0);
    assertThat(response.getAssignablePoints()).isEqualTo(50L);
    assertThat(response.getAssignedPoints()).isEqualTo(55L);
    assertThat(response.getOverAllocated()).isTrue();
  }

  @Test
  @DisplayName("スプリントが存在しない場合、例外が伝播すること")
  void shouldPropagateNotFound() {
    when(sprintCapacityQueryService.getCapacity(SPRINT_ID))
        .thenThrow(new IllegalArgumentException("Sprint not found"));

    assertThatThrownBy(() -> controller.getSprintCapacity(SPRINT_ID.value().toString()))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
