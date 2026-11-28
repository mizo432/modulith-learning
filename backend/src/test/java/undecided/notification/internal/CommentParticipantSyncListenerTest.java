package undecided.notification.internal;
import java.time.Clock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.agiloom.requirementBacklog.spi.BacklogItemCommentAdded;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

/**
 * テストリスト項目1〜4: CommentParticipantSyncListener の重複登録修正
 *
 * <p>- 項目1: 参加者が未登録のとき、コメント追加イベントを受信すると、参加者が保存される
 * <p>- 項目2: 参加者が既に登録済みのとき、コメント追加イベントを受信すると、保存されない
 * <p>- 項目3: 異なる投稿者のコメント追加イベントを受信すると、それぞれ独立して参加者が保存される
 * <p>- 項目4: 投稿者IDがnullのとき、コメント追加イベントを受信すると、保存されない
 */
@Tag("small")
@DisplayName("CommentParticipantSyncListenerの単体テスト")
@ExtendWith(MockitoExtension.class)
class CommentParticipantSyncListenerTest {

  private static final long ITEM_ID_VALUE = 10L;
  private static final long COMMENT_ID_VALUE = 1000L;
  private static final long AUTHOR_ID_VALUE = 100L;
  private static final long AUTHOR_2_ID_VALUE = 200L;
  private static final long PARTICIPANT_ID_VALUE = 300L;
  private static final long SUBSET_ID_VALUE = 5000L;

  private static final SnowflakeId ITEM_ID = SnowflakeId.of(ITEM_ID_VALUE);
  private static final SnowflakeId COMMENT_ID = SnowflakeId.of(COMMENT_ID_VALUE);
  private static final SnowflakeId AUTHOR_ID = SnowflakeId.of(AUTHOR_ID_VALUE);
  private static final SnowflakeId AUTHOR_2_ID = SnowflakeId.of(AUTHOR_2_ID_VALUE);
  private static final SnowflakeId PARTICIPANT_ID = SnowflakeId.of(PARTICIPANT_ID_VALUE);
  private static final SnowflakeId SUBSET_ID = SnowflakeId.of(SUBSET_ID_VALUE);

  @Mock
  private NotificationCommentParticipantSubsetRepository commentParticipantSubsetRepository;

  @Mock
  private SnowflakeIdGenerator snowflakeIdGenerator;

  private CommentParticipantSyncListener listener;

  @BeforeEach
  void setUp() {
    listener =
        new CommentParticipantSyncListener(
            commentParticipantSubsetRepository,
            snowflakeIdGenerator, Clock.systemDefaultZone());
  }

  @Test
  @DisplayName("参加者が未登録のとき、コメント追加イベントを受信すると、参加者が保存される")
  void shouldSaveParticipantWhenNotRegistered() {
    // テストリスト項目1
    // Arrange: 参加者が未登録（existsByBacklogItemIdAndEmployeeId が false）
    when(commentParticipantSubsetRepository.existsByBacklogItemIdAndEmployeeId(ITEM_ID, AUTHOR_ID))
        .thenReturn(false);
    when(snowflakeIdGenerator.nextIdValue()).thenReturn(SUBSET_ID);

    BacklogItemCommentAdded event =
        new BacklogItemCommentAdded(
            ITEM_ID, COMMENT_ID, AUTHOR_ID, "投稿者", "author@example.com", LocalDateTime.now());

    // Act
    listener.onCommentAdded(event);

    // Assert: 参加者が保存される
    ArgumentCaptor<NotificationCommentParticipantSubset> captor =
        ArgumentCaptor.forClass(NotificationCommentParticipantSubset.class);
    verify(commentParticipantSubsetRepository).save(captor.capture());
    NotificationCommentParticipantSubset saved = captor.getValue();
    assertThat(saved.getBacklogItemId()).isEqualTo(ITEM_ID);
    assertThat(saved.getEmployeeId()).isEqualTo(AUTHOR_ID);
    assertThat(saved.getEmployeeName()).isEqualTo("投稿者");
    assertThat(saved.getEmployeeEmail()).isEqualTo("author@example.com");
  }

  @Test
  @DisplayName("参加者が既に登録済みのとき、コメント追加イベントを受信すると、保存されない")
  void shouldNotSaveParticipantWhenAlreadyRegistered() {
    // テストリスト項目2
    // Arrange: 参加者が既に登録済み（existsByBacklogItemIdAndEmployeeId が true）
    when(commentParticipantSubsetRepository.existsByBacklogItemIdAndEmployeeId(ITEM_ID, AUTHOR_ID))
        .thenReturn(true);

    BacklogItemCommentAdded event =
        new BacklogItemCommentAdded(
            ITEM_ID, COMMENT_ID, AUTHOR_ID, "投稿者", "author@example.com", LocalDateTime.now());

    // Act
    listener.onCommentAdded(event);

    // Assert: 保存は行われない
    verify(commentParticipantSubsetRepository, never()).save(any(NotificationCommentParticipantSubset.class));
  }

  @Test
  @DisplayName("異なる投稿者のコメント追加イベントを受信すると、それぞれ独立して参加者が保存される")
  void shouldSaveParticipantsIndependentlyForDifferentAuthors() {
    // テストリスト項目3
    // Arrange: 2人の異なる投稿者が未登録
    when(commentParticipantSubsetRepository.existsByBacklogItemIdAndEmployeeId(ITEM_ID, AUTHOR_ID))
        .thenReturn(false);
    when(commentParticipantSubsetRepository.existsByBacklogItemIdAndEmployeeId(ITEM_ID, AUTHOR_2_ID))
        .thenReturn(false);
    when(snowflakeIdGenerator.nextIdValue()).thenReturn(SUBSET_ID);

    BacklogItemCommentAdded event1 =
        new BacklogItemCommentAdded(
            ITEM_ID, COMMENT_ID, AUTHOR_ID, "投稿者1", "author1@example.com", LocalDateTime.now());
    BacklogItemCommentAdded event2 =
        new BacklogItemCommentAdded(
            ITEM_ID, COMMENT_ID, AUTHOR_2_ID, "投稿者2", "author2@example.com", LocalDateTime.now());

    // Act
    listener.onCommentAdded(event1);
    listener.onCommentAdded(event2);

    // Assert: 2人とも独立して保存される
    ArgumentCaptor<NotificationCommentParticipantSubset> captor =
        ArgumentCaptor.forClass(NotificationCommentParticipantSubset.class);
    verify(commentParticipantSubsetRepository, org.mockito.Mockito.times(2)).save(captor.capture());
    assertThat(captor.getAllValues())
        .extracting(NotificationCommentParticipantSubset::getEmployeeId)
        .containsExactlyInAnyOrder(AUTHOR_ID, AUTHOR_2_ID);
  }

  @Test
  @DisplayName("投稿者IDがnullのとき、コメント追加イベントを受信すると、保存されない")
  void shouldNotSaveParticipantWhenAuthorIdIsNull() {
    // テストリスト項目4
    // Arrange: 投稿者IDがnull
    BacklogItemCommentAdded event =
        new BacklogItemCommentAdded(
            ITEM_ID, COMMENT_ID, null, "投稿者", "author@example.com", LocalDateTime.now());

    // Act
    listener.onCommentAdded(event);

    // Assert: 保存は行われない
    verify(commentParticipantSubsetRepository, never()).save(any(NotificationCommentParticipantSubset.class));
  }
}
