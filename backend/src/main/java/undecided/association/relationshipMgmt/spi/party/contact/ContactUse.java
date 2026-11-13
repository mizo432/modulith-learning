package undecided.association.relationshipMgmt.spi.party.contact;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import undecided.association.relationshipMgmt.SchemaRelationshipMgmt;
import com.github.mizo432.shared.value.datetime.WhenOccured;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 連絡先の使用履歴を表すエンティティです。
 *
 * <p>パーティと連絡先アドレスの関連付けを管理します。
 */
@Table(schema = SchemaRelationshipMgmt.NAME, name = "ContactUses")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactUse {
  @Id private SnowflakeId contactUseId = SnowflakeId.EMPTY;
  @Column(name = "party_id")
  private SnowflakeId partyId = SnowflakeId.EMPTY;
  private Contact contact = Contact.empty();
  private PhoneNo phoneNo = PhoneNo.empty();
  private ContactUseType type = ContactUseType.UNKNOWN;
  private WhenOccured whenOccured = WhenOccured.empty();
}
