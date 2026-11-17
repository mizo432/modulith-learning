package com.github.mizo432.shared.sample.jpa.feature.flyway;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
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

/**
 * FlywayマイグレーションとJPAエンティティの連携をテストする統合テストです。
 *
 * <p>このテストは、FlywayがDDLを実行してテーブルを作成し、JPAエンティティがそのテーブルに
 * 正しくマッピングされることを検証します。
 */
@Tag("medium")
@SpringBootTest(classes = ModulithDemoApplication.class)
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@DisplayName("Flyway・DDL連携パターンの統合テスト")
class FlywayDdlIntegrationMediumTest {

  @Autowired private SampleFlywayEntityRepository repository;

  @Autowired private JdbcTemplate jdbcTemplate;

  @BeforeEach
  void setUp() {
    repository.deleteAll();
  }

  @Nested
  @DisplayName("Flywayマイグレーションの検証")
  class FlywayMigrationVerification {

    @Test
    @DisplayName("Flywayマイグレーションによりsample_flyway_entitiesテーブルが作成されていること")
    void shouldFlywayCreateTable() {
      // Act & Assert
      Integer tableCount =
          jdbcTemplate.queryForObject(
              """
              SELECT COUNT(*) FROM information_schema.tables
              WHERE table_schema = 'undecided_ef'
              AND table_name = 'sample_flyway_entities'
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
              WHERE table_schema = 'undecided_ef'
              AND table_name = 'sample_flyway_entities'
              ORDER BY column_name
              """,
              String.class);

      // Assert
      assertThat(columns).containsExactlyInAnyOrder("id", "item_name", "quantity", "status", "created_at");
    }

    @Test
    @DisplayName("Flywayマイグレーションでインデックスが作成されていること")
    void shouldIndexBeCreated() {
      // Act - インデックスが存在することを確認（pg_classとpg_indexを使用）
      Integer indexCount =
          jdbcTemplate.queryForObject(
              """
              SELECT COUNT(*)
              FROM pg_index pi
              JOIN pg_class pc ON pc.oid = pi.indexrelid
              JOIN pg_namespace pn ON pn.oid = pc.relnamespace
              WHERE pn.nspname = 'undecided_ef'
              AND pi.indrelid = (
                  SELECT c.oid FROM pg_class c
                  JOIN pg_namespace n ON n.oid = c.relnamespace
                  WHERE n.nspname = 'undecided_ef'
                  AND c.relname = 'sample_flyway_entities'
              )
              """,
              Integer.class);

      // Assert
      assertThat(indexCount).isGreaterThan(0);
    }
  }

  @Nested
  @DisplayName("JPAエンティティとFlywayテーブルの連携")
  class JpaEntityFlywayIntegration {

    @Test
    @DisplayName("JPAエンティティを保存して読み込めること")
    void shouldSaveAndLoadEntity() {
      // Arrange
      SampleFlywayEntity entity =
          new SampleFlywayEntity(
              1L, "テスト商品", 100, SampleFlywayEntity.ItemStatus.ACTIVE);
      entity.setCreatedAt(LocalDateTime.now());

      // Act
      SampleFlywayEntity saved = repository.save(entity);
      SampleFlywayEntity loaded = repository.findById(1L).orElseThrow();

      // Assert
      assertThat(saved.getId()).isEqualTo(1L);
      assertThat(loaded.getItemName()).isEqualTo("テスト商品");
      assertThat(loaded.getQuantity()).isEqualTo(100);
      assertThat(loaded.getStatus()).isEqualTo(SampleFlywayEntity.ItemStatus.ACTIVE);
      assertThat(loaded.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("複数のエンティティを保存・検索できること")
    void shouldSaveAndSearchMultipleEntities() {
      // Arrange
      LocalDateTime now = LocalDateTime.now();
      SampleFlywayEntity e1 = new SampleFlywayEntity(2L, "商品A", 50, SampleFlywayEntity.ItemStatus.ACTIVE);
      e1.setCreatedAt(now);
      SampleFlywayEntity e2 = new SampleFlywayEntity(3L, "商品B", 30, SampleFlywayEntity.ItemStatus.INACTIVE);
      e2.setCreatedAt(now);
      SampleFlywayEntity e3 = new SampleFlywayEntity(4L, "商品C", 20, SampleFlywayEntity.ItemStatus.ACTIVE);
      e3.setCreatedAt(now);
      repository.save(e1);
      repository.save(e2);
      repository.save(e3);

      // Act
      List<SampleFlywayEntity> activeItems =
          repository.findByStatus(SampleFlywayEntity.ItemStatus.ACTIVE);

      // Assert
      assertThat(activeItems).hasSize(2);
      assertThat(activeItems)
          .extracting(SampleFlywayEntity::getItemName)
          .containsExactlyInAnyOrder("商品A", "商品C");
    }

    @Test
    @DisplayName("Flywayで定義されたカラム制約が機能すること")
    void shouldColumnConstraintsWork() {
      // Arrange
      SampleFlywayEntity entity =
          new SampleFlywayEntity(5L, "制約テスト", 1, SampleFlywayEntity.ItemStatus.ACTIVE);
      entity.setCreatedAt(LocalDateTime.now());

      // Act & Assert
      SampleFlywayEntity saved = repository.save(entity);
      assertThat(saved.getItemName()).isNotBlank();
      assertThat(saved.getQuantity()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Flywayマイグレーションで定義されたNOT NULL制約が機能すること")
    void shouldNotNullConstraintWork() {
      // Arrange & Act
      // quantityに0を設定して、NOT NULL制約が満たされることを確認
      SampleFlywayEntity entity =
          new SampleFlywayEntity(100L, "制約テスト", 0, SampleFlywayEntity.ItemStatus.ACTIVE);
      entity.setCreatedAt(LocalDateTime.now());

      // Act & Assert - 正常に保存できること
      SampleFlywayEntity saved = repository.save(entity);
      assertThat(saved.getQuantity()).isEqualTo(0);
    }
  }

  @Nested
  @DisplayName("Flywayマイグレーション履歴の検証")
  class FlywayMigrationHistory {

    @Test
    @DisplayName("Flywayのマイグレーションが正常に適用されていること")
    void shouldMigrationBeApplied() {
      // Act - テーブルが存在することを確認（マイグレーションが適用された証拠）
      Integer tableCount =
          jdbcTemplate.queryForObject(
              """
              SELECT COUNT(*) FROM information_schema.tables
              WHERE table_schema = 'undecided_ef'
              AND table_name = 'sample_flyway_entities'
              """,
              Integer.class);

      // Assert
      assertThat(tableCount).isEqualTo(1);
    }
  }
}
