package undecided.generic.calendarReg.internal.batch.processor;

import static undecided.supporting.precondition.ObjectPrecondition.checkNotNull;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;
import undecided.generic.calendarReg.internal.HolidayRepository;
import undecided.generic.calendarReg.internal.batch.dto.HolidayCsvDto;
import undecided.generic.calendarReg.spi.Holiday;

/**
 * CSV から読み込んだ {@link HolidayCsvDto} を検証し、既存データと照合して {@link Holiday} エンティティを生成・更新する {@link
 * ItemProcessor} 実装。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HolidayItemProcessor implements ItemProcessor<HolidayCsvDto, Holiday> {

  private final HolidayRepository holidayRepository;

  @Override
  public @Nullable Holiday process(@NonNull HolidayCsvDto item) {
    checkNotNull(item, () -> new IllegalArgumentException("item must not be null"));

    LocalDate holidayDate = item.getParsedHolidayDate();
    if (holidayDate == null) {
      log.warn("Skipping record due to missing or invalid holiday date: {}", item);
      return null;
    }

    String holidayName = item.getHolidayName();
    if (holidayName == null || holidayName.isBlank()) {
      log.warn("Skipping record due to missing or empty holiday name: {}", item);
      return null;
    }

    Holiday existing = holidayRepository.findByHolidayDate(holidayDate).orElse(null);
    Holiday holiday = existing != null ? existing : new Holiday();

    if (existing == null) {
      Long id = item.getHolidayId();
      if (id == null) {
        log.warn("Invalid holiday ID generation for date: {}", holidayDate);
        return null;
      }
      holiday.setId(id);
      holiday.setHolidayDate(holidayDate);
    }

    holiday.setHolidayName(holidayName.trim());
    holiday.setRemarks(item.getRemarks() != null ? item.getRemarks().trim() : null);

    return holiday;
  }
}
