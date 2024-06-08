package undecided.erp.scrum.model.sprint;

import org.springframework.data.repository.PagingAndSortingRepository;
import undecided.erp.shared.entity.SnowflakeId;

public interface SprintRepository extends PagingAndSortingRepository<Sprint, SnowflakeId<Sprint>> {

  void save(Sprint sprint);
  
}
