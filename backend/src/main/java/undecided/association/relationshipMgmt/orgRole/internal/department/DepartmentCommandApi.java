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
import undecided.association.relationshipMgmt.orgRole.spi.dto.DepartmentDTO;

/**
 * 部署情報の変更（Command）を担当するREST APIコントローラーです。
 *
 * <p>CQRSパターンに従い、書き込み操作のみを担当します。
 */
@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentCommandApi {
  private final DepartmentCommandService commandService;

  /**
   * 新規部署を作成します。
   *
   * @param departmentDTO 作成する部署情報
   * @param uriComponentsBuilder URI構築用ビルダー
   * @return 201 Createdレスポンス（Locationヘッダーに新規リソースのURIを含む）
   */
  @PostMapping
  ResponseEntity<Void> post(
      @RequestBody DepartmentDTO departmentDTO, UriComponentsBuilder uriComponentsBuilder) {
    Department department = Department.create(departmentDTO.code());
    commandService.insert(department);
    URI uri =
        uriComponentsBuilder
            .path("api/departments/{departmentCode}")
            .build(department.getCode().value());
    return ResponseEntity.created(uri).build();
  }

  /**
   * 部署情報を更新します。
   *
   * @param departmentCode 更新対象の部署コード
   * @param departmentDTO 更新する部署情報
   * @return 204 No Contentレスポンス
   */
  @PutMapping("/{departmentCode}")
  ResponseEntity<Void> put(
      @PathVariable DepartmentCode departmentCode, @RequestBody DepartmentDTO departmentDTO) {
    Department department = Department.create(departmentDTO.code());
    commandService.update(departmentCode, department);
    return ResponseEntity.noContent().build();
  }

  /**
   * 部署情報を部分的に更新します。
   *
   * @param departmentCode 更新対象の部署コード
   * @param departmentDTO 更新する部署情報
   * @return 204 No Contentレスポンス
   */
  @PatchMapping("/{departmentCode}")
  ResponseEntity<Void> patch(
      @PathVariable DepartmentCode departmentCode, @RequestBody DepartmentDTO departmentDTO) {
    Department department = Department.create(departmentDTO.code());
    commandService.update(departmentCode, department);
    return ResponseEntity.noContent().build();
  }

  /**
   * 部署を削除します。
   *
   * @param departmentCode 削除対象の部署コード
   * @return 204 No Contentレスポンス
   */
  @DeleteMapping("/{departmentCode}")
  ResponseEntity<Void> delete(@PathVariable DepartmentCode departmentCode) {
    commandService.delete(departmentCode);
    return ResponseEntity.noContent().build();
  }
}
