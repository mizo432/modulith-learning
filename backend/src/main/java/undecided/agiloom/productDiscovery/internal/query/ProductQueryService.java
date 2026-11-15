package undecided.agiloom.productDiscovery.internal.query;

import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.productDiscovery.internal.ProductRepository;
import undecided.agiloom.productDiscovery.spi.Product;
import undecided.agiloom.productDiscovery.spi.ProductStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Service
@Transactional(readOnly = true)
public class ProductQueryService {

  private final ProductRepository productRepository;

  public ProductQueryService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Optional<Product> findById(@NonNull SnowflakeId id) {
    return productRepository.findById(id);
  }

  public List<Product> findAll(Pageable pageable) {
    return productRepository.findAll(pageable).getContent();
  }

  public List<Product> findByStatus(@NonNull ProductStatus status) {
    return productRepository.findByStatus(status);
  }
}
