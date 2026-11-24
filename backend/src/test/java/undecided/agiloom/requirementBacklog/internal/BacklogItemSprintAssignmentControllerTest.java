package undecided.agiloom.requirementBacklog.internal;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@ExtendWith(MockitoExtension.class)
@DisplayName("スプリント割り当てAPIのテスト")
class BacklogItemSprintAssignmentControllerTest {

  private static final SnowflakeId ITEM_ID = SnowflakeId.of(100L);
  private static final SnowflakeId MEMBER_ID = SnowflakeId.of(2001L);
  private static final SnowflakeId SPRINT_B_ID = SnowflakeId.of(3002L);

  @Mock private AssignBacklogItemSprintCommandService assignService;
  @Mock private BacklogItemQueryService backlogItemQueryService;
  @Mock private BacklogItemResponseConverter backlogItemResponseConverter;
  @InjectMocks private BacklogItemCommandController commandController;
  @InjectMocks private BacklogItemQueryController queryController;

  private MockMvc commandMvc;
  private MockMvc queryMvc;
  private BacklogItem item;

  @BeforeEach
  void setUp() {
    commandMvc = MockMvcBuilders.standaloneSetup(commandController).build();
    queryMvc = MockMvcBuilders.standaloneSetup(queryController).build();
    item =
        BacklogItem.builder()
            .id(ITEM_ID)
            .title("Item")
            .creatorName("Creator")
            .sprintId(SPRINT_B_ID)
            .build();
  }

  @Test
  @DisplayName("POST sprint-assignment を呼んだとき、200でsprintIdを含む応答が返る")
  void assignReturnsOkWithSprintId() throws Exception {
    when(backlogItemQueryService.findById(ITEM_ID)).thenReturn(Optional.of(item));
    when(backlogItemResponseConverter.toBacklogItemResponse(item))
        .thenReturn(BacklogItemResponse.builder().id(ITEM_ID).sprintId("3002").build());

    commandMvc
        .perform(
            post("/api/v1/backlog-items/100/sprint-assignment")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"sprintId\":\"3002\",\"actorEmployeeId\":2001}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sprintId").value("3002"));

    verify(assignService)
        .assign(
            argThat(
                command ->
                    command.getBacklogItemId().equals(ITEM_ID)
                        && command.getSprintId().equals(SPRINT_B_ID)
                        && command.getActorEmployeeId().equals(MEMBER_ID)));
  }

  @Test
  @DisplayName("DELETE sprint-assignment を呼んだとき、200が返る")
  void unassignReturnsOk() throws Exception {
    when(backlogItemQueryService.findById(ITEM_ID)).thenReturn(Optional.of(item));
    when(backlogItemResponseConverter.toBacklogItemResponse(item))
        .thenReturn(BacklogItemResponse.builder().id(ITEM_ID).sprintId(null).build());

    commandMvc
        .perform(
            delete("/api/v1/backlog-items/100/sprint-assignment")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"actorEmployeeId\":2001}"))
        .andExpect(status().isOk());

    verify(assignService)
        .unassign(
            argThat(
                command ->
                    command.getBacklogItemId().equals(ITEM_ID)
                        && command.getActorEmployeeId().equals(MEMBER_ID)));
  }

  @Test
  @DisplayName("GET assignable-sprints を呼んだとき、窓内のスプリント一覧が返る")
  void assignableSprintsReturnsWindow() throws Exception {
    when(backlogItemQueryService.findAssignableSprints(ITEM_ID, MEMBER_ID))
        .thenReturn(
            List.of(
                new AssignableSprintResponse(
                    "3001",
                    2,
                    "Sprint 2",
                    "ACTIVE",
                    LocalDate.of(2026, 11, 16),
                    LocalDate.of(2026, 11, 27),
                    false),
                new AssignableSprintResponse(
                    "3002",
                    3,
                    "Sprint 3",
                    "PLANNED",
                    LocalDate.of(2026, 11, 30),
                    LocalDate.of(2026, 12, 11),
                    true)));

    queryMvc
        .perform(get("/api/v1/backlog-items/100/assignable-sprints").param("actorEmployeeId", "2001"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].sprintNumber").value(2))
        .andExpect(jsonPath("$[1].assigned").value(true));
  }
}
