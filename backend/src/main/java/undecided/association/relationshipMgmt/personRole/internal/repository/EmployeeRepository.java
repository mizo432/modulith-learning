package undecided.association.relationshipMgmt.personRole.internal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.association.relationshipMgmt.personRole.spi.employee.SimpleEmployee;
import undecided.supporting.snowflake.SnowflakeId;

@Repository
public interface EmployeeRepository extends JpaRepository<SimpleEmployee, SnowflakeId> {}
