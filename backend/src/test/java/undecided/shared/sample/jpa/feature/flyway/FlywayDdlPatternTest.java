package undecided.shared.sample.jpa.feature.flyway;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.Table;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("Flyway・DDL連携パターンの単体テスト")
class FlywayDdlPatternTest {

  @Nested
  @DisplayName("SchemaSampleFlywayのテスト")
  class SchemaSampleFlywayTest {

    @Test
    @DisplayName("SchemaSampleFlyway.NAMEはundecided_efである")
    void shouldSchemaNameBeUndecidedEf() {
      // Assert
      assertThat(SchemaSampleFlyway.NAME).isEqualTo("undecided_ef");
    }
  }

  @Nested
  @DisplayName("SampleFlywayEntityのテスト")
  class SampleFlywayEntityTest {

    @Test
    @DisplayName("SampleFlywayEntityが正常に作成される")
    void shouldCreateSampleFlywayEntity() {
      // Arrange
      Long id = 1L;
      String itemName = "テストアイテム";
      Integer quantity = 100;
      SampleFlywayEntity.ItemStatus status = SampleFlywayEntity.ItemStatus.ACTIVE;

      // Act
      SampleFlywayEntity entity = new SampleFlywayEntity(id, itemName, quantity, status);

      // Assert
      assertThat(entity).isNotNull();
      assertThat(entity.getId()).isEqualTo(id);
      assertThat(entity.getItemName()).isEqualTo(itemName);
      assertThat(entity.getQuantity()).isEqualTo(quantity);
      assertThat(entity.getStatus()).isEqualTo(status);
    }

    @Test
    @DisplayName("@Table.schemaはSchemaSampleFlyway.NAMEに設定されている")
    void shouldTableSchemaBeSet() {
      // Arrange & Act
      Table table = SampleFlywayEntity.class.getAnnotation(Table.class);

      // Assert
      assertThat(table).isNotNull();
      assertThat(table.schema()).isEqualTo(SchemaSampleFlyway.NAME);
      assertThat(table.name()).isEqualTo("sample_flyway_entities");
    }

    @Test
    @DisplayName("SampleFlywayEntityのステータスenumが正しくマッピングされる")
    void shouldStatusEnumBeMapped() {
      // Arrange & Act
      SampleFlywayEntity entity =
          new SampleFlywayEntity(1L, "アイテム", 50, SampleFlywayEntity.ItemStatus.ACTIVE);

      // Assert
      assertThat(entity.getStatus()).isEqualTo(SampleFlywayEntity.ItemStatus.ACTIVE);
    }

    @Test
    @DisplayName("複数のSampleFlywayEntityが作成される")
    void shouldCreateMultipleEntities() {
      // Act
      SampleFlywayEntity entity1 =
          new SampleFlywayEntity(1L, "アイテム1", 10, SampleFlywayEntity.ItemStatus.ACTIVE);
      SampleFlywayEntity entity2 =
          new SampleFlywayEntity(2L, "アイテム2", 20, SampleFlywayEntity.ItemStatus.INACTIVE);

      // Assert
      assertThat(entity1.getId()).isNotEqualTo(entity2.getId());
      assertThat(entity1.getItemName()).isNotEqualTo(entity2.getItemName());
      assertThat(entity1.getStatus()).isNotEqualTo(entity2.getStatus());
    }
  }
}
