package undecided.erp.scrum.application.query.sprint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import undecided.erp.scrum.model.sprint.Sprint;

public interface SprintQuery {

  Page<Sprint> findAll(Pageable pageable);

}
