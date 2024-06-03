package undecided.erp.scrum.model;

import org.springframework.data.repository.PagingAndSortingRepository;
import undecided.erp.shared.entity.SnowflakeId;

public interface SprintRepository extends PagingAndSortingRepository<Sprint, SnowflakeId<Sprint>> {

  Long countAllSprints();

}
