package undecided.agiloom.productDiscovery.internal.command;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.agiloom.productDiscovery.internal.ProductRepository;
import undecided.agiloom.productDiscovery.spi.Product;
import undecided.agiloom.productDiscovery.spi.ProductStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateProductCommandServiceのテスト")
class UpdateProductCommandServiceTest {

  @Mock private ProductRepository productRepository;

  private UpdateProductCommandService service;

  private SnowflakeId productId;
  private Product product;

  @BeforeEach
  void setUp() {
    service = new UpdateProductCommandService(productRepository, Clock.systemDefaultZone());
    productId = SnowflakeId.of(1L);
    product =
        Product.builder()
            .id(productId)
            .name("Test Product")
            .description("Test Description")
            .managerId(SnowflakeId.of(100L))
            .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
  }

  @Nested
  @DisplayName("executeメソッドのテスト")
  class ExecuteMethodTest {

    @Test
    @DisplayName("名前が更新される")
    void shouldUpdateName() {
      when(productRepository.findById(productId)).thenReturn(Optional.of(product));

      UpdateProductCommand command =
          UpdateProductCommand.builder().id(productId).name("New Name").build();
      service.execute(command);

      assertThat(product.getName()).isEqualTo("New Name");
    }

    @Test
    @DisplayName("説明が更新される")
    void shouldUpdateDescription() {
      when(productRepository.findById(productId)).thenReturn(Optional.of(product));

      UpdateProductCommand command =
          UpdateProductCommand.builder().id(productId).description("New Description").build();
      service.execute(command);

      assertThat(product.getDescription()).isEqualTo("New Description");
    }

    @Test
    @DisplayName("マネージャーIDが更新される")
    void shouldUpdateManagerId() {
      SnowflakeId newManagerId = SnowflakeId.of(200L);
      when(productRepository.findById(productId)).thenReturn(Optional.of(product));

      UpdateProductCommand command =
          UpdateProductCommand.builder().id(productId).managerId(newManagerId).build();
      service.execute(command);

      assertThat(product.getManagerId()).isEqualTo(newManagerId);
    }

    @Test
    @DisplayName("存在しないプロダクトを更新すると例外がスローされる")
    void shouldThrowExceptionWhenProductNotFound() {
      when(productRepository.findById(productId)).thenReturn(Optional.empty());

      UpdateProductCommand command =
          UpdateProductCommand.builder().id(productId).name("New Name").build();

      assertThatThrownBy(() -> service.execute(command))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Product not found");
    }
  }

  @Nested
  @DisplayName("archiveメソッドのテスト")
  class ArchiveMethodTest {

    @Test
    @DisplayName("プロダクトがアーカイブされる")
    void shouldArchiveProduct() {
      when(productRepository.findById(productId)).thenReturn(Optional.of(product));

      service.archive(productId);

      assertThat(product.getStatus()).isEqualTo(ProductStatus.ARCHIVED);
    }

    @Test
    @DisplayName("存在しないプロダクトをアーカイブすると例外がスローされる")
    void shouldThrowExceptionWhenProductNotFound() {
      when(productRepository.findById(productId)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> service.archive(productId))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Product not found");
    }
  }

  @Nested
  @DisplayName("deleteメソッドのテスト")
  class DeleteMethodTest {

    @Test
    @DisplayName("プロダクトが論理削除される")
    void shouldDeleteProduct() {
      when(productRepository.findById(productId)).thenReturn(Optional.of(product));

      service.delete(productId);

      assertThat(product.getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("存在しないプロダクトを削除すると例外がスローされる")
    void shouldThrowExceptionWhenProductNotFound() {
      when(productRepository.findById(productId)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> service.delete(productId))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Product not found");
    }
  }
}
