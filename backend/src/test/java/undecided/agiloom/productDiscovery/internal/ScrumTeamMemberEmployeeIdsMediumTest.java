package undecided.agiloom.productDiscovery.internal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
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
import undecided.agiloom.productDiscovery.spi.Product;
import undecided.agiloom.productDiscovery.spi.ProductStatus;
import undecided.agiloom.productDiscovery.spi.ScrumTeam;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * memberEmployeeIds（BIGINT[]）の保存・読込をテストする統合テストです。
 *
 * <p>Flyway V20261120100000 による TEXT[] → BIGINT[] 移行と、
 * SnowflakeId.SnowflakeIdListConverter の動作を検証します。
 * ScrumTeamSubset のテストは {@link undecided.agiloom.requirementBacklog.internal.ScrumTeamSubsetMemberEmployeeIdsMediumTest} に分離されています。
 */
@Tag("medium")
@SpringBootTest(classes = ModulithDemoApplication.class)
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@DisplayName("ScrumTeam memberEmployeeIds BIGINT[] 保存・読込の統合テスト")
class ScrumTeamMemberEmployeeIdsMediumTest {

  @Autowired private ScrumTeamRepository scrumTeamRepository;

  @Autowired private ProductRepository productRepository;

  @Autowired private JdbcTemplate jdbcTemplate;

  @BeforeEach
  void setUp() {
    scrumTeamRepository.deleteAll();
    productRepository.deleteAll();
  }

  private Product createAndSaveProduct() {
    Product product = Product.builder()
        .id(SnowflakeId.of(100L))
        .name("テストプロダクト")
        .description("テスト用のプロダクト")
        .status(ProductStatus.ACTIVE)
        .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
    return productRepository.save(product);
  }

  @Nested
  @DisplayName("ScrumTeam の memberEmployeeIds 保存・読込")
  class ScrumTeamMemberEmployeeIds {

    @Test
    @DisplayName("memberEmployeeIds付きScrumTeamを保存し、再取得して一致すること")
    void shouldSaveAndLoadScrumTeamWithMemberEmployeeIds() {
      // Arrange
      Product product = createAndSaveProduct();
      List<SnowflakeId> memberIds = List.of(
          SnowflakeId.of(1001L),
          SnowflakeId.of(1002L),
          SnowflakeId.of(1003L)
      );

      ScrumTeam scrumTeam = ScrumTeam.builder()
          .id(SnowflakeId.of(200L))
          .name("テストチーム")
          .businessDomain("テスト領域")
          .poEmployeeId(SnowflakeId.of(1001L))
          .poName("テストPO")
          .smEmployeeId(SnowflakeId.of(1002L))
          .smName("テストSM")
          .memberEmployeeIds(memberIds)
          .product(product)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

      // Act
      ScrumTeam saved = scrumTeamRepository.save(scrumTeam);
      ScrumTeam loaded = scrumTeamRepository.findById(SnowflakeId.of(200L)).orElseThrow();

      // Assert
      assertThat(loaded.getMemberEmployeeIds()).hasSize(3);
      assertThat(loaded.getMemberEmployeeIds())
          .containsExactly(
              SnowflakeId.of(1001L),
              SnowflakeId.of(1002L),
              SnowflakeId.of(1003L)
          );
    }

    @Test
    @DisplayName("memberEmployeeIdsが空のリストの場合、空配列として保存・読込されること")
    void shouldSaveAndLoadEmptyMemberEmployeeIds() {
      // Arrange
      Product product = createAndSaveProduct();

      ScrumTeam scrumTeam = ScrumTeam.builder()
          .id(SnowflakeId.of(201L))
          .name("メンバーなしチーム")
          .businessDomain("テスト領域")
          .product(product)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

      // Act
      scrumTeamRepository.save(scrumTeam);
      ScrumTeam loaded = scrumTeamRepository.findById(SnowflakeId.of(201L)).orElseThrow();

      // Assert
      assertThat(loaded.getMemberEmployeeIds()).isEmpty();
    }

