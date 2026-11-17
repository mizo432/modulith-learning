package undecided.agiloom.requirementBacklog.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("BacklogItemQueryServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class BacklogItemQueryServiceTest {

  @Mock private BacklogItemRepository backlogItemRepository;

  @Mock private BacklogItemCommentRepository backlogItemCommentRepository;

  private BacklogItemQueryService service;

  private SnowflakeId teamId;

  @BeforeEach
  void setUp() {
    service = new BacklogItemQueryService(backlogItemRepository, backlogItemCommentRepository);
    teamId = SnowflakeId.of(10L);
  }

  @Test
  @DisplayName("Value降順でソートされること")
  void shouldSortByValueDescending() {
    // Arrange
    BacklogItem item100 = createBacklogItem(SnowflakeId.of(3L), "Item100", 100, BacklogItemStatus.SUGGESTED,
        LocalDateTime.of(2026, 1, 3, 0, 0));
    BacklogItem item50 = createBacklogItem(SnowflakeId.of(2L), "Item50", 50, BacklogItemStatus.SUGGESTED,
        LocalDateTime.of(2026, 1, 2, 0, 0));
    BacklogItem item10 = createBacklogItem(SnowflakeId.of(1L), "Item10", 10, BacklogItemStatus.SUGGESTED,
        LocalDateTime.of(2026, 1, 1, 0, 0));

    when(backlogItemRepository.findByScrumTeamId(teamId))
        .thenReturn(Arrays.asList(item10, item50, item100));

    // Act
    List<BacklogItem> result = service.findByScrumTeamId(teamId, null);

    // Assert
    assertThat(result).hasSize(3);
    assertThat(result.get(0).getValue()).isEqualTo(100);
    assertThat(result.get(1).getValue()).isEqualTo(50);
    assertThat(result.get(2).getValue()).isEqualTo(10);
  }

  @Test
  @DisplayName("Valueが同値の場合、createdAt昇順（古い方が上）でソートされること")
  void shouldSortByCreatedAtAscendingWhenValueEqual() {
    // Arrange
    BacklogItem older = createBacklogItem(SnowflakeId.of(1L), "Older", 50, BacklogItemStatus.SUGGESTED,
        LocalDateTime.of(2026, 1, 1, 0, 0));
    BacklogItem newer = createBacklogItem(SnowflakeId.of(2L), "Newer", 50, BacklogItemStatus.SUGGESTED,
        LocalDateTime.of(2026, 1, 2, 0, 0));

    when(backlogItemRepository.findByScrumTeamId(teamId))
        .thenReturn(Arrays.asList(newer, older));

    // Act
    List<BacklogItem> result = service.findByScrumTeamId(teamId, null);

    // Assert
    assertThat(result).hasSize(2);
    assertThat(result.get(0).getId()).isEqualTo(SnowflakeId.of(1L)); // 古い方が先
    assertThat(result.get(1).getId()).isEqualTo(SnowflakeId.of(2L));
  }

  @Test
  @DisplayName("ValueとcreatedAtが同値の場合、id昇順でソートされること")
  void shouldSortByIdAscendingWhenValueAndCreatedAtEqual() {
    // Arrange
    LocalDateTime sameTime = LocalDateTime.of(2026, 1, 1, 12, 0);
    BacklogItem id3 = createBacklogItem(SnowflakeId.of(3L), "Id3", 50, BacklogItemStatus.SUGGESTED, sameTime);
    BacklogItem id1 = createBacklogItem(SnowflakeId.of(1L), "Id1", 50, BacklogItemStatus.SUGGESTED, sameTime);
    BacklogItem id2 = createBacklogItem(SnowflakeId.of(2L), "Id2", 50, BacklogItemStatus.SUGGESTED, sameTime);

    when(backlogItemRepository.findByScrumTeamId(teamId))
        .thenReturn(Arrays.asList(id3, id1, id2));

    // Act
    List<BacklogItem> result = service.findByScrumTeamId(teamId, null);

    // Assert
    assertThat(result).hasSize(3);
    assertThat(result.get(0).getId()).isEqualTo(SnowflakeId.of(1L));
    assertThat(result.get(1).getId()).isEqualTo(SnowflakeId.of(2L));
    assertThat(result.get(2).getId()).isEqualTo(SnowflakeId.of(3L));
  }

  @Test
  @DisplayName("Valueがnullのアイテムは末尾に配置されること")
  void shouldPutNullValueAtEnd() {
    // Arrange
    BacklogItem item100 = createBacklogItem(SnowflakeId.of(1L), "Item100", 100, BacklogItemStatus.SUGGESTED,
        LocalDateTime.of(2026, 1, 1, 0, 0));
    BacklogItem itemNull1 = createBacklogItem(SnowflakeId.of(2L), "Null1", null, BacklogItemStatus.SUGGESTED,
        LocalDateTime.of(2026, 1, 2, 0, 0));
    BacklogItem item50 = createBacklogItem(SnowflakeId.of(3L), "Item50", 50, BacklogItemStatus.SUGGESTED,
        LocalDateTime.of(2026, 1, 3, 0, 0));
    BacklogItem itemNull2 = createBacklogItem(SnowflakeId.of(4L), "Null2", null, BacklogItemStatus.SUGGESTED,
        LocalDateTime.of(2026, 1, 4, 0, 0));

    when(backlogItemRepository.findByScrumTeamId(teamId))
        .thenReturn(Arrays.asList(itemNull1, item50, itemNull2, item100));

    // Act
    List<BacklogItem> result = service.findByScrumTeamId(teamId, null);

    // Assert
    assertThat(result).hasSize(4);
    // Valueありは降順: 100, 50
    assertThat(result.get(0).getValue()).isEqualTo(100);
    assertThat(result.get(1).getValue()).isEqualTo(50);
    // Value nullは末尾。createdAt昇順で並ぶ
    assertThat(result.get(2).getValue()).isNull();
    assertThat(result.get(2).getId()).isEqualTo(SnowflakeId.of(2L)); // Null1 (createdAt 2026-01-02)
    assertThat(result.get(3).getValue()).isNull();
    assertThat(result.get(3).getId()).isEqualTo(SnowflakeId.of(4L)); // Null2 (createdAt 2026-01-04)
  }

  @Test
  @DisplayName("状態フィルタ適用時もValue降順が維持されること")
  void shouldMaintainValueSortWithStatusFilter() {
    // Arrange
    BacklogItem accepted100 = createBacklogItem(SnowflakeId.of(1L), "Accepted100", 100, BacklogItemStatus.ACCEPTED,
        LocalDateTime.of(2026, 1, 1, 0, 0));
    BacklogItem accepted50 = createBacklogItem(SnowflakeId.of(2L), "Accepted50", 50, BacklogItemStatus.ACCEPTED,
        LocalDateTime.of(2026, 1, 2, 0, 0));
    BacklogItem accepted10 = createBacklogItem(SnowflakeId.of(3L), "Accepted10", 10, BacklogItemStatus.ACCEPTED,
        LocalDateTime.of(2026, 1, 3, 0, 0));

    when(backlogItemRepository.findByScrumTeamIdAndStatus(teamId, BacklogItemStatus.ACCEPTED))
        .thenReturn(Arrays.asList(accepted10, accepted50, accepted100));

    // Act
    List<BacklogItem> result = service.findByScrumTeamId(teamId, BacklogItemStatus.ACCEPTED);

    // Assert
    assertThat(result).hasSize(3);
    assertThat(result.get(0).getValue()).isEqualTo(100);
    assertThat(result.get(1).getValue()).isEqualTo(50);
    assertThat(result.get(2).getValue()).isEqualTo(10);
  }

  @Test
  @DisplayName("アイテムがない場合は空リストが返されること")
  void shouldReturnEmptyListWhenNoItems() {
    // Arrange
    when(backlogItemRepository.findByScrumTeamId(teamId)).thenReturn(Collections.emptyList());

    // Act
    List<BacklogItem> result = service.findByScrumTeamId(teamId, null);

    // Assert
    assertThat(result).isEmpty();
  }

  private BacklogItem createBacklogItem(
      SnowflakeId id,
      String title,
      Integer value,
      BacklogItemStatus status,
      LocalDateTime createdAt) {
    return BacklogItem.builder()
        .id(id)
        .title(title)
        .scrumTeamId(teamId)
        .creatorName("TestUser")
        .status(status)
        .value(value)
        .createdAt(createdAt)
        .updatedAt(createdAt)
        .build();
  }
}
