package undecided.agiloom.requirementBacklog.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

@Tag("small")
@ExtendWith(MockitoExtension.class)
@DisplayName("バックログアイテム分割API")
class SplitBacklogItemControllerTest {
  private static final SnowflakeId PARENT_ID = SnowflakeId.of(9007199254740993L);
  private static final SnowflakeId TEAM_ID = SnowflakeId.of(10L);
  private static final SnowflakeId ACTOR_ID = SnowflakeId.of(101L);
  private static final SnowflakeId MEMBER_ID = SnowflakeId.of(102L);
  private static final List<SplitBacklogItemCommand.Child> CHILDREN =
      List.of(
          new SplitBacklogItemCommand.Child("子A", 5),
          new SplitBacklogItemCommand.Child("子B", 3));

  private final BacklogItemRepository items = mock(BacklogItemRepository.class);
  private final ScrumTeamSubsetRepository teams = mock(ScrumTeamSubsetRepository.class);
  private final SnowflakeIdGenerator ids = mock(SnowflakeIdGenerator.class);
  private final ApplicationEventPublisher events = mock(ApplicationEventPublisher.class);

  @Spy private SplitBacklogItemCommandService splitService =
      new SplitBacklogItemCommandService(items, teams, ids, events);
  @Spy private BacklogItemQueryService queryService = new BacklogItemQueryService(items, null, null, null, null);
  @Spy private BacklogItemResponseConverter converter = new BacklogItemResponseConverter();
  @InjectMocks private BacklogItemCommandController controller;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(controller)
        .setControllerAdvice(new BacklogItemExceptionHandler())
        .build();
  }

  @Nested
  @DisplayName("POST /api/v1/backlog-items/{backlogItemId}/split")
  class SplitTest {
    @Test
    @DisplayName("分割に成功すると元アイテムと子一覧を返し、入力をサービスに渡す")
    void shouldReturnParentAndChildren() throws Exception {
      prepareSuccessfulSplit();

      mockMvc.perform(splitRequest(ACTOR_ID, CHILDREN))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(MockMvcResultMatchers.jsonPath("$.parent.id")
              .value(is("9007199254740993"), String.class))
          .andExpect(MockMvcResultMatchers.jsonPath("$.parent.title").value("分割元"))
          .andExpect(MockMvcResultMatchers.jsonPath("$.children.length()").value(2))
          .andExpect(MockMvcResultMatchers.jsonPath("$.children[0].id").exists())
          .andExpect(MockMvcResultMatchers.jsonPath("$.children[1].id").exists())
          .andExpect(MockMvcResultMatchers.jsonPath("$.children[*].title")
              .value(containsInAnyOrder("子A", "子B")))
          .andExpect(MockMvcResultMatchers.jsonPath("$.children[*].status")
              .value(containsInAnyOrder("SUGGESTED", "SUGGESTED")))
          .andExpect(MockMvcResultMatchers.jsonPath("$.children[*].storyPoint")
              .value(containsInAnyOrder(5, 3)))
          .andExpect(MockMvcResultMatchers.jsonPath("$.children[?(@.title == '子A')].storyPoint")
              .value(containsInAnyOrder(5)))
          .andExpect(MockMvcResultMatchers.jsonPath("$.children[?(@.title == '子B')].storyPoint")
              .value(containsInAnyOrder(3)));

      verifyCommand(ACTOR_ID, CHILDREN);
    }

    @Test
    @DisplayName("子のparentItemIdは精度を保った文字列の元アイテムIDになる")
    void shouldReturnParentItemIdForEachChild() throws Exception {
      prepareSuccessfulSplit();

      mockMvc.perform(splitRequest(ACTOR_ID, CHILDREN))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(MockMvcResultMatchers.jsonPath("$.children.length()").value(2))
          .andExpect(MockMvcResultMatchers.jsonPath("$.children[0].parentItemId")
              .value("9007199254740993"))
          .andExpect(MockMvcResultMatchers.jsonPath("$.children[1].parentItemId")
              .value("9007199254740993"));

      verifyCommand(ACTOR_ID, CHILDREN);
    }

    @Test
    @DisplayName("分割後の元アイテムの状態はDONEで返る")
    void shouldReturnParentDone() throws Exception {
      prepareSuccessfulSplit();

      mockMvc.perform(splitRequest(ACTOR_ID, CHILDREN))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(MockMvcResultMatchers.jsonPath("$.parent.status").value("DONE"));

      verifyCommand(ACTOR_ID, CHILDREN);
    }

    @Test
    @DisplayName("権限のない操作者による分割は403を返す")
    void shouldReturnForbiddenForUnauthorizedActor() throws Exception {
      prepareParent(13);

      mockMvc.perform(splitRequest(MEMBER_ID, CHILDREN))
          .andExpect(MockMvcResultMatchers.status().isForbidden())
          .andExpect(MockMvcResultMatchers.jsonPath("$.message")
              .value("Only the Scrum Master or Product Owner of the team can split a backlog item"));

      verifyCommand(MEMBER_ID, CHILDREN);
      verifyNoWrites();
    }

    @Test
    @DisplayName("子が21件の場合は400を返す")
    void shouldReturnBadRequestForTwentyOneChildren() throws Exception {
      List<SplitBacklogItemCommand.Child> children = IntStream.range(0, 21)
          .mapToObj(index -> new SplitBacklogItemCommand.Child("子" + index, 1))
          .toList();
      prepareParent(13);

      mockMvc.perform(splitRequest(ACTOR_ID, children))
          .andExpect(MockMvcResultMatchers.status().isBadRequest())
          .andExpect(MockMvcResultMatchers.jsonPath("$.message")
              .value("The number of children must be between 1 and 20"));

      verifyCommand(ACTOR_ID, children);
      verifyNoWrites();
    }

    @Test
    @DisplayName("元アイテムのストーリーポイントが8の場合は400を返す")
    void shouldReturnBadRequestForStoryPointEight() throws Exception {
      prepareParent(8);

      mockMvc.perform(splitRequest(ACTOR_ID, CHILDREN))
          .andExpect(MockMvcResultMatchers.status().isBadRequest())
          .andExpect(MockMvcResultMatchers.jsonPath("$.message")
              .value("BacklogItem story point must be at least 13"));

      verifyCommand(ACTOR_ID, CHILDREN);
      verifyNoWrites();
    }

    @Test
    @DisplayName("元アイテムが存在しない場合は400を返す")
    void shouldReturnBadRequestForMissingParent() throws Exception {
      when(items.findById(PARENT_ID)).thenReturn(Optional.empty());

      mockMvc.perform(splitRequest(ACTOR_ID, CHILDREN))
          .andExpect(MockMvcResultMatchers.status().isBadRequest())
          .andExpect(MockMvcResultMatchers.jsonPath("$.message")
              .value("BacklogItem not found: " + PARENT_ID));

      verifyCommand(ACTOR_ID, CHILDREN);
      verifyNoWrites();
    }
  }

  private void prepareParent(int storyPoint) {
    BacklogItem parent = BacklogItem.builder().id(PARENT_ID).title("分割元")
        .creatorName("作成者").scrumTeamId(TEAM_ID).storyPoint(storyPoint)
        .status(BacklogItemStatus.ESTIMATED).build();
    ScrumTeamSubset team = ScrumTeamSubset.builder().id(TEAM_ID).name("チーム")
        .poEmployeeId(ACTOR_ID).memberEmployeeIds(List.of(ACTOR_ID, MEMBER_ID)).build();
    when(items.findById(PARENT_ID)).thenReturn(Optional.of(parent));
    when(teams.findById(TEAM_ID)).thenReturn(Optional.of(team));
  }

  private void prepareSuccessfulSplit() {
    prepareParent(13);
    AtomicLong nextId = new AtomicLong(201L);
    when(ids.nextIdValue()).thenAnswer(invocation -> SnowflakeId.of(nextId.getAndIncrement()));
    List<BacklogItem> savedChildren = new ArrayList<>();
    when(items.saveAll(any())).thenAnswer(invocation -> {
      Iterable<BacklogItem> batch = invocation.getArgument(0);
      batch.forEach(savedChildren::add);
      return savedChildren;
    });
    when(items.findByParentItemId(PARENT_ID)).thenAnswer(invocation -> savedChildren);
  }

  private org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder splitRequest(
      SnowflakeId actor, List<SplitBacklogItemCommand.Child> children) {
    String childJson = children.stream()
        .map(child -> "{\"title\":\"" + child.title() + "\",\"storyPoint\":" + child.storyPoint() + "}")
        .collect(Collectors.joining(","));
    String body = "{\"actorEmployeeId\":\"" + actor.value() + "\",\"children\":[" + childJson + "]}";
    return MockMvcRequestBuilders.post("/api/v1/backlog-items/{backlogItemId}/split", PARENT_ID.value())
        .contentType(MediaType.APPLICATION_JSON)
        .content(body);
  }

  private void verifyCommand(SnowflakeId actor, List<SplitBacklogItemCommand.Child> children) {
    ArgumentCaptor<SplitBacklogItemCommand> captor = ArgumentCaptor.forClass(SplitBacklogItemCommand.class);
    verify(splitService).execute(captor.capture());
    assertThat(captor.getValue()).isEqualTo(new SplitBacklogItemCommand(PARENT_ID, actor, children));
  }

  private void verifyNoWrites() {
    verify(items, never()).save(any());
    verify(items, never()).saveAll(any());
    verify(events, never()).publishEvent(any(Object.class));
  }
}