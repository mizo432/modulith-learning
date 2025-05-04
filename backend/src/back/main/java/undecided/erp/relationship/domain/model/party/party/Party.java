package undecided.erp.relationship.domain.model.party.party;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import undecided.erp.relationship.domain.model.party.partyRelationship.IPartyRelationship;
import undecided.erp.relationship.domain.model.party.partyRelationship.IPartyRelationship.IPartyRelationships;
import undecided.erp.relationship.domain.model.party.partyRelationship.PartyRelationship.PartyRelationships;
import undecided.erp.shared.entity.SnowflakeId;

/**
 * Partyクラスは、組織または個人となるパーティを表します。
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@EqualsAndHashCode
public class Party {

  /**
   * この変数は、Snowflakeアルゴリズムを使用して一意の識別子を表します。 通常、Partyクラスのフィールドとして使用されます。
   */
  private SnowflakeId id;

  private PartyType type;

  private SimpleName simpleName;

  private IPartyRelationships partyRelationships;

  /**
   * 一意の識別子、タイプ、簡単な名前、およびパーティーの関係を持つ Party クラスの新しいインスタンスを挿入のために作成します。
   *
   * @param type パーティーの種類で、個人か組織を指定します。null であってはなりません。
   * @param simpleName パーティーを表す簡単な名前で、null でもかまいません。
   * @param partyRelationships パーティーに関連する関係で、null であってはなりません。
   * @return 一意の識別子、指定されたタイプ、簡単な名前、およびパーティーの関係で 初期化された新しい Party インスタンスを返します。
   */
  public static Party createForInsert(@NonNull PartyType type, SimpleName simpleName,
      @NonNull IPartyRelationships partyRelationships) {
    return new Party(SnowflakeId.newInstance(), type, simpleName, partyRelationships);

  }

  /**
   * 与えられたパラメータからPartyインスタンスを再構築します。
   *
   * @param id パーティの一意の識別子です。nullである可能性があり、その場合はSnowflakeId.EMPTYが割り当てられます。
   * @param type パーティのタイプで、個人または組織のいずれかです。nullにはできません。
   * @param simpleName パーティを表す簡易的な名前です。nullである可能性があり、その場合は空のSimpleNameが割り当てられます。
   * @param partyRelationships パーティに関連する関係です。nullにはできません。
   * @return 提供された一意の識別子、指定されたタイプ、簡易名、および関係で初期化した再構築されたPartyインスタンスを返します。
   */
  public static Party reconstruct(Long id, PartyType type, String simpleName,
      List<IPartyRelationship> partyRelationships) {
    return new Party(SnowflakeId.reconstruct(id), type, SimpleName.reconstruct(simpleName),
        PartyRelationships.reconstruct(partyRelationships));
  }

  @Override
  public String toString() {
    return "Party{" +
        "id=" + id +
        ", type=" + type +
        ", simpleName=" + simpleName +
        ", partyRelationships=" + partyRelationships +
        '}';
  }
}
