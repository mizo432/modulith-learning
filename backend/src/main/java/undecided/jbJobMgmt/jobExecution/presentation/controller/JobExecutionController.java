package undecided.jbJobMgmt.jobExecution.presentation.controller;

import jakarta.persistence.EntityNotFoundException;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import undecided.jbJobMgmt.jobExecution.application.command.JobExecutionCommand;
import undecided.jbJobMgmt.jobExecution.application.query.JobExecutionQuery;
import undecided.jbJobMgmt.jobExecution.domain.jobexecution.JobExecution;

@RestController
@RequestMapping("/api/job-executions")
@RequiredArgsConstructor
public class JobExecutionController {

  private final JobExecutionCommand command;
  private final JobExecutionQuery query;

  @GetMapping
  List<JobExecution> findAll() {
    return query.findAll();
  }

  @GetMapping("/{executionId}")
  JobExecution findById(@PathVariable String executionId) {
    return query.findById(executionId)
        .orElseThrow(() -> new EntityNotFoundException("JobExecution not found: " + executionId));
  }

  @PostMapping
  ResponseEntity<Void> startJob(@RequestBody JobExecution jobExecution, UriComponentsBuilder uriBuilder) {
    command.startJob(jobExecution);
    URI uri = uriBuilder.path("/api/job-executions/{executionId}")
        .build(jobExecution.getId().toString());
    return ResponseEntity.created(uri).build();
  }

  @PostMapping("/{executionId}/cancel")
  ResponseEntity<Void> cancel(@PathVariable String executionId) {
    command.cancel(executionId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{executionId}/re-execute")
  ResponseEntity<Void> reExecute(@PathVariable String executionId) {
    command.reExecute(executionId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{executionId}/resume")
  ResponseEntity<Void> resume(@PathVariable String executionId) {
    command.resume(executionId);
    return ResponseEntity.noContent().build();
  }
}
