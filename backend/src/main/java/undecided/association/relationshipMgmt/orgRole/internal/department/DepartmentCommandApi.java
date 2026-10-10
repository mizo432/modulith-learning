package undecided.association.relationshipMgmt.orgRole.internal.department;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import undecided.association.relationshipMgmt.orgRole.spi.Department;
import undecided.association.relationshipMgmt.orgRole.spi.DepartmentCode;

/**
 * 部署情報の変更（Command）を担当するREST APIコントローラーです。
 *
 * <p>CQRSパターンに従い、書き込み操作のみを担当します。
 */
@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentCommandApi {
  private final DepartmentCommand command;

  /**
   * @param department
   * @param uriComponentsBuilder
   * @return
   */
  @PostMapping
  ResponseEntity<Department> post(
      @RequestBody Department department, UriComponentsBuilder uriComponentsBuilder) {
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
  ResponseEntity<Void> put(
      @PathVariable DepartmentCode departmentCode, @RequestBody Department department) {
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
  ResponseEntity<Void> patch(
      @PathVariable DepartmentCode departmentCode, @RequestBody Department department) {
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
