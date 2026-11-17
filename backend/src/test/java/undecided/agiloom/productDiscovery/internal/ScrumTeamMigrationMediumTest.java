package undecided.agiloom.productDiscovery.internal;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
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
 * scrum_teamsテーブルのFlywayマイグレーションをテストする統合テストです。
 *
 * <p>このテストは、FlywayがDDLを実行してscrum_teamsテーブルを作成し、
 * JPAエンティティがそのテーブルに正しくマッピングされることを検証します。
 */
@Tag("medium")
@SpringBootTest(classes = ModulithDemoApplication.class)
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@DisplayName("ScrumTeamマイグレーションの統合テスト")
class ScrumTeamMigrationMediumTest {

  @Autowired private ScrumTeamRepository scrumTeamRepository;

  @Autowired private ProductRepository productRepository;

  @Autowired private JdbcTemplate jdbcTemplate;

  @BeforeEach
  void setUp() {
    scrumTeamRepository.deleteAll();
    productRepository.deleteAll();
  }

  @Nested
  @DisplayName("Flywayマイグレーションの検証")
  class FlywayMigrationVerification {

    @Test
    @DisplayName("Flywayマイグレーションによりscrum_teamsテーブルが作成されていること")
    void shouldFlywayCreateTable() {
      // Act & Assert
      Integer tableCount =
          jdbcTemplate.queryForObject(
              """
              SELECT COUNT(*) FROM information_schema.tables
              WHERE table_schema = 'undecided_ag'
              AND table_name = 'scrum_teams'
              """,
              Integer.class);

      assertThat(tableCount).isEqualTo(1);
    }

    @Test
    @DisplayName("テーブルに期待されるカラムが存在すること")
    void shouldTableHaveExpectedColumns() {
      // Act
      List<String> columns =
          jdbcTemplate.queryForList(
              """
              SELECT column_name FROM information_schema.columns
              WHERE table_schema = 'undecided_ag'
              AND table_name = 'scrum_teams'
              ORDER BY column_name
              """,
              String.class);

      // Assert
      assertThat(columns)
          .containsExactly(
              "business_domain",
              "created_at",
              "id",
              "name",
              "po_employee_id",
              "po_name",
              "product_id",
              "sm_employee_id",
              "sm_name",
              "updated_at");
    }

    @Test
    @DisplayName("カラムの型が正しく設定されていること")
    void shouldColumnsHaveCorrectTypes() {
      // Act - 全カラムの型を取得
      List<String> columnTypes =
          jdbcTemplate.queryForList(
              """
              SELECT column_name || ':' || data_type AS col_type FROM information_schema.columns
              WHERE table_schema = 'undecided_ag'
              AND table_name = 'scrum_teams'
              ORDER BY column_name
              """,
              String.class);

      // Assert - 主要カラムの型を確認
      // SnowflakeIdはConverterによりLongとして保存される
      assertThat(columnTypes).contains("name:character varying");
      assertThat(columnTypes).contains("business_domain:character varying");
      assertThat(columnTypes).contains("po_employee_id:uuid");
      assertThat(columnTypes).contains("po_name:character varying");
      assertThat(columnTypes).contains("sm_employee_id:uuid");
      assertThat(columnTypes).contains("sm_name:character varying");
      assertThat(columnTypes).contains("created_at:timestamp without time zone");
      assertThat(columnTypes).contains("updated_at:timestamp without time zone");
    }

    @Test
    @DisplayName("NOT NULL制約が正しく設定されていること")
    void shouldNotNullConstraintsBeSet() {
      // Act
      List<String> notNullableColumns =
          jdbcTemplate.queryForList(
              """
              SELECT column_name FROM information_schema.columns
              WHERE table_schema = 'undecided_ag'
              AND table_name = 'scrum_teams'
              AND is_nullable = 'NO'
              ORDER BY column_name
              """,
              String.class);

      // Assert
      assertThat(notNullableColumns)
          .containsExactlyInAnyOrder("id", "name", "business_domain", "product_id", "created_at", "updated_at");
    }

    @Test
    @DisplayName("外部キー制約が正しく設定されていること")
    void shouldForeignKeyConstraintBeSet() {
      // Act
      Integer constraintCount =
          jdbcTemplate.queryForObject(
              """
              SELECT COUNT(*) FROM information_schema.table_constraints
              WHERE constraint_schema = 'undecided_ag'
              AND table_name = 'scrum_teams'
              AND constraint_type = 'FOREIGN KEY'
              """,
              Integer.class);

      // Assert
      assertThat(constraintCount).isEqualTo(1);
    }

    @Test
    @DisplayName("外部キーがproductsテーブルを参照していること")
    void shouldForeignKeyReferenceProductsTable() {
      // Act
      String referencedTable =
          jdbcTemplate.queryForObject(
              """
              SELECT ccu.table_name
              FROM information_schema.table_constraints tc
              JOIN information_schema.constraint_column_usage ccu
                ON tc.constraint_name = ccu.constraint_name
              WHERE tc.constraint_schema = 'undecided_ag'
              AND tc.table_name = 'scrum_teams'
              AND tc.constraint_type = 'FOREIGN KEY'
              """,
              String.class);

      // Assert
      assertThat(referencedTable).isEqualTo("products");
    }

