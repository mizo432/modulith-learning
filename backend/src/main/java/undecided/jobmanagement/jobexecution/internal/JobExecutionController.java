package undecided.jobmanagement.jobexecution.internal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import undecided.jobmanagement.jobexecution.spi.jobexecution.command.StartJobCommand;
import undecided.jobmanagement.jobexecution.spi.jobexecution.dto.JobExecutionDTO;
import undecided.supporting.snowflake.SnowflakeId;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/job-executions")
public class JobExecutionController {

  private final StartJobCommand startJobCommand;

  @PostMapping
  public ResponseEntity<Void> startJob(@Valid @RequestBody JobExecutionDTO jobExecutionDTO) {
    SnowflakeId id = startJobCommand.startJob(jobExecutionDTO);
    URI location = UriComponentsBuilder.fromPath("/api/job-executions/{id}")
        .buildAndExpand(id.value())
        .toUri();
    return ResponseEntity.created(location).build();
  }
}
