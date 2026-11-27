package undecided.agiloom.productDiscovery.internal.command;

import static com.github.mizo432.shared.common.precondition.StringPrecondition.checkNonEmpty;

import java.time.Clock;
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
  private final Clock clock;

  public CreateProductCommandService(ProductRepository productRepository, Clock clock) {
    this.productRepository = productRepository;
    this.clock = clock;
  }

  public SnowflakeId execute(@NonNull CreateProductCommand command) {
    checkNonEmpty(command.getName(), () -> new IllegalArgumentException("Product name must not be empty"));
    
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
