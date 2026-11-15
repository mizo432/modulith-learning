package undecided.agiloom.productDiscovery.internal.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doAnswer;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import undecided.agiloom.productDiscovery.internal.command.CreateProductCommandService;
import undecided.agiloom.productDiscovery.internal.command.UpdateProductCommandService;
import undecided.agiloom.productDiscovery.internal.query.ProductQueryService;
import undecided.agiloom.productDiscovery.spi.Product;
import undecided.agiloom.productDiscovery.spi.ProductStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductCommandControllerのテスト")
class ProductCommandControllerTest {

  private MockMvc mockMvc;

  @Mock private CreateProductCommandService createProductCommandService;

  @Mock private UpdateProductCommandService updateProductCommandService;

  @Mock private ProductQueryService productQueryService;

  @Mock private ProductResponseConverter productResponseConverter;

  @InjectMocks private ProductCommandController controller;

  private SnowflakeId productId;
  private Product product;
  private ProductResponse productResponse;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    productId = SnowflakeId.of(1L);
    product =
        Product.builder()
            .id(productId)
            .name("Test Product")
            .description("Test Description")
            .managerId(UUID.randomUUID())
            .status(ProductStatus.ACTIVE)
            .build();
    productResponse =
        ProductResponse.builder()
            .id(productId)
            .name("Test Product")
            .description("Test Description")
            .managerId(product.getManagerId())
            .status(ProductStatus.ACTIVE)
            .build();
  }

  @Nested
  @DisplayName("POST /api/v1/products のテスト")
  class CreateProductTest {

    @Test
    @DisplayName("プロダクトが正常に作成される")
    void shouldCreateProduct() throws Exception {
      when(createProductCommandService.execute(any())).thenReturn(productId);
      when(productQueryService.findById(productId)).thenReturn(Optional.of(product));
      when(productResponseConverter.toProductResponse(product)).thenReturn(productResponse);

      String requestBody =
          "{\"name\":\"New Product\",\"description\":\"New Description\"}";

      mockMvc.perform(
              MockMvcRequestBuilders.post("/api/v1/products")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(requestBody))
          .andExpect(MockMvcResultMatchers.status().isOk());

      verify(createProductCommandService).execute(any());
    }
  }

  @Nested
  @DisplayName("PATCH /api/v1/products/{id} のテスト")
  class UpdateProductTest {

    @Test
    @DisplayName("プロダクトが正常に更新される")
    void shouldUpdateProduct() throws Exception {
      when(productQueryService.findById(any())).thenReturn(Optional.of(product));
      when(productResponseConverter.toProductResponse(any())).thenReturn(productResponse);

      String requestBody = "{\"name\":\"Updated Product\"}";

      mockMvc.perform(
              MockMvcRequestBuilders.patch("/api/v1/products/1")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(requestBody))
          .andExpect(MockMvcResultMatchers.status().isOk());

      verify(updateProductCommandService).execute(any());
    }
  }

  @Nested
  @DisplayName("PATCH /api/v1/products/{id}/archive のテスト")
  class ArchiveProductTest {

    @Test
    @DisplayName("プロダクトが正常にアーカイブされる")
    void shouldArchiveProduct() throws Exception {
      when(productQueryService.findById(any())).thenReturn(Optional.of(product));
      when(productResponseConverter.toProductResponse(any())).thenReturn(productResponse);

      mockMvc.perform(
              MockMvcRequestBuilders.patch("/api/v1/products/1/archive"))
          .andExpect(MockMvcResultMatchers.status().isOk());

      verify(updateProductCommandService).archive(any());
    }
  }

  @Nested
  @DisplayName("DELETE /api/v1/products/{id} のテスト")
  class DeleteProductTest {

    @Test
    @DisplayName("プロダクトが正常に削除される")
    void shouldDeleteProduct() throws Exception {
      mockMvc.perform(
              MockMvcRequestBuilders.delete("/api/v1/products/1"))
          .andExpect(MockMvcResultMatchers.status().isNoContent());

      verify(updateProductCommandService).delete(any());
    }
  }
}
