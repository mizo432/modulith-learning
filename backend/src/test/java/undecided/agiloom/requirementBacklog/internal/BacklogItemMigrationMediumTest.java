package undecided.agiloom.requirementBacklog.internal;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import undecided.ModulithDemoApplication;
import undecided.TestcontainersConfiguration;

/**
 * バックログアイテム関連テーブルのスキーマをテストする統合テストです。
 *
 * <p>scrum_team_subsets、backlog_items、backlog_item_comments テーブルが存在し、
 * 期待するカラムを持つことを検証します。
 *
 * <p>なお、テストプロファイル（application-test.yml）は Hibernate の create-drop で
 * スキーマを生成するため、Flyway の DDL にのみ存在する CHECK 制約は実行時DBには現れません。
 * そのため制約については、クラスパス上の Flyway マイグレーションスクリプトの内容で検証します
 * （本番プロファイルは ddl-auto: none で Flyway の DDL が正本です）。
 */
@Tag("medium")
@SpringBootTest(classes = ModulithDemoApplication.class)
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@DisplayName("BacklogItemマイグレーションの統合テスト")
class BacklogItemMigrationMediumTest {

  @Autowired private JdbcTemplate jdbcTemplate;

  private int tableCount(String tableName) {
    Integer count =
        jdbcTemplate.queryForObject(
            """
            SELECT COUNT(*) FROM information_schema.tables
            WHERE table_schema = 'undecided_ag'
            AND table_name = ?
            """,
            Integer.class,
            tableName);
    return count != null ? count : 0;
  }

  private List<String> columns(String tableName) {
    return jdbcTemplate.queryForList(
        """
        SELECT column_name FROM information_schema.columns
        WHERE table_schema = 'undecided_ag'
        AND table_name = ?
        ORDER BY column_name
        """,
        String.class,
        tableName);
  }

  @Nested
  @DisplayName("スキーマの検証")
  class SchemaVerification {

    @Test
    @DisplayName("backlog_itemsテーブルが作成されていること")
    void shouldCreateBacklogItemsTable() {
      assertThat(tableCount("backlog_items")).isEqualTo(1);
    }

    @Test
    @DisplayName("backlog_item_commentsテーブルが作成されていること")
    void shouldCreateBacklogItemCommentsTable() {
      assertThat(tableCount("backlog_item_comments")).isEqualTo(1);
    }

    @Test
    @DisplayName("scrum_team_subsetsテーブルが作成されていること")
    void shouldCreateScrumTeamSubsetsTable() {
      assertThat(tableCount("scrum_team_subsets")).isEqualTo(1);
    }

    @Test
    @DisplayName("admin_employee_subsetsテーブルが作成されていること")
    void shouldCreateAdminEmployeeSubsetsTable() {
      assertThat(tableCount("admin_employee_subsets")).isEqualTo(1);
    }

    @Test
    @DisplayName("backlog_itemsテーブルに期待されるカラムが存在すること")
    void shouldBacklogItemsHaveExpectedColumns() {
      assertThat(columns("backlog_items"))
          .containsExactly(
              "created_at",
              "creator_employee_id",
              "creator_name",
              "description",
              "id",
              "priority",
              "quality_category",
              "scrum_team_id",
              "sprint_id",
              "status",
              "story_point",
              "title",
              "updated_at",
              "value",
              "version");
    }

    @Test
    @DisplayName("Flywayマイグレーションスクリプトにstatusのチェック制約が定義されていること")
    void shouldMigrationScriptDefineStatusCheckConstraint() {
      // テストプロファイルは Hibernate create-drop でスキーマを生成するため、
      // Flyway の CHECK 制約は実行時DBに現れない。スクリプト自体の内容で検証する。
      String script =
          readClasspathResource("db/migration/ag/V20261116150000__add_backlog_items_tables.sql");
      assertThat(script)
          .contains("CONSTRAINT ck_backlog_items_status CHECK (status IN ('SUGGESTED', 'ACCEPTED', 'REJECTED'))");
    }

    @Test
    @DisplayName("FlywayマイグレーションスクリプトにvalueカラムとCHECK制約が定義されていること")
    void shouldMigrationScriptDefineValueColumnAndCheckConstraint() {
      String script =
          readClasspathResource("db/migration/ag/V20261116160000__add_backlog_item_value.sql");
      assertThat(script)
          .contains("ADD COLUMN value INTEGER")
          .contains("CONSTRAINT ck_backlog_items_value CHECK (value IS NULL OR (value >= 0 AND value <= 100))");
    }

    @Test
    @DisplayName("Flywayマイグレーションスクリプトにquality_categoryカラムが定義されていること")
    void shouldMigrationScriptDefineQualityCategoryColumn() {
      String script =
          readClasspathResource("db/migration/ag/V20261117160000__add_quality_category_to_backlog_items.sql");
      assertThat(script).contains("ADD COLUMN quality_category VARCHAR(20)");
    }

    @Test
    @DisplayName("Flywayマイグレーションスクリプトでemployee_idカラムがBIGINTへ変更されること")
    void shouldMigrationScriptAlterEmployeeIdsToBigint() {
      String script =
          readClasspathResource("db/migration/ag/V20261117180000__alter_backlog_employee_ids_to_bigint.sql");
      assertThat(script)
          .contains("ALTER COLUMN creator_employee_id TYPE BIGINT")
          .contains("ALTER COLUMN author_employee_id TYPE BIGINT");
    }

    @Test
    @DisplayName("FlywayマイグレーションスクリプトでREJECTEDがFROZENに置換されること")
    void shouldMigrationScriptReplaceRejectedWithFrozen() {
      String script =
          readClasspathResource("db/migration/ag/V20261117190000__replace_rejected_with_frozen.sql");
      assertThat(script)
          .contains("DROP CONSTRAINT ck_backlog_items_status")
          .contains("SET status = 'FROZEN' WHERE status = 'REJECTED'")
          .contains("ADD CONSTRAINT ck_backlog_items_status CHECK (status IN ('SUGGESTED', 'ACCEPTED', 'FROZEN'))");
    }

    @Test
    @DisplayName("Flywayマイグレーションスクリプトでadmin_employee_subsetsテーブルが作成されること")
    void shouldMigrationScriptCreateAdminEmployeeSubsets() {
      String script =
          readClasspathResource("db/migration/ag/V20261118200000__add_admin_employee_subsets.sql");
      assertThat(script)
          .contains("CREATE TABLE undecided_ag.admin_employee_subsets")
          .contains("employee_id BIGINT       NOT NULL PRIMARY KEY")
          .contains("updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP");
    }

    @Test
    @DisplayName("Flywayマイグレーションスクリプトでversionカラムが追加されること")
    void shouldMigrationScriptAddVersionColumn() {
      String script =
          readClasspathResource("db/migration/ag/V20261118210000__add_version_to_backlog_items.sql");
      assertThat(script)
          .contains("ADD COLUMN version INTEGER")
          .contains("DEFAULT 0");
    }

    private String readClasspathResource(String path) {
      try {
        return new ClassPathResource(path).getContentAsString(StandardCharsets.UTF_8);
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }
  }
}
