package undecided.jbJobMgmt.jobDefinition.presentation.controller;

import jakarta.persistence.EntityNotFoundException;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import undecided.jbJobMgmt.jobDefinition.application.command.JobDefinitionCommand;
import undecided.jbJobMgmt.jobDefinition.application.query.JobDefinitionQuery;
import undecided.jbJobMgmt.jobDefinition.domain.jobdefinition.JobDefinition;

@RestController
@RequestMapping("/api/job-definitions")
@RequiredArgsConstructor
public class JobDefinitionController {

  private final JobDefinitionCommand command;
  private final JobDefinitionQuery query;

  @GetMapping
  List<JobDefinition> findAll(@RequestParam(required = false) Boolean enabled) {
    if (enabled != null) {
      return query.findByEnabled(enabled);
    }
    return query.findAll();
  }

  @GetMapping("/{jobCode}")
  JobDefinition findByJobCode(@PathVariable String jobCode) {
    return query.findByJobCode(jobCode)
        .orElseThrow(() -> new EntityNotFoundException("JobDefinition not found: " + jobCode));
  }

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
