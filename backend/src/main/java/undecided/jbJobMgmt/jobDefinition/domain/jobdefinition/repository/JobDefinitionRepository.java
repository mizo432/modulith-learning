package undecided.jbJobMgmt.jobDefinition.domain.jobdefinition.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import undecided.jbJobMgmt.jobDefinition.domain.jobdefinition.JobCode;
import undecided.jbJobMgmt.jobDefinition.domain.jobdefinition.JobDefinition;
import undecided.supporting.snowflake.SnowflakeId;

@Repository
public interface JobDefinitionRepository extends CrudRepository<JobDefinition, SnowflakeId> {

  Optional<JobDefinition> findByJobCode(JobCode jobCode);

  List<JobDefinition> findByEnabled(boolean enabled);
}
