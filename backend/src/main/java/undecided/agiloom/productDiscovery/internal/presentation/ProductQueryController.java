package undecided.agiloom.productDiscovery.internal.presentation;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import undecided.agiloom.productDiscovery.internal.query.ProductQueryService;
import undecided.agiloom.productDiscovery.spi.Product;
import undecided.supporting.snowflake.SnowflakeId;

@RestController
@RequestMapping("/api/v1/products")
public class ProductQueryController {

  private final ProductQueryService productQueryService;

  public ProductQueryController(ProductQueryService productQueryService) {
    this.productQueryService = productQueryService;
  }

  @GetMapping
  public List<ProductResponse> getProducts(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    return productQueryService.findAll(pageable).stream().map(this::toResponse).toList();
  }

  @GetMapping("/{id}")
  public ProductResponse getProduct(@PathVariable @NotNull String id) {
    SnowflakeId snowflakeId = SnowflakeId.of(Long.parseLong(id));
    Product product =
        productQueryService
            .findById(snowflakeId)
            .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
    return toResponse(product);
  }

  ProductResponse toResponse(Product product) {
    return ProductResponse.builder()
        .id(product.getId())
        .name(product.getName())
        .description(product.getDescription())
        .managerId(product.getManagerId())
        .status(product.getStatus())
        .createdAt(product.getCreatedAt())
        .updatedAt(product.getUpdatedAt())
        .build();
  }
}
