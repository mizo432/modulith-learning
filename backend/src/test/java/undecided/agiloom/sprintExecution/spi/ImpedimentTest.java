package undecided.agiloom.sprintExecution.spi;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("Impedimentのテスト")
class ImpedimentTest {

  private Impediment createImpediment() {
    return Impediment.builder()
        .id(SnowflakeId.of(1L))
        .sprintId(SnowflakeId.of(100L))
        .title("Database connection issue")
        .description("Cannot connect to production database")
        .impact("Blocking all deployments")
        .build();
  }

  @Nested
  @DisplayName("ビルダーのテスト")
  class BuilderTest {

    @Test
    @DisplayName("ビルダーでインスタンスが作成される")
    void shouldCreateInstanceWithBuilder() {
      Impediment impediment = createImpediment();

      assertThat(impediment).isNotNull();
      assertThat(impediment.getTitle()).isEqualTo("Database connection issue");
      assertThat(impediment.getStatus()).isEqualTo(ImpedimentStatus.NEW);
    }

    @Test
    @DisplayName("デフォルトステータスはNEWである")
    void shouldHaveDefaultStatusNew() {
      Impediment impediment = createImpediment();

      assertThat(impediment.getStatus()).isEqualTo(ImpedimentStatus.NEW);
    }
  }

  @Nested
  @DisplayName("resolveメソッドのテスト")
  class ResolveMethodTest {

    @Test
    @DisplayName("ステータスがRESOLVEDに更新される")
    void shouldUpdateStatusToResolved() {
      Impediment impediment = createImpediment();
      impediment.startWorkingOn();

      impediment.resolve();

      assertThat(impediment.getStatus()).isEqualTo(ImpedimentStatus.RESOLVED);
      assertThat(impediment.getResolvedAt()).isNotNull();
      assertThat(impediment.getUpdatedAt()).isNotNull();
    }
  }

  @Nested
  @DisplayName("startWorkingOnメソッドのテスト")
  class StartWorkingOnMethodTest {

    @Test
    @DisplayName("ステータスがIN_PROGRESSに更新される")
    void shouldUpdateStatusToInProgress() {
      Impediment impediment = createImpediment();

      impediment.startWorkingOn();

      assertThat(impediment.getStatus()).isEqualTo(ImpedimentStatus.IN_PROGRESS);
      assertThat(impediment.getUpdatedAt()).isNotNull();
    }
  }

  @Nested
  @DisplayName("updateメソッドのテスト")
  class UpdateMethodTest {

    @Test
    @DisplayName("全プロパティが更新される")
    void shouldUpdateAllProperties() {
      Impediment impediment = createImpediment();
      UUID assigneeId = UUID.randomUUID();

      impediment.update("New title", "New description", "New impact", assigneeId);

      assertThat(impediment.getTitle()).isEqualTo("New title");
      assertThat(impediment.getDescription()).isEqualTo("New description");
      assertThat(impediment.getImpact()).isEqualTo("New impact");
      assertThat(impediment.getAssigneeId()).isEqualTo(assigneeId);
      assertThat(impediment.getUpdatedAt()).isNotNull();
    }
  }

  @Nested
  @DisplayName("ステータス遷移のテスト")
  class StatusTransitionTest {

    @Test
    @DisplayName("NEWからIN_PROGRESSへ遷移できる")
    void shouldTransitionFromNewToInProgress() {
      Impediment impediment = createImpediment();
      assertThat(impediment.getStatus()).isEqualTo(ImpedimentStatus.NEW);

      impediment.startWorkingOn();
      assertThat(impediment.getStatus()).isEqualTo(ImpedimentStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("IN_PROGRESSからRESOLVEDへ遷移できる")
    void shouldTransitionFromInProgressToResolved() {
      Impediment impediment = createImpediment();
      impediment.startWorkingOn();

      impediment.resolve();
      assertThat(impediment.getStatus()).isEqualTo(ImpedimentStatus.RESOLVED);
    }
  }
}
