package undecided.jobMgmt.jobDefinition.application.query;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import undecided.jobMgmt.jobDefinition.spi.jobdefinition.JobCode;
import undecided.jobMgmt.jobDefinition.spi.jobdefinition.JobDefinition;
import undecided.jobMgmt.jobDefinition.spi.jobdefinition.repository.JobDefinitionRepository;

@RequiredArgsConstructor
@Service
public class JobDefinitionQueryImpl implements JobDefinitionQuery {

  private final JobDefinitionRepository repository;

  @Override
  public Optional<JobDefinition> findByJobCode(String jobCode) {
    return repository.findByJobCode(new JobCode(jobCode));
  }

  @Override
  public List<JobDefinition> findAll() {
    return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
  }

  @Override
  public List<JobDefinition> findByEnabled(boolean enabled) {
    return repository.findByEnabled(enabled);
  }
}
