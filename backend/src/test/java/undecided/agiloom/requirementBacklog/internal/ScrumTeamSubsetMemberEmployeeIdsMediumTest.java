package undecided.agiloom.requirementBacklog.internal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import undecided.ModulithDemoApplication;
import undecided.TestcontainersConfiguration;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * ScrumTeamSubset の memberEmployeeIds（BIGINT[]）の保存・読込をテストする統合テストです。
 *
 * <p>ScrumTeamSubset はパッケージプライベートであるため、
 * 同じパッケージ（requirementBacklog.internal）にテストを配置します。
 * Flyway V20261120100000 による TEXT[] → BIGINT[] 移行と、
 * SnowflakeId.SnowflakeIdListConverter の動作を検証します。
 */
@Tag("medium")
@SpringBootTest(classes = ModulithDemoApplication.class)
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@DisplayName("ScrumTeamSubset memberEmployeeIds BIGINT[] 保存・読込の統合テスト")
class ScrumTeamSubsetMemberEmployeeIdsMediumTest {

  @Autowired private ScrumTeamSubsetRepository scrumTeamSubsetRepository;

  @Autowired private JdbcTemplate jdbcTemplate;

  @BeforeEach
  void setUp() {
    scrumTeamSubsetRepository.deleteAll();
  }

  @Nested
  @DisplayName("ScrumTeamSubset の memberEmployeeIds 保存・読込")
  class ScrumTeamSubsetMemberEmployeeIds {

    @Test
    @DisplayName("memberEmployeeIds付きScrumTeamSubsetを保存し、再取得して一致すること")
    void shouldSaveAndLoadScrumTeamSubsetWithMemberEmployeeIds() {
      // Arrange
      List<SnowflakeId> memberIds = List.of(
          SnowflakeId.of(3001L),
          SnowflakeId.of(3002L),
          SnowflakeId.of(3003L),
          SnowflakeId.of(3004L)
      );

      ScrumTeamSubset subset = ScrumTeamSubset.builder()
          .id(SnowflakeId.of(300L))
          .name("サブセットチーム")
          .poEmployeeId(SnowflakeId.of(3001L))
          .poName("サブセットPO")
          .smEmployeeId(SnowflakeId.of(3002L))
          .smName("サブセットSM")
          .memberEmployeeIds(memberIds)
          .updatedAt(LocalDateTime.now()).build();

      // Act
      scrumTeamSubsetRepository.save(subset);
      ScrumTeamSubset loaded = scrumTeamSubsetRepository.findById(SnowflakeId.of(300L)).orElseThrow();

      // Assert
      assertThat(loaded.getMemberEmployeeIds()).hasSize(4);
      assertThat(loaded.getMemberEmployeeIds())
          .containsExactly(
              SnowflakeId.of(3001L),
              SnowflakeId.of(3002L),
              SnowflakeId.of(3003L),
              SnowflakeId.of(3004L)
          );
    }

    @Test
    @DisplayName("ScrumTeamSubsetのmemberEmployeeIdsが空の場合、空リストとして読込されること")
    void shouldLoadEmptyMemberEmployeeIdsForSubset() {
      // Arrange
      ScrumTeamSubset subset = ScrumTeamSubset.builder()
          .id(SnowflakeId.of(301L))
          .name("メンバーなしサブセット")
          .updatedAt(LocalDateTime.now()).build();

      // Act
      scrumTeamSubsetRepository.save(subset);
      ScrumTeamSubset loaded = scrumTeamSubsetRepository.findById(SnowflakeId.of(301L)).orElseThrow();

      // Assert
      assertThat(loaded.getMemberEmployeeIds()).isEmpty();
    }

    @Test
    @DisplayName("DBのscrum_team_subsetsのmember_employee_idsカラムがBIGINT[]型であること")
    void shouldSubsetMemberEmployeeIdsColumnBeBigintArray() {
      // Act
      String elementType = jdbcTemplate.queryForObject(
          """
          SELECT pg_catalog.format_type(e.oid, a.atttypmod)
          FROM pg_catalog.pg_attribute a
          JOIN pg_catalog.pg_type e ON a.atttypid = e.oid
          WHERE a.attrelid = 'undecided_ag.scrum_team_subsets'::regclass
          AND a.attname = 'member_employee_ids'
          """,
          String.class);

      // Assert
      assertThat(elementType).isEqualTo("bigint[]");
    }

    @Test
    @DisplayName("isTeamMemberがmemberEmployeeIdsを正しく参照すること")
    void shouldIsTeamMemberUseMemberEmployeeIds() {
      // Arrange
      List<SnowflakeId> memberIds = List.of(
          SnowflakeId.of(4001L),
          SnowflakeId.of(4002L),
          SnowflakeId.of(3003L)
      );

      ScrumTeamSubset subset = ScrumTeamSubset.builder()
          .id(SnowflakeId.of(400L))
          .name("メンバー判定テストチーム")
          .poEmployeeId(SnowflakeId.of(4001L))
          .poName("PO")
          .smEmployeeId(SnowflakeId.of(4002L))
          .smName("SM")
          .memberEmployeeIds(memberIds)
          .updatedAt(LocalDateTime.now()).build();
      scrumTeamSubsetRepository.save(subset);
      ScrumTeamSubset loaded = scrumTeamSubsetRepository.findById(SnowflakeId.of(400L)).orElseThrow();

      // Assert - PO/SM はメンバー
      assertThat(loaded.isTeamMember(SnowflakeId.of(4001L))).isTrue();
      assertThat(loaded.isTeamMember(SnowflakeId.of(4002L))).isTrue();

      // Assert - memberEmployeeIds リスト固有のIDもメンバー（PO/SM とは異なる値）
      assertThat(loaded.isTeamMember(SnowflakeId.of(3003L))).isTrue();

      // Assert - 含まれないIDはメンバーではない
      assertThat(loaded.isTeamMember(SnowflakeId.of(9999L))).isFalse();
    }
  }

  @Nested
  @DisplayName("Md-1: scrum_team_subsets のデフォルト動作確認")
  class MigrationDefaultConstraint {

    @Test
    @DisplayName("memberEmployeeIdsを指定せずに保存すると、空配列として保存・読込されること")
    void shouldDefaultToEmptyArrayWhenNotSpecified() {
      // Arrange
      ScrumTeamSubset subset = ScrumTeamSubset.builder()
          .id(SnowflakeId.of(500L))
          .name("デフォルトテストサブセット")
          .updatedAt(LocalDateTime.now()).build();

      // Act
      scrumTeamSubsetRepository.save(subset);
      ScrumTeamSubset loaded = scrumTeamSubsetRepository.findById(SnowflakeId.of(500L)).orElseThrow();

      // Assert
      assertThat(loaded.getMemberEmployeeIds()).isEmpty();
    }
  }
}
