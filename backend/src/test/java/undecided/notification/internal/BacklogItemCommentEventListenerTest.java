package undecided.notification.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.agiloom.requirementBacklog.spi.BacklogItemCommentAdded;
import undecided.agiloom.requirementBacklog.spi.BacklogItemCommentDeleted;
import undecided.notification.spi.NotificationEntity;
import undecided.notification.spi.NotificationType;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

/**
 * テストリスト項目3〜6, 12〜13: イベント受信時の通知レコード作成
 *
 * <p>- 項目3: イベントを受信したとき、コメント参加者（操作者除く）に通知レコードが作成される
 * <p>- 項目4: コメントが1件のみで操作者が投稿者だったとき、通知レコードは作成されない
 * <p>- 項目5: 複数の参加者がいるとき、操作者以外の全参加者に通知レコードが作成される
 * <p>- 項目6: 通知レコード作成時、isReadはfalseである
 * <p>- 項目12: コメント追加イベントのとき、NotificationTypeはCOMMENT_ADDEDである
 * <p>- 項目13: コメント削除イベントのとき、NotificationTypeはCOMMENT_DELETEDである
 */
@Tag("small")
@DisplayName("BacklogItemCommentEventListenerの単体テスト")
@ExtendWith(MockitoExtension.class)
class BacklogItemCommentEventListenerTest {

  private static final long ITEM_ID_VALUE = 10L;
  private static final long COMMENT_ID_VALUE = 1000L;
  private static final long ACTOR_ID_VALUE = 100L;
  private static final long PARTICIPANT_1_ID_VALUE = 200L;
  private static final long PARTICIPANT_2_ID_VALUE = 300L;
  private static final long NOTIFICATION_ID_VALUE = 5000L;

  private static final SnowflakeId ITEM_ID = SnowflakeId.of(ITEM_ID_VALUE);
  private static final SnowflakeId COMMENT_ID = SnowflakeId.of(COMMENT_ID_VALUE);
  private static final SnowflakeId ACTOR_ID = SnowflakeId.of(ACTOR_ID_VALUE);
  private static final SnowflakeId PARTICIPANT_1_ID = SnowflakeId.of(PARTICIPANT_1_ID_VALUE);
  private static final SnowflakeId PARTICIPANT_2_ID = SnowflakeId.of(PARTICIPANT_2_ID_VALUE);
  private static final SnowflakeId NOTIFICATION_ID = SnowflakeId.of(NOTIFICATION_ID_VALUE);

  @Mock
  private NotificationEntityRepository notificationRepository;

  @Mock
  private NotificationCommentParticipantSubsetRepository commentParticipantSubsetRepository;

  @Mock
  private EmailNotificationService emailNotificationService;

  @Mock
  private SnowflakeIdGenerator snowflakeIdGenerator;

  private BacklogItemCommentEventListener listener;

  @BeforeEach
  void setUp() {
    listener =
        new BacklogItemCommentEventListener(
            notificationRepository,
            commentParticipantSubsetRepository,
            emailNotificationService,
            snowflakeIdGenerator);
  }

  @Nested
  @DisplayName("コメント追加イベントのテスト")
  class CommentAddedEventTest {

