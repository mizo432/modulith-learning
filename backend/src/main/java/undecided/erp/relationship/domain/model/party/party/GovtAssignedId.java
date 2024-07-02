package undecided.erp.relationship.domain.model.party.party;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.erp.common.primitive.Strings2;

/**
 * GovtAssignedIdクラスは、政府がパーティまたは組織に割り当てた識別子です。 これは、政府システム内でパーティまたは組織を一意に識別するために使われます。
 * GovtAssignedIdは通常、Partyクラスのフィールドとして使用されます。
 * <p>
 * 使用例: GovtAssignedId govtAssignedId = new GovtAssignedId();
 *
 * @see Party
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class GovtAssignedId {

  public static final GovtAssignedId EMPTY = new GovtAssignedId(null);

  @JsonValue
  private String value;

  public static GovtAssignedId empty() {
    return EMPTY;
  }

  public static GovtAssignedId of(String value) {
    if (Strings2.isEmpty(value)) {
      return EMPTY;

    }
    return new GovtAssignedId(value);
  }

  public static GovtAssignedId reconstruct(String value) {
    if (Strings2.isEmpty(value)) {
      return EMPTY;

    }
    return new GovtAssignedId(value);
  }

  @Override
  public String toString() {
    return String.valueOf(value);

  }

}