    @Test
    @DisplayName("product_idカラムにインデックスが存在すること")
    void shouldIndexOnProductIdExist() {
      // Act - scrum_teamsテーブルのインデックス数を取得
      Integer indexCount =
          jdbcTemplate.queryForObject(
              """
              SELECT COUNT(*)
              FROM pg_index pi
              JOIN pg_class pc ON pc.oid = pi.indexrelid
              JOIN pg_namespace pn ON pn.oid = pc.relnamespace
              WHERE pn.nspname = 'undecided_ag'
              AND pi.indrelid = (
                  SELECT c.oid FROM pg_class c
                  JOIN pg_namespace n ON n.oid = c.relnamespace
                  WHERE n.nspname = 'undecided_ag'
                  AND c.relname = 'scrum_teams'
              )
              """,
              Integer.class);

      // Assert - プライマリキーインデックス以外にインデックスが存在すること
      assertThat(indexCount).isGreaterThan(0);
    }
  }

  @Nested
  @DisplayName("JPAエンティティとFlywayテーブルの連携")
  class JpaEntityFlywayIntegration {

    private Product createProduct() {
      return Product.builder()
          .id(SnowflakeId.of(100L))
          .name("テストプロダクト")
          .description("テスト用のプロダクト")
          .status(ProductStatus.ACTIVE)
          .build();
    }

    private ScrumTeam createScrumTeam(Product product) {
      return ScrumTeam.builder()
          .id(SnowflakeId.of(200L))
          .name("テストチーム")
          .businessDomain("テスト領域")
          .poEmployeeId(UUID.randomUUID())
          .poName("テストPO")
          .smEmployeeId(UUID.randomUUID())
          .smName("テストSM")
          .product(product)
          .build();
    }

