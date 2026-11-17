package undecided.association.relationshipMgmt.spi.party;

import undecided.association.relationshipMgmt.spi.party.org.dto.OrgDto;

/**
 * 組織新規作成のコマンドインターフェースです。
 *
 * <p>このインターフェースは、組織（Org）を新規作成するために使用されます。
 */
public interface CreateOrgCommand {

  /**
   * 組織を新規作成します。
   *
   * @param orgDto 組織情報のDTO。orgName、orgCode、orgTypeCode、parentOrgId、statusを含みます。
   */
  void create(OrgDto orgDto);
}
