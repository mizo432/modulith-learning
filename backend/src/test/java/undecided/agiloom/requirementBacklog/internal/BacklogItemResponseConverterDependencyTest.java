package undecided.agiloom.requirementBacklog.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("BacklogItemResponseConverterの依存関係フィールドのテスト")
class BacklogItemResponseConverterDependencyTest {

  private static final SnowflakeId TEAM_ID = SnowflakeId.of(10L);
  private static final SnowflakeId ITEM_ID = SnowflakeId.of(100L);

  private final BacklogItemDependencyQueryService dependencyQueryService =
      mock(BacklogItemDependencyQueryService.class);
  private final BacklogItemResponseConverter converter =
      new BacklogItemResponseConverter(dependencyQueryService);

  private BacklogItem item() {
    return BacklogItem.builder()
        .id(ITEM_ID)
        .title("Item")
        .creatorName("Creator")
        .scrumTeamId(TEAM_ID)
        .status(BacklogItemStatus.ACCEPTED)
        .build();
  }

  @Test
  @DisplayName("バックログアイテムのレスポンスを取得したとき、依存件数と充足フラグが実際の依存関係を反映している")
  void reflectsDependencySummary() {
    when(dependencyQueryService.summarize(ITEM_ID))
        .thenReturn(new BacklogItemDependencySummary(2, false));

    BacklogItemResponse response = converter.toBacklogItemResponse(item());

    assertThat(response.getDependencyCount()).isEqualTo(2);
    assertThat(response.isDependenciesSatisfied()).isFalse();
  }

  @Test
  @DisplayName("依存関係が無いアイテムのレスポンスを取得したとき、依存件数は0で充足フラグはtrueである")
  void noDependencies() {
    when(dependencyQueryService.summarize(ITEM_ID))
        .thenReturn(new BacklogItemDependencySummary(0, true));

    BacklogItemResponse response = converter.toBacklogItemResponse(item());

    assertThat(response.getDependencyCount()).isZero();
    assertThat(response.isDependenciesSatisfied()).isTrue();
  }
}
