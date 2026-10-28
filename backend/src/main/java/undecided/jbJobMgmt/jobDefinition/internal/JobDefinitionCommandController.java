package undecided.jbJobMgmt.jobDefinition.internal;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import undecided.jbJobMgmt.jobDefinition.application.command.JobDefinitionCommand;
import undecided.jbJobMgmt.jobDefinition.spi.jobdefinition.JobDefinition;

@RestController
@RequestMapping("/api/job-definitions")
@RequiredArgsConstructor
public class JobDefinitionCommandController {

  private final JobDefinitionCommand command;

  @PostMapping
  ResponseEntity<Void> register(@RequestBody JobDefinition jobDefinition, UriComponentsBuilder uriBuilder) {
    command.register(jobDefinition);
    URI uri = uriBuilder.path("/api/job-definitions/{jobCode}")
        .build(jobDefinition.getJobCode().getValue());
    return ResponseEntity.created(uri).build();
  }

  @PutMapping("/{jobCode}/parameters")
  ResponseEntity<Void> updateParameterSpec(@PathVariable String jobCode,
      @RequestBody String parameterSpecJson) {
    command.updateParameterSpec(jobCode, parameterSpecJson);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{jobCode}/disable")
  ResponseEntity<Void> disable(@PathVariable String jobCode) {
    command.disable(jobCode);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{jobCode}/enable")
  ResponseEntity<Void> enable(@PathVariable String jobCode) {
    command.enable(jobCode);
    return ResponseEntity.noContent().build();
  }
}
