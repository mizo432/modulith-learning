package undecided.erp.department.internal;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import undecided.erp.department.Department;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentApi {
    private final DepartmentCommand command;
    private final DepartmentQuery query;


    /**
     * @return
     */
    @GetMapping
    List<Department> findAll() {
        return query.findAll();
    }

    /**
     * @param departmentCode
     * @return
     */
    @GetMapping("/{depertmentCode}")
    Department getById(DepartmentCode departmentCode) {
        return query.findByCode(departmentCode).orElseThrow(() -> new EntityNotFoundException("Department not found"));

    }

    /**
     * @param department
     * @param uriComponentsBuilder
     * @return
     */
    @PostMapping
    ResponseEntity<Department> post(Department department, UriComponentsBuilder uriComponentsBuilder) {
        command.insert(department);
        URI uri = uriComponentsBuilder.path("api/departments/{depertmentCode}").build(department.getDepartmentCode().value());
        return ResponseEntity.created(uri).build();

    }

    @PutMapping("/{depertmentCode}")
    ResponseEntity<Void> put(@PathVariable DepartmentCode departmentCode, Department department) {
        command.update(departmentCode, department);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{depertmentCode}")
    ResponseEntity<Void> delete(@PathVariable DepartmentCode departmentCode) {
        command.delete(departmentCode);
        return ResponseEntity.noContent().build();
    }

}
