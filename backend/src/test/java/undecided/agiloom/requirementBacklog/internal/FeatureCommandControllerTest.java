package undecided.agiloom.requirementBacklog.internal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
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
import undecided.agiloom.requirementBacklog.spi.Feature;
import undecided.agiloom.requirementBacklog.spi.FeatureStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@ExtendWith(MockitoExtension.class)
@DisplayName("FeatureCommandControllerのテスト")
class FeatureCommandControllerTest {

  private MockMvc mockMvc;

  @Mock private CreateFeatureCommandService createFeatureCommandService;
  @Mock private UpdateFeatureCommandService updateFeatureCommandService;
  @Mock private DeleteFeatureCommandService deleteFeatureCommandService;
  @Mock private AssignBacklogItemToFeatureCommandService assignBacklogItemToFeatureCommandService;
  @Mock private RemoveBacklogItemFromFeatureCommandService removeBacklogItemFromFeatureCommandService;
  @Mock private FeatureQueryService featureQueryService;

  private FeatureResponseConverter featureResponseConverter;

  private static final SnowflakeId FEATURE_ID = SnowflakeId.of(100L);
  private static final SnowflakeId SCRUM_TEAM_ID = SnowflakeId.of(10L);
  private static final SnowflakeId ACTOR_EMPLOYEE_ID = SnowflakeId.of(1L);
  private static final SnowflakeId BACKLOG_ITEM_ID = SnowflakeId.of(200L);
  private static final SnowflakeId OTHER_FEATURE_ID = SnowflakeId.of(300L);

  private Feature feature;

  @BeforeEach
  void setUp() {
    featureResponseConverter = new FeatureResponseConverter();
    mockMvc = MockMvcBuilders.standaloneSetup(new FeatureCommandController(
            createFeatureCommandService,
            updateFeatureCommandService,
            deleteFeatureCommandService,
            assignBacklogItemToFeatureCommandService,
            removeBacklogItemFromFeatureCommandService,
            featureQueryService,
            featureResponseConverter))
        .setControllerAdvice(new FeatureExceptionHandler())
        .build();

    feature = Feature.builder()
        .id(FEATURE_ID)
        .name("Test Feature")
        .description("Test Description")
        .status(FeatureStatus.PLANNED)
        .scrumTeamId(SCRUM_TEAM_ID)
        .createdAt(LocalDateTime.of(2026, 1, 1, 0, 0))
        .updatedAt(LocalDateTime.of(2026, 1, 1, 0, 0))
        .build();
  }

  @Nested
  @DisplayName("POST /api/v1/features のテスト")
  class CreateFeatureTest {

    @Test
    @DisplayName("有効なリクエストでフィーチャーを作成したとき、201が返される")
    void shouldCreateFeature() throws Exception {
      when(createFeatureCommandService.execute(any())).thenReturn(FEATURE_ID);
      when(featureQueryService.findById(FEATURE_ID)).thenReturn(java.util.Optional.of(feature));

      String requestBody =
          "{\"name\":\"New Feature\",\"description\":\"Desc\","
          + "\"scrumTeamId\":\"" + SCRUM_TEAM_ID.value()
          + "\",\"actorEmployeeId\":\"" + ACTOR_EMPLOYEE_ID.value() + "\"}";

      mockMvc.perform(
              MockMvcRequestBuilders.post("/api/v1/features")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(requestBody))
          .andExpect(MockMvcResultMatchers.status().isCreated());

      verify(createFeatureCommandService).execute(any());
    }

