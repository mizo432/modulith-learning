package undecided.generic.calendarReg.internal;

import static undecided.supporting.precondition.ObjectPrecondition.checkNotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import undecided.generic.calendarReg.spi.Holiday;
import undecided.generic.calendarReg.spi.HolidayQuery;

/** {@link HolidayQuery} の実装クラス。 */
@Service
@RequiredArgsConstructor
public class HolidayQueryImpl implements HolidayQuery {

  private final HolidayRepository holidayRepository;

  @Override
  public @NonNull Optional<Holiday> findByHolidayDate(@NonNull LocalDate date) {
    checkNotNull(date, () -> new IllegalArgumentException("date must not be null"));
    return holidayRepository.findByHolidayDate(date);
  }

  @Override
  public @NonNull List<Holiday> findByYear(int year) {
    LocalDate from = LocalDate.of(year, 1, 1);
    LocalDate to = LocalDate.of(year, 12, 31);
    return holidayRepository.findBetween(from, to);
  }

  @Override
  public @NonNull List<Holiday> findBetween(@NonNull LocalDate from, @NonNull LocalDate to) {
    checkNotNull(from, () -> new IllegalArgumentException("from must not be null"));
    checkNotNull(to, () -> new IllegalArgumentException("to must not be null"));
    return holidayRepository.findBetween(from, to);
  }
}
