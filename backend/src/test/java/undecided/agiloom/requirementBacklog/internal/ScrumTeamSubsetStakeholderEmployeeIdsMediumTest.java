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
import undecided.agiloom.productDiscovery.spi.ScrumTeamChanged;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * ScrumTeamSubset の stakeholderEmployeeIds（BIGINT[]）の保存・読込と、
 * ScrumTeamChanged イベントからの射影をテストする統合テストです（#734）。
 */
@Tag("medium")
@SpringBootTest(classes = ModulithDemoApplication.class)
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@DisplayName("ScrumTeamSubset stakeholderEmployeeIds の統合テスト")
class ScrumTeamSubsetStakeholderEmployeeIdsMediumTest {

  @Autowired private ScrumTeamSubsetRepository scrumTeamSubsetRepository;

  @Autowired private JdbcTemplate jdbcTemplate;

  @BeforeEach
  void setUp() {
    scrumTeamSubsetRepository.deleteAll();
  }

  @Nested
  @DisplayName("stakeholderEmployeeIds の保存・読込")
  class SaveAndLoad {

    @Test
    @DisplayName("stakeholderEmployeeIds付きScrumTeamSubsetを保存し、再取得して一致すること")
    void shouldSaveAndLoadStakeholderEmployeeIds() {
      ScrumTeamSubset subset =
          ScrumTeamSubset.builder()
              .id(SnowflakeId.of(600L))
              .name("ステークホルダーテストチーム")
              .poEmployeeId(SnowflakeId.of(6001L))
              .poName("PO")
              .smEmployeeId(SnowflakeId.of(6002L))
              .smName("SM")
              .memberEmployeeIds(List.of(SnowflakeId.of(6001L), SnowflakeId.of(6002L)))
              .stakeholderEmployeeIds(
                  List.of(SnowflakeId.of(6901L), SnowflakeId.of(6902L)))
              .build();

      scrumTeamSubsetRepository.save(subset);
      ScrumTeamSubset loaded =
          scrumTeamSubsetRepository.findById(SnowflakeId.of(600L)).orElseThrow();

      assertThat(loaded.getStakeholderEmployeeIds())
          .containsExactly(SnowflakeId.of(6901L), SnowflakeId.of(6902L));
      assertThat(loaded.isStakeholder(SnowflakeId.of(6901L))).isTrue();
      assertThat(loaded.isStakeholder(SnowflakeId.of(6001L))).isFalse();
    }

    @Test
    @DisplayName("stakeholderEmployeeIdsを指定せずに保存すると、空リストとして読込されること")
    void shouldDefaultToEmptyStakeholders() {
      ScrumTeamSubset subset =
          ScrumTeamSubset.builder().id(SnowflakeId.of(601L)).name("ステークホルダーなしチーム").build();

      scrumTeamSubsetRepository.save(subset);
      ScrumTeamSubset loaded =
          scrumTeamSubsetRepository.findById(SnowflakeId.of(601L)).orElseThrow();

      assertThat(loaded.getStakeholderEmployeeIds()).isEmpty();
    }

    @Test
    @DisplayName("DBのscrum_team_subsetsのstakeholder_employee_idsカラムがBIGINT[]型であること")
    void shouldStakeholderColumnBeBigintArray() {
      String elementType =
          jdbcTemplate.queryForObject(
              """
              SELECT pg_catalog.format_type(e.oid, a.atttypmod)
              FROM pg_catalog.pg_attribute a
              JOIN pg_catalog.pg_type e ON a.atttypid = e.oid
              WHERE a.attrelid = 'undecided_ag.scrum_team_subsets'::regclass
              AND a.attname = 'stakeholder_employee_ids'
              """,
              String.class);

      assertThat(elementType).isEqualTo("bigint[]");
    }
  }

  @Nested
  @DisplayName("イベントからの射影")
  class Projection {

    @Test
    @DisplayName("ScrumTeamChangedのstakeholderEmployeeIdsがサブセットへ反映され永続化されること")
    void shouldProjectStakeholdersFromEvent() {
      ScrumTeamChanged event =
          new ScrumTeamChanged(
              SnowflakeId.of(602L),
              SnowflakeId.of(1L),
              "射影テストチーム",
              "Domain",
              SnowflakeId.of(6021L),
              "PO",
              SnowflakeId.of(6022L),
              "SM",
              List.of(SnowflakeId.of(6021L), SnowflakeId.of(6022L)),
              List.of(SnowflakeId.of(6921L)),
              false);

      scrumTeamSubsetRepository.save(ScrumTeamSubset.from(event, LocalDateTime.now()));
      ScrumTeamSubset loaded =
          scrumTeamSubsetRepository.findById(SnowflakeId.of(602L)).orElseThrow();

      assertThat(loaded.getStakeholderEmployeeIds()).containsExactly(SnowflakeId.of(6921L));
      assertThat(loaded.isStakeholder(SnowflakeId.of(6921L))).isTrue();
    }
  }
}
