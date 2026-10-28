package undecided.jbJobMgmt.jobDefinition.application.query;

import java.util.List;
import java.util.Optional;
import undecided.jbJobMgmt.jobDefinition.spi.jobdefinition.JobDefinition;

public interface JobDefinitionQuery {

  Optional<JobDefinition> findByJobCode(String jobCode);

  List<JobDefinition> findAll();

  List<JobDefinition> findByEnabled(boolean enabled);
}
