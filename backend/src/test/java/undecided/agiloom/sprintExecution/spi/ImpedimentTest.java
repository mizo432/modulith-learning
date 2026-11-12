package undecided.agiloom.sprintExecution.spi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.mizo432.shared.common.exception.BusinessException;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("Impedimentのテスト")
class ImpedimentTest {

  private static final UUID FIXED_ASSIGNEE_ID =
      UUID.fromString("55555555-5555-5555-5555-555555555555");

  private Impediment createImpediment() {
    return Impediment.of(
        SnowflakeId.of(1L),
        SnowflakeId.of(100L),
        "Database connection issue",
        "Cannot connect to production database",
        "Blocking all deployments",
        null,
        null,
        null,
        null);
  }

  @Nested
  @DisplayName("ファクトリーメソッドのテスト")
  class FactoryMethodTest {

    @Test
    @DisplayName("ファクトリーメソッドでインスタンスが作成される")
    void shouldCreateInstanceWithFactoryMethod() {
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
    @DisplayName("IN_PROGRESS状態からRESOLVEDに更新される")
    void shouldUpdateStatusToResolved() {
      Impediment impediment = createImpediment();
      impediment.startWorkingOn();

      impediment.resolve();

      assertThat(impediment.getStatus()).isEqualTo(ImpedimentStatus.RESOLVED);
      assertThat(impediment.getResolvedAt()).isNotNull();
    }

    @Test
    @DisplayName("NEW状態からresolveするとBusinessExceptionがスローされる")
    void shouldThrowExceptionWhenResolveFromNew() {
      Impediment impediment = createImpediment();

      assertThatThrownBy(() -> impediment.resolve())
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("NEW");
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
    }
  }

  @Nested
  @DisplayName("updateメソッドのテスト")
  class UpdateMethodTest {

    @Test
    @DisplayName("全プロパティが更新される")
    void shouldUpdateAllProperties() {
      Impediment impediment = createImpediment();

      impediment.update("New title", "New description", "New impact", FIXED_ASSIGNEE_ID);

      assertThat(impediment.getTitle()).isEqualTo("New title");
      assertThat(impediment.getDescription()).isEqualTo("New description");
      assertThat(impediment.getImpact()).isEqualTo("New impact");
      assertThat(impediment.getAssigneeId()).isEqualTo(FIXED_ASSIGNEE_ID);
    }

    @Test
    @DisplayName("タイトルがnullの場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenTitleIsNull() {
      Impediment impediment = createImpediment();

      assertThatThrownBy(() -> impediment.update(null, "Description", "Impact", FIXED_ASSIGNEE_ID))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("title must not be null");
    }

    @Test
    @DisplayName("説明がnullの場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenDescriptionIsNull() {
      Impediment impediment = createImpediment();

      assertThatThrownBy(() -> impediment.update("Title", null, "Impact", FIXED_ASSIGNEE_ID))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("description must not be null");
    }

    @Test
    @DisplayName("タイトルが空文字の場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenTitleIsEmpty() {
      Impediment impediment = createImpediment();

      assertThatThrownBy(() -> impediment.update("", "Description", "Impact", FIXED_ASSIGNEE_ID))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("title must not be empty");
    }

    @Test
    @DisplayName("タイトルが空白文字みの場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenTitleIsBlank() {
      Impediment impediment = createImpediment();

      assertThatThrownBy(() -> impediment.update("   ", "Description", "Impact", FIXED_ASSIGNEE_ID))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("title must not be empty");
    }

    @Test
    @DisplayName("タイトルが200文字を超える場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenTitleExceedsLength() {
      Impediment impediment = createImpediment();
      String longTitle = "A".repeat(201);

      assertThatThrownBy(
              () -> impediment.update(longTitle, "Description", "Impact", FIXED_ASSIGNEE_ID))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("title must not exceed 200 characters");
    }

    @Test
    @DisplayName("説明が空文字の場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenDescriptionIsEmpty() {
      Impediment impediment = createImpediment();

      assertThatThrownBy(() -> impediment.update("Title", "", "Impact", FIXED_ASSIGNEE_ID))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("description must not be empty");
    }

    @Test
    @DisplayName("説明が2000文字を超える場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenDescriptionExceedsLength() {
      Impediment impediment = createImpediment();
      String longDescription = "A".repeat(2001);

      assertThatThrownBy(
              () -> impediment.update("Title", longDescription, "Impact", FIXED_ASSIGNEE_ID))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("description must not exceed 2000 characters");
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

    @Test
    @DisplayName("IN_PROGRESS状態からstartWorkingOnするとBusinessExceptionがスローされる")
    void shouldThrowExceptionWhenStartWorkingOnFromInProgress() {
      Impediment impediment = createImpediment();
      impediment.startWorkingOn();

      assertThatThrownBy(() -> impediment.startWorkingOn())
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("IN_PROGRESS");
    }

    @Test
    @DisplayName("RESOLVED状態からresolveするとBusinessExceptionがスローされる")
    void shouldThrowExceptionWhenResolveFromResolved() {
      Impediment impediment = createImpediment();
      impediment.startWorkingOn();
      impediment.resolve();

      assertThatThrownBy(() -> impediment.resolve())
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("RESOLVED");
    }

    @Test
    @DisplayName("RESOLVED状態からstartWorkingOnするとBusinessExceptionがスローされる")
    void shouldThrowExceptionWhenStartWorkingOnFromResolved() {
      Impediment impediment = createImpediment();
      impediment.startWorkingOn();
      impediment.resolve();

      assertThatThrownBy(() -> impediment.startWorkingOn())
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("RESOLVED");
    }
  }

  @Nested
  @DisplayName("論理削除後の変更禁止テスト")
  class DeletedEntityChangeProhibitionTest {

    @Test
    @DisplayName("論理削除後にresolveすると例外がスローされる")
    void shouldThrowExceptionWhenResolveAfterDeleted() {
      Impediment impediment = createImpediment();
      impediment.startWorkingOn();
      impediment.setDeletedAt(LocalDateTime.now());

      assertThatThrownBy(() -> impediment.resolve())
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("already deleted");
    }

    @Test
    @DisplayName("論理削除後にstartWorkingOnすると例外がスローされる")
    void shouldThrowExceptionWhenStartWorkingOnAfterDeleted() {
      Impediment impediment = createImpediment();
      impediment.setDeletedAt(LocalDateTime.now());

      assertThatThrownBy(() -> impediment.startWorkingOn())
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("already deleted");
    }

    @Test
    @DisplayName("論理削除後にupdateすると例外がスローされる")
    void shouldThrowExceptionWhenUpdateAfterDeleted() {
      Impediment impediment = createImpediment();
      impediment.setDeletedAt(LocalDateTime.now());

      assertThatThrownBy(
              () -> impediment.update("New title", "New description", "New impact", FIXED_ASSIGNEE_ID))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("already deleted");
    }
  }
}
