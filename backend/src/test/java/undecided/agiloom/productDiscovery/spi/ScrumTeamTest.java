package undecided.agiloom.productDiscovery.spi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("ScrumTeamのテスト")
class ScrumTeamTest {

  private ScrumTeam createScrumTeam() {
    return ScrumTeam.builder()
        .id(SnowflakeId.of(1L))
        .name("Test Team")
        .businessDomain("Test Domain")
        .poEmployeeId(UUID.randomUUID())
        .poName("Test PO")
        .build();
  }

  @Nested
  @DisplayName("ビルダーのテスト")
  class BuilderTest {

    @Test
    @DisplayName("ビルダーでインスタンスが作成される")
    void shouldCreateInstanceWithBuilder() {
      ScrumTeam scrumTeam = createScrumTeam();

      assertThat(scrumTeam).isNotNull();
      assertThat(scrumTeam.getName()).isEqualTo("Test Team");
      assertThat(scrumTeam.getBusinessDomain()).isEqualTo("Test Domain");
      assertThat(scrumTeam.getPoEmployeeId()).isNotNull();
      assertThat(scrumTeam.getPoName()).isEqualTo("Test PO");
      assertThat(scrumTeam.getCreatedAt()).isNotNull();
      assertThat(scrumTeam.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("createdAtとupdatedAtが自動設定される")
    void shouldSetCreatedAtAndUpdatedAt() {
      ScrumTeam scrumTeam = createScrumTeam();

      assertThat(scrumTeam.getCreatedAt()).isNotNull();
      assertThat(scrumTeam.getUpdatedAt()).isNotNull();
    }
  }

  @Nested
  @DisplayName("updateメソッドのテスト")
  class UpdateMethodTest {

    @Test
    @DisplayName("チーム名が更新される")
    void shouldUpdateName() {
      ScrumTeam scrumTeam = createScrumTeam();

      scrumTeam.update("New Team Name", null, null, null);

      assertThat(scrumTeam.getName()).isEqualTo("New Team Name");
      assertThat(scrumTeam.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("事業領域が更新される")
    void shouldUpdateBusinessDomain() {
      ScrumTeam scrumTeam = createScrumTeam();

      scrumTeam.update(null, "New Domain", null, null);

      assertThat(scrumTeam.getBusinessDomain()).isEqualTo("New Domain");
    }

    @Test
    @DisplayName("POの従業員IDが更新される")
    void shouldUpdatePoEmployeeId() {
      ScrumTeam scrumTeam = createScrumTeam();
      UUID newPoId = UUID.randomUUID();

      scrumTeam.update(null, null, newPoId, null);

      assertThat(scrumTeam.getPoEmployeeId()).isEqualTo(newPoId);
    }

    @Test
    @DisplayName("POの名前が更新される")
    void shouldUpdatePoName() {
      ScrumTeam scrumTeam = createScrumTeam();

      scrumTeam.update(null, null, null, "New PO Name");

      assertThat(scrumTeam.getPoName()).isEqualTo("New PO Name");
    }

    @Test
    @DisplayName("複数のフィールドが一度に更新される")
    void shouldUpdateMultipleFields() {
      ScrumTeam scrumTeam = createScrumTeam();
      UUID newPoId = UUID.randomUUID();

      scrumTeam.update("New Team Name", "New Domain", newPoId, "New PO Name");

      assertThat(scrumTeam.getName()).isEqualTo("New Team Name");
      assertThat(scrumTeam.getBusinessDomain()).isEqualTo("New Domain");
      assertThat(scrumTeam.getPoEmployeeId()).isEqualTo(newPoId);
      assertThat(scrumTeam.getPoName()).isEqualTo("New PO Name");
    }

    @Test
    @DisplayName("updatedAtが更新される")
    void shouldUpdateUpdatedAt() {
      ScrumTeam scrumTeam = createScrumTeam();
      LocalDateTime originalUpdatedAt = scrumTeam.getUpdatedAt();

      scrumTeam.update("New Team Name", null, null, null);

      assertThat(scrumTeam.getUpdatedAt()).isNotEqualTo(originalUpdatedAt);
    }

    @Test
    @DisplayName("nullフィールドは変更されない")
    void shouldNotChangeNullFields() {
      ScrumTeam scrumTeam = createScrumTeam();
      String originalName = scrumTeam.getName();
      String originalDomain = scrumTeam.getBusinessDomain();

      scrumTeam.update(null, null, null, null);

      assertThat(scrumTeam.getName()).isEqualTo(originalName);
      assertThat(scrumTeam.getBusinessDomain()).isEqualTo(originalDomain);
    }

    @Test
    @DisplayName("空文字列のチーム名の場合、例外がスローされる")
    void shouldThrowExceptionWhenNameIsEmpty() {
      ScrumTeam scrumTeam = createScrumTeam();

      assertThatThrownBy(() -> scrumTeam.update("", null, null, null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("must not be empty");
    }

    @Test
    @DisplayName("空文字列の事業領域の場合、例外がスローされる")
    void shouldThrowExceptionWhenBusinessDomainIsEmpty() {
      ScrumTeam scrumTeam = createScrumTeam();

      assertThatThrownBy(() -> scrumTeam.update(null, "", null, null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("must not be empty");
    }
  }

  @Nested
  @DisplayName("assignPoメソッドのテスト")
  class AssignPoTest {

    @Test
    @DisplayName("POが割り当てられる")
    void shouldAssignPo() {
      ScrumTeam scrumTeam =
          ScrumTeam.builder()
              .id(SnowflakeId.of(1L))
              .name("Test Team")
              .businessDomain("Test Domain")
              .build();

      UUID poId = UUID.randomUUID();
      scrumTeam.assignPo(poId, "New PO");

      assertThat(scrumTeam.getPoEmployeeId()).isEqualTo(poId);
      assertThat(scrumTeam.getPoName()).isEqualTo("New PO");
    }

    @Test
    @DisplayName("poEmployeeIdがnullの場合、例外がスローされる")
    void shouldThrowExceptionWhenPoEmployeeIdIsNull() {
      ScrumTeam scrumTeam = createScrumTeam();

      assertThatThrownBy(() -> scrumTeam.assignPo(null, "New PO"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("poEmployeeId must not be null");
    }

    @Test
    @DisplayName("poNameがnullの場合、例外がスローされる")
    void shouldThrowExceptionWhenPoNameIsNull() {
      ScrumTeam scrumTeam = createScrumTeam();

      assertThatThrownBy(() -> scrumTeam.assignPo(UUID.randomUUID(), null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("poName must not be null");
    }
  }

  @Nested
  @DisplayName("unassignPoメソッドのテスト")
  class UnassignPoTest {

    @Test
    @DisplayName("POの割り当てが解除される")
    void shouldUnassignPo() {
      ScrumTeam scrumTeam = createScrumTeam();

      scrumTeam.unassignPo();

      assertThat(scrumTeam.getPoEmployeeId()).isNull();
      assertThat(scrumTeam.getPoName()).isNull();
    }
  }
}
