package undecided.erp.shared.date;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class YearInterval {

  private final ApplicableYear beginYear;
  private final ApplicableYear endYear;

  public static YearInterval create(@NonNull ApplicableYear beginYear,
      @NonNull ApplicableYear endYear) {
    return new YearInterval(beginYear, endYear);
  }

  public static YearInterval create(@NonNull ApplicableYear beginYear) {
    return new YearInterval(beginYear, ApplicableYear.MAX);
  }

}
