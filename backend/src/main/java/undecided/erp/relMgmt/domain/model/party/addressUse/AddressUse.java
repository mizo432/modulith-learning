package undecided.erp.relMgmt.domain.model.party.addressUse;

import java.util.List;
import undecided.erp.relMgmt.domain.model.party.address.Address;
import undecided.erp.relMgmt.domain.model.party.organizationUnit.OrganizationUnit;
import undecided.erp.relMgmt.domain.model.party.party.Party;
import undecided.erp.shared.entity.SnowflakeId;

/**
 * AddressUseクラスは、パーティーまたは組織単位が住所を使用する方法を表します。
 */
public class AddressUse {

  private SnowflakeId<AddressUse> id;
  /**
   * このコメントは、パーティーや組織単位の住所使用タイプを表しています
   */
  private AddressUseType type;
  private List<Party> parties;
  private Address address;
  private List<OrganizationUnit> organizationUnits;

}
