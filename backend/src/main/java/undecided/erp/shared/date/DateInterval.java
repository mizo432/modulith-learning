package undecided.erp.shared.date;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Embeddable
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class DateInterval {

  public static final DateInterval EMPTY = new DateInterval();

  @Embedded
  @JsonProperty
  private ApplicableDate beginDate = ApplicableDate.EMPTY;

  @Embedded
  @JsonProperty
  private ApplicableDate endDate = ApplicableDate.EMPTY;

  @JsonCreator
  public static DateInterval create(@JsonProperty("beginDate") @NonNull ApplicableDate beginDate,
      @JsonProperty("endDate") @NonNull ApplicableDate endDate) {
    return new DateInterval(beginDate, endDate);
  }


  public static DateInterval create(@NonNull ApplicableDate beginDate) {
    return new DateInterval(beginDate, ApplicableDate.MAX);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DateInterval that = (DateInterval) o;
    return Objects.equal(beginDate, that.beginDate)
        && Objects.equal(endDate, that.endDate);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(beginDate, endDate);
  }
}
