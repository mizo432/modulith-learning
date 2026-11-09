package undecided.agiloom.sprintExecution.internal.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.mizo432.shared.exception.BusinessException;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import undecided.agiloom.sprintExecution.spi.SprintDTO;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.agiloom.sprintExecution.internal.command.CreateSprintCommand;
import undecided.agiloom.sprintExecution.internal.command.CreateSprintCommandServiceInterface;
import undecided.agiloom.sprintExecution.internal.command.SendNotificationCommand;
import undecided.agiloom.sprintExecution.internal.command.SendNotificationCommandServiceInterface;
import undecided.agiloom.sprintExecution.internal.command.UpdateSprintCommandServiceInterface;
import undecided.agiloom.sprintExecution.internal.presentation.ChangeStatusRequest;
import undecided.supporting.notification.spi.NotificationDTO;
import undecided.supporting.notification.spi.NotificationType;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("SprintCommandControllerの単体テスト")
@ExtendWith(MockitoExtension.class)
class SprintCommandControllerTest {

  @Mock private CreateSprintCommandServiceInterface createSprintCommandService;
  @Mock private UpdateSprintCommandServiceInterface updateSprintCommandService;
  @Mock private SendNotificationCommandServiceInterface sendNotificationCommandService;
  private SprintResponseAssembler sprintResponseAssembler;

  @Captor private ArgumentCaptor<CreateSprintCommand> commandCaptor;
  @Captor private ArgumentCaptor<SendNotificationCommand> notificationCommandCaptor;
  @Captor private ArgumentCaptor<SnowflakeId> snowflakeIdCaptor;

  private SprintCommandController controller;

  @BeforeEach
  void setUp() {
    sprintResponseAssembler = new SprintResponseAssembler();
    controller = new SprintCommandController(
        createSprintCommandService,
        updateSprintCommandService,
        sendNotificationCommandService,
        sprintResponseAssembler
    );
  }

  private static final UUID FIXED_PRODUCT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
  private static final UUID FIXED_TEAM_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
  private static final UUID FIXED_RECIPIENT_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
  private static final UUID FIXED_SOURCE_ID = UUID.fromString("44444444-4444-4444-4444-444444444444");

  private SprintDTO createSprintDTO(SnowflakeId id) {
    return SprintDTO.builder()
        .id(id)
        .productId(FIXED_PRODUCT_ID)
        .sprintNumber(1)
        .title("Sprint 1")
        .goal("Goal 1")
        .startDate(LocalDate.now())
        .endDate(LocalDate.now().plusWeeks(2))
        .status(SprintStatus.PLANNED)
        .teamId(FIXED_TEAM_ID)
        .teamName("Team A")
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
  }

  @Nested
  @DisplayName("createSprint メソッドのテスト")
  class CreateSprintTest {

    @Test
    @DisplayName("Sprintを作成してResponseEntityを返すこと")
    void shouldCreateSprintAndReturnResponseEntity() {
      // Arrange
      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      LocalDate startDate = LocalDate.now();
      LocalDate endDate = LocalDate.now().plusWeeks(2);
      CreateSprintRequest request = CreateSprintRequest.builder()
          .productId(FIXED_PRODUCT_ID)
          .sprintNumber(1)
          .title("Sprint 1")
          .goal("Goal 1")
          .startDate(startDate)
          .endDate(endDate)
          .teamId(FIXED_TEAM_ID)
          .teamName("Team A")
          .build();

      SprintDTO sprint = createSprintDTO(snowflakeId);

      when(createSprintCommandService.execute(any())).thenReturn(sprint);

      // Act
      ResponseEntity<SprintResponse> result = controller.createSprint(request);

      // Assert
      assertThat(result.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.CREATED);
      assertThat(result.getBody()).isNotNull();
      assertThat(result.getBody().getId()).isEqualTo(snowflakeId);
      assertThat(result.getBody().getTitle()).isEqualTo("Sprint 1");
      verify(createSprintCommandService).execute(commandCaptor.capture());
      CreateSprintCommand captured = commandCaptor.getValue();
      assertThat(captured.getProductId()).isEqualTo(FIXED_PRODUCT_ID);
      assertThat(captured.getSprintNumber()).isEqualTo(1);
      assertThat(captured.getTitle()).isEqualTo("Sprint 1");
      assertThat(captured.getGoal()).isEqualTo("Goal 1");
      assertThat(captured.getStartDate()).isEqualTo(startDate);
      assertThat(captured.getEndDate()).isEqualTo(endDate);
      assertThat(captured.getTeamId()).isEqualTo(FIXED_TEAM_ID);
      assertThat(captured.getTeamName()).isEqualTo("Team A");
    }
  }