    @Test
    @DisplayName("イベントを受信したとき、コメント参加者（操作者除く）に通知レコードが作成される")
    void shouldCreateNotificationForParticipantsExcludingActor() {
      // Arrange: テストリスト項目3
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(NOTIFICATION_ID);
      BacklogItemCommentAdded event =
          new BacklogItemCommentAdded(
              ITEM_ID, COMMENT_ID, ACTOR_ID, "Actor", "actor@example.com", LocalDateTime.now());

      Set<NotificationCommentParticipantSubset> participants = new HashSet<>();
      participants.add(
          NotificationCommentParticipantSubset.builder()
              .id(SnowflakeId.of(1L))
              .backlogItemId(ITEM_ID)
              .employeeId(PARTICIPANT_1_ID)
              .employeeName("Participant1")
              .employeeEmail("participant1@example.com")
              .build());
      participants.add(
          NotificationCommentParticipantSubset.builder()
              .id(SnowflakeId.of(2L))
              .backlogItemId(ITEM_ID)
              .employeeId(PARTICIPANT_2_ID)
              .employeeName("Participant2")
              .employeeEmail("participant2@example.com")
              .build());
      when(commentParticipantSubsetRepository.findByBacklogItemId(ITEM_ID)).thenReturn(participants);

      // Act
      listener.onCommentAdded(event);

      // Assert: 2人の参加者に通知が作成される
      ArgumentCaptor<NotificationEntity> captor = ArgumentCaptor.forClass(NotificationEntity.class);
      verify(notificationRepository, org.mockito.Mockito.times(2)).save(captor.capture());
      assertThat(captor.getAllValues())
          .extracting(NotificationEntity::getRecipientId)
          .containsExactlyInAnyOrder(PARTICIPANT_1_ID, PARTICIPANT_2_ID);
      assertThat(captor.getAllValues().get(0).getIsRead()).isFalse();
      assertThat(captor.getAllValues().get(0).getType()).isEqualTo(NotificationType.COMMENT_ADDED);
    }

    @Test
    @DisplayName("コメントが1件のみで操作者が投稿者だったとき、通知レコードは作成されない")
    void shouldNotCreateNotificationWhenActorIsOnlyParticipant() {
      // Arrange: テストリスト項目4
      BacklogItemCommentAdded event =
          new BacklogItemCommentAdded(
              ITEM_ID, COMMENT_ID, ACTOR_ID, "Actor", "actor@example.com", LocalDateTime.now());

      // 参加者は操作者のみ
      Set<NotificationCommentParticipantSubset> participants = new HashSet<>();
      participants.add(
          NotificationCommentParticipantSubset.builder()
              .id(SnowflakeId.of(1L))
              .backlogItemId(ITEM_ID)
              .employeeId(ACTOR_ID)
              .employeeName("Actor")
              .employeeEmail("actor@example.com")
              .build());
      when(commentParticipantSubsetRepository.findByBacklogItemId(ITEM_ID)).thenReturn(participants);

      // Act
      listener.onCommentAdded(event);

      // Assert: 通知は作成されない
      verify(notificationRepository, never()).save(any(NotificationEntity.class));
    }

    @Test
    @DisplayName("複数の参加者がいるとき、操作者以外の全参加者に通知レコードが作成される")
    void shouldCreateNotificationsForAllParticipantsExceptActor() {
      // Arrange: テストリスト項目5
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(NOTIFICATION_ID);
      BacklogItemCommentAdded event =
          new BacklogItemCommentAdded(
              ITEM_ID, COMMENT_ID, ACTOR_ID, "Actor", "actor@example.com", LocalDateTime.now());

      Set<NotificationCommentParticipantSubset> participants = new HashSet<>();
      participants.add(
          NotificationCommentParticipantSubset.builder()
              .id(SnowflakeId.of(1L))
              .backlogItemId(ITEM_ID)
              .employeeId(ACTOR_ID)
              .employeeName("Actor")
              .employeeEmail("actor@example.com")
              .build());
      participants.add(
          NotificationCommentParticipantSubset.builder()
              .id(SnowflakeId.of(2L))
              .backlogItemId(ITEM_ID)
              .employeeId(PARTICIPANT_1_ID)
              .employeeName("Participant1")
              .employeeEmail("participant1@example.com")
              .build());
      participants.add(
          NotificationCommentParticipantSubset.builder()
              .id(SnowflakeId.of(3L))
              .backlogItemId(ITEM_ID)
              .employeeId(PARTICIPANT_2_ID)
              .employeeName("Participant2")
              .employeeEmail("participant2@example.com")
              .build());
      when(commentParticipantSubsetRepository.findByBacklogItemId(ITEM_ID)).thenReturn(participants);

      // Act
      listener.onCommentAdded(event);

      // Assert: 操作者以外の2人に通知が作成される
      ArgumentCaptor<NotificationEntity> captor = ArgumentCaptor.forClass(NotificationEntity.class);
      verify(notificationRepository, org.mockito.Mockito.times(2)).save(captor.capture());
      assertThat(captor.getAllValues())
          .extracting(NotificationEntity::getRecipientId)
          .containsExactlyInAnyOrder(PARTICIPANT_1_ID, PARTICIPANT_2_ID);
    }