    @Test
    @DisplayName("権限がないユーザーがフィーチャーを作成したとき、403が返される")
    void shouldReturn403WhenNoPermission() throws Exception {
      when(createFeatureCommandService.execute(any()))
          .thenThrow(new BacklogItemPermissionDeniedException("Not a team member"));

      String requestBody =
          "{\"name\":\"New Feature\",\"description\":\"Desc\","
          + "\"scrumTeamId\":\"" + SCRUM_TEAM_ID.value()
          + "\",\"actorEmployeeId\":\"" + ACTOR_EMPLOYEE_ID.value() + "\"}";

      mockMvc.perform(
              MockMvcRequestBuilders.post("/api/v1/features")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(requestBody))
          .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("名前が空でフィーチャーを作成したとき、400が返される")
    void shouldReturn400WhenNameEmpty() throws Exception {
      String requestBody =
          "{\"name\":\"\",\"description\":\"Desc\","
          + "\"scrumTeamId\":\"" + SCRUM_TEAM_ID.value()
          + "\",\"actorEmployeeId\":\"" + ACTOR_EMPLOYEE_ID.value() + "\"}";

      mockMvc.perform(
              MockMvcRequestBuilders.post("/api/v1/features")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(requestBody))
          .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
  }

  @Nested
  @DisplayName("PUT /api/v1/features/{id} のテスト")
  class UpdateFeatureTest {

    @Test
    @DisplayName("有効なリクエストでフィーチャーを更新したとき、200が返される")
    void shouldUpdateFeature() throws Exception {
      doAnswer(invocation -> null).when(updateFeatureCommandService).execute(any());
      when(featureQueryService.findById(FEATURE_ID)).thenReturn(java.util.Optional.of(feature));

      String requestBody =
          "{\"name\":\"Updated Feature\","
          + "\"scrumTeamId\":\"" + SCRUM_TEAM_ID.value()
          + "\",\"actorEmployeeId\":\"" + ACTOR_EMPLOYEE_ID.value() + "\"}";

      mockMvc.perform(
              MockMvcRequestBuilders.put("/api/v1/features/" + FEATURE_ID.value())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(requestBody))
          .andExpect(MockMvcResultMatchers.status().isOk());

      verify(updateFeatureCommandService).execute(any());
    }

    @Test
    @DisplayName("許可されないステータス遷移で更新したとき、409が返される")
    void shouldReturn409WhenInvalidStatusTransition() throws Exception {
      doThrow(new IllegalStateException("Invalid status transition"))
          .when(updateFeatureCommandService).execute(any());

      String requestBody =
          "{\"status\":\"DONE\","
          + "\"scrumTeamId\":\"" + SCRUM_TEAM_ID.value()
          + "\",\"actorEmployeeId\":\"" + ACTOR_EMPLOYEE_ID.value() + "\"}";

      mockMvc.perform(
              MockMvcRequestBuilders.put("/api/v1/features/" + FEATURE_ID.value())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(requestBody))
          .andExpect(MockMvcResultMatchers.status().isConflict());
    }
  }

  @Nested
  @DisplayName("DELETE /api/v1/features/{id} のテスト")
  class DeleteFeatureTest {

    @Test
    @DisplayName("存在するフィーチャーを削除したとき、204が返される")
    void shouldDeleteFeature() throws Exception {
      doAnswer(invocation -> null).when(deleteFeatureCommandService).execute(any());

      String requestBody =
          "{\"scrumTeamId\":\"" + SCRUM_TEAM_ID.value()
          + "\",\"actorEmployeeId\":\"" + ACTOR_EMPLOYEE_ID.value() + "\"}";

      mockMvc.perform(
              MockMvcRequestBuilders.delete("/api/v1/features/" + FEATURE_ID.value())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(requestBody))
          .andExpect(MockMvcResultMatchers.status().isNoContent());

      verify(deleteFeatureCommandService).execute(any());
    }
  }

  @Nested
  @DisplayName("POST /api/v1/features/{featureId}/backlog-items/{backlogItemId} のテスト")
  class AssignBacklogItemTest {

    @Test
    @DisplayName("バックログアイテムをフィーチャーに割り当てたとき、204が返される")
    void shouldAssignBacklogItem() throws Exception {
      doAnswer(invocation -> null).when(assignBacklogItemToFeatureCommandService).execute(any());

      String requestBody =
          "{\"actorEmployeeId\":\"" + ACTOR_EMPLOYEE_ID.value()
          + "\",\"scrumTeamId\":\"" + SCRUM_TEAM_ID.value() + "\"}";

      mockMvc.perform(
              MockMvcRequestBuilders.post(
                  "/api/v1/features/" + FEATURE_ID.value() + "/backlog-items/" + BACKLOG_ITEM_ID.value())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(requestBody))
          .andExpect(MockMvcResultMatchers.status().isNoContent());

      verify(assignBacklogItemToFeatureCommandService).execute(any());
    }

    @Test
    @DisplayName("別のフィーチャーに割り当て済みのバックログアイテムを割り当てたとき、409が返される")
    void shouldReturn409WhenAlreadyAssignedToOtherFeature() throws Exception {
      doThrow(new BacklogItemAlreadyAssignedToFeatureException("Backlog item is already assigned to another feature"))
          .when(assignBacklogItemToFeatureCommandService).execute(any());

      String requestBody =
          "{\"actorEmployeeId\":\"" + ACTOR_EMPLOYEE_ID.value()
          + "\",\"scrumTeamId\":\"" + SCRUM_TEAM_ID.value() + "\"}";

      mockMvc.perform(
              MockMvcRequestBuilders.post(
                  "/api/v1/features/" + FEATURE_ID.value() + "/backlog-items/" + BACKLOG_ITEM_ID.value())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(requestBody))
          .andExpect(MockMvcResultMatchers.status().isConflict());
    }
  }

  @Nested
  @DisplayName("DELETE /api/v1/features/{featureId}/backlog-items/{backlogItemId} のテスト")
  class RemoveBacklogItemTest {

    @Test
    @DisplayName("バックログアイテムをフィーチャーから外したとき、204が返される")
    void shouldRemoveBacklogItem() throws Exception {
      doAnswer(invocation -> null).when(removeBacklogItemFromFeatureCommandService).execute(any());

      String requestBody =
          "{\"actorEmployeeId\":\"" + ACTOR_EMPLOYEE_ID.value()
          + "\",\"scrumTeamId\":\"" + SCRUM_TEAM_ID.value() + "\"}";

      mockMvc.perform(
              MockMvcRequestBuilders.delete(
                  "/api/v1/features/" + FEATURE_ID.value() + "/backlog-items/" + BACKLOG_ITEM_ID.value())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(requestBody))
          .andExpect(MockMvcResultMatchers.status().isNoContent());

      verify(removeBacklogItemFromFeatureCommandService).execute(any());
    }
  }

}
