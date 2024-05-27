package undecided.erp.relMgmt.model.party;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
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
  private final SnowflakeId<Party> id;

  private final PartyType type;
  /**
   * 政府が割り当てた団体や組織の識別子を表します。 この識別子は、政府システム内で団体や組織を一意に識別するために使用されます。
   * GovtAssignedIdは通常、Partyクラスのフィールドとして使用されます。
   *
   * @see Party
   */
  private final GovtAssignedId govtAssignedId;
  /**
   * この変数は、パーティーや組織単位が使用する現在の住所を表します。 AddressUseは、パーティーや組織単位が住所をどのように使用するかを定義します。
   * これはAddressUseオブジェクトのリストです。
   */
  private final List<AddressUse> currentAddressUses = new ArrayList<>();
  ;
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
  private final List<PartyRelationship> allPartyRelationships = new ArrayList<>();

  public static Party create(long id, PartyType type, String govtAssignedId) {
    return create(SnowflakeId.of(id), type, GovtAssignedId.of(govtAssignedId));
  }

  public List<PartyRelationship> listCurrentPartyRelationships() {
    return Collections.unmodifiableList(currentPartyRelationships);

  }

  @Override
  public String toString() {
    return "Party{" +
        "id=" + id +
        ", type=" + type +
        ", govtAssignedId=" + govtAssignedId +
        ", currentAddressUses=" + currentAddressUses +
        ", allAddressUses=" + allAddressUses +
        ", currentPartyRelationships=" + currentPartyRelationships +
        ", allPartyRelationships=" + allPartyRelationships +
        '}';
  }
}
