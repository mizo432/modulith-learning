package undecided.erp.shared.date;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class YearMonthInterval {

  private final ApplicableYearMonth beginYearMonth;
  private final ApplicableYearMonth endYearMonth;

  public static YearMonthInterval create(@NonNull ApplicableYearMonth beginYearMonth,
      @NonNull ApplicableYearMonth endYearMonth) {
    return new YearMonthInterval(beginYearMonth, endYearMonth);
  }


  public static YearMonthInterval create(@NonNull ApplicableYearMonth beginYearMonth) {
    return new YearMonthInterval(beginYearMonth, ApplicableYearMonth.MAX);
    
  }

}
