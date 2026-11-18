package undecided.agiloom.requirementBacklog.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
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
@DisplayName("UpdateBacklogItemCommentCommandServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class UpdateBacklogItemCommentCommandServiceTest {

  private static final long AUTHOR_ID_VALUE = 100L;
  private static final long ADMIN_ID_VALUE = 200L;
  private static final long OTHER_ID_VALUE = 300L;
  private static final long COMMENT_ID_VALUE = 1000L;
  private static final long ITEM_ID_VALUE = 10L;

  private static final SnowflakeId AUTHOR_ID = SnowflakeId.of(AUTHOR_ID_VALUE);
  private static final SnowflakeId ADMIN_ID = SnowflakeId.of(ADMIN_ID_VALUE);
  private static final SnowflakeId OTHER_ID = SnowflakeId.of(OTHER_ID_VALUE);
  private static final SnowflakeId COMMENT_ID = SnowflakeId.of(COMMENT_ID_VALUE);
  private static final SnowflakeId ITEM_ID = SnowflakeId.of(ITEM_ID_VALUE);

  @Mock private BacklogItemCommentRepository backlogItemCommentRepository;

  @Mock private AdminEmployeeSubsetRepository adminEmployeeSubsetRepository;

  private UpdateBacklogItemCommentCommandService service;

  @BeforeEach
  void setUp() {
    service =
        new UpdateBacklogItemCommentCommandService(
            backlogItemCommentRepository, adminEmployeeSubsetRepository);
  }

  private BacklogItemComment comment() {
    return BacklogItemComment.builder()
        .id(COMMENT_ID)
        .backlogItem(BacklogItem.builder().id(ITEM_ID).title("Test").creatorName("Creator").build())
        .authorEmployeeId(AUTHOR_ID)
        .authorName("Author")
        .body("Original body")
        .build();
  }

  private UpdateBacklogItemCommentCommand command(long actorIdValue, String body) {
    return UpdateBacklogItemCommentCommand.builder()
        .commentId(COMMENT_ID)
        .body(body)
        .actorEmployeeId(SnowflakeId.of(actorIdValue))
        .build();
  }

  private UpdateBacklogItemCommentCommand commandNullActor(String body) {
    return UpdateBacklogItemCommentCommand.builder()
        .commentId(COMMENT_ID)
        .body(body)
        .build();
  }

  @Nested
  @DisplayName("execute メソッドのテスト")
  class ExecuteTest {

    @Test
    @DisplayName("本人がコメントを編集できること")
    void shouldUpdateByAuthor() {
      // Arrange
      BacklogItemComment comment = comment();
      when(backlogItemCommentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));

      // Act
      service.execute(command(AUTHOR_ID_VALUE, "Updated body"));

      // Assert
      // 本人がコメントを編集すると、本文が更新されて保存される
      verify(backlogItemCommentRepository).save(comment);
      assertThat(comment.getBody()).isEqualTo("Updated body");
    }

    @Test
    @DisplayName("ADMIN保持者がコメントを編集できること")
    void shouldUpdateByAdmin() {
      // Arrange
      BacklogItemComment comment = comment();
      when(backlogItemCommentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));
      when(adminEmployeeSubsetRepository.existsByEmployeeId(ADMIN_ID)).thenReturn(true);

      // Act
      service.execute(command(ADMIN_ID_VALUE, "Updated by admin"));

      // Assert
      // ADMIN保持者がコメントを編集すると、本文が更新されて保存される
      verify(backlogItemCommentRepository).save(comment);
      assertThat(comment.getBody()).isEqualTo("Updated by admin");
    }

    @Test
    @DisplayName("第三者がコメントを編集しようとすると権限例外になること")
    void shouldDenyByOther() {
      // Arrange
      BacklogItemComment comment = comment();
      when(backlogItemCommentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));
      when(adminEmployeeSubsetRepository.existsByEmployeeId(OTHER_ID)).thenReturn(false);

      // Act & Assert
      // 第三者がコメントを編集しようとすると BacklogItemPermissionDeniedException がスローされる
      assertThatThrownBy(() -> service.execute(command(OTHER_ID_VALUE, "Hacked body")))
          .isInstanceOf(BacklogItemPermissionDeniedException.class);
      verify(backlogItemCommentRepository, never()).save(any());
    }

    @Test
    @DisplayName("子返信があるコメントを編集しようとすると例外がスローされること")
    void shouldDenyWhenHasReplies() {
      // Arrange
      BacklogItemComment comment = comment();
      when(backlogItemCommentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));
      BacklogItemComment reply = BacklogItemComment.builder()
          .id(SnowflakeId.of(2000L))
          .backlogItem(BacklogItem.builder().id(ITEM_ID).title("Test").creatorName("Creator").build())
          .authorEmployeeId(SnowflakeId.of(400L))
          .authorName("Replier")
          .body("Reply comment")
          .build();
      when(backlogItemCommentRepository.findByParentCommentId(COMMENT_ID)).thenReturn(List.of(reply));

      // Act & Assert
      // 子返信があるコメントを編集しようとすると IllegalStateException がスローされる
      assertThatThrownBy(() -> service.execute(command(AUTHOR_ID_VALUE, "Updated body")))
          .isInstanceOf(IllegalStateException.class);
      verify(backlogItemCommentRepository, never()).save(any());
    }

    @Test
    @DisplayName("存在しないコメントを編集しようとすると引数例外になること")
    void shouldThrowWhenCommentNotFound() {
      // Arrange & Act & Assert
      // 存在しないコメントを編集しようとすると IllegalArgumentException がスローされる
      assertThatThrownBy(() -> service.execute(command(AUTHOR_ID_VALUE, "Updated body")))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("本文が空で編集しようとすると引数例外になること")
    void shouldThrowWhenBodyIsEmpty() {
      // Arrange & Act & Assert
      // 本文が空で編集しようとすると IllegalArgumentException がスローされる
      assertThatThrownBy(() -> service.execute(command(AUTHOR_ID_VALUE, "")))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("actorEmployeeIdがnullのとき、引数例外がスローされること")
    void shouldThrowWhenActorEmployeeIdIsNull() {
      // Arrange & Act & Assert
      // actorEmployeeIdがnullのとき IllegalArgumentException がスローされる
      assertThatThrownBy(() -> service.execute(commandNullActor("Updated body")))
          .isInstanceOf(IllegalArgumentException.class);
    }
  }
}
