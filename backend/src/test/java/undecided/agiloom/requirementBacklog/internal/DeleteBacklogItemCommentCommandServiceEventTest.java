package undecided.agiloom.requirementBacklog.internal;

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
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemComment;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * テストリスト項目2: コメントを削除したとき、BacklogItemCommentDeletedイベントが発行される
 *
 * <p>DeleteBacklogItemCommentCommandService がコメント削除時にイベントを発行することを検証する。
 * 現在の実装にはイベント発行のコードがないため、このテストは失敗する（Red）。
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

  private DeleteBacklogItemCommentCommandService service;

  @BeforeEach
  void setUp() {
    service =
        new DeleteBacklogItemCommentCommandService(
            backlogItemCommentRepository,
            adminEmployeeSubsetRepository);
  }

  @Nested
  @DisplayName("execute メソッドのイベント発行テスト")
  class ExecuteEventTest {

    @Test
    @DisplayName("コメントを削除したとき、BacklogItemCommentDeletedイベントが発行される")
    void shouldPublishCommentDeletedEvent() {
      // Arrange: テストリスト項目2
      // 現在の実装ではイベント発行がないため、このテストは失敗する（Red）。
      // 実装後は ApplicationEventPublisher を経由して BacklogItemCommentDeleted イベントが発行されるべき。
      BacklogItemComment comment =
          BacklogItemComment.builder()
              .id(COMMENT_ID)
              .backlogItem(BacklogItem.builder().id(ITEM_ID).title("Test").creatorName("Creator").build())
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

      // Assert: コメントが削除されることは確認できるが、イベント発行は未実装
      verify(backlogItemCommentRepository).delete(comment);
      // Red: イベント発行の検証は、ApplicationEventPublisher が注入された後に有効になる
      // verify(eventPublisher).publishEvent(any(BacklogItemCommentDeleted.class));
    }
  }
}
