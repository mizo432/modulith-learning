package undecided.jobmanagement.jobdefinition.internal;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.dto.JobDefinitionDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/job-definitions")
public class JobDefinitionQueryController {

  private final JobDefinitionQueryService jobDefinitionQueryService;

  @GetMapping
  public ResponseEntity<List<JobDefinitionDTO>> listJobDefinitions() {
    return ResponseEntity.ok(jobDefinitionQueryService.listDefinitions());
  }
}
