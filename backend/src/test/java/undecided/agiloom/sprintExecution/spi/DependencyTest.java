package undecided.agiloom.sprintExecution.spi;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("Dependencyのテスト")
class DependencyTest {

  private Dependency createDependency() {
    return Dependency.builder()
        .id(SnowflakeId.of(1L))
        .sourceItemId(SnowflakeId.of(100L))
        .targetItemId(SnowflakeId.of(200L))
        .dependencyType(DependencyType.BLOCKS)
        .description("Task A blocks Task B")
        .build();
  }

  @Nested
  @DisplayName("ビルダーのテスト")
  class BuilderTest {

    @Test
    @DisplayName("ビルダーでインスタンスが作成される")
    void shouldCreateInstanceWithBuilder() {
      Dependency dependency = createDependency();

      assertThat(dependency).isNotNull();
      assertThat(dependency.getDependencyType()).isEqualTo(DependencyType.BLOCKS);
      assertThat(dependency.getDescription()).isEqualTo("Task A blocks Task B");
    }
  }

  @Nested
  @DisplayName("updateメソッドのテスト")
  class UpdateMethodTest {

    @Test
    @DisplayName("依存関係タイプと説明が更新される")
    void shouldUpdateDependencyTypeAndDescription() {
      Dependency dependency = createDependency();

      dependency.update(DependencyType.REQUIRES, "New description");

      assertThat(dependency.getDependencyType()).isEqualTo(DependencyType.REQUIRES);
      assertThat(dependency.getDescription()).isEqualTo("New description");
      assertThat(dependency.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("全依存関係タイプへ更新できる")
    void shouldUpdateToAllDependencyTypes() {
      Dependency dependency = createDependency();

      dependency.update(DependencyType.REQUIRES, "Requires");
      assertThat(dependency.getDependencyType()).isEqualTo(DependencyType.REQUIRES);

      dependency.update(DependencyType.RELATES_TO, "Relates to");
      assertThat(dependency.getDependencyType()).isEqualTo(DependencyType.RELATES_TO);

      dependency.update(DependencyType.BLOCKS, "Blocks");
      assertThat(dependency.getDependencyType()).isEqualTo(DependencyType.BLOCKS);
    }
  }
}
