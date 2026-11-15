package undecided.agiloom.productDiscovery.internal.command;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.productDiscovery.internal.ProductRepository;
import undecided.agiloom.productDiscovery.spi.Product;
import undecided.supporting.snowflake.SnowflakeId;

@Service
@Transactional
public class UpdateProductCommandService {

  private final ProductRepository productRepository;

  public UpdateProductCommandService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public void execute(@NonNull UpdateProductCommand command) {
    Product product =
        productRepository
            .findById(command.getId())
            .orElseThrow(
                () -> new IllegalArgumentException("Product not found: " + command.getId()));

    product.update(command.getName(), command.getDescription(), command.getManagerId());
  }

  public void archive(@NonNull SnowflakeId id) {
    Product product =
        productRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

    product.archive();
  }

  public void delete(@NonNull SnowflakeId id) {
    Product product =
        productRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

    product.delete();
  }
}