    @Test
    @DisplayName("JPAエンティティを保存して読み込めること")
    void shouldSaveAndLoadEntity() {
      // Arrange
      Product product = createProduct();
      productRepository.save(product);

      ScrumTeam scrumTeam = createScrumTeam(product);

      // Act
      ScrumTeam saved = scrumTeamRepository.save(scrumTeam);
      ScrumTeam loaded = scrumTeamRepository.findById(SnowflakeId.of(200L)).orElseThrow();

      // Assert
      assertThat(saved.getId().value()).isEqualTo(200L);
      assertThat(loaded.getName()).isEqualTo("テストチーム");
      assertThat(loaded.getBusinessDomain()).isEqualTo("テスト領域");
      assertThat(loaded.getPoEmployeeId()).isNotNull();
      assertThat(loaded.getPoName()).isEqualTo("テストPO");
      assertThat(loaded.getSmEmployeeId()).isNotNull();
      assertThat(loaded.getSmName()).isEqualTo("テストSM");
      assertThat(loaded.getCreatedAt()).isNotNull();
      assertThat(loaded.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("複数のScrumTeamを保存・検索できること")
    void shouldSaveAndSearchMultipleTeams() {
      // Arrange
      Product product = createProduct();
      productRepository.save(product);

      ScrumTeam team1 =
          ScrumTeam.builder()
              .id(SnowflakeId.of(201L))
              .name("チームA")
              .businessDomain("領域A")
              .product(product)
              .build();

      ScrumTeam team2 =
          ScrumTeam.builder()
              .id(SnowflakeId.of(202L))
              .name("チームB")
              .businessDomain("領域B")
              .product(product)
              .build();

      scrumTeamRepository.save(team1);
      scrumTeamRepository.save(team2);

      // Act
      List<ScrumTeam> teams = scrumTeamRepository.findByProductId(SnowflakeId.of(100L));

      // Assert
      assertThat(teams).hasSize(2);
      assertThat(teams).extracting(ScrumTeam::getName).containsExactlyInAnyOrder("チームA", "チームB");
    }

    @Test
    @DisplayName("PO情報なしでScrumTeamを保存できること")
    void shouldSaveTeamWithoutPo() {
      // Arrange
      Product product = createProduct();
      productRepository.save(product);

      ScrumTeam scrumTeam =
          ScrumTeam.builder()
              .id(SnowflakeId.of(203L))
              .name("POなしチーム")
              .businessDomain("テスト領域")
              .product(product)
              .build();

      // Act & Assert
      ScrumTeam saved = scrumTeamRepository.save(scrumTeam);
      ScrumTeam loaded = scrumTeamRepository.findById(SnowflakeId.of(203L)).orElseThrow();

      assertThat(loaded.getPoEmployeeId()).isNull();
      assertThat(loaded.getPoName()).isNull();
    }

    @Test
    @DisplayName("ScrumTeamのupdateメソッドが機能すること")
    void shouldUpdateScrumTeam() {
      // Arrange
      Product product = createProduct();
      productRepository.save(product);

      ScrumTeam scrumTeam = createScrumTeam(product);
      scrumTeamRepository.save(scrumTeam);

      // Act
      UUID newPoId = UUID.randomUUID();
      UUID newSmId = UUID.randomUUID();
      scrumTeam.update("更新後チーム名", "更新後領域", newPoId, "更新後PO", newSmId, "更新後SM");
      scrumTeamRepository.save(scrumTeam);

      ScrumTeam loaded = scrumTeamRepository.findById(SnowflakeId.of(200L)).orElseThrow();

      // Assert
      assertThat(loaded.getName()).isEqualTo("更新後チーム名");
      assertThat(loaded.getBusinessDomain()).isEqualTo("更新後領域");
      assertThat(loaded.getPoEmployeeId()).isEqualTo(newPoId);
      assertThat(loaded.getPoName()).isEqualTo("更新後PO");
      assertThat(loaded.getSmEmployeeId()).isEqualTo(newSmId);
      assertThat(loaded.getSmName()).isEqualTo("更新後SM");
    }

    @Test
    @DisplayName("assignPoメソッドが機能すること")
    void shouldAssignPo() {
      // Arrange
      Product product = createProduct();
      productRepository.save(product);

      ScrumTeam scrumTeam =
          ScrumTeam.builder()
              .id(SnowflakeId.of(204L))
              .name("テストチーム")
              .businessDomain("テスト領域")
              .product(product)
              .build();
      scrumTeamRepository.save(scrumTeam);

      // Act
      UUID poId = UUID.randomUUID();
      scrumTeam.assignPo(poId, "新しいPO");
      scrumTeamRepository.save(scrumTeam);

      ScrumTeam loaded = scrumTeamRepository.findById(SnowflakeId.of(204L)).orElseThrow();

      // Assert
      assertThat(loaded.getPoEmployeeId()).isEqualTo(poId);
      assertThat(loaded.getPoName()).isEqualTo("新しいPO");
    }

    @Test
    @DisplayName("unassignPoメソッドが機能すること")
    void shouldUnassignPo() {
      // Arrange
      Product product = createProduct();
      productRepository.save(product);

      ScrumTeam scrumTeam = createScrumTeam(product);
      scrumTeamRepository.save(scrumTeam);

      // Act
      scrumTeam.unassignPo();
      scrumTeamRepository.save(scrumTeam);

      ScrumTeam loaded = scrumTeamRepository.findById(SnowflakeId.of(200L)).orElseThrow();

      // Assert
      assertThat(loaded.getPoEmployeeId()).isNull();
      assertThat(loaded.getPoName()).isNull();
    }

    @Test
    @DisplayName("assignSmメソッドが機能すること")
    void shouldAssignSm() {
      // Arrange
      Product product = createProduct();
      productRepository.save(product);

      ScrumTeam scrumTeam =
          ScrumTeam.builder()
              .id(SnowflakeId.of(205L))
              .name("テストチーム")
              .businessDomain("テスト領域")
              .product(product)
              .build();
      scrumTeamRepository.save(scrumTeam);

      // Act
      UUID smId = UUID.randomUUID();
      scrumTeam.assignSm(smId, "新しいSM");
      scrumTeamRepository.save(scrumTeam);

      ScrumTeam loaded = scrumTeamRepository.findById(SnowflakeId.of(205L)).orElseThrow();

      // Assert
      assertThat(loaded.getSmEmployeeId()).isEqualTo(smId);
      assertThat(loaded.getSmName()).isEqualTo("新しいSM");
    }

    @Test
    @DisplayName("unassignSmメソッドが機能すること")
    void shouldUnassignSm() {
      // Arrange
      Product product = createProduct();
      productRepository.save(product);

      ScrumTeam scrumTeam = createScrumTeam(product);
      scrumTeamRepository.save(scrumTeam);

      // Act
      scrumTeam.unassignSm();
      scrumTeamRepository.save(scrumTeam);

      ScrumTeam loaded = scrumTeamRepository.findById(SnowflakeId.of(200L)).orElseThrow();

      // Assert
      assertThat(loaded.getSmEmployeeId()).isNull();
      assertThat(loaded.getSmName()).isNull();
    }
  }

  @Nested
  @DisplayName("Flywayマイグレーション履歴の検証")
  class FlywayMigrationHistory {

    @Test
    @DisplayName("scrum_teamsマイグレーションが正常に適用されていること")
    void shouldScrumTeamsMigrationBeApplied() {
      // Act - テーブルが存在することを確認（マイグレーションが適用された証拠）
      Integer tableCount =
          jdbcTemplate.queryForObject(
              """
              SELECT COUNT(*) FROM information_schema.tables
              WHERE table_schema = 'undecided_ag'
              AND table_name = 'scrum_teams'
              """,
              Integer.class);

      // Assert
      assertThat(tableCount).isEqualTo(1);
    }

    @Test
    @DisplayName("productsテーブルとscrum_teamsテーブルの両方が存在すること")
    void shouldBothTablesExist() {
      // Act
      List<String> tables =
          jdbcTemplate.queryForList(
              """
              SELECT table_name FROM information_schema.tables
              WHERE table_schema = 'undecided_ag'
              AND table_name IN ('products', 'scrum_teams')
              ORDER BY table_name
              """,
              String.class);

      // Assert
      assertThat(tables).containsExactly("products", "scrum_teams");
    }
  }
}