    @Test
    @DisplayName("setMemberEmployeeIdsで更新後、DBに反映されて再取得できること")
    void shouldUpdateMemberEmployeeIds() {
      // Arrange
      Product product = createAndSaveProduct();

      ScrumTeam scrumTeam = ScrumTeam.builder()
          .id(SnowflakeId.of(202L))
          .name("更新テストチーム")
          .businessDomain("テスト領域")
          .product(product)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
      scrumTeamRepository.save(scrumTeam);

      // Act
      List<SnowflakeId> newMemberIds = List.of(
          SnowflakeId.of(2001L),
          SnowflakeId.of(2002L)
      );
      scrumTeam.setMemberEmployeeIds(newMemberIds, LocalDateTime.now());
      scrumTeamRepository.save(scrumTeam);

      ScrumTeam loaded = scrumTeamRepository.findById(SnowflakeId.of(202L)).orElseThrow();

      // Assert
      assertThat(loaded.getMemberEmployeeIds()).hasSize(2);
      assertThat(loaded.getMemberEmployeeIds())
          .containsExactly(SnowflakeId.of(2001L), SnowflakeId.of(2002L));
    }

    @Test
    @DisplayName("DBのmember_employee_idsカラムがBIGINT[]型であること")
    void shouldMemberEmployeeIdsColumnBeBigintArray() {
      // Act - 配列の要素型を確認（pg_type.typidはPG16+で削除されたためoidを使用）
      String elementType = jdbcTemplate.queryForObject(
          """
          SELECT pg_catalog.format_type(e.oid, a.atttypmod)
          FROM pg_catalog.pg_attribute a
          JOIN pg_catalog.pg_type e ON a.atttypid = e.oid
          WHERE a.attrelid = 'undecided_ag.scrum_teams'::regclass
          AND a.attname = 'member_employee_ids'
          """,
          String.class);

      // Assert
      assertThat(elementType).isEqualTo("bigint[]");
    }
  }

  @Nested
  @DisplayName("M-2: SnowflakeIdConverter と SnowflakeIdListConverter の使い分け")
  class ConverterUsage {

    @Test
    @DisplayName("単体SnowflakeIdフィールドはSnowflakeIdConverterでBIGINTとして保存・読込されること")
    void shouldSingleSnowflakeIdUseSnowflakeIdConverter() {
      // Arrange
      Product product = createAndSaveProduct();

      ScrumTeam scrumTeam = ScrumTeam.builder()
          .id(SnowflakeId.of(400L))
          .name("コンバーターテストチーム")
          .businessDomain("テスト領域")
          .poEmployeeId(SnowflakeId.of(5001L))
          .poName("PO")
          .smEmployeeId(SnowflakeId.of(5002L))
          .smName("SM")
          .product(product)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

      // Act
      scrumTeamRepository.save(scrumTeam);
      ScrumTeam loaded = scrumTeamRepository.findById(SnowflakeId.of(400L)).orElseThrow();

      // Assert - 単体SnowflakeIdフィールドが正しく保存・読込される（SnowflakeIdConverter動作確認）
      assertThat(loaded.getPoEmployeeId()).isEqualTo(SnowflakeId.of(5001L));
      assertThat(loaded.getSmEmployeeId()).isEqualTo(SnowflakeId.of(5002L));

      // DBカラム型がbigintであることを確認
      List<String> columnTypes = jdbcTemplate.queryForList(
          """
          SELECT column_name || ':' || data_type AS col_type FROM information_schema.columns
          WHERE table_schema = 'undecided_ag'
          AND table_name = 'scrum_teams'
          AND column_name IN ('po_employee_id', 'sm_employee_id')
          ORDER BY column_name
          """,
          String.class);
      assertThat(columnTypes).contains("po_employee_id:bigint");
      assertThat(columnTypes).contains("sm_employee_id:bigint");
    }

