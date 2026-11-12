package undecided.association.relationshipMgmt.orgRole.internal.department;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.association.relationshipMgmt.orgRole.spi.Department;
import undecided.association.relationshipMgmt.orgRole.spi.DepartmentCode;

@RequiredArgsConstructor
@Service
@Transactional
public class DepartmentCommandService implements DepartmentCommand {

  private final DepartmentRepository repository;

  @Override
  public void insert(Department department) {
    repository.save(department);
  }

  @Override
  public void update(DepartmentCode departmentCode, Department department) {
    Optional<Department> oldOption = repository.findByCode(departmentCode);
    Department oldDepartment =
        oldOption.orElseThrow(
            () -> new EntityNotFoundException("Department not found: " + departmentCode));
    repository.save(oldDepartment.update(department));
  }

  @Override
  public void delete(DepartmentCode departmentCode) {
    Optional<Department> oldOption = repository.findByCode(departmentCode);
    Department oldDepartment =
        oldOption.orElseThrow(
            () -> new EntityNotFoundException("Department not found: " + departmentCode));
    repository.deleteById(oldDepartment.getId());
  }
}
