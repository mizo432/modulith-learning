package undecided.agiloom.requirementBacklog.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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

@Tag("small")
@DisplayName("DeleteBacklogItemCommentCommandServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class DeleteBacklogItemCommentCommandServiceTest {

  private static final long AUTHOR_ID_VALUE = 100L;
  private static final long ADMIN_ID_VALUE = 200L;
  private static final long OTHER_ID_VALUE = 300L;
  private static final long COMMENT_ID_VALUE = 1000L;
  private static final long ITEM_ID_VALUE = 10L;
  private static final long DIFFERENT_ITEM_ID_VALUE = 99L;

  private static final SnowflakeId AUTHOR_ID = SnowflakeId.of(AUTHOR_ID_VALUE);
  private static final SnowflakeId ADMIN_ID = SnowflakeId.of(ADMIN_ID_VALUE);
  private static final SnowflakeId OTHER_ID = SnowflakeId.of(OTHER_ID_VALUE);
  private static final SnowflakeId COMMENT_ID = SnowflakeId.of(COMMENT_ID_VALUE);
  private static final SnowflakeId ITEM_ID = SnowflakeId.of(ITEM_ID_VALUE);
  private static final SnowflakeId DIFFERENT_ITEM_ID = SnowflakeId.of(DIFFERENT_ITEM_ID_VALUE);

  @Mock private BacklogItemCommentRepository backlogItemCommentRepository;

  @Mock private AdminEmployeeSubsetRepository adminEmployeeSubsetRepository;

  private DeleteBacklogItemCommentCommandService service;

  @BeforeEach
  void setUp() {
    service =
        new DeleteBacklogItemCommentCommandService(
            backlogItemCommentRepository, adminEmployeeSubsetRepository);
  }

  private BacklogItemComment comment() {
    return BacklogItemComment.builder()
        .id(COMMENT_ID)
        .backlogItem(BacklogItem.builder().id(ITEM_ID).title("Test").creatorName("Creator").build())
        .authorEmployeeId(AUTHOR_ID)
        .authorName("Author")
        .body("Test comment")
        .build();
  }

  private DeleteBacklogItemCommentCommand command(long actorIdValue) {
    return DeleteBacklogItemCommentCommand.builder()
        .backlogItemId(ITEM_ID)
        .commentId(COMMENT_ID)
        .actorEmployeeId(SnowflakeId.of(actorIdValue))
        .build();
  }

  private DeleteBacklogItemCommentCommand commandNullActor() {
    return DeleteBacklogItemCommentCommand.builder()
        .backlogItemId(ITEM_ID)
        .commentId(COMMENT_ID)
        .build();
  }

  private DeleteBacklogItemCommentCommand commandWithDifferentBacklogId(long actorIdValue) {
    return DeleteBacklogItemCommentCommand.builder()
        .backlogItemId(DIFFERENT_ITEM_ID)
        .commentId(COMMENT_ID)
        .actorEmployeeId(SnowflakeId.of(actorIdValue))
        .build();
  }

  @Nested
  @DisplayName("execute メソッドのテスト")
  class ExecuteTest {

    @Test
    @DisplayName("本人がコメントを削除できること")
    void shouldDeleteByAuthor() {
      // Arrange
      BacklogItemComment comment = comment();
      when(backlogItemCommentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));

      // Act
      service.execute(command(AUTHOR_ID_VALUE));

      // Assert
      verify(backlogItemCommentRepository).delete(comment);
    }

    @Test
    @DisplayName("ADMIN保持者がコメントを削除できること")
    void shouldDeleteByAdmin() {
      // Arrange
      BacklogItemComment comment = comment();
      when(backlogItemCommentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));
      when(adminEmployeeSubsetRepository.existsByEmployeeId(ADMIN_ID)).thenReturn(true);

      // Act
      service.execute(command(ADMIN_ID_VALUE));

      // Assert
      verify(backlogItemCommentRepository).delete(comment);
    }

    @Test
    @DisplayName("第三者がコメントを削除しようとすると権限例外になること")
    void shouldDenyByOther() {
      // Arrange
      BacklogItemComment comment = comment();
      when(backlogItemCommentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));
      when(adminEmployeeSubsetRepository.existsByEmployeeId(OTHER_ID)).thenReturn(false);

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command(OTHER_ID_VALUE)))
          .isInstanceOf(BacklogItemPermissionDeniedException.class);
      verify(backlogItemCommentRepository, never()).delete(any());
    }

    @Test
    @DisplayName("存在しないコメントを削除しようとすると引数例外になること")
    void shouldThrowWhenCommentNotFound() {
      // Arrange
      when(backlogItemCommentRepository.findById(COMMENT_ID)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command(AUTHOR_ID_VALUE)))
          .isInstanceOf(IllegalArgumentException.class);
      verify(backlogItemCommentRepository, never()).delete(any());
    }

    @Test
    @DisplayName("actorEmployeeIdがnullの場合に引数例外になること")
    void shouldThrowWhenActorEmployeeIdIsNull() {
      // Arrange & Act & Assert
      assertThatThrownBy(() -> service.execute(commandNullActor()))
          .isInstanceOf(IllegalArgumentException.class);
      verify(backlogItemCommentRepository, never()).delete(any());
    }

    @Test
    @DisplayName("コメントがコマンドのbacklogItemIdに属さない場合に引数例外になること")
    void shouldThrowWhenCommentDoesNotBelongToBacklogItem() {
      // Arrange
      BacklogItemComment comment = comment();
      when(backlogItemCommentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));

      // Act & Assert
      assertThatThrownBy(() -> service.execute(commandWithDifferentBacklogId(AUTHOR_ID_VALUE)))
          .isInstanceOf(IllegalArgumentException.class);
      verify(backlogItemCommentRepository, never()).delete(any());
    }

    @Test
    @DisplayName("子返信があるコメントを削除しようとすると、例外がスローされる")
    void shouldDenyDeleteWhenHasReplies() {
      // Arrange
      BacklogItemComment comment = comment();
      when(backlogItemCommentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));
      // 子返信が存在する
      BacklogItemComment reply = BacklogItemComment.builder()
          .id(SnowflakeId.of(2000L))
          .backlogItem(BacklogItem.builder().id(ITEM_ID).title("Test").creatorName("Creator").build())
          .authorEmployeeId(SnowflakeId.of(400L))
          .authorName("Replier")
          .body("Reply comment")
          .build();
      when(backlogItemCommentRepository.findByParentCommentId(COMMENT_ID)).thenReturn(java.util.List.of(reply));

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command(AUTHOR_ID_VALUE)))
          .isInstanceOf(IllegalArgumentException.class);
      verify(backlogItemCommentRepository, never()).delete(any());
    }
  }
}
