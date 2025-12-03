package undecided.erp.depertment.spi;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import undecided.erp.shared.entity.AuditResource;

/**
 * Organizationは、組織に関する基本情報を保持するエンティティクラスです。
 *
 * <p>このクラスは、組織の階層構造（レベル0、レベル1、レベル2）を表現する情報を保管します。また、
 * AuditResourceを継承しており、作成者・更新者および作成・更新日時といった監査情報を自動的に管理します。
 *
 * <p>主要な属性として、組織ID、組織UUID、フルネーム、階層レベル名およびIDを含みます。
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Organization extends AuditResource {
  private String organizationId;
  private UUID organizationUuid;
  private String fullName;
  private String level0Name;
  private String level1Name;
  private String level2Name;
  private String level0Id;
  private String level1Id;
  private String level2Id;

  /**
   * 現在の組織IDを基に、親組織のIDを取得します。
   *
   * <pre>
   * 組織IDの階層レベルに応じて、親組織IDを計算して返します。
   * 階層レベルが0の場合は、親組織が存在しないためnullを返します。
   *
   * </pre>
   *
   * @return 親組織ID。階層レベルが0の場合はnullを返します。それ以外の場合は計算された親組織IDを返します。
   */
  public String getParentOrganizationId() {
    OrganizationId organizationId = new OrganizationId(this.organizationId);
    Integer level = organizationId.getLevel();
    return level == 0 ? null : organizationId.getParentOrganizationId();
  }
}
