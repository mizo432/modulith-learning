package undecided.agiloom.requirementBacklog.spi;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("Featureの単体テスト")
class FeatureTest {

  private Feature feature() {
    return Feature.builder()
        .id(SnowflakeId.of(100L))
        .name("Test Feature")
        .scrumTeamId(SnowflakeId.of(10L))
        .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
  }

  @Nested
  @DisplayName("初期状態のテスト")
  class InitialStatusTest {

    @Test
    @DisplayName("新規作成時の状態はPLANNEDであること")
    void shouldDefaultToPlanned() {
      assertThat(feature().getStatus()).isEqualTo(FeatureStatus.PLANNED);
    }

    @Test
    @DisplayName("新規作成時のversionはnullであること")
    void shouldHaveNullVersion() {
      assertThat(feature().getVersion()).isNull();
    }

    @Test
    @DisplayName("説明はnullで設定できること")
    void shouldAllowNullDescription() {
      Feature f = Feature.builder()
          .id(SnowflakeId.of(100L))
          .name("Test Feature")
          .scrumTeamId(SnowflakeId.of(10L))
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
      assertThat(f.getDescription()).isNull();
    }

    @Test
    @DisplayName("説明を設定して作成できること")
    void shouldSetDescription() {
      Feature f = Feature.builder()
          .id(SnowflakeId.of(100L))
          .name("Test Feature")
          .description("Test Description")
          .scrumTeamId(SnowflakeId.of(10L))
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
      assertThat(f.getDescription()).isEqualTo("Test Description");
    }
  }

  @Nested
  @DisplayName("名前長のテスト")
  class NameLengthTest {

    @Test
    @DisplayName("名前が200文字でフィーチャーを作成したとき、正常に保存されること")
    void shouldAcceptNameWith200Characters() {
      String name = "a".repeat(Feature.NAME_MAX_LENGTH);
      Feature f = Feature.builder()
          .id(SnowflakeId.of(100L))
          .name(name)
          .scrumTeamId(SnowflakeId.of(10L))
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
      assertThat(f.getName().length()).isEqualTo(Feature.NAME_MAX_LENGTH);
    }

    @Test
    @DisplayName("名前が201文字でupdateしたとき、例外がスローされること")
    void shouldThrowWhenNameExceeds200Characters() {
      Feature f = feature();
      String name = "a".repeat(Feature.NAME_MAX_LENGTH + 1);
      assertThatThrownBy(() -> f.update(name, null, LocalDateTime.now()))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("must not exceed");
    }

    @Test
    @DisplayName("名前が空でupdateしたとき、例外がスローされること")
    void shouldThrowWhenNameIsEmpty() {
      Feature f = feature();
      assertThatThrownBy(() -> f.update("", null, LocalDateTime.now()))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("must not be empty");
    }
  }

  @Nested
  @DisplayName("changeStatus メソッドのテスト")
  class ChangeStatusTest {

    @Test
    @DisplayName("PLANNEDからIN_PROGRESSへ遷移できること")
    void shouldTransitionFromPlannedToInProgress() {
      Feature f = feature();
      f.changeStatus(FeatureStatus.IN_PROGRESS, LocalDateTime.now());
      assertThat(f.getStatus()).isEqualTo(FeatureStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("IN_PROGRESSからDONEへ遷移できること")
    void shouldTransitionFromInProgressToDone() {
      Feature f = Feature.builder()
          .id(SnowflakeId.of(100L))
          .name("Test Feature")
          .status(FeatureStatus.IN_PROGRESS)
          .scrumTeamId(SnowflakeId.of(10L))
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
      f.changeStatus(FeatureStatus.DONE, LocalDateTime.now());
      assertThat(f.getStatus()).isEqualTo(FeatureStatus.DONE);
    }

    @Test
    @DisplayName("IN_PROGRESSからPLANNEDへ差し戻せること")
    void shouldRollbackFromInProgressToPlanned() {
      Feature f = Feature.builder()
          .id(SnowflakeId.of(100L))
          .name("Test Feature")
          .status(FeatureStatus.IN_PROGRESS)
          .scrumTeamId(SnowflakeId.of(10L))
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
      f.changeStatus(FeatureStatus.PLANNED, LocalDateTime.now());
      assertThat(f.getStatus()).isEqualTo(FeatureStatus.PLANNED);
    }

    @Test
    @DisplayName("DONEからIN_PROGRESSへ差し戻せること")
    void shouldRollbackFromDoneToInProgress() {
      Feature f = Feature.builder()
          .id(SnowflakeId.of(100L))
          .name("Test Feature")
          .status(FeatureStatus.DONE)
          .scrumTeamId(SnowflakeId.of(10L))
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
      f.changeStatus(FeatureStatus.IN_PROGRESS, LocalDateTime.now());
      assertThat(f.getStatus()).isEqualTo(FeatureStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("PLANNEDからDONEへの直接遷移は許可しないこと")
    void shouldNotTransitionFromPlannedToDone() {
      Feature f = feature();
      assertThatThrownBy(() -> f.changeStatus(FeatureStatus.DONE, LocalDateTime.now()))
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("Invalid status transition");
    }

    @Test
    @DisplayName("DONEからPLANNEDへの直接遷移は許可しないこと")
    void shouldNotTransitionFromDoneToPlanned() {
      Feature f = Feature.builder()
          .id(SnowflakeId.of(100L))
          .name("Test Feature")
          .status(FeatureStatus.DONE)
          .scrumTeamId(SnowflakeId.of(10L))
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
      assertThatThrownBy(() -> f.changeStatus(FeatureStatus.PLANNED, LocalDateTime.now()))
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("Invalid status transition");
    }
  }

  @Nested
  @DisplayName("update メソッドのテスト")
  class UpdateTest {

    @Test
    @DisplayName("名前を更新できること")
    void shouldUpdateName() {
      Feature f = feature();
      f.update("New Name", null, LocalDateTime.now());
      assertThat(f.getName()).isEqualTo("New Name");
    }

    @Test
    @DisplayName("説明を更新できること")
    void shouldUpdateDescription() {
      Feature f = feature();
      f.update(null, "New Description", LocalDateTime.now());
      assertThat(f.getDescription()).isEqualTo("New Description");
    }

    @Test
    @DisplayName("nullを渡すと変更なしになること")
    void shouldNotChangeWhenBothNull() {
      Feature f = Feature.builder()
          .id(SnowflakeId.of(100L))
          .name("Original Name")
          .description("Original Description")
          .scrumTeamId(SnowflakeId.of(10L))
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
      f.update(null, null, LocalDateTime.now());
      assertThat(f.getName()).isEqualTo("Original Name");
      assertThat(f.getDescription()).isEqualTo("Original Description");
    }
  }
}
