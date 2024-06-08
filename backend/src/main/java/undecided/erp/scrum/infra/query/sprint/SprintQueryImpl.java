package undecided.erp.scrum.infra.query.sprint;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import undecided.erp.scrum.appl.query.sprint.SprintQuery;
import undecided.erp.scrum.model.sprint.Sprint;
import undecided.erp.scrum.model.sprint.SprintRepository;

@RequiredArgsConstructor
@Service
public class SprintQueryImpl implements SprintQuery {

  private final SprintRepository sprintRepository;

  public Page<Sprint> findAll(Pageable pageable) {
    return sprintRepository.findAll(pageable);
  }

}
