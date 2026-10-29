package undecided.association.relationshipMgmt.spi.party.address;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import undecided.association.relationshipMgmt.SchemaRelationshipMgmt;
import com.github.mizo432.shared.value.datetime.WhenOccured;
import undecided.supporting.snowflake.SnowflakeId;

@Table(schema = SchemaRelationshipMgmt.NAME, name = "AddressUses")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressUse {
  @Id private SnowflakeId addressUseId = SnowflakeId.EMPTY;
  private SnowflakeId addressId = SnowflakeId.EMPTY;
  private SnowflakeId partyId = SnowflakeId.EMPTY;
  private AddressUseType type = AddressUseType.UNKNOWN;
  private WhenOccured whenOccured = WhenOccured.empty();
}
