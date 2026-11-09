package undecided.agiloom.sprintExecution.internal.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import undecided.agiloom.sprintExecution.spi.SprintDTO;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.agiloom.sprintExecution.internal.command.CreateSprintCommandServiceInterface;
import undecided.agiloom.sprintExecution.internal.command.SendNotificationCommandServiceInterface;
import undecided.agiloom.sprintExecution.internal.command.UpdateSprintCommandServiceInterface;
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
  @Mock private SprintResponseAssembler sprintResponseAssembler;

  private SprintCommandController controller;

  @BeforeEach
  void setUp() {
    controller = new SprintCommandController(
        createSprintCommandService,
        updateSprintCommandService,
        sendNotificationCommandService,
        sprintResponseAssembler
    );
  }

  private SprintDTO createSprintDTO(SnowflakeId id) {
    return SprintDTO.builder()
        .id(id)
        .productId(UUID.randomUUID())
        .sprintNumber(1)
        .title("Sprint 1")
        .goal("Goal 1")
        .startDate(LocalDate.now())
        .endDate(LocalDate.now().plusWeeks(2))
        .status(SprintStatus.PLANNED)
        .teamId(UUID.randomUUID())
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
      UUID productId = UUID.randomUUID();
      UUID teamId = UUID.randomUUID();
      CreateSprintRequest request = CreateSprintRequest.builder()
          .productId(productId)
          .sprintNumber(1)
          .title("Sprint 1")
          .goal("Goal 1")
          .startDate(LocalDate.now())
          .endDate(LocalDate.now().plusWeeks(2))
          .teamId(teamId)
          .teamName("Team A")
          .build();

      SprintDTO sprint = createSprintDTO(snowflakeId);
      SprintResponse expectedResponse = SprintResponse.builder()
          .id(snowflakeId)
          .title("Sprint 1")
          .build();

      when(createSprintCommandService.execute(any())).thenReturn(sprint);
      when(sprintResponseAssembler.toResponse(any(SprintDTO.class))).thenReturn(expectedResponse);

      // Act
      ResponseEntity<SprintResponse> result = controller.createSprint(request);

      // Assert
      assertThat(result.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.OK);
      assertThat(result.getBody()).isEqualTo(expectedResponse);
      verify(createSprintCommandService).execute(any());
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
      SprintDTO sprint = createSprintDTO(snowflakeId);
      SprintResponse expectedResponse = SprintResponse.builder()
          .id(snowflakeId)
          .status(SprintStatus.ACTIVE)
          .build();

      when(updateSprintCommandService.changeStatus(any(SnowflakeId.class), any(SprintStatus.class))).thenReturn(sprint);
      when(sprintResponseAssembler.toResponse(any(SprintDTO.class))).thenReturn(expectedResponse);

      // Act
      ResponseEntity<SprintResponse> result = controller.changeStatus(
          String.valueOf(snowflakeId.value()), SprintStatus.ACTIVE);

      // Assert
      assertThat(result.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.OK);
      assertThat(result.getBody()).isEqualTo(expectedResponse);
      verify(updateSprintCommandService).changeStatus(any(SnowflakeId.class), any(SprintStatus.class));
    }

    @Test
    @DisplayName("Sprintが見つからない場合は例外をスローすること")
    void shouldThrowExceptionWhenSprintNotFound() {
      // Arrange
      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      when(updateSprintCommandService.changeStatus(any(SnowflakeId.class), any(SprintStatus.class)))
          .thenThrow(new IllegalArgumentException("Sprint not found"));

      // Act & Assert
      assertThatThrownBy(() -> controller.changeStatus(
          String.valueOf(snowflakeId.value()), SprintStatus.ACTIVE))
          .isInstanceOf(IllegalArgumentException.class)
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
      UUID recipientId = UUID.randomUUID();
      UUID sourceId = UUID.randomUUID();
      SendNotificationRequest request = SendNotificationRequest.builder()
          .recipientId(recipientId)
          .sourceId(sourceId)
          .title("テスト通知")
          .content("テスト本文")
          .type(NotificationType.MENTION)
          .build();

      NotificationDTO expectedNotification = NotificationDTO.builder()
          .id(SnowflakeId.of(1234567890L))
          .recipientId(recipientId)
          .sourceId(sourceId)
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
      assertThat(result.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.OK);
      assertThat(result.getBody()).isEqualTo(expectedNotification);
      verify(sendNotificationCommandService).execute(any());
    }
  }
}
