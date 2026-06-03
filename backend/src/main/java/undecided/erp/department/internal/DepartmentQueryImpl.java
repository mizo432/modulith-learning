package undecided.erp.department.internal;

import org.springframework.stereotype.Service;
import undecided.erp.department.Department;

import java.util.List;
import java.util.Optional;

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
