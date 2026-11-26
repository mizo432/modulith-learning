package undecided.jobmanagement.jobexecution.internal;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.jobmanagement.jobexecution.spi.jobexecution.dto.JobExecutionDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/job-executions")
public class JobExecutionQueryController {

  private final JobExecutionQueryService jobExecutionQueryService;

  @GetMapping
  public ResponseEntity<List<JobExecutionDTO>> listJobExecutions() {
    return ResponseEntity.ok(jobExecutionQueryService.listExecutions());
  }
}
