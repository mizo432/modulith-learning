package undecided.erp.scrum.domain.model.sprint;

import org.springframework.data.repository.PagingAndSortingRepository;
import undecided.erp.shared.entity.SnowflakeId;

public interface SprintRepository extends PagingAndSortingRepository<Sprint, SnowflakeId> {

  void save(Sprint sprint);

}
