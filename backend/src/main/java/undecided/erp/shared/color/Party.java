package undecided.erp.shared.color;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Immutable;
import undecided.erp.relationship.domain.model.party.party.PartyType;
import undecided.erp.shared.entity.PptEntity;
import undecided.erp.shared.entity.SnowflakeId;

@MappedSuperclass
@Getter
@Setter
@Immutable
public class Party extends PptEntity<Party> {

  SnowflakeId partyId;
  SearchName searchName;
  PartyType partyType;
}
