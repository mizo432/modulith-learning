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
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemComment;
import undecided.agiloom.requirementBacklog.spi.BacklogItemCommentAdded;
import undecided.association.relationshipMgmt.personRole.spi.employee.EmployeeEmailQuery;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

@Tag("small")
@DisplayName("AddBacklogItemCommentCommandServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class AddBacklogItemCommentCommandServiceTest {

  private static final long ITEM_ID_VALUE = 10L;
  private static final long DIFFERENT_ITEM_ID_VALUE = 99L;
  private static final long AUTHOR_ID_VALUE = 100L;
  private static final long COMMENT_ID_VALUE = 1000L;
  private static final long PARENT_COMMENT_ID_VALUE = 2000L;
  private static final long NEW_COMMENT_ID_VALUE = 3000L;

  private static final SnowflakeId ITEM_ID = SnowflakeId.of(ITEM_ID_VALUE);
  private static final SnowflakeId DIFFERENT_ITEM_ID = SnowflakeId.of(DIFFERENT_ITEM_ID_VALUE);
  private static final SnowflakeId AUTHOR_ID = SnowflakeId.of(AUTHOR_ID_VALUE);
  private static final SnowflakeId COMMENT_ID = SnowflakeId.of(COMMENT_ID_VALUE);
  private static final SnowflakeId PARENT_COMMENT_ID = SnowflakeId.of(PARENT_COMMENT_ID_VALUE);
  private static final SnowflakeId NEW_COMMENT_ID = SnowflakeId.of(NEW_COMMENT_ID_VALUE);

  @Mock private BacklogItemRepository backlogItemRepository;

  @Mock private BacklogItemCommentRepository backlogItemCommentRepository;

  @Mock private SnowflakeIdGenerator snowflakeIdGenerator;

  @Mock private CommentParticipantSubsetRepository commentParticipantSubsetRepository;

  @Mock private ApplicationEventPublisher eventPublisher;

  @Mock private EmployeeEmailQuery employeeEmailQuery;

  private AddBacklogItemCommentCommandService service;

  @BeforeEach
  void setUp() {
    service =
        new AddBacklogItemCommentCommandService(
            backlogItemRepository,
            backlogItemCommentRepository,
            commentParticipantSubsetRepository,
            snowflakeIdGenerator,
            eventPublisher,
            employeeEmailQuery);
  }

  private BacklogItem backlogItem() {
    return BacklogItem.builder()
        .id(ITEM_ID)
        .title("Test Item")
        .creatorName("Creator")
        .build();
  }

  private BacklogItem differentBacklogItem() {
    return BacklogItem.builder()
        .id(DIFFERENT_ITEM_ID)
        .title("Different Item")
        .creatorName("Creator")
        .build();
  }

  private BacklogItemComment parentComment() {
    return BacklogItemComment.builder()
        .id(PARENT_COMMENT_ID)
        .backlogItem(backlogItem())
        .authorEmployeeId(AUTHOR_ID)
        .authorName("Parent Author")
        .body("Parent comment")
        .build();
  }

  private AddBacklogItemCommentCommand command() {
    return AddBacklogItemCommentCommand.builder()
        .backlogItemId(ITEM_ID)
        .authorEmployeeId(AUTHOR_ID)
        .authorName("Author")
        .body("Test comment")
        .build();
  }

  private AddBacklogItemCommentCommand commandWithParent() {
    return AddBacklogItemCommentCommand.builder()
        .backlogItemId(ITEM_ID)
        .authorEmployeeId(AUTHOR_ID)
        .authorName("Author")
        .body("Reply comment")
        .parentCommentId(PARENT_COMMENT_ID)
        .build();
  }

  @Nested
  @DisplayName("execute メソッドのテスト")
  class ExecuteTest {

    @Test
    @DisplayName("親なしでコメントを追加したとき、親なしのコメントが保存される")
    void shouldSaveCommentWithoutParent() {
      // Arrange
      BacklogItem item = backlogItem();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(NEW_COMMENT_ID);
      when(backlogItemCommentRepository.save(any(BacklogItemComment.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      // Act
      SnowflakeId result = service.execute(command());

      // Assert
      assertThat(result).isEqualTo(NEW_COMMENT_ID);
      ArgumentCaptor<BacklogItemComment> captor = ArgumentCaptor.forClass(BacklogItemComment.class);
      verify(backlogItemCommentRepository).save(captor.capture());
      assertThat(captor.getValue().getParentComment()).isNull();
    }

    @Test
    @DisplayName("親コメントを指定して返信を追加したとき、親コメントが設定されたコメントが保存される")
    void shouldSaveReplyWithParentComment() {
      // Arrange
      BacklogItem item = backlogItem();
      BacklogItemComment parent = parentComment();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(backlogItemCommentRepository.findById(PARENT_COMMENT_ID)).thenReturn(Optional.of(parent));
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(NEW_COMMENT_ID);
      when(backlogItemCommentRepository.save(any(BacklogItemComment.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      // Act
      SnowflakeId result = service.execute(commandWithParent());

      // Assert
      assertThat(result).isEqualTo(NEW_COMMENT_ID);
      ArgumentCaptor<BacklogItemComment> captor = ArgumentCaptor.forClass(BacklogItemComment.class);
      verify(backlogItemCommentRepository).save(captor.capture());
      assertThat(captor.getValue().getParentComment()).isEqualTo(parent);
    }

    @Test
    @DisplayName("存在しない親コメントIDを指定したとき、IllegalArgumentExceptionがスローされる")
    void shouldThrowWhenParentCommentNotFound() {
      // Arrange
      when(backlogItemCommentRepository.findById(PARENT_COMMENT_ID)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> service.execute(commandWithParent()))
          .isInstanceOf(IllegalArgumentException.class);
      verify(backlogItemCommentRepository, never()).save(any());
    }

    @Test
    @DisplayName("異なるバックログアイテムのコメントを親としたとき、IllegalArgumentExceptionがスローされる")
    void shouldThrowWhenParentBelongsToDifferentBacklogItem() {
      // Arrange
      BacklogItemComment parentOfDifferentItem = BacklogItemComment.builder()
          .id(PARENT_COMMENT_ID)
          .backlogItem(differentBacklogItem())
          .authorEmployeeId(AUTHOR_ID)
          .authorName("Other Author")
          .body("Other parent comment")
          .build();
      when(backlogItemCommentRepository.findById(PARENT_COMMENT_ID))
          .thenReturn(Optional.of(parentOfDifferentItem));

      // Act & Assert
      assertThatThrownBy(() -> service.execute(commandWithParent()))
          .isInstanceOf(IllegalArgumentException.class);
      verify(backlogItemCommentRepository, never()).save(any());
    }

    @Test
    @DisplayName("本文が空のとき、IllegalArgumentExceptionがスローされる")
    void shouldThrowWhenBodyIsEmpty() {
      // Arrange
      AddBacklogItemCommentCommand emptyBodyCommand = AddBacklogItemCommentCommand.builder()
          .backlogItemId(ITEM_ID)
          .authorEmployeeId(AUTHOR_ID)
          .authorName("Author")
          .body("")
          .build();

      // Act & Assert
      assertThatThrownBy(() -> service.execute(emptyBodyCommand))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("must not be empty");
      verify(backlogItemCommentRepository, never()).save(any());
    }

    @Test
    @DisplayName("投稿者名が空のとき、IllegalArgumentExceptionがスローされる")
    void shouldThrowWhenAuthorNameIsEmpty() {
      // Arrange
      AddBacklogItemCommentCommand emptyAuthorCommand = AddBacklogItemCommentCommand.builder()
          .backlogItemId(ITEM_ID)
          .authorEmployeeId(AUTHOR_ID)
          .authorName("")
          .body("Test comment")
          .build();

      // Act & Assert
      assertThatThrownBy(() -> service.execute(emptyAuthorCommand))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("must not be empty");
      verify(backlogItemCommentRepository, never()).save(any());
    }

    @Test
    @DisplayName("バックログアイテムが存在しないとき、IllegalArgumentExceptionがスローされる")
    void shouldThrowWhenBacklogItemNotFound() {
      // Arrange
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command()))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("BacklogItem not found");
      verify(backlogItemCommentRepository, never()).save(any());
    }

    @Test
    @DisplayName("自身を親としたとき、IllegalArgumentExceptionがスローされる")
    void shouldThrowWhenSelfAsParent() {
      // Arrange
      BacklogItem item = backlogItem();
      BacklogItemComment parent = parentComment();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(backlogItemCommentRepository.findById(PARENT_COMMENT_ID)).thenReturn(Optional.of(parent));
      // 採番されたIDが親コメントのIDと一致するケース
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(PARENT_COMMENT_ID);

      // Act & Assert
      assertThatThrownBy(() -> service.execute(commandWithParent()))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("自身");
      verify(backlogItemCommentRepository, never()).save(any());
    }

    // Issue #685: テスト項目17
    @Test
    @DisplayName("投稿者IDがありメールが解決できる場合でコメントを投稿したとき、authorEmailが解決された値で保存される")
    void shouldSaveResolvedEmailWhenAuthorIdPresentAndEmailResolvable() {
      // Arrange
      BacklogItem item = backlogItem();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(NEW_COMMENT_ID);
      when(backlogItemCommentRepository.save(any(BacklogItemComment.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));
      when(employeeEmailQuery.resolveEmailByEmployeeId(AUTHOR_ID))
          .thenReturn(Optional.of("taro@example.com"));

      AddBacklogItemCommentCommand cmd = AddBacklogItemCommentCommand.builder()
          .backlogItemId(ITEM_ID)
          .authorEmployeeId(AUTHOR_ID)
          .authorName("Author")
          .body("Test comment")
          .build();

      // Act
      service.execute(cmd);

      // Assert
      ArgumentCaptor<CommentParticipantSubset> captor = ArgumentCaptor.forClass(CommentParticipantSubset.class);
      verify(commentParticipantSubsetRepository).save(captor.capture());
      assertThat(captor.getValue().getEmployeeEmail()).isEqualTo("taro@example.com");
    }

    // Issue #685: テスト項目18
    @Test
    @DisplayName("投稿者IDがありメールが解決できない場合でコメントを投稿したとき、authorEmailが空文字で保存される")
    void shouldSaveEmptyEmailWhenAuthorIdPresentButEmailNotResolvable() {
      // Arrange
      BacklogItem item = backlogItem();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(NEW_COMMENT_ID);
      when(backlogItemCommentRepository.save(any(BacklogItemComment.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));
      when(employeeEmailQuery.resolveEmailByEmployeeId(AUTHOR_ID))
          .thenReturn(Optional.empty());

      AddBacklogItemCommentCommand cmd = AddBacklogItemCommentCommand.builder()
          .backlogItemId(ITEM_ID)
          .authorEmployeeId(AUTHOR_ID)
          .authorName("Author")
          .body("Test comment")
          .build();

      // Act
      service.execute(cmd);

      // Assert
      ArgumentCaptor<CommentParticipantSubset> captor = ArgumentCaptor.forClass(CommentParticipantSubset.class);
      verify(commentParticipantSubsetRepository).save(captor.capture());
      assertThat(captor.getValue().getEmployeeEmail()).isEqualTo("");
    }

    // Issue #685: テスト項目19
    @Test
    @DisplayName("投稿者IDがnullでコメントを投稿したとき、authorEmailが空文字でイベントに含められる")
    void shouldIncludeEmptyEmailInEventWhenAuthorIdIsNull() {
      // Arrange
      BacklogItem item = backlogItem();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(NEW_COMMENT_ID);
      when(backlogItemCommentRepository.save(any(BacklogItemComment.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      AddBacklogItemCommentCommand cmd = AddBacklogItemCommentCommand.builder()
          .backlogItemId(ITEM_ID)
          .authorEmployeeId(null)
          .authorName("Author")
          .body("Test comment")
          .build();

      // Act
      service.execute(cmd);

      // Assert: 投稿者IDがnullの場合はCommentParticipantSubsetは保存されないが、
      // イベントには空文字のauthorEmailが含まれる
      verify(commentParticipantSubsetRepository, never()).save(any(CommentParticipantSubset.class));
      ArgumentCaptor<BacklogItemCommentAdded> eventCaptor =
          ArgumentCaptor.forClass(BacklogItemCommentAdded.class);
      verify(eventPublisher).publishEvent(eventCaptor.capture());
      assertThat(eventCaptor.getValue().authorEmail()).isEqualTo("");
    }
  }
}
