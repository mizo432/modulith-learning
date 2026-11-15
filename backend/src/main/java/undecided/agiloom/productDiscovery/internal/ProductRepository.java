package undecided.agiloom.productDiscovery.internal;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import undecided.agiloom.productDiscovery.spi.Product;
import undecided.agiloom.productDiscovery.spi.ProductStatus;
import undecided.supporting.snowflake.SnowflakeId;

public interface ProductRepository
    extends CrudRepository<Product, SnowflakeId>, PagingAndSortingRepository<Product, SnowflakeId> {

  List<Product> findByStatus(ProductStatus status);

  Optional<Product> findByIdAndDeletedAtIsNull(SnowflakeId id);
}
