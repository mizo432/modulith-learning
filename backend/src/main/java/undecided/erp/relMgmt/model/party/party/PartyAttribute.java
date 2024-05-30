package undecided.erp.relMgmt.model.party.party;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PartyAttribute {

  /**
   * 政府が割り当てた団体や組織の識別子を表します。 この識別子は、政府システム内で団体や組織を一意に識別するために使用されます。
   * GovtAssignedIdは通常、Partyクラスのフィールドとして使用されます。
   *
   * @see Party
   */
  private final GovtAssignedId govtAssignedId;

  public static PartyAttribute create(GovtAssignedId govtAssignedId) {
    return new PartyAttribute(govtAssignedId);
  }

  @Override
  public String toString() {
    return "PartyAttribute{" +
        "govtAssignedId=" + govtAssignedId +
        '}';
  }
}
