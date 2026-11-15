package undecided.agiloom.productDiscovery.internal.presentation;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import undecided.agiloom.productDiscovery.internal.command.CreateProductCommand;
import undecided.agiloom.productDiscovery.internal.command.CreateProductCommandService;
import undecided.agiloom.productDiscovery.internal.command.UpdateProductCommand;
import undecided.agiloom.productDiscovery.internal.command.UpdateProductCommandService;
import undecided.agiloom.productDiscovery.internal.query.ProductQueryService;
import undecided.agiloom.productDiscovery.spi.Product;
import undecided.supporting.snowflake.SnowflakeId;

@RestController
@RequestMapping("/api/v1/products")
public class ProductCommandController {

  private final CreateProductCommandService createProductCommandService;
  private final UpdateProductCommandService updateProductCommandService;
  private final ProductQueryService productQueryService;
  private final ProductResponseConverter productResponseConverter;

  public ProductCommandController(
      CreateProductCommandService createProductCommandService,
      UpdateProductCommandService updateProductCommandService,
      ProductQueryService productQueryService,
      ProductResponseConverter productResponseConverter) {
    this.createProductCommandService = createProductCommandService;
    this.updateProductCommandService = updateProductCommandService;
    this.productQueryService = productQueryService;
    this.productResponseConverter = productResponseConverter;
  }

  @PostMapping
  public ResponseEntity<ProductResponse> createProduct(
      @Valid @RequestBody CreateProductRequest request) {
    CreateProductCommand command =
        CreateProductCommand.builder()
            .name(request.getName())
            .description(request.getDescription())
            .managerId(request.getManagerId())
            .build();

    SnowflakeId id = createProductCommandService.execute(command);
    Product product = productQueryService.findById(id).orElseThrow();
    return ResponseEntity.ok(productResponseConverter.toProductResponse(product));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<ProductResponse> updateProduct(
      @PathVariable String id, @Valid @RequestBody UpdateProductRequest request) {
    SnowflakeId snowflakeId = SnowflakeId.of(Long.parseLong(id));
    updateProductCommandService.execute(
        UpdateProductCommand.builder()
            .id(snowflakeId)
            .name(request.getName())
            .description(request.getDescription())
            .managerId(request.getManagerId())
            .build());
    Product product = productQueryService.findById(snowflakeId).orElseThrow();
    return ResponseEntity.ok(productResponseConverter.toProductResponse(product));
  }

  @PatchMapping("/{id}/archive")
  public ResponseEntity<ProductResponse> archiveProduct(@PathVariable String id) {
    SnowflakeId snowflakeId = SnowflakeId.of(Long.parseLong(id));
    updateProductCommandService.archive(snowflakeId);
    Product product = productQueryService.findById(snowflakeId).orElseThrow();
    return ResponseEntity.ok(productResponseConverter.toProductResponse(product));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
    SnowflakeId snowflakeId = SnowflakeId.of(Long.parseLong(id));
    updateProductCommandService.delete(snowflakeId);
    return ResponseEntity.noContent().build();
  }
}
