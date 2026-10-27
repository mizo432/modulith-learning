package undecided.association.relationshipMgmt.spi.party.partyRelationship;

/**
 * パーティ間の関係の種類を表す列挙型です。
 *
 * <p>このenumは、パーティ（人物や組織など）之间存在する関連付けのタイプを定義します。
 */
public enum RelationshipType {
  /** 不明な関係タイプです。 */
  UNKNOWN,
  /** 雇用関係を表します。一方のパーティが他方のパーティに雇用されている状態です。 */
  EMPLOYED_BY
}
