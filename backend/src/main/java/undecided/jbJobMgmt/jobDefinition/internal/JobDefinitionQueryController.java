package undecided.jbJobMgmt.jobDefinition.internal;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import undecided.jbJobMgmt.jobDefinition.application.query.JobDefinitionQuery;
import undecided.jbJobMgmt.jobDefinition.spi.jobdefinition.JobDefinition;

@RestController
@RequestMapping("/api/job-definitions")
@RequiredArgsConstructor
public class JobDefinitionQueryController {

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
}