    @Test
    @DisplayName("通知レコード作成時、isReadはfalseである")
    void shouldHaveIsReadFalseOnCreation() {
      // Arrange: テストリスト項目6
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(NOTIFICATION_ID);
      BacklogItemCommentAdded event =
          new BacklogItemCommentAdded(
              ITEM_ID, COMMENT_ID, ACTOR_ID, "Actor", "actor@example.com", LocalDateTime.now());

      Set<NotificationCommentParticipantSubset> participants = new HashSet<>();
      participants.add(
          NotificationCommentParticipantSubset.builder()
              .id(SnowflakeId.of(1L))
              .backlogItemId(ITEM_ID)
              .employeeId(PARTICIPANT_1_ID)
              .employeeName("Participant1")
              .employeeEmail("participant1@example.com")
              .build());
      when(commentParticipantSubsetRepository.findByBacklogItemId(ITEM_ID)).thenReturn(participants);

      // Act
      listener.onCommentAdded(event);

      // Assert
      ArgumentCaptor<NotificationEntity> captor = ArgumentCaptor.forClass(NotificationEntity.class);
      verify(notificationRepository).save(captor.capture());
      assertThat(captor.getValue().getIsRead()).isFalse();
    }

    @Test
    @DisplayName("コメント追加イベントのとき、NotificationTypeはCOMMENT_ADDEDである")
    void shouldHaveCommentAddedType() {
      // Arrange: テストリスト項目12
      assertThat(listener.getNotificationType(true)).isEqualTo(NotificationType.COMMENT_ADDED);
    }
  }

  @Nested
  @DisplayName("コメント削除イベントのテスト")
  class CommentDeletedEventTest {

    @Test
    @DisplayName("コメント削除イベントのとき、NotificationTypeはCOMMENT_DELETEDである")
    void shouldHaveCommentDeletedType() {
      // Arrange: テストリスト項目13
      assertThat(listener.getNotificationType(false)).isEqualTo(NotificationType.COMMENT_DELETED);
    }

    @Test
    @DisplayName("コメント削除イベントを受信したとき、参加者（操作者除く）に通知レコードが作成される")
    void shouldCreateNotificationOnCommentDeleted() {
      // Arrange
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(NOTIFICATION_ID);
      BacklogItemCommentDeleted event =
          new BacklogItemCommentDeleted(ITEM_ID, COMMENT_ID, ACTOR_ID, LocalDateTime.now());

      Set<NotificationCommentParticipantSubset> participants = new HashSet<>();
      participants.add(
          NotificationCommentParticipantSubset.builder()
              .id(SnowflakeId.of(1L))
              .backlogItemId(ITEM_ID)
              .employeeId(PARTICIPANT_1_ID)
              .employeeName("Participant1")
              .employeeEmail("participant1@example.com")
              .build());
      when(commentParticipantSubsetRepository.findByBacklogItemId(ITEM_ID)).thenReturn(participants);

      // Act
      listener.onCommentDeleted(event);

      // Assert
      ArgumentCaptor<NotificationEntity> captor = ArgumentCaptor.forClass(NotificationEntity.class);
      verify(notificationRepository).save(captor.capture());
      NotificationEntity saved = captor.getValue();
      assertThat(saved.getRecipientId()).isEqualTo(PARTICIPANT_1_ID);
      assertThat(saved.getType()).isEqualTo(NotificationType.COMMENT_DELETED);
    }
  }
}
