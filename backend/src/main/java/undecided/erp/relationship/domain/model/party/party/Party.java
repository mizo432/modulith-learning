package undecided.erp.relationship.domain.model.party.party;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.erp.shared.entity.SnowflakeId;

/**
 * Partyクラスは、組織または個人となるパーティを表します。
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Party {

  /**
   * この変数は、Snowflakeアルゴリズムを使用して一意の識別子を表します。 通常、Partyクラスのフィールドとして使用されます。
   */
  private SnowflakeId<Party> id;

  private PartyType type;

  @JsonProperty
  @AttributeOverrides({
      @AttributeOverride(name = "govtAssignedId.value", column = @Column(name = "govt_assigned_id"))
  })
  private PartyAttribute attribute;

  public static Party createForInsert(PartyType type, PartyAttribute attribute) {
    return new Party(SnowflakeId.newInstance(), type, attribute);

  }

  public static Party reconstruct(Long id, PartyType type, String govtAssignedId) {
    return new Party(SnowflakeId.reconstruct(id), type,
        PartyAttribute.create(GovtAssignedId.of(govtAssignedId)));
  }

  @Override
  public String toString() {
    return "Party{" +
        "id=" + id +
        ", type=" + type +
        ", attribute=" + attribute +
        '}';
  }
}
