package undecided.erp.relMgmt.model.party.party;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import undecided.erp.relMgmt.model.party.addressUse.AddressUse;
import undecided.erp.relMgmt.model.party.partyRelationship.PartyRelationship;
import undecided.erp.shared.entity.SnowflakeId;

/**
 * Partyクラスは、組織または個人となるパーティを表します。
 */
@RequiredArgsConstructor(access = AccessLevel.PUBLIC, staticName = "create")
public class Party {

  /**
   * この変数は、Snowflakeアルゴリズムを使用して一意の識別子を表します。 通常、Partyクラスのフィールドとして使用されます。
   */
  @Getter
  private final SnowflakeId<Party> id;

  @Getter
  private final PartyType type;

  @Getter
  private final PartyAttribute attribute;
  /**
   * この変数は、パーティーや組織単位が使用する現在の住所を表します。 AddressUseは、パーティーや組織単位が住所をどのように使用するかを定義します。
   * これはAddressUseオブジェクトのリストです。
   */
  @Getter
  private final List<AddressUse> currentAddressUses = new ArrayList<>();
  ;
  @Getter
  private final List<AddressUse> allAddressUses = new ArrayList<>();
  ;
  /**
   * この変数は、パーティーに関連付けられた現在のPartyRelationshipsを表します。
   * <p>
   * PartyRelationshipsは、パーティーや組織単位間の関係を定義します。 これはPartyRelationshipオブジェクトのリストです。
   *
   * @see Party
   * @see PartyRelationship
   */
  private final List<PartyRelationship> currentPartyRelationships = new ArrayList<>();
  @Getter
  private final List<PartyRelationship> allPartyRelationships = new ArrayList<>();

  public static Party create(Long id, PartyType type, PartyAttribute attribute) {
    return create(SnowflakeId.of(id), type, attribute);
  }

  public static Party createForInsert(PartyType type, PartyAttribute attribute) {
    return new Party(SnowflakeId.newInstance(), type, attribute);

  }

  public static Party reconstruct(Long id, PartyType type, String govtAssignedId) {
    return new Party(SnowflakeId.reconstruct(id), type,
        PartyAttribute.create(GovtAssignedId.of(govtAssignedId)));
  }

  public List<PartyRelationship> listCurrentPartyRelationships() {
    return Collections.unmodifiableList(currentPartyRelationships);

  }

  @Override
  public String toString() {
    return "Party{" +
        "id=" + id +
        ", type=" + type +
        ", attribute=" + attribute +
        ", allAddressUses=" + allAddressUses +
        ", allPartyRelationships=" + allPartyRelationships +
        '}';
  }
}
