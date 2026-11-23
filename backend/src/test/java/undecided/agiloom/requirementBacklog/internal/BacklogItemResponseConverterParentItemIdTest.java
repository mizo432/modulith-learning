package undecided.agiloom.requirementBacklog.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("BacklogItemResponseConverterの親アイテムID変換のテスト")
class BacklogItemResponseConverterParentItemIdTest {

  private static final SnowflakeId PARENT_ID = SnowflakeId.of(9007199254740993L);

  private final BacklogItemResponseConverter converter = new BacklogItemResponseConverter();

  @Nested
  @DisplayName("toBacklogItemResponseメソッドのテスト")
  class ToBacklogItemResponseTest {

    @Test
    @DisplayName("親IDを持つアイテムを変換するとレスポンスに同じ親IDが文字列で含まれる")
    void shouldIncludeParentItemIdInResponse() {
      BacklogItem item = BacklogItem.builder().parentItemId(PARENT_ID).build();

      BacklogItemResponse response = converter.toBacklogItemResponse(item);

      assertThat(response.getParentItemId()).isEqualTo("9007199254740993");
    }

    @Test
    @DisplayName("親IDがnullのアイテムを変換するとレスポンスの親IDもnullになる")
    void shouldHaveNullParentItemIdWhenItemHasNoParent() {
      BacklogItem item = BacklogItem.builder().parentItemId(null).build();

      BacklogItemResponse response = converter.toBacklogItemResponse(item);

      assertThat(response.getParentItemId()).isNull();
    }
  }
}