package undecided.agiloom.productDiscovery.internal.command;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.productDiscovery.internal.ProductRepository;
import undecided.agiloom.productDiscovery.spi.Product;
import undecided.supporting.snowflake.SnowflakeId;

@Service
@Transactional
public class CreateProductCommandService {

  private final ProductRepository productRepository;

  public CreateProductCommandService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public SnowflakeId execute(@NonNull CreateProductCommand command) {
    Product product =
        Product.builder()
            .name(command.getName())
            .description(command.getDescription())
            .managerId(command.getManagerId())
            .build();

    Product saved = productRepository.save(product);
    return saved.getId();
  }
}
