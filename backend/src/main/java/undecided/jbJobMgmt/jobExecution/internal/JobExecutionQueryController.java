package undecided.jbJobMgmt.jobExecution.internal;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.jbJobMgmt.jobExecution.application.query.JobExecutionQuery;
import undecided.jbJobMgmt.jobExecution.spi.jobexecution.JobExecution;

@RestController
@RequestMapping("/api/job-executions")
@RequiredArgsConstructor
public class JobExecutionQueryController {

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
}