    @Test
    @DisplayName("List<SnowflakeId>フィールドはSnowflakeIdListConverterでBIGINT[]として保存・読込されること")
    void shouldListSnowflakeIdUseSnowflakeIdListConverter() {
      // Arrange
      Product product = createAndSaveProduct();
      List<SnowflakeId> memberIds = List.of(
          SnowflakeId.of(6001L),
          SnowflakeId.of(6002L),
          SnowflakeId.of(6003L)
      );

      ScrumTeam scrumTeam = ScrumTeam.builder()
          .id(SnowflakeId.of(401L))
          .name("リストコンバーターテストチーム")
          .businessDomain("テスト領域")
          .memberEmployeeIds(memberIds)
          .product(product)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

      // Act
      scrumTeamRepository.save(scrumTeam);
      ScrumTeam loaded = scrumTeamRepository.findById(SnowflakeId.of(401L)).orElseThrow();

      // Assert - List<SnowflakeId>が正しく保存・読込される（SnowflakeIdListConverter動作確認）
      assertThat(loaded.getMemberEmployeeIds()).hasSize(3);
      assertThat(loaded.getMemberEmployeeIds())
          .containsExactly(SnowflakeId.of(6001L), SnowflakeId.of(6002L), SnowflakeId.of(6003L));

      // DBカラム型がbigint[]であることを確認
      String elementType = jdbcTemplate.queryForObject(
          """
          SELECT pg_catalog.format_type(e.oid, a.atttypmod)
          FROM pg_catalog.pg_attribute a
          JOIN pg_catalog.pg_type e ON a.atttypid = e.oid
          WHERE a.attrelid = 'undecided_ag.scrum_teams'::regclass
          AND a.attname = 'member_employee_ids'
          """,
          String.class);
      assertThat(elementType).isEqualTo("bigint[]");
    }

    @Test
    @DisplayName("単体SnowflakeIdとList<SnowflakeId>が同じエンティティで共存して正しく動作すること")
    void shouldBothConvertersCoexistInSameEntity() {
      // Arrange
      Product product = createAndSaveProduct();
      List<SnowflakeId> memberIds = List.of(
          SnowflakeId.of(7001L),
          SnowflakeId.of(7002L)
      );

      ScrumTeam scrumTeam = ScrumTeam.builder()
          .id(SnowflakeId.of(402L))
          .name("共存テストチーム")
          .businessDomain("テスト領域")
          .poEmployeeId(SnowflakeId.of(7001L))
          .poName("PO")
          .smEmployeeId(SnowflakeId.of(7002L))
          .smName("SM")
          .memberEmployeeIds(memberIds)
          .product(product)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

      // Act
      scrumTeamRepository.save(scrumTeam);
      ScrumTeam loaded = scrumTeamRepository.findById(SnowflakeId.of(402L)).orElseThrow();

      // Assert - 単体フィールド（SnowflakeIdConverter）
      assertThat(loaded.getPoEmployeeId()).isEqualTo(SnowflakeId.of(7001L));
      assertThat(loaded.getSmEmployeeId()).isEqualTo(SnowflakeId.of(7002L));

      // Assert - リストフィールド（SnowflakeIdListConverter）
      assertThat(loaded.getMemberEmployeeIds()).hasSize(2);
      assertThat(loaded.getMemberEmployeeIds())
          .containsExactly(SnowflakeId.of(7001L), SnowflakeId.of(7002L));
    }
  }

  @Nested
  @DisplayName("Md-1: memberEmployeeIdsのデフォルト動作確認")
  class MigrationDefaultConstraint {

    @Test
    @DisplayName("memberEmployeeIdsを指定せずに保存すると、空配列として保存・読込されること")
    void shouldDefaultToEmptyArrayWhenNotSpecified() {
      // Arrange - memberEmployeeIdsを明示的に設定せずに保存（@Builder.Default で空リスト）
      Product product = createAndSaveProduct();

      ScrumTeam scrumTeam = ScrumTeam.builder()
          .id(SnowflakeId.of(500L))
          .name("デフォルトテストチーム")
          .businessDomain("テスト領域")
          .product(product)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

      // Act
      scrumTeamRepository.save(scrumTeam);
      ScrumTeam loaded = scrumTeamRepository.findById(SnowflakeId.of(500L)).orElseThrow();

      // Assert - @Builder.Default で空リストがセットされ、SnowflakeIdListConverterは new Long[0] に変換
      // そのためDBには NULL ではなく空配列 {} が保存される
      assertThat(loaded.getMemberEmployeeIds()).isEmpty();
    }

