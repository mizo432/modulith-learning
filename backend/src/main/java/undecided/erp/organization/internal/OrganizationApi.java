package undecided.erp.organization.internal;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.erp.organization.spi.Organization;
import undecided.erp.organization.spi.OrganizationQuery;

/** 組織に関連するAPIエンドポイントを提供するコントローラクラスです。 このクラスは組織データの取得およびIDを基にした検索機能を提供します。 */
@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationApi {
  private final OrganizationQuery organizationQuery;

  /**
   * すべての組織データを取得します。
   *
   * @return 組織オブジェクトのリスト
   */
  @GetMapping()
  List<Organization> findAll() {
    return organizationQuery.findAll();
  }

  /**
   * 指定されたIDに基づいて組織を検索します。
   *
   * @param id 検索対象の組織を識別するUUID
   * @return 指定されたIDに対応する組織オブジェクト
   * @throws EntityNotFoundException 指定されたIDに対応する組織が見つからない場合
   */
  @GetMapping("/{id}")
  Organization findById(@PathVariable("id") UUID id) {
    return organizationQuery
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Organization not found: " + id + ""));
  }
}
