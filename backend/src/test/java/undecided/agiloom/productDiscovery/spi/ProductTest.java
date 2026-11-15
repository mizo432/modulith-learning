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
@DisplayName("Productのテスト")
class ProductTest {

  private Product createProduct() {
    return Product.builder()
        .id(SnowflakeId.of(1L))
        .name("Test Product")
        .description("Test Description")
        .managerId(UUID.randomUUID())
        .build();
  }

  @Nested
  @DisplayName("ビルダーのテスト")
  class BuilderTest {

    @Test
    @DisplayName("ビルダーでインスタンスが作成される")
    void shouldCreateInstanceWithBuilder() {
      Product product = createProduct();

      assertThat(product).isNotNull();
      assertThat(product.getName()).isEqualTo("Test Product");
      assertThat(product.getDescription()).isEqualTo("Test Description");
      assertThat(product.getStatus()).isEqualTo(ProductStatus.ACTIVE);
      assertThat(product.getCreatedAt()).isNotNull();
      assertThat(product.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("デフォルトステータスはACTIVEである")
    void shouldHaveDefaultStatusActive() {
      Product product = createProduct();

      assertThat(product.getStatus()).isEqualTo(ProductStatus.ACTIVE);
    }

    @Test
    @DisplayName("createdAtとupdatedAtが自動設定される")
    void shouldSetCreatedAtAndUpdatedAt() {
      Product product = createProduct();

      assertThat(product.getCreatedAt()).isNotNull();
      assertThat(product.getUpdatedAt()).isNotNull();
    }
  }

  @Nested
  @DisplayName("updateメソッドのテスト")
  class UpdateMethodTest {

    @Test
    @DisplayName("名前が更新される")
    void shouldUpdateName() {
      Product product = createProduct();

      product.update("New Name", null, null);

      assertThat(product.getName()).isEqualTo("New Name");
      assertThat(product.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("説明が更新される")
    void shouldUpdateDescription() {
      Product product = createProduct();

      product.update(null, "New Description", null);

      assertThat(product.getDescription()).isEqualTo("New Description");
    }

    @Test
    @DisplayName("マネージャーIDが更新される")
    void shouldUpdateManagerId() {
      Product product = createProduct();
      UUID newManagerId = UUID.randomUUID();

      product.update(null, null, newManagerId);

      assertThat(product.getManagerId()).isEqualTo(newManagerId);
    }

    @Test
    @DisplayName("複数のフィールドが一度に更新される")
    void shouldUpdateMultipleFields() {
      Product product = createProduct();
      UUID newManagerId = UUID.randomUUID();

      product.update("New Name", "New Description", newManagerId);

      assertThat(product.getName()).isEqualTo("New Name");
      assertThat(product.getDescription()).isEqualTo("New Description");
      assertThat(product.getManagerId()).isEqualTo(newManagerId);
    }

    @Test
    @DisplayName("updatedAtが更新される")
    void shouldUpdateUpdatedAt() {
      Product product = createProduct();
      LocalDateTime originalUpdatedAt = product.getUpdatedAt();

      product.update("New Name", null, null);

      assertThat(product.getUpdatedAt()).isNotEqualTo(originalUpdatedAt);
    }

    @Test
    @DisplayName("空文字列の名前の場合、例外がスローされる")
    void shouldThrowExceptionWhenNameIsEmpty() {
      Product product = createProduct();

      assertThatThrownBy(() -> product.update("", null, null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("must not be empty");
    }

    @Test
    @DisplayName("nullの名前は変更されない")
    void shouldNotChangeNameWhenNull() {
      Product product = createProduct();
      String originalName = product.getName();

      product.update(null, null, null);

      assertThat(product.getName()).isEqualTo(originalName);
    }
  }

  @Nested
  @DisplayName("archiveメソッドのテスト")
  class ArchiveMethodTest {

    @Test
    @DisplayName("ステータスがARCHIVEDに更新される")
    void shouldUpdateStatusToArchived() {
      Product product = createProduct();

      product.archive();

      assertThat(product.getStatus()).isEqualTo(ProductStatus.ARCHIVED);
      assertThat(product.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("ARCHIVED状態から再度archiveすると例外がスローされる")
    void shouldThrowExceptionWhenArchivingArchivedProduct() {
      Product product = createProduct();
      product.archive();

      assertThatThrownBy(() -> product.archive())
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("ARCHIVED");
    }
  }

  @Nested
  @DisplayName("ステータス遷移のテスト")
  class StatusTransitionTest {

    @Test
    @DisplayName("ACTIVEからARCHIVEDへ遷移できる")
    void shouldTransitionFromActiveToArchived() {
      Product product = createProduct();
      assertThat(product.getStatus()).isEqualTo(ProductStatus.ACTIVE);

      product.archive();
      assertThat(product.getStatus()).isEqualTo(ProductStatus.ARCHIVED);
    }
  }

  @Nested
  @DisplayName("deleteメソッドのテスト")
  class DeleteMethodTest {

    @Test
    @DisplayName("deleted_atが設定される")
    void shouldSetDeletedAt() {
      Product product = createProduct();
      assertThat(product.getDeletedAt()).isNull();

      product.delete();

      assertThat(product.getDeletedAt()).isNotNull();
      assertThat(product.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("delete後もステータスは変更されない")
    void shouldNotChangeStatusOnDelete() {
      Product product = createProduct();

      product.delete();

      assertThat(product.getStatus()).isEqualTo(ProductStatus.ACTIVE);
    }
  }
}
