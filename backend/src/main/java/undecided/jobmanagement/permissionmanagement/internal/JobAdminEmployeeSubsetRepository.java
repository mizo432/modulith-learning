package undecided.jobmanagement.permissionmanagement.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.jobmanagement.permissionmanagement.spi.userpermission.JobAdminEmployeeSubset;
import undecided.supporting.snowflake.SnowflakeId;

@Repository
public interface JobAdminEmployeeSubsetRepository
    extends JpaRepository<JobAdminEmployeeSubset, SnowflakeId> {}