  @Nested
  @DisplayName("changeStatus メソッドのテスト")
  class ChangeStatusTest {

    @Test
    @DisplayName("Sprintのステータスを変更してResponseEntityを返すこと")
    void shouldChangeStatusAndReturnResponseEntity() {
      // Arrange
      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      SprintDTO sprint = SprintDTO.builder()
          .id(snowflakeId)
          .productId(FIXED_PRODUCT_ID)
          .sprintNumber(1)
          .title("Sprint 1")
          .goal("Goal 1")
          .startDate(LocalDate.now())
          .endDate(LocalDate.now().plusWeeks(2))
          .status(SprintStatus.ACTIVE)
          .teamId(FIXED_TEAM_ID)
          .teamName("Team A")
          .createdAt(LocalDateTime.now())
          .updatedAt(LocalDateTime.now())
          .build();

      ChangeStatusRequest request = ChangeStatusRequest.builder()
          .status(SprintStatus.ACTIVE)
          .build();

      when(updateSprintCommandService.changeStatus(any(SnowflakeId.class), any(SprintStatus.class))).thenReturn(sprint);

      // Act
      ResponseEntity<SprintResponse> result = controller.changeStatus(
          String.valueOf(snowflakeId.value()), request);

      // Assert
      assertThat(result.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.OK);
      assertThat(result.getBody()).isNotNull();
      assertThat(result.getBody().getId()).isEqualTo(snowflakeId);
      assertThat(result.getBody().getStatus()).isEqualTo(SprintStatus.ACTIVE);
      verify(updateSprintCommandService).changeStatus(snowflakeIdCaptor.capture(), eq(SprintStatus.ACTIVE));
      assertThat(snowflakeIdCaptor.getValue().value()).isEqualTo(snowflakeId.value());
    }

    @Test
    @DisplayName("Sprintが見つからない場合は例外をスローすること")
    void shouldThrowExceptionWhenSprintNotFound() {
      // Arrange
      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      ChangeStatusRequest request = ChangeStatusRequest.builder()
          .status(SprintStatus.ACTIVE)
          .build();

      when(updateSprintCommandService.changeStatus(any(SnowflakeId.class), any(SprintStatus.class)))
          .thenThrow(new BusinessException("Sprint not found"));

      // Act & Assert
      assertThatThrownBy(() -> controller.changeStatus(
          String.valueOf(snowflakeId.value()), request))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("Sprint not found");
    }
  }

  @Nested
  @DisplayName("sendNotification メソッドのテスト")
  class SendNotificationTest {

    @Test
    @DisplayName("通知を送信してResponseEntityを返すこと")
    void shouldSendNotificationAndReturnResponseEntity() {
      // Arrange
      SendNotificationRequest request = SendNotificationRequest.builder()
          .recipientId(FIXED_RECIPIENT_ID)
          .sourceId(FIXED_SOURCE_ID)
          .title("テスト通知")
          .content("テスト本文")
          .type(NotificationType.MENTION)
          .build();

      NotificationDTO expectedNotification = NotificationDTO.builder()
          .id(SnowflakeId.of(1234567890L))
          .recipientId(FIXED_RECIPIENT_ID)
          .sourceId(FIXED_SOURCE_ID)
          .sourceDomain("agiloom")
          .title("テスト通知")
          .content("テスト本文")
          .type(NotificationType.MENTION)
          .isRead(false)
          .createdAt(LocalDateTime.now())
          .updatedAt(LocalDateTime.now())
          .build();

      when(sendNotificationCommandService.execute(any())).thenReturn(expectedNotification);

      // Act
      ResponseEntity<NotificationDTO> result = controller.sendNotification(request);

      // Assert
      assertThat(result.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.CREATED);
      assertThat(result.getBody()).isEqualTo(expectedNotification);
      verify(sendNotificationCommandService).execute(notificationCommandCaptor.capture());
      SendNotificationCommand captured = notificationCommandCaptor.getValue();
      assertThat(captured.getRecipientId()).isEqualTo(FIXED_RECIPIENT_ID);
      assertThat(captured.getSourceId()).isEqualTo(FIXED_SOURCE_ID);
      assertThat(captured.getTitle()).isEqualTo("テスト通知");
      assertThat(captured.getContent()).isEqualTo("テスト本文");
      assertThat(captured.getType()).isEqualTo(NotificationType.MENTION);
    }
  }
}
