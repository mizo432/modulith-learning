package undecided.erp.relationship.domain.model.party.addressUse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import undecided.erp.relationship.domain.model.party.address.Address;
import undecided.erp.relationship.domain.model.party.party.Party;
import undecided.erp.shared.date.DateInterval;
import undecided.erp.shared.entity.SnowflakeId;

/**
 * AddressUseクラスは、パーティーまたは組織単位が住所を使用する方法を表します。
 */
@Entity
@Table(schema = "relationship", name = "address_use_history")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AddressUse {

  @Id
  @EmbeddedId
  @AttributeOverride(name = "value", column = @Column(name = "address_use_id"))
  private SnowflakeId<AddressUse> id;
  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "party_id"))
  private SnowflakeId<Party> partyId;

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "address_id"))
  private SnowflakeId<Address> addressId;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "beginDate.value", column = @Column(name = "begin_date")),
      @AttributeOverride(name = "endDate.value", column = @Column(name = "end_date"))})
  private DateInterval interval = DateInterval.EMPTY;
  /**
   * このコメントは、パーティーや組織単位の住所使用タイプを表しています
   */
  @Column(name = "type", nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  private AddressUseType type = AddressUseType.UNKNOWN;

  @Column(name = "reason_for_transfer", nullable = false, length = 30)
  @Enumerated(EnumType.STRING)
  private ReasonForTransferType reasonForTransfer = ReasonForTransferType.UNKNOWN;

  @JsonCreator
  public static AddressUse create(
      @JsonProperty("id") SnowflakeId<AddressUse> id,
      @JsonProperty("partyId") SnowflakeId<Party> partyId,
      @JsonProperty("addressId") SnowflakeId<Address> addressId,
      @JsonProperty("interval") DateInterval interval,
      @JsonProperty("type") AddressUseType type,
      @JsonProperty("reasonForTransfer") ReasonForTransferType reasonForTransfer
  ) {
    return new AddressUse(id, partyId, addressId, interval, type, reasonForTransfer);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AddressUse that = (AddressUse) o;
    return Objects.equal(id, that.id)
        && Objects.equal(partyId, that.partyId)
        && Objects.equal(addressId, that.addressId)
        && Objects.equal(interval, that.interval)
        && type == that.type
        && Objects.equal(reasonForTransfer, that.reasonForTransfer);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, type, partyId, addressId, interval, type, reasonForTransfer);

  }
}
