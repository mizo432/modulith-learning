package undecided.agiloom.requirementBacklog.internal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemComment;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@ExtendWith(MockitoExtension.class)
@DisplayName("BacklogItemCommandControllerのコメント関連APIテスト")
class BacklogItemCommandControllerTest {

  private MockMvc mockMvc;

  @Mock private AddBacklogItemCommentCommandService addBacklogItemCommentCommandService;

  @Mock private DeleteBacklogItemCommentCommandService deleteBacklogItemCommentCommandService;

  @Mock private UpdateBacklogItemCommentCommandService updateBacklogItemCommentCommandService;

  @Mock private BacklogItemQueryService backlogItemQueryService;

  @Mock private BacklogItemResponseConverter backlogItemResponseConverter;

  @InjectMocks private BacklogItemCommandController controller;

  private static final SnowflakeId ITEM_ID = SnowflakeId.of(10L);
  private static final SnowflakeId COMMENT_ID = SnowflakeId.of(1000L);
  private static final SnowflakeId PARENT_ID = SnowflakeId.of(2000L);
  private static final SnowflakeId AUTHOR_ID = SnowflakeId.of(100L);

  private BacklogItemComment comment;
  private BacklogItemCommentResponse commentResponse;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

    BacklogItem item = BacklogItem.builder()
        .id(ITEM_ID)
        .title("Test Item")
        .creatorName("Creator")
        .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

    comment = BacklogItemComment.builder()
        .id(COMMENT_ID)
        .backlogItem(item)
        .authorEmployeeId(AUTHOR_ID)
        .authorName("Author")
        .body("Test comment")
        .createdAt(LocalDateTime.now()).build();

    commentResponse = BacklogItemCommentResponse.builder()
        .id(COMMENT_ID)
        .backlogItemId(String.valueOf(ITEM_ID.value()))
        .authorEmployeeId(AUTHOR_ID)
        .authorName("Author")
        .body("Test comment")
        .parentId(null)
        .hasReplies(false)
        .build();
  }

  @Nested
  @DisplayName("POST /api/v1/backlog-items/{id}/comments のテスト")
  class AddCommentTest {

    @Test
    @DisplayName("親コメントIDを指定して返信を追加できる")
    void shouldAddCommentWithParent() throws Exception {
      when(addBacklogItemCommentCommandService.execute(any())).thenReturn(COMMENT_ID);
      when(backlogItemQueryService.findComments(ITEM_ID)).thenReturn(List.of(comment));
      when(backlogItemResponseConverter.toCommentResponse(comment)).thenReturn(commentResponse);

      String requestBody =
          "{\"authorEmployeeId\":\"" + AUTHOR_ID.value()
          + "\",\"authorName\":\"Author\",\"body\":\"Reply\",\"parentCommentId\":\"" + PARENT_ID.value() + "\"}";

      mockMvc.perform(
              MockMvcRequestBuilders.post("/api/v1/backlog-items/" + ITEM_ID.value() + "/comments")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(requestBody))
          .andExpect(MockMvcResultMatchers.status().isOk());

      verify(addBacklogItemCommentCommandService).execute(any());
    }
  }

  @Nested
  @DisplayName("PATCH /api/v1/backlog-items/{id}/comments/{commentId} のテスト")
  class UpdateCommentTest {

    @Test
    @DisplayName("コメントをPATCHで編集できる")
    void shouldUpdateComment() throws Exception {
      when(backlogItemQueryService.findComments(ITEM_ID)).thenReturn(List.of(comment));
      when(backlogItemResponseConverter.toCommentResponse(comment)).thenReturn(commentResponse);

      String requestBody =
          "{\"actorEmployeeId\":\"" + AUTHOR_ID.value() + "\",\"body\":\"Updated body\"}";

      mockMvc.perform(
              MockMvcRequestBuilders.patch(
                  "/api/v1/backlog-items/" + ITEM_ID.value() + "/comments/" + COMMENT_ID.value())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(requestBody))
          .andExpect(MockMvcResultMatchers.status().isOk());

      verify(updateBacklogItemCommentCommandService).execute(any());
    }
  }

  @Nested
  @DisplayName("DELETE /api/v1/backlog-items/{id}/comments/{commentId} のテスト")
  class DeleteCommentTest {

    @Test
    @DisplayName("コメントをDELETEで削除できる")
    void shouldDeleteComment() throws Exception {
      String requestBody = "{\"actorEmployeeId\":\"" + AUTHOR_ID.value() + "\"}";

      mockMvc.perform(
              MockMvcRequestBuilders.delete(
                  "/api/v1/backlog-items/" + ITEM_ID.value() + "/comments/" + COMMENT_ID.value())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(requestBody))
          .andExpect(MockMvcResultMatchers.status().isNoContent());

      verify(deleteBacklogItemCommentCommandService).execute(any());
    }
  }
}
