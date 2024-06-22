package undecided.erp.scrum.domain.model.product;

import org.springframework.data.repository.CrudRepository;
import undecided.erp.shared.entity.SnowflakeId;

public interface ProductRepository extends CrudRepository<Product, SnowflakeId<Product>> {

}
