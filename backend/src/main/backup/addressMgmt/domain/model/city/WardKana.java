package undecided.erp.addressMgmt.domain.model.city;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class WardKana {

  private String value;

  public static WardKana reconstruct(String value) {
    return new WardKana(value);

  }

  public static WardKana of(String value) {
    return new WardKana(value);

  }

  @Override
  public String toString() {
    return value;
  }

}
