package undecided.erp.scrum.adapters.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.erp.scrum.application.command.sprint.AddSprintCommand;
import undecided.erp.scrum.domain.model.product.Product;
import undecided.erp.scrum.domain.model.sprint.Sprint;
import undecided.erp.shared.entity.SnowflakeId;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/scrum/spi/v1")
public class SprintController {

  private final AddSprintCommand addSprintCommand;

  @PostMapping(path = "products/{productId}/sprints")
  ResponseEntity<Void> post(Sprint sprint,
      @PathVariable("productId") SnowflakeId<Product> productId) {
    addSprintCommand.execute(productId, sprint);

    return ResponseEntity.ok().build();


  }

}
