package undecided.erp.relationship.domain.model.party.party;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Embeddable
@EqualsAndHashCode
public class PartyAttribute {

  /**
   * 政府が割り当てた団体や組織の識別子を表します。 この識別子は、政府システム内で団体や組織を一意に識別するために使用されます。
   * GovtAssignedIdは通常、Partyクラスのフィールドとして使用されます。
   *
   * @see Party
   */
  @JsonProperty
  private GovtAssignedId govtAssignedId;

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
