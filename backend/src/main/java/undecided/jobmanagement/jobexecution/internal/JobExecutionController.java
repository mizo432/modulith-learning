package undecided.jobmanagement.jobexecution.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.jobmanagement.jobexecution.spi.jobexecution.command.StartJobCommand;
import undecided.jobmanagement.jobexecution.spi.jobexecution.dto.JobExecutionDTO;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/job-executions")
public class JobExecutionController {

  private final StartJobCommand startJobCommand;

  @PostMapping
  public ResponseEntity<Void> startJob(@RequestBody JobExecutionDTO jobExecutionDTO) {
    startJobCommand.startJob(jobExecutionDTO);
    return ResponseEntity.created(URI.create("/api/job-executions/" + jobExecutionDTO.getExecutionId())).build();
  }
}
