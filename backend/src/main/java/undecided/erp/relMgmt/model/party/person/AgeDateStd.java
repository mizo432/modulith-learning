package undecided.erp.relMgmt.model.party.person;

import org.springframework.lang.NonNull;
import undecided.erp.shared.date.Today;

public record AgeDateStd(Integer value) {

  public static AgeDateStd calculateFrom(@NonNull Birthday birthday, @NonNull Today today) {
    return new AgeDateStd(
        (today.nextMonthInteger() - birthday.dateInteger()) / 10000);
  }

}
