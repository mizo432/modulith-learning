package undecided.erp.relMgmt.model.party.party;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * GovtAssignedIdクラスは、政府がパーティまたは組織に割り当てた識別子です。 これは、政府システム内でパーティまたは組織を一意に識別するために使われます。
 * GovtAssignedIdは通常、Partyクラスのフィールドとして使用されます。
 * <p>
 * 使用例: GovtAssignedId govtAssignedId = new GovtAssignedId();
 *
 * @see Party
 */
@RequiredArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
@EqualsAndHashCode
public class GovtAssignedId {

  public static final GovtAssignedId EMPTY = new GovtAssignedId(null);
  @Getter
  private final String value;

  public static GovtAssignedId empty() {
    return EMPTY;
  }

  @Override
  public String toString() {
    return value;

  }

}
