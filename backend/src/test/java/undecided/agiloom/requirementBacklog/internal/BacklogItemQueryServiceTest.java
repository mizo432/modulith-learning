package undecided.agiloom.requirementBacklog.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemComment;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("BacklogItemQueryServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class BacklogItemQueryServiceTest {

  @Mock private BacklogItemRepository backlogItemRepository;

  @Mock private BacklogItemCommentRepository backlogItemCommentRepository;

  private BacklogItemQueryService service;

  private SnowflakeId teamId;

  @BeforeEach
  void setUp() {
    service = new BacklogItemQueryService(backlogItemRepository, backlogItemCommentRepository, null, null);
    teamId = SnowflakeId.of(10L);
  }

  @Test
  @DisplayName("Value降順でソートされること")
  void shouldSortByValueDescending() {
    // Arrange
    BacklogItem item100 = createBacklogItem(SnowflakeId.of(3L), "Item100", 100, BacklogItemStatus.SUGGESTED,
        LocalDateTime.of(2026, 1, 3, 0, 0));
    BacklogItem item50 = createBacklogItem(SnowflakeId.of(2L), "Item50", 50, BacklogItemStatus.SUGGESTED,
        LocalDateTime.of(2026, 1, 2, 0, 0));
    BacklogItem item10 = createBacklogItem(SnowflakeId.of(1L), "Item10", 10, BacklogItemStatus.SUGGESTED,
        LocalDateTime.of(2026, 1, 1, 0, 0));

    when(backlogItemRepository.findByScrumTeamId(teamId))
        .thenReturn(Arrays.asList(item10, item50, item100));

    // Act
    List<BacklogItem> result = service.findByScrumTeamId(teamId, null);

    // Assert
    assertThat(result).hasSize(3);
    assertThat(result.get(0).getValue()).isEqualTo(100);
    assertThat(result.get(1).getValue()).isEqualTo(50);
    assertThat(result.get(2).getValue()).isEqualTo(10);
  }

  @Test
  @DisplayName("Valueが同値の場合、createdAt昇順（古い方が上）でソートされること")
  void shouldSortByCreatedAtAscendingWhenValueEqual() {
    // Arrange
    BacklogItem older = createBacklogItem(SnowflakeId.of(1L), "Older", 50, BacklogItemStatus.SUGGESTED,
        LocalDateTime.of(2026, 1, 1, 0, 0));
    BacklogItem newer = createBacklogItem(SnowflakeId.of(2L), "Newer", 50, BacklogItemStatus.SUGGESTED,
        LocalDateTime.of(2026, 1, 2, 0, 0));

    when(backlogItemRepository.findByScrumTeamId(teamId))
        .thenReturn(Arrays.asList(newer, older));

    // Act
    List<BacklogItem> result = service.findByScrumTeamId(teamId, null);

    // Assert
    assertThat(result).hasSize(2);
    assertThat(result.get(0).getId()).isEqualTo(SnowflakeId.of(1L)); // 古い方が先
    assertThat(result.get(1).getId()).isEqualTo(SnowflakeId.of(2L));
  }

  @Test
  @DisplayName("ValueとcreatedAtが同値の場合、id昇順でソートされること")
  void shouldSortByIdAscendingWhenValueAndCreatedAtEqual() {
    // Arrange
    LocalDateTime sameTime = LocalDateTime.of(2026, 1, 1, 12, 0);
    BacklogItem id3 = createBacklogItem(SnowflakeId.of(3L), "Id3", 50, BacklogItemStatus.SUGGESTED, sameTime);
    BacklogItem id1 = createBacklogItem(SnowflakeId.of(1L), "Id1", 50, BacklogItemStatus.SUGGESTED, sameTime);
    BacklogItem id2 = createBacklogItem(SnowflakeId.of(2L), "Id2", 50, BacklogItemStatus.SUGGESTED, sameTime);

    when(backlogItemRepository.findByScrumTeamId(teamId))
        .thenReturn(Arrays.asList(id3, id1, id2));

    // Act
    List<BacklogItem> result = service.findByScrumTeamId(teamId, null);

    // Assert
    assertThat(result).hasSize(3);
    assertThat(result.get(0).getId()).isEqualTo(SnowflakeId.of(1L));
    assertThat(result.get(1).getId()).isEqualTo(SnowflakeId.of(2L));
    assertThat(result.get(2).getId()).isEqualTo(SnowflakeId.of(3L));
  }

  @Test
  @DisplayName("Valueがnullのアイテムは末尾に配置されること")
  void shouldPutNullValueAtEnd() {
    // Arrange
    BacklogItem item100 = createBacklogItem(SnowflakeId.of(1L), "Item100", 100, BacklogItemStatus.SUGGESTED,
        LocalDateTime.of(2026, 1, 1, 0, 0));
    BacklogItem itemNull1 = createBacklogItem(SnowflakeId.of(2L), "Null1", null, BacklogItemStatus.SUGGESTED,
        LocalDateTime.of(2026, 1, 2, 0, 0));
    BacklogItem item50 = createBacklogItem(SnowflakeId.of(3L), "Item50", 50, BacklogItemStatus.SUGGESTED,
        LocalDateTime.of(2026, 1, 3, 0, 0));
    BacklogItem itemNull2 = createBacklogItem(SnowflakeId.of(4L), "Null2", null, BacklogItemStatus.SUGGESTED,
        LocalDateTime.of(2026, 1, 4, 0, 0));

    when(backlogItemRepository.findByScrumTeamId(teamId))
        .thenReturn(Arrays.asList(itemNull1, item50, itemNull2, item100));

    // Act
    List<BacklogItem> result = service.findByScrumTeamId(teamId, null);

    // Assert
    assertThat(result).hasSize(4);
    // Valueありは降順: 100, 50
    assertThat(result.get(0).getValue()).isEqualTo(100);
    assertThat(result.get(1).getValue()).isEqualTo(50);
    // Value nullは末尾。createdAt昇順で並ぶ
    assertThat(result.get(2).getValue()).isNull();
    assertThat(result.get(2).getId()).isEqualTo(SnowflakeId.of(2L)); // Null1 (createdAt 2026-01-02)
    assertThat(result.get(3).getValue()).isNull();
    assertThat(result.get(3).getId()).isEqualTo(SnowflakeId.of(4L)); // Null2 (createdAt 2026-01-04)
  }

  @Test
  @DisplayName("状態フィルタ適用時もValue降順が維持されること")
  void shouldMaintainValueSortWithStatusFilter() {
    // Arrange
    BacklogItem accepted100 = createBacklogItem(SnowflakeId.of(1L), "Accepted100", 100, BacklogItemStatus.ACCEPTED,
        LocalDateTime.of(2026, 1, 1, 0, 0));
    BacklogItem accepted50 = createBacklogItem(SnowflakeId.of(2L), "Accepted50", 50, BacklogItemStatus.ACCEPTED,
        LocalDateTime.of(2026, 1, 2, 0, 0));
    BacklogItem accepted10 = createBacklogItem(SnowflakeId.of(3L), "Accepted10", 10, BacklogItemStatus.ACCEPTED,
        LocalDateTime.of(2026, 1, 3, 0, 0));

    when(backlogItemRepository.findByScrumTeamIdAndStatus(teamId, BacklogItemStatus.ACCEPTED))
        .thenReturn(Arrays.asList(accepted10, accepted50, accepted100));

    // Act
    List<BacklogItem> result = service.findByScrumTeamId(teamId, BacklogItemStatus.ACCEPTED);

    // Assert
    assertThat(result).hasSize(3);
    assertThat(result.get(0).getValue()).isEqualTo(100);
    assertThat(result.get(1).getValue()).isEqualTo(50);
    assertThat(result.get(2).getValue()).isEqualTo(10);
  }

  @Test
  @DisplayName("アイテムがない場合は空リストが返されること")
  void shouldReturnEmptyListWhenNoItems() {
    // Arrange
    when(backlogItemRepository.findByScrumTeamId(teamId)).thenReturn(Collections.emptyList());

    // Act
    List<BacklogItem> result = service.findByScrumTeamId(teamId, null);

    // Assert
    assertThat(result).isEmpty();
  }

  private BacklogItem createBacklogItem(
      SnowflakeId id,
      String title,
      Integer value,
      BacklogItemStatus status,
      LocalDateTime createdAt) {
    return BacklogItem.builder()
        .id(id)
        .title(title)
        .scrumTeamId(teamId)
        .creatorName("TestUser")
        .status(status)
        .value(value)
        .createdAt(createdAt)
        .updatedAt(createdAt)
        .build();
  }

  @Nested
  @DisplayName("コメント関連のテスト")
  class CommentTests {

    private static final SnowflakeId ITEM_ID = SnowflakeId.of(10L);
    private static final SnowflakeId COMMENT_1_ID = SnowflakeId.of(100L);
    private static final SnowflakeId COMMENT_2_ID = SnowflakeId.of(200L);
    private static final SnowflakeId REPLY_ID = SnowflakeId.of(300L);

    @Test
    @DisplayName("コメント一覧を取得したとき、createdAt昇順で返る")
    void shouldReturnCommentsOrderedByCreatedAt() {
      // Arrange
      BacklogItem item = createBacklogItem(ITEM_ID, "Item", 50, BacklogItemStatus.SUGGESTED,
          LocalDateTime.of(2026, 1, 1, 0, 0));
      BacklogItemComment comment2 = BacklogItemComment.builder()
          .id(COMMENT_2_ID)
          .backlogItem(item)
          .authorEmployeeId(SnowflakeId.of(1L))
          .authorName("Author2")
          .body("Second")
          .createdAt(LocalDateTime.of(2026, 1, 2, 0, 0))
          .build();
      BacklogItemComment comment1 = BacklogItemComment.builder()
          .id(COMMENT_1_ID)
          .backlogItem(item)
          .authorEmployeeId(SnowflakeId.of(1L))
          .authorName("Author1")
          .body("First")
          .createdAt(LocalDateTime.of(2026, 1, 1, 0, 0))
          .build();

      // リポジトリはcreatedAt昇順で返す（すでにソート済み）
      when(backlogItemCommentRepository.findByBacklogItemIdOrderByCreatedAtAsc(ITEM_ID))
          .thenReturn(Arrays.asList(comment1, comment2));

      // Act
      List<BacklogItemComment> result = service.findComments(ITEM_ID);

      // Assert
      assertThat(result).hasSize(2);
      assertThat(result.get(0).getId()).isEqualTo(COMMENT_1_ID);
      assertThat(result.get(1).getId()).isEqualTo(COMMENT_2_ID);
    }

    @Test
    @DisplayName("親なしコメントの一覧を取得したとき、コメントのparentCommentがnullである")
    void shouldReturnCommentsWithNullParentComment() {
      // Arrange
      BacklogItem item = createBacklogItem(ITEM_ID, "Item", 50, BacklogItemStatus.SUGGESTED,
          LocalDateTime.of(2026, 1, 1, 0, 0));
      BacklogItemComment rootComment = BacklogItemComment.builder()
          .id(COMMENT_1_ID)
          .backlogItem(item)
          .authorEmployeeId(SnowflakeId.of(1L))
          .authorName("Author")
          .body("Root comment")
          .createdAt(LocalDateTime.of(2026, 1, 1, 0, 0))
          .build();

      when(backlogItemCommentRepository.findByBacklogItemIdOrderByCreatedAtAsc(ITEM_ID))
          .thenReturn(List.of(rootComment));

      // Act
      List<BacklogItemComment> result = service.findComments(ITEM_ID);

      // Assert
      assertThat(result).hasSize(1);
      assertThat(result.get(0).getParentComment()).isNull();
    }

    @Test
    @DisplayName("コメントがないbacklogItemの一覧を取得したとき、空リストが返る")
    void shouldReturnEmptyListWhenNoComments() {
      // Arrange
      when(backlogItemCommentRepository.findByBacklogItemIdOrderByCreatedAtAsc(ITEM_ID))
          .thenReturn(Collections.emptyList());

      // Act
      List<BacklogItemComment> result = service.findComments(ITEM_ID);

      // Assert
      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("親コメントを持つ返信コメントを取得したとき、parentCommentが設定されている")
    void shouldReturnReplyWithParentCommentSet() {
      // Arrange
      BacklogItem item = createBacklogItem(ITEM_ID, "Item", 50, BacklogItemStatus.SUGGESTED,
          LocalDateTime.of(2026, 1, 1, 0, 0));
      BacklogItemComment parentComment = BacklogItemComment.builder()
          .id(COMMENT_1_ID)
          .backlogItem(item)
          .authorEmployeeId(SnowflakeId.of(1L))
          .authorName("Author")
          .body("Parent comment")
          .createdAt(LocalDateTime.of(2026, 1, 1, 0, 0))
          .build();
      BacklogItemComment replyComment = BacklogItemComment.builder()
          .id(REPLY_ID)
          .backlogItem(item)
          .authorEmployeeId(SnowflakeId.of(2L))
          .authorName("Replier")
          .body("Reply comment")
          .createdAt(LocalDateTime.of(2026, 1, 2, 0, 0))
          .parentComment(parentComment)
          .build();

      when(backlogItemCommentRepository.findByBacklogItemIdOrderByCreatedAtAsc(ITEM_ID))
          .thenReturn(Arrays.asList(parentComment, replyComment));

      // Act
      List<BacklogItemComment> result = service.findComments(ITEM_ID);

      // Assert
      assertThat(result).hasSize(2);
      assertThat(result.get(0).getParentComment()).isNull();
      assertThat(result.get(1).getParentComment()).isEqualTo(parentComment);
    }

    @Test
    @DisplayName("子返信があるコメントを取得したとき、hasRepliesがtrueになる")
    void shouldDetectHasRepliesForCommentWithReplies() {
      // Arrange
      BacklogItem item = createBacklogItem(ITEM_ID, "Item", 50, BacklogItemStatus.SUGGESTED,
          LocalDateTime.of(2026, 1, 1, 0, 0));
      BacklogItemComment parentComment = BacklogItemComment.builder()
          .id(COMMENT_1_ID)
          .backlogItem(item)
          .authorEmployeeId(SnowflakeId.of(1L))
          .authorName("Author")
          .body("Parent comment")
          .createdAt(LocalDateTime.of(2026, 1, 1, 0, 0))
          .build();
      BacklogItemComment replyComment = BacklogItemComment.builder()
          .id(REPLY_ID)
          .backlogItem(item)
          .authorEmployeeId(SnowflakeId.of(2L))
          .authorName("Replier")
          .body("Reply comment")
          .createdAt(LocalDateTime.of(2026, 1, 2, 0, 0))
          .parentComment(parentComment)
          .build();

      when(backlogItemCommentRepository.findByBacklogItemIdOrderByCreatedAtAsc(ITEM_ID))
          .thenReturn(Arrays.asList(parentComment, replyComment));

      // Act
      List<BacklogItemComment> result = service.findComments(ITEM_ID);

      // Assert
      assertThat(result).hasSize(2);
      // 親コメントには子返信がある → hasReplies=true
      assertThat(result.get(0).isHasReplies()).isTrue();
      // 返信コメントには子返信がない → hasReplies=false
      assertThat(result.get(1).isHasReplies()).isFalse();
    }

    @Test
    @DisplayName("子返信がないコメントを取得したとき、hasRepliesがfalseになる")
    void shouldSetHasRepliesFalseWhenNoReplies() {
      // Arrange
      BacklogItem item = createBacklogItem(ITEM_ID, "Item", 50, BacklogItemStatus.SUGGESTED,
          LocalDateTime.of(2026, 1, 1, 0, 0));
      BacklogItemComment comment1 = BacklogItemComment.builder()
          .id(COMMENT_1_ID)
          .backlogItem(item)
          .authorEmployeeId(SnowflakeId.of(1L))
          .authorName("Author1")
          .body("Comment 1")
          .createdAt(LocalDateTime.of(2026, 1, 1, 0, 0))
          .build();
      BacklogItemComment comment2 = BacklogItemComment.builder()
          .id(COMMENT_2_ID)
          .backlogItem(item)
          .authorEmployeeId(SnowflakeId.of(2L))
          .authorName("Author2")
          .body("Comment 2")
          .createdAt(LocalDateTime.of(2026, 1, 2, 0, 0))
          .build();

      when(backlogItemCommentRepository.findByBacklogItemIdOrderByCreatedAtAsc(ITEM_ID))
          .thenReturn(Arrays.asList(comment1, comment2));

      // Act
      List<BacklogItemComment> result = service.findComments(ITEM_ID);

      // Assert
      assertThat(result).hasSize(2);
      // 子返信がないコメントはhasReplies=false
      assertThat(result.get(0).isHasReplies()).isFalse();
      assertThat(result.get(1).isHasReplies()).isFalse();
    }
  }
}
