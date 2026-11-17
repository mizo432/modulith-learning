package undecided.agiloom.requirementBacklog.spi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("BacklogItemの単体テスト")
class BacklogItemTest {

  private BacklogItem item() {
    return BacklogItem.builder()
        .id(SnowflakeId.of(100L))
        .title("Test Item")
        .creatorName("Creator")
        .scrumTeamId(SnowflakeId.of(10L))
        .build();
  }

  @Nested
  @DisplayName("初期状態のテスト")
  class InitialStatusTest {

    @Test
    @DisplayName("新規作成時の状態はSUGGESTEDであること")
    void shouldDefaultToSuggested() {
      assertThat(item().getStatus()).isEqualTo(BacklogItemStatus.SUGGESTED);
    }
  }

  @Nested
  @DisplayName("changeStatus メソッドのテスト")
  class ChangeStatusTest {

    @Test
    @DisplayName("SUGGESTEDからACCEPTEDへ遷移できること（value設定済み）")
    void shouldAcceptFromSuggested() {
      BacklogItem backlogItem = BacklogItem.builder()
          .id(SnowflakeId.of(100L))
          .title("Test Item")
          .creatorName("Creator")
          .scrumTeamId(SnowflakeId.of(10L))
          .value(50)
          .build();
      backlogItem.changeStatus(BacklogItemStatus.ACCEPTED);
      assertThat(backlogItem.getStatus()).isEqualTo(BacklogItemStatus.ACCEPTED);
    }

    @Test
    @DisplayName("SUGGESTEDからREJECTEDへ遷移できること（value未設定）")
    void shouldRejectFromSuggested() {
      BacklogItem backlogItem = item();
      backlogItem.changeStatus(BacklogItemStatus.REJECTED);
      assertThat(backlogItem.getStatus()).isEqualTo(BacklogItemStatus.REJECTED);
    }

    @Test
    @DisplayName("SUGGESTEDからREJECTEDへ遷移できること（value設定済み）")
    void shouldRejectFromSuggestedWithValue() {
      BacklogItem backlogItem = BacklogItem.builder()
          .id(SnowflakeId.of(100L))
          .title("Test Item")
          .creatorName("Creator")
          .scrumTeamId(SnowflakeId.of(10L))
          .value(50)
          .build();
      backlogItem.changeStatus(BacklogItemStatus.REJECTED);
      assertThat(backlogItem.getStatus()).isEqualTo(BacklogItemStatus.REJECTED);
    }

    @Test
    @DisplayName("ACCEPTEDからは遷移できないこと")
    void shouldNotTransitionFromAccepted() {
      BacklogItem backlogItem = BacklogItem.builder()
          .id(SnowflakeId.of(100L))
          .title("Test Item")
          .creatorName("Creator")
          .scrumTeamId(SnowflakeId.of(10L))
          .value(50)
          .build();
      backlogItem.changeStatus(BacklogItemStatus.ACCEPTED);
      assertThatThrownBy(() -> backlogItem.changeStatus(BacklogItemStatus.REJECTED))
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("Invalid status transition");
    }

    @Test
    @DisplayName("REJECTEDからは遷移できないこと")
    void shouldNotTransitionFromRejected() {
      BacklogItem backlogItem = item();
      backlogItem.changeStatus(BacklogItemStatus.REJECTED);
      assertThatThrownBy(() -> backlogItem.changeStatus(BacklogItemStatus.ACCEPTED))
          .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("SUGGESTEDからSUGGESTEDへの遷移はできないこと")
    void shouldNotTransitionToSuggested() {
      BacklogItem backlogItem = item();
      assertThatThrownBy(() -> backlogItem.changeStatus(BacklogItemStatus.SUGGESTED))
          .isInstanceOf(IllegalStateException.class);
    }
  }

  @Nested
  @DisplayName("update メソッドのテスト")
  class UpdateTest {

    @Test
    @DisplayName("タイトル等を更新できること（nullは変更なし）")
    void shouldUpdateFields() {
      BacklogItem backlogItem = item();
      backlogItem.update("New Title", null, 5, null, null);
      assertThat(backlogItem.getTitle()).isEqualTo("New Title");
      assertThat(backlogItem.getPriority()).isEqualTo(5);
    }

    @Test
    @DisplayName("空タイトルへの更新は例外になること")
    void shouldThrowOnEmptyTitle() {
      BacklogItem backlogItem = item();
      assertThatThrownBy(() -> backlogItem.update("", null, null, null, null))
          .isInstanceOf(IllegalArgumentException.class);
    }
  }

  @Nested
  @DisplayName("update時のValue範囲検証のテスト")
  class ValueRangeTest {

    @Test
    @DisplayName("value=0でupdate成功すること（下限境界値）")
    void shouldAcceptValueZero() {
      BacklogItem backlogItem = item();
      backlogItem.update(null, null, null, null, 0);
      assertThat(backlogItem.getValue()).isEqualTo(0);
    }

    @Test
    @DisplayName("value=100でupdate成功すること（上限境界値）")
    void shouldAcceptValue100() {
      BacklogItem backlogItem = item();
      backlogItem.update(null, null, null, null, 100);
      assertThat(backlogItem.getValue()).isEqualTo(100);
    }

