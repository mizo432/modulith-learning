package undecided.agiloom.sprintExecution.internal.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import undecided.agiloom.sprintExecution.spi.SprintDTO;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.agiloom.sprintExecution.internal.command.CreateSprintCommandServiceInterface;
import undecided.agiloom.sprintExecution.internal.command.SendNotificationCommandServiceInterface;
import undecided.agiloom.sprintExecution.internal.command.UpdateSprintCommandServiceInterface;
import undecided.supporting.notification.spi.NotificationDTO;
import undecided.supporting.notification.spi.NotificationType;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * SprintCommandControllerに対する敵対的検証テスト。
 * 不正入力、境界値、インジェクション試行など、悪意のあるリクエストに対するシステムの堅牢性を検証します。
 */
@Tag("small")
@DisplayName("SprintCommandControllerの敵対的検証テスト")
@ExtendWith(MockitoExtension.class)
class SprintCommandControllerAdversarialTest {

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
  @DisplayName("createSprint - 不正入力検証")
  class CreateSprintAdversarialTest {

    @Test
    @DisplayName("タイトルにSQLインジェクション文字列を含むリクエストを処理すること")
    void shouldHandleSqlInjectionInTitle() {
      // Arrange - SQLインジェクション試行
      CreateSprintRequest request = CreateSprintRequest.builder()
          .productId(UUID.randomUUID())
          .sprintNumber(1)
          .title("'; DROP TABLE sprint; --")
          .goal("Goal")
          .startDate(LocalDate.now())
          .endDate(LocalDate.now().plusWeeks(2))
          .teamId(UUID.randomUUID())
          .teamName("Team A")
          .build();

      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      SprintDTO sprint = createSprintDTO(snowflakeId);
      SprintResponse expectedResponse = SprintResponse.builder().id(snowflakeId).build();

      when(createSprintCommandService.execute(any())).thenReturn(sprint);
      when(sprintResponseAssembler.toResponse(any(SprintDTO.class))).thenReturn(expectedResponse);

      // Act & Assert - 例外をスローせずに処理できること（JPAのパラメータ化クエリで防御されている前提）
      // コントローラー層では例外をスローしない
      assertThatCode(() -> controller.createSprint(request))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("タイトルにXSS文字列を含むリクエストを処理すること")
    void shouldHandleXssInTitle() {
      // Arrange - XSS試行
      CreateSprintRequest request = CreateSprintRequest.builder()
          .productId(UUID.randomUUID())
          .sprintNumber(1)
          .title("<script>alert('xss')</script>")
          .goal("<img src=x onerror=alert(1)>")
          .startDate(LocalDate.now())
          .endDate(LocalDate.now().plusWeeks(2))
          .teamId(UUID.randomUUID())
          .teamName("Team A")
          .build();

      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      SprintDTO sprint = createSprintDTO(snowflakeId);
      SprintResponse expectedResponse = SprintResponse.builder().id(snowflakeId).build();

      when(createSprintCommandService.execute(any())).thenReturn(sprint);
      when(sprintResponseAssembler.toResponse(any(SprintDTO.class))).thenReturn(expectedResponse);

      // Act & Assert
      assertThatCode(() -> controller.createSprint(request))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("sprintNumberにInteger.MAX_VALUEを指定した場合")
    void shouldHandleMaxSprintNumber() {
      // Arrange
      CreateSprintRequest request = CreateSprintRequest.builder()
          .productId(UUID.randomUUID())
          .sprintNumber(Integer.MAX_VALUE)
          .title("Sprint")
          .startDate(LocalDate.now())
          .endDate(LocalDate.now().plusWeeks(2))
          .teamId(UUID.randomUUID())
          .teamName("Team A")
          .build();

      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      SprintDTO sprint = createSprintDTO(snowflakeId);
      SprintResponse expectedResponse = SprintResponse.builder().id(snowflakeId).build();

      when(createSprintCommandService.execute(any())).thenReturn(sprint);
      when(sprintResponseAssembler.toResponse(any(SprintDTO.class))).thenReturn(expectedResponse);

      // Act & Assert
      assertThatCode(() -> controller.createSprint(request))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("endDateがstartDateより前にある場合、コマンド層でIllegalArgumentExceptionをスローすること")
    void shouldRejectEndDateBeforeStartDate() {
      // Arrange - 日付範囲が不正
      CreateSprintRequest request = CreateSprintRequest.builder()
          .productId(UUID.randomUUID())
          .sprintNumber(1)
          .title("Sprint")
          .startDate(LocalDate.now().plusWeeks(2))
          .endDate(LocalDate.now())  // startDateより前
          .teamId(UUID.randomUUID())
          .teamName("Team A")
          .build();

      // コントローラーはCreateSprintCommandに変換してコマンド層に渡す
      // モックのコマンドサービスでendDate < startDateの場合にIllegalArgumentExceptionをスローするように設定
      when(createSprintCommandService.execute(any()))
          .thenAnswer(invocation -> {
            undecided.agiloom.sprintExecution.internal.command.CreateSprintCommand cmd = invocation.getArgument(0);
            if (!cmd.getEndDate().isAfter(cmd.getStartDate())) {
              throw new IllegalArgumentException("endDate must be after startDate");
            }
            return SnowflakeId.of(1234567890L);
          });

      // Act & Assert - コマンド層でendDateがstartDateより後であることを検証
      assertThatThrownBy(() -> controller.createSprint(request))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("endDate must be after startDate");
    }

    @Test
    @DisplayName("過去の日付をstartDateとして指定した場合")
    void shouldHandlePastStartDate() {
      // Arrange
      CreateSprintRequest request = CreateSprintRequest.builder()
          .productId(UUID.randomUUID())
          .sprintNumber(1)
          .title("Sprint")
          .startDate(LocalDate.now().minusYears(10))
          .endDate(LocalDate.now().minusYears(10).plusWeeks(2))
          .teamId(UUID.randomUUID())
          .teamName("Team A")
          .build();

      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      SprintDTO sprint = createSprintDTO(snowflakeId);
      SprintResponse expectedResponse = SprintResponse.builder().id(snowflakeId).build();

      when(createSprintCommandService.execute(any())).thenReturn(sprint);
      when(sprintResponseAssembler.toResponse(any(SprintDTO.class))).thenReturn(expectedResponse);

      // Act & Assert
      assertThatCode(() -> controller.createSprint(request))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("teamNameに非常に長い文字列を指定した場合")
    void shouldHandleVeryLongTeamName() {
      // Arrange - 非常に長い文字列
      String longName = "A".repeat(10000);
      CreateSprintRequest request = CreateSprintRequest.builder()
          .productId(UUID.randomUUID())
          .sprintNumber(1)
          .title("Sprint")
          .startDate(LocalDate.now())
          .endDate(LocalDate.now().plusWeeks(2))
          .teamId(UUID.randomUUID())
          .teamName(longName)
          .build();

      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      SprintDTO sprint = createSprintDTO(snowflakeId);
      SprintResponse expectedResponse = SprintResponse.builder().id(snowflakeId).build();

      when(createSprintCommandService.execute(any())).thenReturn(sprint);
      when(sprintResponseAssembler.toResponse(any(SprintDTO.class))).thenReturn(expectedResponse);

      // Act & Assert - 文字列長の上限チェックが欠如
      assertThatCode(() -> controller.createSprint(request))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("goalにNullバイトを含む文字列を指定した場合")
    void shouldHandleNullByteInGoal() {
      // Arrange - Nullバイトインジェクション
      CreateSprintRequest request = CreateSprintRequest.builder()
          .productId(UUID.randomUUID())
          .sprintNumber(1)
          .title("Sprint")
          .goal("Normal\0Injected")
          .startDate(LocalDate.now())
          .endDate(LocalDate.now().plusWeeks(2))
          .teamId(UUID.randomUUID())
          .teamName("Team A")
          .build();

      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      SprintDTO sprint = createSprintDTO(snowflakeId);
      SprintResponse expectedResponse = SprintResponse.builder().id(snowflakeId).build();

      when(createSprintCommandService.execute(any())).thenReturn(sprint);
      when(sprintResponseAssembler.toResponse(any(SprintDTO.class))).thenReturn(expectedResponse);

      // Act & Assert
      assertThatCode(() -> controller.createSprint(request))
          .doesNotThrowAnyException();
    }
  }

  @Nested
  @DisplayName("changeStatus - 不正入力検証")
  class ChangeStatusAdversarialTest {

    @Test
    @DisplayName("IDに数値以外の文字列を指定した場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowIllegalArgumentExceptionForNonNumericId() {
      // Arrange
      // Act & Assert - IDパース時にNumberFormatExceptionをIllegalArgumentExceptionに変換する
      assertThatThrownBy(() -> controller.changeStatus("abc", SprintStatus.ACTIVE))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("数値である必要があります");
    }

    @Test
    @DisplayName("IDに空文字列を指定した場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowIllegalArgumentExceptionForEmptyId() {
      // Arrange
      // Act & Assert
      assertThatThrownBy(() -> controller.changeStatus("", SprintStatus.ACTIVE))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("数値である必要があります");
    }

    @Test
    @DisplayName("IDに負の値を指定した場合、SnowflakeIdでIllegalArgumentExceptionをスローすること")
    void shouldHandleNegativeId() {
      // Act & Assert - Long.parseLong("-123")は正常にパースされるが、SnowflakeId.of()で正の値チェックに失敗
      assertThatThrownBy(() -> controller.changeStatus("-123", SprintStatus.ACTIVE))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("value must be positive");
    }

    @Test
    @DisplayName("IDにLong.MAX_VALUEより大きな値を指定した場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowIllegalArgumentExceptionForOverflowId() {
      // Arrange
      // Act & Assert
      assertThatThrownBy(() -> controller.changeStatus("99999999999999999999", SprintStatus.ACTIVE))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("数値である必要があります");
    }

    @Test
    @DisplayName("IDにSQLインジェクション文字列を指定した場合、IllegalArgumentExceptionをスローすること")
    void shouldRejectSqlInjectionInId() {
      // Arrange
      // Act & Assert - SQLインジェクション文字列は数値としてパースできないため拒否される
      assertThatThrownBy(() -> controller.changeStatus("1; DROP TABLE sprint;", SprintStatus.ACTIVE))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("数値である必要があります");
    }

    @Test
    @DisplayName("IDにXSS文字列を指定した場合、IllegalArgumentExceptionをスローすること")
    void shouldRejectXssInId() {
      // Arrange
      // Act & Assert - XSS文字列は数値としてパースできないため拒否される
      assertThatThrownBy(() -> controller.changeStatus("<script>alert(1)</script>", SprintStatus.ACTIVE))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("数値である必要があります");
    }

    @Test
    @DisplayName("Sprintが見つからない場合、適切な例外をスローすること")
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

    @Test
    @DisplayName("IDにスペースを含む文字列を指定した場合、IllegalArgumentExceptionをスローすること")
    void shouldRejectIdWithSpaces() {
      // Arrange
      // Act & Assert - スペースを含むIDは数値としてパースできないため拒否される
      assertThatThrownBy(() -> controller.changeStatus(" 123 ", SprintStatus.ACTIVE))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("数値である必要があります");
    }

    @Test
    @DisplayName("IDに浮動小数点数を指定した場合、IllegalArgumentExceptionをスローすること")
    void shouldRejectFloatingPointId() {
      // Arrange
      // Act & Assert - 浮動小数点数は整数としてパースできないため拒否される
      assertThatThrownBy(() -> controller.changeStatus("123.456", SprintStatus.ACTIVE))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("数値である必要があります");
    }
  }

  @Nested
  @DisplayName("sendNotification - 不正入力検証")
  class SendNotificationAdversarialTest {

    @Test
    @DisplayName("タイトルにSQLインジェクション文字列を含むリクエストを処理すること")
    void shouldHandleSqlInjectionInTitle() {
      // Arrange - SQLインジェクション試行
      UUID recipientId = UUID.randomUUID();
      SendNotificationRequest request = SendNotificationRequest.builder()
          .recipientId(recipientId)
          .title("'; DROP TABLE notifications; --")
          .content("テスト本文")
          .type(NotificationType.MENTION)
          .build();

      NotificationDTO expectedNotification = NotificationDTO.builder()
          .id(SnowflakeId.of(1234567890L))
          .recipientId(recipientId)
          .sourceDomain("agiloom")
          .title("'; DROP TABLE notifications; --")
          .content("テスト本文")
          .type(NotificationType.MENTION)
          .isRead(false)
          .createdAt(LocalDateTime.now())
          .updatedAt(LocalDateTime.now())
          .build();

      when(sendNotificationCommandService.execute(any())).thenReturn(expectedNotification);

      // Act & Assert - 例外をスローせずに処理できること
      assertThatCode(() -> controller.sendNotification(request))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("タイトルにXSS文字列を含むリクエストを処理すること")
    void shouldHandleXssInTitle() {
      // Arrange - XSS試行
      UUID recipientId = UUID.randomUUID();
      SendNotificationRequest request = SendNotificationRequest.builder()
          .recipientId(recipientId)
          .title("<script>alert('xss')</script>")
          .content("<img src=x onerror=alert(1)>")
          .type(NotificationType.MENTION)
          .build();

      NotificationDTO expectedNotification = NotificationDTO.builder()
          .id(SnowflakeId.of(1234567890L))
          .recipientId(recipientId)
          .sourceDomain("agiloom")
          .title("<script>alert('xss')</script>")
          .content("<img src=x onerror=alert(1)>")
          .type(NotificationType.MENTION)
          .isRead(false)
          .createdAt(LocalDateTime.now())
          .updatedAt(LocalDateTime.now())
          .build();

      when(sendNotificationCommandService.execute(any())).thenReturn(expectedNotification);

      // Act & Assert
      assertThatCode(() -> controller.sendNotification(request))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("contentに非常に長い文字列を指定した場合")
    void shouldHandleVeryLongContent() {
      // Arrange - 非常に長い文字列
      UUID recipientId = UUID.randomUUID();
      String longContent = "A".repeat(2000);
      SendNotificationRequest request = SendNotificationRequest.builder()
          .recipientId(recipientId)
          .title("テスト通知")
          .content(longContent)
          .type(NotificationType.SYSTEM)
          .build();

      NotificationDTO expectedNotification = NotificationDTO.builder()
          .id(SnowflakeId.of(1234567890L))
          .recipientId(recipientId)
          .sourceDomain("agiloom")
          .title("テスト通知")
          .content(longContent)
          .type(NotificationType.SYSTEM)
          .isRead(false)
          .createdAt(LocalDateTime.now())
          .updatedAt(LocalDateTime.now())
          .build();

      when(sendNotificationCommandService.execute(any())).thenReturn(expectedNotification);

      // Act & Assert
      assertThatCode(() -> controller.sendNotification(request))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("すべての通知タイプで通知が送信されること")
    void shouldSendNotificationWithAllTypes() {
      // Arrange
      UUID recipientId = UUID.randomUUID();

      // Act & Assert
      for (NotificationType type : NotificationType.values()) {
        SendNotificationRequest request = SendNotificationRequest.builder()
            .recipientId(recipientId)
            .title(type + "通知")
            .content(type + "の本文")
            .type(type)
            .build();

        NotificationDTO expectedNotification = NotificationDTO.builder()
            .id(SnowflakeId.of(1234567890L))
            .recipientId(recipientId)
            .sourceDomain("agiloom")
            .title(type + "通知")
            .content(type + "の本文")
            .type(type)
            .isRead(false)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        when(sendNotificationCommandService.execute(any())).thenReturn(expectedNotification);

        assertThatCode(() -> controller.sendNotification(request))
            .as("通知タイプ %s で通知が送信されること", type)
            .doesNotThrowAnyException();
      }
    }
  }
}
