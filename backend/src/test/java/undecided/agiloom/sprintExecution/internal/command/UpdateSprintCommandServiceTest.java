package undecided.agiloom.sprintExecution.internal.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import undecided.agiloom.sprintExecution.internal.SprintRepository;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintCancelled;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("スプリント更新コマンドサービスの単体テスト（キャンセルイベント）")
@ExtendWith(MockitoExtension.class)
class UpdateSprintCommandServiceTest {

  private static final SnowflakeId SPRINT_ID = SnowflakeId.of(1001L);
  private static final SnowflakeId PRODUCT_ID = SnowflakeId.of(3000L);
  private static final SnowflakeId TEAM_ID = SnowflakeId.of(2000L);
  private static final ZoneId ZONE = ZoneId.of("Asia/Tokyo");
  private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2026, 11, 27, 12, 0);
  private static final Clock FIXED_CLOCK = Clock.fixed(FIXED_TIME.atZone(ZONE).toInstant(), ZONE);


  @Mock private SprintRepository sprintRepository;
  @Mock private ApplicationEventPublisher eventPublisher;

  private UpdateSprintCommandService service;

  @BeforeEach
  void setUp() {
    service = new UpdateSprintCommandService(sprintRepository, eventPublisher, FIXED_CLOCK);
  }

  private Sprint sprint() {
    return Sprint.builder()
        .id(SPRINT_ID)
        .productId(PRODUCT_ID)
        .sprintNumber(1)
        .title("Sprint 1")
        .status(SprintStatus.PLANNED)
        .teamId(TEAM_ID)
        .build();
  }

  @Test
  @DisplayName("ステータスをCANCELLEDへ変更したとき、SprintCancelledが発行される")
  void publishesSprintCancelledWhenStatusChangesToCancelled() {
    when(sprintRepository.findById(SPRINT_ID)).thenReturn(Optional.of(sprint()));

    service.changeStatus(SPRINT_ID, SprintStatus.CANCELLED);

    ArgumentCaptor<SprintCancelled> captor = ArgumentCaptor.forClass(SprintCancelled.class);
    verify(eventPublisher).publishEvent(captor.capture());
    assertThat(captor.getValue().sprintId()).isEqualTo(SPRINT_ID);
    assertThat(captor.getValue().scrumTeamId()).isEqualTo(TEAM_ID);
    assertThat(captor.getValue().sprintNumber()).isEqualTo(1);
    assertThat(captor.getValue().occurredAt()).isEqualTo(FIXED_TIME);
  }

  @Test
  @DisplayName("ステータスをACTIVEへ変更したとき、SprintCancelledは発行されない")
  void doesNotPublishSprintCancelledWhenStatusChangesToActive() {
    when(sprintRepository.findById(SPRINT_ID)).thenReturn(Optional.of(sprint()));

    service.changeStatus(SPRINT_ID, SprintStatus.ACTIVE);

    verify(eventPublisher, never()).publishEvent(any(SprintCancelled.class));
  }

  @Test
  @DisplayName("スプリントを削除したとき、SprintCancelledが発行される")
  void publishesSprintCancelledWhenSprintIsDeleted() {
    when(sprintRepository.findById(SPRINT_ID)).thenReturn(Optional.of(sprint()));

    service.delete(SPRINT_ID);

    ArgumentCaptor<SprintCancelled> captor = ArgumentCaptor.forClass(SprintCancelled.class);
    verify(eventPublisher).publishEvent(captor.capture());
    assertThat(captor.getValue().sprintId()).isEqualTo(SPRINT_ID);
    assertThat(captor.getValue().scrumTeamId()).isEqualTo(TEAM_ID);
    assertThat(captor.getValue().sprintNumber()).isEqualTo(1);
  }
}
