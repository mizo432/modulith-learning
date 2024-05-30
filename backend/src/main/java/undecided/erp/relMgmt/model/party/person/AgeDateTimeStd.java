package undecided.erp.relMgmt.model.party.person;

import java.time.temporal.ChronoUnit;
import org.springframework.lang.NonNull;
import undecided.erp.shared.date.Today;

public record AgeDateTimeStd(Integer value) {

  public static AgeDateTimeStd calculateFrom(@NonNull Birthday birthday, @NonNull Today today) {
    return new AgeDateTimeStd(
        Long.valueOf(ChronoUnit.YEARS.between(birthday.value(), today.value())).intValue());
  }

}
