package undecided.agiloom.productDiscovery.internal.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.agiloom.productDiscovery.internal.ProductRepository;
import undecided.agiloom.productDiscovery.spi.Product;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@ExtendWith(MockitoExtension.class)
@DisplayName("CreateProductCommandServiceのテスト")
class CreateProductCommandServiceTest {

  @Mock private ProductRepository productRepository;

  @InjectMocks private CreateProductCommandService service;

  private SnowflakeId productId;

  @BeforeEach
  void setUp() {
    productId = SnowflakeId.of(1L);
  }

  @Nested
  @DisplayName("executeメソッドのテスト")
  class ExecuteMethodTest {

    @Test
    @DisplayName("プロダクトが作成されIDが返される")
    void shouldCreateProductAndReturnId() {
      CreateProductCommand command =
          CreateProductCommand.builder().name("Test Product").description("Test Description").build();

      Product saved =
          Product.builder()
              .id(productId)
              .name("Test Product")
              .description("Test Description")
              .build();
      when(productRepository.save(any(Product.class))).thenReturn(saved);

      SnowflakeId result = service.execute(command);

      assertThat(result).isEqualTo(productId);
      verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("マネージャーID付きでプロダクトが作成される")
    void shouldCreateProductWithManagerId() {
      UUID managerId = UUID.randomUUID();
      CreateProductCommand command =
          CreateProductCommand.builder()
              .name("Test Product")
              .description("Test Description")
              .managerId(managerId)
              .build();

      Product saved =
          Product.builder().id(productId).name("Test Product").managerId(managerId).build();
      when(productRepository.save(any(Product.class))).thenReturn(saved);

      SnowflakeId result = service.execute(command);

      assertThat(result).isEqualTo(productId);
      verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("空文字列の名前の場合、例外がスローされること")
    void shouldThrowExceptionWhenNameIsEmpty() {
      // Arrange
      CreateProductCommand command =
          CreateProductCommand.builder()
              .name("")
              .description("Test Description")
              .build();

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("must not be empty");
    }

  }
}
