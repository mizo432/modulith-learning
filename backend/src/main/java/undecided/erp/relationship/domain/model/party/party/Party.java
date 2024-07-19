package undecided.erp.relationship.domain.model.party.party;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
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

  public static Party createForInsert(@NonNull PartyType type) {
    return new Party(SnowflakeId.newInstance(), type);

  }

  public static Party reconstruct(Long id, PartyType type) {
    return new Party(SnowflakeId.reconstruct(id), type);
  }

  @Override
  public String toString() {
    return "Party{" +
        "id=" + id +
        ", type=" + type +
        '}';
  }
}
