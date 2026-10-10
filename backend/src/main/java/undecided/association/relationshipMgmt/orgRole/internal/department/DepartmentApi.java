package undecided.association.relationshipMgmt.orgRole.internal.department;

import jakarta.persistence.EntityNotFoundException;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import undecided.association.relationshipMgmt.orgRole.spi.Department;
import undecided.association.relationshipMgmt.orgRole.spi.DepartmentCode;

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
  Department getById(@PathVariable DepartmentCode departmentCode) {
    return query
        .findByCode(departmentCode)
        .orElseThrow(() -> new EntityNotFoundException("Department not found"));
  }

  /**
   * @param department
   * @param uriComponentsBuilder
   * @return
   */
  @PostMapping
  ResponseEntity<Department> post(
      Department department, UriComponentsBuilder uriComponentsBuilder) {
    command.insert(department);
    URI uri =
        uriComponentsBuilder
            .path("api/departments/{departmentCode}")
            .build(department.getCode().value());
    return ResponseEntity.created(uri).build();
  }

  /**
   * @param departmentCode
   * @param department
   * @return
   */
  @PutMapping("/{departmentCode}")
  ResponseEntity<Void> put(@PathVariable DepartmentCode departmentCode, Department department) {
    command.update(departmentCode, department);
    return ResponseEntity.noContent().build();
  }

  /**
   * 部署情報を部分的に更新します。
   *
   * @param departmentCode 更新対象の部署コード
   * @param department 更新する部署情報
   * @return 更新結果のレスポンス
   */
  @PatchMapping("/{departmentCode}")
  ResponseEntity<Void> patch(@PathVariable DepartmentCode departmentCode, Department department) {
    command.update(departmentCode, department);
    return ResponseEntity.noContent().build();
  }

  /**
   * @param departmentCode
   * @return
   */
  @DeleteMapping("/{departmentCode}")
  ResponseEntity<Void> delete(@PathVariable DepartmentCode departmentCode) {
    command.delete(departmentCode);
    return ResponseEntity.noContent().build();
  }
}
