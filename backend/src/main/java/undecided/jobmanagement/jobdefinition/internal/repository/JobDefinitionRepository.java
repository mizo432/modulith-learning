package undecided.jobmanagement.jobdefinition.internal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.JobDefinition;
import undecided.supporting.snowflake.SnowflakeId;

import java.util.Optional;

@Repository
public interface JobDefinitionRepository extends JpaRepository<JobDefinition, SnowflakeId> {
  
  Optional<JobDefinition> findByJobCodeValue(String jobCodeValue);
  
  boolean existsByJobCodeValue(String jobCodeValue);
}
