package undecided.erp.shared.date;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DateInterval {

  private final ApplicableDate beginDate;
  private final ApplicableDate endDate;

  public static DateInterval create(@NonNull ApplicableDate beginDate,
      @NonNull ApplicableDate endDate) {
    return new DateInterval(beginDate, endDate);
  }


  public static DateInterval create(@NonNull ApplicableDate beginDate) {
    return new DateInterval(beginDate, ApplicableDate.MAX);
  }

}