    @Test
    @DisplayName("value=-1でupdate時IllegalArgumentExceptionをスローすること")
    void shouldThrowOnValueMinusOne() {
      BacklogItem backlogItem = item();
      assertThatThrownBy(() -> backlogItem.update(null, null, null, null, -1))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("value=101でupdate時IllegalArgumentExceptionをスローすること")
    void shouldThrowOnValue101() {
      BacklogItem backlogItem = item();
      assertThatThrownBy(() -> backlogItem.update(null, null, null, null, 101))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("value=nullでupdate成功すること（変更なし）")
    void shouldAcceptValueNull() {
      BacklogItem backlogItem = item();
      backlogItem.update(null, null, null, null, null);
      assertThat(backlogItem.getValue()).isNull();
    }
  }

  @Nested
  @DisplayName("qualityCategoryのテスト")
  class QualityCategoryTest {

    @Test
    @DisplayName("Builderでvalue=80を指定して作成するとqualityCategoryがATTRACTIVEになること")
    void shouldSetQualityCategoryToAttractiveWhenValue80() {
      BacklogItem backlogItem = BacklogItem.builder()
          .id(SnowflakeId.of(100L))
          .title("Test Item")
          .creatorName("Creator")
          .scrumTeamId(SnowflakeId.of(10L))
          .value(80)
          .build();
      assertThat(backlogItem.getQualityCategory()).isEqualTo(QualityCategory.ATTRACTIVE);
    }

    @Test
    @DisplayName("Builderでvalue未指定ならqualityCategoryはnullであること")
    void shouldHaveNullQualityCategoryWhenValueNotSet() {
      BacklogItem backlogItem = item();
      assertThat(backlogItem.getQualityCategory()).isNull();
    }

    @Test
    @DisplayName("updateでvalueを30に設定するとqualityCategoryがONE_DIMENSIONALになること")
    void shouldSetQualityCategoryToOneDimensionalWhenValue30() {
      BacklogItem backlogItem = item();
      backlogItem.update(null, null, null, null, 30);
      assertThat(backlogItem.getQualityCategory()).isEqualTo(QualityCategory.ONE_DIMENSIONAL);
    }

    @Test
    @DisplayName("updateでvalueを90に変更するとqualityCategoryがATTRACTIVEになること")
    void shouldUpdateQualityCategoryToAttractiveWhenValue90() {
      BacklogItem backlogItem = BacklogItem.builder()
          .id(SnowflakeId.of(100L))
          .title("Test Item")
          .creatorName("Creator")
          .scrumTeamId(SnowflakeId.of(10L))
          .value(30)
          .build();
      backlogItem.update(null, null, null, null, 90);
      assertThat(backlogItem.getQualityCategory()).isEqualTo(QualityCategory.ATTRACTIVE);
    }
  }

  @Nested
  @DisplayName("ACCEPTED遷移時のValue範囲検証のテスト")
  class ValueOnAcceptedTransitionTest {

    @Test
    @DisplayName("value=0でACCEPTED遷移成功すること（下限境界値）")
    void shouldAcceptValueZeroOnAccepted() {
      BacklogItem backlogItem = BacklogItem.builder()
          .id(SnowflakeId.of(100L))
          .title("Test Item")
          .creatorName("Creator")
          .scrumTeamId(SnowflakeId.of(10L))
          .value(0)
          .build();
      backlogItem.changeStatus(BacklogItemStatus.ACCEPTED);
      assertThat(backlogItem.getStatus()).isEqualTo(BacklogItemStatus.ACCEPTED);
    }

    @Test
    @DisplayName("value=100でACCEPTED遷移成功すること（上限境界値）")
    void shouldAcceptValue100OnAccepted() {
      BacklogItem backlogItem = BacklogItem.builder()
          .id(SnowflakeId.of(100L))
          .title("Test Item")
          .creatorName("Creator")
          .scrumTeamId(SnowflakeId.of(10L))
          .value(100)
          .build();
      backlogItem.changeStatus(BacklogItemStatus.ACCEPTED);
      assertThat(backlogItem.getStatus()).isEqualTo(BacklogItemStatus.ACCEPTED);
    }

    @Test
    @DisplayName("value=-1でACCEPTED遷移時IllegalArgumentExceptionをスローすること")
    void shouldThrowOnValueMinusOneOnAccepted() {
      BacklogItem backlogItem = BacklogItem.builder()
          .id(SnowflakeId.of(100L))
          .title("Test Item")
          .creatorName("Creator")
          .scrumTeamId(SnowflakeId.of(10L))
          .value(-1)
          .build();
      assertThatThrownBy(() -> backlogItem.changeStatus(BacklogItemStatus.ACCEPTED))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("value=101でACCEPTED遷移時IllegalArgumentExceptionをスローすること")
    void shouldThrowOnValue101OnAccepted() {
      BacklogItem backlogItem = BacklogItem.builder()
          .id(SnowflakeId.of(100L))
          .title("Test Item")
          .creatorName("Creator")
          .scrumTeamId(SnowflakeId.of(10L))
          .value(101)
          .build();
      assertThatThrownBy(() -> backlogItem.changeStatus(BacklogItemStatus.ACCEPTED))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("value=nullでACCEPTED遷移時IllegalArgumentExceptionをスローすること")
    void shouldThrowOnValueNullOnAccepted() {
      BacklogItem backlogItem = item();
      assertThatThrownBy(() -> backlogItem.changeStatus(BacklogItemStatus.ACCEPTED))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("value=50でACCEPTED遷移成功すること（中間値）")
    void shouldAcceptValue50OnAccepted() {
      BacklogItem backlogItem = BacklogItem.builder()
          .id(SnowflakeId.of(100L))
          .title("Test Item")
          .creatorName("Creator")
          .scrumTeamId(SnowflakeId.of(10L))
          .value(50)
          .build();
      backlogItem.changeStatus(BacklogItemStatus.ACCEPTED);
      assertThat(backlogItem.getStatus()).isEqualTo(BacklogItemStatus.ACCEPTED);
    }
  }
}
