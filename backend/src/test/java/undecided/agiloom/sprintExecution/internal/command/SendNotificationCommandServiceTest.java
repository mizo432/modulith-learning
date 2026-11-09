package undecided.agiloom.sprintExecution.internal.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.supporting.notification.spi.NotificationDTO;
import undecided.supporting.notification.spi.NotificationRequest;
import undecided.supporting.notification.spi.NotificationService;
import undecided.supporting.notification.spi.NotificationType;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * {@code SendNotificationCommandServiceTest} は、SendNotificationCommandService の単体テストクラスです。
 * <p>
 * 規約要件:
 * 1. @Tag("small") を付与
 * 2. クラス・メソッドともに日本語 @DisplayName を付与
 * 3. 対象メソッドごとに @Nested クラスでグループ化
 * 4. メソッド名は should で開始し、アンダースコアを使用しない
 * 5. package-private 可視性
 * </p>
 */
@Tag("small")
@DisplayName("SendNotificationCommandServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class SendNotificationCommandServiceTest {

    @Mock
    private NotificationService notificationService;

    private SendNotificationCommandService service;

    @BeforeEach
    void setUp() {
        service = new SendNotificationCommandService(notificationService);
    }

    private NotificationDTO createNotificationDTO(SnowflakeId id, UUID recipientId, UUID sourceId,
            String title, String content, NotificationType type) {
        return NotificationDTO.builder()
                .id(id)
                .recipientId(recipientId)
                .sourceId(sourceId)
                .sourceDomain("agiloom")
                .title(title)
                .content(content)
                .type(type)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("executeメソッドのテスト")
    class ExecuteTest {

        private final UUID recipientId = UUID.randomUUID();
        private final UUID sourceId = UUID.randomUUID();

        @Test
        @DisplayName("メンション通知が正常に送信されること")
        void shouldSendMentionNotification() {
            // Arrange
            SendNotificationCommand command = SendNotificationCommand.builder()
                    .recipientId(recipientId)
                    .sourceId(sourceId)
                    .title("メンション通知")
                    .content("あなたはメンションされました")
                    .type(NotificationType.MENTION)
                    .build();

            SnowflakeId expectedId = SnowflakeId.of(1234567890L);
            NotificationDTO savedNotification = createNotificationDTO(
                    expectedId, recipientId, sourceId,
                    "メンション通知", "あなたはメンションされました", NotificationType.MENTION);

            when(notificationService.send(argThat(req ->
                    req.getRecipientId().equals(recipientId)
                            && req.getSourceId().equals(sourceId)
                            && "メンション通知".equals(req.getTitle())
                            && "あなたはメンションされました".equals(req.getContent())
                            && req.getType() == NotificationType.MENTION
                            && "agiloom".equals(req.getSourceDomain())
            ))).thenReturn(savedNotification);

            // Act
            NotificationDTO result = service.execute(command);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getId().value()).isEqualTo(1234567890L);
            assertThat(result.getRecipientId()).isEqualTo(recipientId);
            assertThat(result.getTitle()).isEqualTo("メンション通知");
            assertThat(result.getType()).isEqualTo(NotificationType.MENTION);
        }

        @Test
        @DisplayName("担当変更通知が正常に送信されること")
        void shouldSendAssigneeChangeNotification() {
            // Arrange
            SendNotificationCommand command = SendNotificationCommand.builder()
                    .recipientId(recipientId)
                    .title("担当変更通知")
                    .content("担当者が変更されました")
                    .type(NotificationType.ASSIGNEE_CHANGE)
                    .build();

            NotificationDTO savedNotification = createNotificationDTO(
                    SnowflakeId.of(2234567890L), recipientId, null,
                    "担当変更通知", "担当者が変更されました", NotificationType.ASSIGNEE_CHANGE);

            when(notificationService.send(argThat(req ->
                    req.getRecipientId().equals(recipientId)
                            && "担当変更通知".equals(req.getTitle())
                            && "担当者が変更されました".equals(req.getContent())
                            && req.getType() == NotificationType.ASSIGNEE_CHANGE
                            && "agiloom".equals(req.getSourceDomain())
            ))).thenReturn(savedNotification);

            // Act
            NotificationDTO result = service.execute(command);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getType()).isEqualTo(NotificationType.ASSIGNEE_CHANGE);
        }

        @Test
        @DisplayName("期限通知が正常に送信されること")
        void shouldSendDueDateNotification() {
            // Arrange
            SendNotificationCommand command = SendNotificationCommand.builder()
                    .recipientId(recipientId)
                    .title("期限通知")
                    .content("期限が近づいています")
                    .type(NotificationType.DUE_DATE)
                    .build();

            NotificationDTO savedNotification = createNotificationDTO(
                    SnowflakeId.of(3234567890L), recipientId, null,
                    "期限通知", "期限が近づいています", NotificationType.DUE_DATE);

            when(notificationService.send(argThat(req ->
                    req.getRecipientId().equals(recipientId)
                            && "期限通知".equals(req.getTitle())
                            && "期限が近づいています".equals(req.getContent())
                            && req.getType() == NotificationType.DUE_DATE
                            && "agiloom".equals(req.getSourceDomain())
            ))).thenReturn(savedNotification);

            // Act
            NotificationDTO result = service.execute(command);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getType()).isEqualTo(NotificationType.DUE_DATE);
        }

        @Test
        @DisplayName("判断待ち通知が正常に送信されること")
        void shouldSendAwaitingDecisionNotification() {
            // Arrange
            SendNotificationCommand command = SendNotificationCommand.builder()
                    .recipientId(recipientId)
                    .title("判断待ち通知")
                    .content("あなたの判断を待っています")
                    .type(NotificationType.AWAITING_DECISION)
                    .build();

            NotificationDTO savedNotification = createNotificationDTO(
                    SnowflakeId.of(4234567890L), recipientId, null,
                    "判断待ち通知", "あなたの判断を待っています", NotificationType.AWAITING_DECISION);

            when(notificationService.send(argThat(req ->
                    req.getRecipientId().equals(recipientId)
                            && "判断待ち通知".equals(req.getTitle())
                            && "あなたの判断を待っています".equals(req.getContent())
                            && req.getType() == NotificationType.AWAITING_DECISION
                            && "agiloom".equals(req.getSourceDomain())
            ))).thenReturn(savedNotification);

            // Act
            NotificationDTO result = service.execute(command);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getType()).isEqualTo(NotificationType.AWAITING_DECISION);
        }

        @Test
        @DisplayName("sourceIdなしで通知が送信されること")
        void shouldSendNotificationWithoutSourceId() {
            // Arrange
            SendNotificationCommand command = SendNotificationCommand.builder()
                    .recipientId(recipientId)
                    .title("sourceIdなしの通知")
                    .content("sourceIdなしの本文")
                    .type(NotificationType.SYSTEM)
                    .build();

            NotificationDTO savedNotification = createNotificationDTO(
                    SnowflakeId.of(5234567890L), recipientId, null,
                    "sourceIdなしの通知", "sourceIdなしの本文", NotificationType.SYSTEM);

            when(notificationService.send(argThat(req ->
                    req.getRecipientId().equals(recipientId)
                            && "sourceIdなしの通知".equals(req.getTitle())
                            && "sourceIdなしの本文".equals(req.getContent())
                            && req.getType() == NotificationType.SYSTEM
                            && "agiloom".equals(req.getSourceDomain())
            ))).thenReturn(savedNotification);

            // Act
            NotificationDTO result = service.execute(command);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getSourceId()).isNull();
        }

        @Test
        @DisplayName("NotificationServiceに正しいリクエストが渡されること")
        void shouldPassCorrectRequestToNotificationService() {
            // Arrange
            SendNotificationCommand command = SendNotificationCommand.builder()
                    .recipientId(recipientId)
                    .sourceId(sourceId)
                    .title("テスト通知")
                    .content("テスト本文")
                    .type(NotificationType.CUSTOM)
                    .build();

            NotificationDTO savedNotification = createNotificationDTO(
                    SnowflakeId.of(6234567890L), recipientId, sourceId,
                    "テスト通知", "テスト本文", NotificationType.CUSTOM);

            when(notificationService.send(argThat(req ->
                    req.getRecipientId().equals(recipientId)
                            && req.getSourceId().equals(sourceId)
                            && "テスト通知".equals(req.getTitle())
                            && "テスト本文".equals(req.getContent())
                            && req.getType() == NotificationType.CUSTOM
                            && "agiloom".equals(req.getSourceDomain())
            ))).thenReturn(savedNotification);

            // Act
            service.execute(command);

            // Assert - argThatを使用してリクエストの内容を検証
            verify(notificationService).send(argThat(request ->
                    request.getRecipientId().equals(recipientId)
                            && request.getSourceId().equals(sourceId)
                            && "テスト通知".equals(request.getTitle())
                            && "テスト本文".equals(request.getContent())
                            && request.getType() == NotificationType.CUSTOM
                            && "agiloom".equals(request.getSourceDomain())
            ));
        }
    }
}