    @Test
    @DisplayName("V20261119220000/V20261119230000でカラムがDEFAULT '{}'付きで追加されていることを確認（マイグレーションファイル検証）")
    void shouldMigrationFilesHaveDefaultEmptyArray() throws Exception {
      // Flywayマイグレーションファイルに DEFAULT '{}' が含まれていることを確認（classpathから読み込み）
      java.net.URL v22Url = getClass().getClassLoader()
          .getResource("db/migration/ag/V20261119220000__add_member_employee_ids_to_scrum_teams.sql");
      java.net.URL v23Url = getClass().getClassLoader()
          .getResource("db/migration/ag/V20261119230000__add_member_employee_ids_to_scrum_team_subsets.sql");

      assertThat(v22Url).isNotNull();
      assertThat(v23Url).isNotNull();

      String content22 = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(v22Url.toURI())));
      String content23 = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(v23Url.toURI())));

      // Assert - 両方のマイグレーションに DEFAULT '{}' が含まれる
      assertThat(content22).contains("DEFAULT '{}'");
      assertThat(content23).contains("DEFAULT '{}'");
    }
  }

  @Nested
  @DisplayName("V20261120100000 マイグレーション実行検証")
  class MigrationExecutionVerification {

    private String scratchSchema;

    @BeforeEach
    void createScratchSchema() {
      scratchSchema = "scratch_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
      jdbcTemplate.execute("CREATE SCHEMA " + scratchSchema);
    }

    @AfterEach
    void dropScratchSchema() {
      jdbcTemplate.execute("DROP SCHEMA " + scratchSchema + " CASCADE");
    }

    @Test
    @DisplayName("マイグレーションSQLを実行してTEXT[]からBIGINT[]へ変換されること")
    void shouldMigrationConvertTextArrayToBigintArray() throws Exception {
      // Arrange - scrum_teams と scrum_team_subsets をスクラッチスキーマに作成
      jdbcTemplate.execute(
          "CREATE TABLE " + scratchSchema + ".scrum_teams (" +
          "id BIGINT PRIMARY KEY, " +
          "name VARCHAR(100) NOT NULL, " +
          "member_employee_ids TEXT[] DEFAULT '{}'::text[])");

      jdbcTemplate.execute(
          "CREATE TABLE " + scratchSchema + ".scrum_team_subsets (" +
          "id BIGINT PRIMARY KEY, " +
          "name VARCHAR(100) NOT NULL, " +
          "member_employee_ids TEXT[] DEFAULT '{}'::text[])");

      // 数値文字列の配列の行と空配列の行を投入
      jdbcTemplate.update(
          "INSERT INTO " + scratchSchema + ".scrum_teams (id, name, member_employee_ids) VALUES " +
          "(1, 'team-with-values', ARRAY['1001', '1002', '1003']::text[]), " +
          "(2, 'team-empty', '{}'::text[])");

      jdbcTemplate.update(
          "INSERT INTO " + scratchSchema + ".scrum_team_subsets (id, name, member_employee_ids) VALUES " +
          "(1, 'subset-with-values', ARRAY['2001', '2002']::text[]), " +
          "(2, 'subset-empty', '{}'::text[])");

      // Act - classpathからV20261120100000のSQLを読み込み、スキーマ名を置換して実行
      java.net.URL migrationUrl = getClass().getClassLoader()
          .getResource("db/migration/ag/V20261120100000__alter_member_employee_ids_to_bigint.sql");
      assertThat(migrationUrl).isNotNull();

      String migrationSql = new String(
          java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(migrationUrl.toURI())),
          StandardCharsets.UTF_8);
      String replacedSql = migrationSql.replace("undecided_ag", scratchSchema);

      // コメント行を除去してからセミコロンで分割（マルチライン文を保持）
      String withoutComments = java.util.Arrays.stream(replacedSql.split("\n"))
          .filter(line -> !line.trim().startsWith("--"))
          .reduce("", (a, b) -> a + b + "\n");

      String[] statements = withoutComments.split(";");
      for (String stmt : statements) {
        String trimmed = stmt.trim();
        if (!trimmed.isEmpty()) {
          jdbcTemplate.execute(trimmed);
        }
      }

      // Assert - 両テーブルの列型がbigint[]であること
      String scrumTeamsType = jdbcTemplate.queryForObject(
          "SELECT pg_catalog.format_type(e.oid, a.atttypmod) " +
          "FROM pg_catalog.pg_attribute a " +
          "JOIN pg_catalog.pg_type e ON a.atttypid = e.oid " +
          "WHERE a.attrelid = '" + scratchSchema + ".scrum_teams'::regclass " +
          "AND a.attname = 'member_employee_ids'",
          String.class);
      assertThat(scrumTeamsType).isEqualTo("bigint[]");

      String subsetsType = jdbcTemplate.queryForObject(
          "SELECT pg_catalog.format_type(e.oid, a.atttypmod) " +
          "FROM pg_catalog.pg_attribute a " +
          "JOIN pg_catalog.pg_type e ON a.atttypid = e.oid " +
          "WHERE a.attrelid = '" + scratchSchema + ".scrum_team_subsets'::regclass " +
          "AND a.attname = 'member_employee_ids'",
          String.class);
      assertThat(subsetsType).isEqualTo("bigint[]");

      // Assert - 既存行の値が保持されること（PgArrayとして読み込み）
      List<Map<String, Object>> scrumTeamsRows = jdbcTemplate.queryForList(
          "SELECT id, member_employee_ids FROM " + scratchSchema + ".scrum_teams ORDER BY id");
      assertThat(scrumTeamsRows).hasSize(2);

      // 値がある行の配列要素を確認
      Map<String, Object> firstRow = scrumTeamsRows.get(0);
      java.sql.Array firstArray = (java.sql.Array) firstRow.get("member_employee_ids");
      Long[] firstValues = (Long[]) firstArray.getArray();
      assertThat(firstValues).containsExactly(1001L, 1002L, 1003L);

      // 空配列の行を確認
      Map<String, Object> secondRow = scrumTeamsRows.get(1);
      java.sql.Array secondArray = (java.sql.Array) secondRow.get("member_employee_ids");
      Object[] secondValues = (Object[]) secondArray.getArray();
      assertThat(secondValues).isEmpty();

      List<Map<String, Object>> subsetsRows = jdbcTemplate.queryForList(
          "SELECT id, member_employee_ids FROM " + scratchSchema + ".scrum_team_subsets ORDER BY id");
      assertThat(subsetsRows).hasSize(2);

      Map<String, Object> firstSubsetRow = subsetsRows.get(0);
      java.sql.Array firstSubsetArray = (java.sql.Array) firstSubsetRow.get("member_employee_ids");
      Long[] firstSubsetValues = (Long[]) firstSubsetArray.getArray();
      assertThat(firstSubsetValues).containsExactly(2001L, 2002L);

      // Assert - member_employee_idsを省略してINSERTした行がNULLではなく空のbigint[]であること
      // INSERT ... RETURNING で直接確認
      jdbcTemplate.update(
          "INSERT INTO " + scratchSchema + ".scrum_teams (id, name) VALUES (999, 'default-check')");
      Map<String, Object> defaultRow = jdbcTemplate.queryForMap(
          "SELECT member_employee_ids FROM " + scratchSchema + ".scrum_teams WHERE id = 999");
      Object insertedDefault = defaultRow.get("member_employee_ids");
      assertThat(insertedDefault).isNotNull();
      java.sql.Array defaultArray = (java.sql.Array) insertedDefault;
      Object[] defaultValues = (Object[]) defaultArray.getArray();
      assertThat(defaultValues).isEmpty();

      jdbcTemplate.update(
          "INSERT INTO " + scratchSchema + ".scrum_team_subsets (id, name) VALUES (999, 'default-check')");
      Map<String, Object> defaultSubsetRow = jdbcTemplate.queryForMap(
          "SELECT member_employee_ids FROM " + scratchSchema + ".scrum_team_subsets WHERE id = 999");
      Object insertedDefaultSubset = defaultSubsetRow.get("member_employee_ids");
      assertThat(insertedDefaultSubset).isNotNull();
      java.sql.Array defaultSubsetArray = (java.sql.Array) insertedDefaultSubset;
      Object[] defaultSubsetValues = (Object[]) defaultSubsetArray.getArray();
      assertThat(defaultSubsetValues).isEmpty();
    }
  }
}
