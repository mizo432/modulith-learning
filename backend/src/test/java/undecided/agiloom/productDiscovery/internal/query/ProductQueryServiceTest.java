package undecided.agiloom.productDiscovery.internal.query;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import undecided.agiloom.productDiscovery.internal.ProductRepository;
import undecided.agiloom.productDiscovery.spi.Product;
import undecided.agiloom.productDiscovery.spi.ProductStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductQueryServiceのテスト")
class ProductQueryServiceTest {

  @Mock private ProductRepository productRepository;

  @InjectMocks private ProductQueryService service;

  private SnowflakeId productId;
  private Product product;

  @BeforeEach
  void setUp() {
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
  @DisplayName("findByIdメソッドのテスト")
  class FindByIdTest {

    @Test
    @DisplayName("存在するプロダクトが見つかる")
    void shouldFindProductById() {
      when(productRepository.findById(productId)).thenReturn(Optional.of(product));

      Optional<Product> result = service.findById(productId);

      assertThat(result).isPresent();
      assertThat(result.get().getName()).isEqualTo("Test Product");
    }

    @Test
    @DisplayName("存在しないプロダクトは空のOptionalが返される")
    void shouldReturnEmptyWhenProductNotFound() {
      when(productRepository.findById(productId)).thenReturn(Optional.empty());

      Optional<Product> result = service.findById(productId);

      assertThat(result).isEmpty();
    }
  }

  @Nested
  @DisplayName("findAllメソッドのテスト")
  class FindAllTest {

    @Test
    @DisplayName("プロダクト一覧が返される")
    void shouldReturnAllProducts() {
      List<Product> products = List.of(product);
      Page<Product> page = new PageImpl<>(products);
      when(productRepository.findAll((Pageable) org.mockito.ArgumentMatchers.any()))
          .thenReturn(page);

      List<Product> result = service.findAll(org.mockito.ArgumentMatchers.any(Pageable.class));

      assertThat(result).hasSize(1);
      assertThat(result.get(0).getName()).isEqualTo("Test Product");
    }
  }

  @Nested
  @DisplayName("findByStatusメソッドのテスト")
  class FindByStatusTest {

    @Test
    @DisplayName("ステータスでフィルタリングされた一覧が返される")
    void shouldReturnProductsByStatus() {
      List<Product> products = List.of(product);
      when(productRepository.findByStatus(ProductStatus.ACTIVE)).thenReturn(products);

      List<Product> result = service.findByStatus(ProductStatus.ACTIVE);

      assertThat(result).hasSize(1);
      assertThat(result.get(0).getStatus()).isEqualTo(ProductStatus.ACTIVE);
    }
  }
}
