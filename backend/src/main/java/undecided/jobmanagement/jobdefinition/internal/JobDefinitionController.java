package undecided.jobmanagement.jobdefinition.internal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.command.RegisterJobDefinitionCommandService;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.dto.JobDefinitionDTO;
import undecided.supporting.snowflake.SnowflakeId;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/job-definitions")
public class JobDefinitionController {

  private final RegisterJobDefinitionCommandService registerJobDefinitionCommandService;

  @PostMapping
  public ResponseEntity<Void> registerJobDefinition(@Valid @RequestBody JobDefinitionDTO jobDefinitionDTO) {
    SnowflakeId id = registerJobDefinitionCommandService.register(jobDefinitionDTO);
    URI location = UriComponentsBuilder.fromPath("/api/job-definitions/{id}")
        .buildAndExpand(id.value())
        .toUri();
    return ResponseEntity.created(location).build();
  }
}
