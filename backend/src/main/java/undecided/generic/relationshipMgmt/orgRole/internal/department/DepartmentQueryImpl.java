package undecided.generic.relationshipMgmt.orgRole.internal.department;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import undecided.generic.relationshipMgmt.orgRole.spi.Department;

@Service
public class DepartmentQueryImpl implements DepartmentQuery {

  @Override
  public Optional<Department> findByCode(DepartmentCode departmentCode) {
    return Optional.empty();
  }

  @Override
  public List<Department> findAll() {
    return List.of();
  }
}
