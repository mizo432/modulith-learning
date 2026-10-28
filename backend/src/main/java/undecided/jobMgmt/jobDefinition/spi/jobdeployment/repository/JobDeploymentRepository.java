package undecided.jobMgmt.jobDefinition.spi.jobdeployment.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import undecided.jobMgmt.jobDefinition.spi.jobdeployment.JobDeployment;
import undecided.jobMgmt.jobDefinition.spi.jobdefinition.JobCode;
import undecided.supporting.snowflake.SnowflakeId;

@Repository
public interface JobDeploymentRepository extends CrudRepository<JobDeployment, SnowflakeId> {

  List<JobDeployment> findByJobCode(JobCode jobCode);

  List<JobDeployment> findByServerAddressHost(String host);
}
