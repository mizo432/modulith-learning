package undecided.erp.relationship.domain.model.party.organizationUnit;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import undecided.erp.common.verifier.StringVerifiers;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrganizationUnitName {

  public static final OrganizationUnitName EMPTY = new OrganizationUnitName();

  @JsonValue
  private String value;


  @JsonCreator
  public static OrganizationUnitName of(@NonNull String value) {
    StringVerifiers.verifyNonEmpty(value,
        () -> new IllegalArgumentException("value cannot be null or empty"));
    return new OrganizationUnitName(value);
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
