package undecided.agiloom.requirementBacklog.internal;
import java.time.LocalDateTime;
import java.time.Clock;

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
import undecided.agiloom.requirementBacklog.spi.BacklogItemCommentDeleted;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * テストリスト項目2: コメントを削除したとき、BacklogItemCommentDeletedイベントが発行される
 *
 * <p>DeleteBacklogItemCommentCommandService がコメント削除時にイベントを発行することを検証する。
 */
@Tag("small")
@DisplayName("DeleteBacklogItemCommentCommandServiceのイベント発行テスト")
@ExtendWith(MockitoExtension.class)
class DeleteBacklogItemCommentCommandServiceEventTest {

  private static final long AUTHOR_ID_VALUE = 100L;
  private static final long COMMENT_ID_VALUE = 1000L;
  private static final long ITEM_ID_VALUE = 10L;

  private static final SnowflakeId AUTHOR_ID = SnowflakeId.of(AUTHOR_ID_VALUE);
  private static final SnowflakeId COMMENT_ID = SnowflakeId.of(COMMENT_ID_VALUE);
  private static final SnowflakeId ITEM_ID = SnowflakeId.of(ITEM_ID_VALUE);

  @Mock
  private BacklogItemCommentRepository backlogItemCommentRepository;

  @Mock
  private AdminEmployeeSubsetRepository adminEmployeeSubsetRepository;

  @Mock
  private ApplicationEventPublisher eventPublisher;

  private DeleteBacklogItemCommentCommandService service;

  @BeforeEach
  void setUp() {
    service =
        new DeleteBacklogItemCommentCommandService(
            backlogItemCommentRepository,
            adminEmployeeSubsetRepository,
            eventPublisher,
            Clock.systemDefaultZone());
  }

  @Nested
  @DisplayName("execute メソッドのイベント発行テスト")
  class ExecuteEventTest {

    @Test
    @DisplayName("コメントを削除したとき、BacklogItemCommentDeletedイベントが発行される")
    void shouldPublishCommentDeletedEvent() {
      // Arrange: テストリスト項目2
      BacklogItemComment comment =
          BacklogItemComment.builder()
              .id(COMMENT_ID)
              .backlogItem(BacklogItem.builder().id(ITEM_ID).title("Test").creatorName("Creator").createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build())
              .authorEmployeeId(AUTHOR_ID)
              .authorName("Author")
              .body("Test comment")
              .build();
      when(backlogItemCommentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));

      DeleteBacklogItemCommentCommand command =
          DeleteBacklogItemCommentCommand.builder()
              .backlogItemId(ITEM_ID)
              .commentId(COMMENT_ID)
              .actorEmployeeId(AUTHOR_ID)
              .build();

      // Act
      service.execute(command);

      // Assert: コメントが削除され、イベントが発行される
      verify(backlogItemCommentRepository).delete(comment);
      verify(eventPublisher).publishEvent(any(BacklogItemCommentDeleted.class));
    }
  }
}
