package undecided.erp.relMgmt.model.party;

import lombok.AccessLevel;
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
public class GovtAssignedId {

  private final String value;

  @Override
  public String toString() {
    return value;
  }
}
