package undecided.association.relationshipMgmt.spi.party;

import undecided.association.relationshipMgmt.spi.party.orgUnit.dto.OrgUnitDto;

/**
 * 組織単位新規作成のコマンドインターフェースです。
 *
 * <p>このインターフェースは、組織単位（OrgUnit）を新規作成するために使用されます。
 */
public interface CreateOrgUnitCommand {

  /**
   * 組織単位を新規作成します。
   *
   * @param orgUnitDto 組織単位情報のDTO。orgUnitName、orgUnitCode、orgUnitTypeCode、parentOrgUnitId、status、orgIdを含みます。
   */
  void create(OrgUnitDto orgUnitDto);
}
