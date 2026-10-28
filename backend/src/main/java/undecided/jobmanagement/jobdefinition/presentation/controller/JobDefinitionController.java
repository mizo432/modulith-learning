package undecided.jobmanagement.jobdefinition.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.command.RegisterJobDefinitionCommand;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.dto.JobDefinitionDTO;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/job-definitions")
public class JobDefinitionController {

  private final RegisterJobDefinitionCommand registerJobDefinitionCommand;

  @PostMapping
  public ResponseEntity<Void> registerJobDefinition(@RequestBody JobDefinitionDTO jobDefinitionDTO) {
    registerJobDefinitionCommand.register(jobDefinitionDTO);
    return ResponseEntity.created(URI.create("/api/job-definitions/" + jobDefinitionDTO.getJobCode())).build();
  }
}
