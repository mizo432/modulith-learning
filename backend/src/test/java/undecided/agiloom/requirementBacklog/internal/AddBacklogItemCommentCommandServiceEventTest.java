package undecided.agiloom.requirementBacklog.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemComment;
import undecided.agiloom.requirementBacklog.spi.BacklogItemCommentAdded;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

/**
 * テストリスト項目1: コメントを追加したとき、BacklogItemCommentAddedイベントが発行される
 *
 * <p>AddBacklogItemCommentCommandService がコメント追加時にイベントを発行することを検証する。
 */
@Tag("small")
@DisplayName("AddBacklogItemCommentCommandServiceのイベント発行テスト")
@ExtendWith(MockitoExtension.class)
class AddBacklogItemCommentCommandServiceEventTest {

  private static final long ITEM_ID_VALUE = 10L;
  private static final long AUTHOR_ID_VALUE = 100L;
  private static final long COMMENT_ID_VALUE = 1000L;

  private static final SnowflakeId ITEM_ID = SnowflakeId.of(ITEM_ID_VALUE);
  private static final SnowflakeId AUTHOR_ID = SnowflakeId.of(AUTHOR_ID_VALUE);
  private static final SnowflakeId COMMENT_ID = SnowflakeId.of(COMMENT_ID_VALUE);

  @Mock
  private BacklogItemCommentRepository backlogItemCommentRepository;

  @Mock
  private BacklogItemRepository backlogItemRepository;

  @Mock
  private CommentParticipantSubsetRepository commentParticipantSubsetRepository;

  @Mock
  private SnowflakeIdGenerator snowflakeIdGenerator;

  @Mock
  private ApplicationEventPublisher eventPublisher;

  private AddBacklogItemCommentCommandService service;

  @BeforeEach
  void setUp() {
    service =
        new AddBacklogItemCommentCommandService(
            backlogItemRepository,
            backlogItemCommentRepository,
            commentParticipantSubsetRepository,
            snowflakeIdGenerator,
            eventPublisher);
  }

  @Nested
  @DisplayName("execute メソッドのイベント発行テスト")
  class ExecuteEventTest {

    @Test
    @DisplayName("コメントを追加したとき、BacklogItemCommentAddedイベントが発行される")
    void shouldPublishCommentAddedEvent() {
      // Arrange: テストリスト項目1
      BacklogItem item =
          BacklogItem.builder().id(ITEM_ID).title("Test Item").creatorName("Creator").build();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(COMMENT_ID);

      BacklogItemComment savedComment =
          BacklogItemComment.builder()
              .id(COMMENT_ID)
              .backlogItem(item)
              .authorEmployeeId(AUTHOR_ID)
              .authorName("Author")
              .body("Test comment")
              .build();
      when(backlogItemCommentRepository.save(any(BacklogItemComment.class))).thenReturn(savedComment);

      AddBacklogItemCommentCommand command =
          AddBacklogItemCommentCommand.builder()
              .backlogItemId(ITEM_ID)
              .authorEmployeeId(AUTHOR_ID)
              .authorName("Author")
              .authorEmail("author@example.com")
              .body("Test comment")
              .build();

      // Act
      SnowflakeId resultId = service.execute(command);

      // Assert: コメントが保存され、イベントが発行される
      assertThat(resultId).isEqualTo(COMMENT_ID);
      verify(eventPublisher).publishEvent(any(BacklogItemCommentAdded.class));
    }
  }
}
