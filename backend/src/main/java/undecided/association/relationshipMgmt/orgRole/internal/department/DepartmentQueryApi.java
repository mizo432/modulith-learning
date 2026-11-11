package undecided.association.relationshipMgmt.orgRole.internal.department;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.association.relationshipMgmt.orgRole.spi.Department;
import undecided.association.relationshipMgmt.orgRole.spi.DepartmentCode;
import undecided.association.relationshipMgmt.orgRole.spi.dto.DepartmentDTO;

/**
 * 部署情報の取得（Query）を担当するREST APIコントローラーです。
 *
 * <p>CQRSパターンに従い、読み取り操作のみを担当します。
 */
@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentQueryApi {
  private final DepartmentQuery query;

  /**
   * @return
   */
  @GetMapping
  List<DepartmentDTO> findAll() {
    return query.findAll().stream()
        .map(dept -> new DepartmentDTO(dept.getId(), dept.getCode()))
        .toList();
  }

  /**
   * @param departmentCode
   * @return
   */
  @GetMapping("/{depertmentCode}")
  DepartmentDTO getById(@PathVariable DepartmentCode departmentCode) {
    Department department = query
        .findByCode(departmentCode)
        .orElseThrow(() -> new EntityNotFoundException("Department not found"));
    return new DepartmentDTO(department.getId(), department.getCode());
  }
}
