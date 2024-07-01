package undecided.erp.relationship.domain.model.party.organizationUnit;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrganizationUnitAttribute {

  public static final OrganizationUnitAttribute EMPTY = new OrganizationUnitAttribute();

  @JsonProperty
  private OrganizationUnitName name = OrganizationUnitName.EMPTY;

  public static OrganizationUnitAttribute create(@NonNull OrganizationUnitName name) {
    return new OrganizationUnitAttribute(name);

  }
}
