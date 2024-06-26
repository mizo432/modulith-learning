package undecided.erp.relMgmt.domain.model.personRole.productOwner;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.erp.relMgmt.domain.model.party.party.Party;
import undecided.erp.shared.entity.SnowflakeId;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
public class ProductOwner {

  public static final ProductOwner EMPTY = new ProductOwner();

  private SnowflakeId<Party> id;
  private String name;
  private String email;

  public static ProductOwner create(SnowflakeId<Party> id, String name, String email) {
    return new ProductOwner(id, name, email);
  }


}
