package undecided.erp.relationship.domain.model.party.party;

/**
 * Partyクラスは、組織または個人となるパーティを表します。
 */
public enum PartyType {
  /**
   * 個人
   */
  PERSON,
  /** 組織 */
  ORGANIZATION,
  /** 組織部門 */
  ORGANIZATION_UNIT;

  public boolean isOrganizationUnit() {
    return this == ORGANIZATION_UNIT;
  }
}
