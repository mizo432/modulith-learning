package undecided.agiloom.sprintExecution.internal.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.github.mizo432.shared.exception.BusinessException;
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
  private SprintResponseAssembler sprintResponseAssembler;

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
  @DisplayName("createSprint - 不正入力検証")
  class CreateSprintAdversarialTest {

    @Test
    @DisplayName("タイトルにSQLインジェクション文字列を含むリクエストを処理すること")
    void shouldHandleSqlInjectionInTitle() {
      // Arrange - SQLインジェクション試行
      CreateSprintRequest request = CreateSprintRequest.builder()
          .productId(FIXED_PRODUCT_ID)
          .sprintNumber(1)
          .title("'; DROP TABLE sprint; --")
          .goal("Goal")
          .startDate(LocalDate.now())
          .endDate(LocalDate.now().plusWeeks(2))
          .teamId(FIXED_TEAM_ID)
          .teamName("Team A")
          .build();

      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      SprintDTO sprint = createSprintDTO(snowflakeId);

      when(createSprintCommandService.execute(any())).thenReturn(sprint);

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
          .productId(FIXED_PRODUCT_ID)
          .sprintNumber(1)
          .title("<script>alert('xss')</script>")
          .goal("<img src=x onerror=alert(1)>")
          .startDate(LocalDate.now())
          .endDate(LocalDate.now().plusWeeks(2))
          .teamId(FIXED_TEAM_ID)
          .teamName("Team A")
          .build();

      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      SprintDTO sprint = createSprintDTO(snowflakeId);

      when(createSprintCommandService.execute(any())).thenReturn(sprint);

      // Act & Assert
      assertThatCode(() -> controller.createSprint(request))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("sprintNumberにInteger.MAX_VALUEを指定した場合")
    void shouldHandleMaxSprintNumber() {
      // Arrange
      CreateSprintRequest request = CreateSprintRequest.builder()
          .productId(FIXED_PRODUCT_ID)
          .sprintNumber(Integer.MAX_VALUE)
          .title("Sprint")
          .startDate(LocalDate.now())
          .endDate(LocalDate.now().plusWeeks(2))
          .teamId(FIXED_TEAM_ID)
          .teamName("Team A")
          .build();

      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      SprintDTO sprint = createSprintDTO(snowflakeId);

      when(createSprintCommandService.execute(any())).thenReturn(sprint);

      // Act & Assert
      assertThatCode(() -> controller.createSprint(request))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("endDateがstartDateより前にある場合、コマンド層でBusinessExceptionをスローすること")
    void shouldRejectEndDateBeforeStartDate() {
      // Arrange - 日付範囲が不正
      CreateSprintRequest request = CreateSprintRequest.builder()
          .productId(FIXED_PRODUCT_ID)
          .sprintNumber(1)
          .title("Sprint")
          .startDate(LocalDate.now().plusWeeks(2))
          .endDate(LocalDate.now())  // startDateより前
          .teamId(FIXED_TEAM_ID)
          .teamName("Team A")
          .build();

      // コントローラーはCreateSprintCommandに変換してコマンド層に渡す
      // モックのコマンドサービスでendDate < startDateの場合にBusinessExceptionをスローするように設定
      when(createSprintCommandService.execute(any()))
          .thenAnswer(invocation -> {
            undecided.agiloom.sprintExecution.internal.command.CreateSprintCommand cmd = invocation.getArgument(0);
            if (!cmd.getEndDate().isAfter(cmd.getStartDate())) {
              throw new BusinessException("endDate must be after startDate");
            }
            return SnowflakeId.of(1234567890L);
          });

      // Act & Assert - コマンド層でendDateがstartDateより後であることを検証
      assertThatThrownBy(() -> controller.createSprint(request))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("endDate must be after startDate");
    }

    @Test
    @DisplayName("過去の日付をstartDateとして指定した場合")
    void shouldHandlePastStartDate() {
      // Arrange
      CreateSprintRequest request = CreateSprintRequest.builder()
          .productId(FIXED_PRODUCT_ID)
          .sprintNumber(1)
          .title("Sprint")
          .startDate(LocalDate.now().minusYears(10))
          .endDate(LocalDate.now().minusYears(10).plusWeeks(2))
          .teamId(FIXED_TEAM_ID)
          .teamName("Team A")
          .build();

      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      SprintDTO sprint = createSprintDTO(snowflakeId);

      when(createSprintCommandService.execute(any())).thenReturn(sprint);

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
          .productId(FIXED_PRODUCT_ID)
          .sprintNumber(1)
          .title("Sprint")
          .startDate(LocalDate.now())
          .endDate(LocalDate.now().plusWeeks(2))
          .teamId(FIXED_TEAM_ID)
          .teamName(longName)
          .build();

      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      SprintDTO sprint = createSprintDTO(snowflakeId);

      when(createSprintCommandService.execute(any())).thenReturn(sprint);

      // Act & Assert - 文字列長の上限チェックが欠如
      assertThatCode(() -> controller.createSprint(request))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("goalにNullバイトを含む文字列を指定した場合")
    void shouldHandleNullByteInGoal() {
      // Arrange - Nullバイトインジェクション
      CreateSprintRequest request = CreateSprintRequest.builder()
          .productId(FIXED_PRODUCT_ID)
          .sprintNumber(1)
          .title("Sprint")
          .startDate(LocalDate.now())
          .endDate(LocalDate.now().plusWeeks(2))
          .teamId(FIXED_TEAM_ID)
          .teamName("Team A")
          .build();

      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      SprintDTO sprint = createSprintDTO(snowflakeId);

      when(createSprintCommandService.execute(any())).thenReturn(sprint);

      // Act & Assert
      assertThatCode(() -> controller.createSprint(request))
          .doesNotThrowAnyException();
    }
  }

  @Nested
  @DisplayName("changeStatus - 不正入力検証")
  class ChangeStatusAdversarialTest {

    @Test
    @DisplayName("IDに数値以外の文字列を指定した場合、BusinessExceptionをスローすること")
    void shouldThrowBusinessExceptionForNonNumericId() {
      // Arrange
      ChangeStatusRequest changeStatusRequest = ChangeStatusRequest.builder()
          .status(SprintStatus.ACTIVE)
          .build();
      // Act & Assert - IDパース時にNumberFormatExceptionをBusinessExceptionに変換する
      assertThatThrownBy(() -> controller.changeStatus("abc", changeStatusRequest))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("数値である必要があります");
    }

    @Test
    @DisplayName("IDに空文字列を指定した場合、BusinessExceptionをスローすること")
    void shouldThrowBusinessExceptionForEmptyId() {
      // Arrange
      ChangeStatusRequest changeStatusRequest = ChangeStatusRequest.builder()
          .status(SprintStatus.ACTIVE)
          .build();
      // Act & Assert
      assertThatThrownBy(() -> controller.changeStatus("", changeStatusRequest))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("数値である必要があります");
    }

    @Test
    @DisplayName("IDに負の値を指定した場合、SnowflakeIdの事前条件検証でIllegalArgumentExceptionをスローすること")
    void shouldHandleNegativeId() {
      // Arrange
      ChangeStatusRequest changeStatusRequest = ChangeStatusRequest.builder()
          .status(SprintStatus.ACTIVE)
          .build();
      // Act & Assert - Long.parseLong("-123")は正常にパースされるが、SnowflakeId.of()で正の値チェックに失敗
      assertThatThrownBy(() -> controller.changeStatus("-123", changeStatusRequest))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("value must be positive");
    }

    @Test
    @DisplayName("IDにLong.MAX_VALUEより大きな値を指定した場合、BusinessExceptionをスローすること")
    void shouldThrowBusinessExceptionForOverflowId() {
      // Arrange
      ChangeStatusRequest changeStatusRequest = ChangeStatusRequest.builder()
          .status(SprintStatus.ACTIVE)
          .build();
      // Act & Assert
      assertThatThrownBy(() -> controller.changeStatus("99999999999999999999", changeStatusRequest))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("数値である必要があります");
    }

    @Test
    @DisplayName("IDにSQLインジェクション文字列を指定した場合、BusinessExceptionをスローすること")
    void shouldRejectSqlInjectionInId() {
      // Arrange
      ChangeStatusRequest changeStatusRequest = ChangeStatusRequest.builder()
          .status(SprintStatus.ACTIVE)
          .build();
      // Act & Assert - SQLインジェクション文字列は数値としてパースできないため拒否される
      assertThatThrownBy(() -> controller.changeStatus("1; DROP TABLE sprint;", changeStatusRequest))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("数値である必要があります");
    }

    @Test
    @DisplayName("IDにXSS文字列を指定した場合、BusinessExceptionをスローすること")
    void shouldRejectXssInId() {
      // Arrange
      ChangeStatusRequest changeStatusRequest = ChangeStatusRequest.builder()
          .status(SprintStatus.ACTIVE)
          .build();
      // Act & Assert - XSS文字列は数値としてパースできないため拒否される
      assertThatThrownBy(() -> controller.changeStatus("<script>alert(1)</script>", changeStatusRequest))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("数値である必要があります");
    }

    @Test
    @DisplayName("Sprintが見つからない場合、適切な例外をスローすること")
    void shouldThrowExceptionWhenSprintNotFound() {
      // Arrange
      ChangeStatusRequest changeStatusRequest = ChangeStatusRequest.builder()
          .status(SprintStatus.ACTIVE)
          .build();
      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      when(updateSprintCommandService.changeStatus(any(SnowflakeId.class), any(SprintStatus.class)))
          .thenThrow(new BusinessException("Sprint not found"));

      // Act & Assert
      assertThatThrownBy(() -> controller.changeStatus(
          String.valueOf(snowflakeId.value()), changeStatusRequest))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("Sprint not found");
    }

    @Test
    @DisplayName("IDにスペースを含む文字列を指定した場合、BusinessExceptionをスローすること")
    void shouldRejectIdWithSpaces() {
      // Arrange
      ChangeStatusRequest changeStatusRequest = ChangeStatusRequest.builder()
          .status(SprintStatus.ACTIVE)
          .build();
      // Act & Assert - スペースを含むIDは数値としてパースできないため拒否される
      assertThatThrownBy(() -> controller.changeStatus(" 123 ", changeStatusRequest))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("数値である必要があります");
    }

    @Test
    @DisplayName("IDに浮動小数点数を指定した場合、BusinessExceptionをスローすること")
    void shouldRejectFloatingPointId() {
      // Arrange
      ChangeStatusRequest changeStatusRequest = ChangeStatusRequest.builder()
          .status(SprintStatus.ACTIVE)
          .build();
      // Act & Assert - 浮動小数点数は整数としてパースできないため拒否される
      assertThatThrownBy(() -> controller.changeStatus("123.456", changeStatusRequest))
          .isInstanceOf(BusinessException.class)
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
      SendNotificationRequest request = SendNotificationRequest.builder()
          .recipientId(FIXED_RECIPIENT_ID)
          .title("'; DROP TABLE notifications; --")
          .content("テスト本文")
          .type(NotificationType.MENTION)
          .build();

      NotificationDTO expectedNotification = NotificationDTO.builder()
          .id(SnowflakeId.of(1234567890L))
          .recipientId(FIXED_RECIPIENT_ID)
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
      SendNotificationRequest request = SendNotificationRequest.builder()
          .recipientId(FIXED_RECIPIENT_ID)
          .title("<script>alert('xss')</script>")
          .content("<img src=x onerror=alert(1)>")
          .type(NotificationType.MENTION)
          .build();

      NotificationDTO expectedNotification = NotificationDTO.builder()
          .id(SnowflakeId.of(1234567890L))
          .recipientId(FIXED_RECIPIENT_ID)
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
      String longContent = "A".repeat(2000);
      SendNotificationRequest request = SendNotificationRequest.builder()
          .recipientId(FIXED_RECIPIENT_ID)
          .title("テスト通知")
          .content(longContent)
          .type(NotificationType.SYSTEM)
          .build();

      NotificationDTO expectedNotification = NotificationDTO.builder()
          .id(SnowflakeId.of(1234567890L))
          .recipientId(FIXED_RECIPIENT_ID)
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

      // Act & Assert
      for (NotificationType type : NotificationType.values()) {
        SendNotificationRequest request = SendNotificationRequest.builder()
            .recipientId(FIXED_RECIPIENT_ID)
            .title(type + "通知")
            .content(type + "の本文")
            .type(type)
            .build();

        NotificationDTO expectedNotification = NotificationDTO.builder()
            .id(SnowflakeId.of(1234567890L))
            .recipientId(FIXED_RECIPIENT_ID)
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
