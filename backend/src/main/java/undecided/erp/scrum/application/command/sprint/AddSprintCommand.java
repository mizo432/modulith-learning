package undecided.erp.scrum.application.command.sprint;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import undecided.erp.scrum.model.product.Product;
import undecided.erp.scrum.model.sprint.Sprint;
import undecided.erp.scrum.model.sprint.SprintRepository;
import undecided.erp.shared.entity.SnowflakeId;

@RequiredArgsConstructor
@Service
public class AddSprintCommand {

  private final SprintRepository sprintRepository;

  public void execute(SnowflakeId<Product> productId, Sprint sprint) {
    sprintRepository.save(sprint);

  }

}