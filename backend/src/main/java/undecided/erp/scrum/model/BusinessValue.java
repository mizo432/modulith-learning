package undecided.erp.scrum.model;

import jakarta.persistence.Embeddable;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Embeddable
public class BusinessValue {

  public static final BusinessValue EMPTY = new BusinessValue();

  private Integer value;

  public static BusinessValue of(Integer value) {
    return new BusinessValue(value);
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class BusinessValues {

    private Integer value;

    public BusinessValue toBusinessValue() {
      return BusinessValue.of(value);
    }


    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static BusinessValues of(List<BusinessValue> value) {
      return new BusinessValues(value.stream().mapToInt(BusinessValue::getValue).sum());

    }
  }
}
