package undecided.erp.addressMgmt.domain.model.city;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class WardName {

  private String value;

  public static WardName reconstruct(String value) {
    return new WardName(value);

  }

  public static WardName of(@NonNull String value) {
    return new WardName(value);

  }

  @Override
  public String toString() {
    return value;
  }

}
