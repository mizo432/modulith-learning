package undecided.jbJobMgmt.jobDefinition.application.command;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import undecided.jbJobMgmt.jobDefinition.spi.jobdefinition.JobCode;
import undecided.jbJobMgmt.jobDefinition.spi.jobdefinition.JobDefinition;
import undecided.jbJobMgmt.jobDefinition.spi.jobdefinition.repository.JobDefinitionRepository;

@RequiredArgsConstructor
@Service
public class JobDefinitionCommandImpl implements JobDefinitionCommand {

  private final JobDefinitionRepository repository;

  @Override
  public void register(JobDefinition jobDefinition) {
    repository.save(jobDefinition);
  }

  @Override
  public void updateParameterSpec(String jobCode, String parameterSpecJson) {
    JobDefinition definition = repository.findByJobCode(new JobCode(jobCode))
        .orElseThrow(() -> new EntityNotFoundException("JobDefinition not found: " + jobCode));
    // TODO: Parse JSON and update parameter spec
    repository.save(definition);
  }

  @Override
  public void disable(String jobCode) {
    JobDefinition definition = repository.findByJobCode(new JobCode(jobCode))
        .orElseThrow(() -> new EntityNotFoundException("JobDefinition not found: " + jobCode));
    definition.disable();
    repository.save(definition);
  }

  @Override
  public void enable(String jobCode) {
    JobDefinition definition = repository.findByJobCode(new JobCode(jobCode))
        .orElseThrow(() -> new EntityNotFoundException("JobDefinition not found: " + jobCode));
    definition.enable();
    repository.save(definition);
  }
}
