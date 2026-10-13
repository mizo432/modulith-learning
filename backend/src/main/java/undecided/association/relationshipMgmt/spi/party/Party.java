package undecided.association.relationshipMgmt.spi.party;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import undecided.supporting.snowflake.SnowflakeId;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class Party {

  private SnowflakeId partyId;
}
