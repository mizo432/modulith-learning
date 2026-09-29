package undecided.generic.addressReg.internal.batch.processor;

import static undecided.supporting.precondition.ObjectPrecondition.checkNotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;
import undecided.generic.addressReg.internal.CityRepository;
import undecided.generic.addressReg.internal.batch.dto.CityCsvDto;
import undecided.generic.addressReg.spi.City;

/**
 * CSV から読み込んだ {@link CityCsvDto} を検証し、既存データと照合して {@link City} エンティティを生成・更新する {@link ItemProcessor}
 * 実装。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CityItemProcessor implements ItemProcessor<CityCsvDto, City> {

  private static final LocalDate DEFAULT_EFFECTIVE_DATE = LocalDate.of(1947, 4, 17);
  private static final LocalDate DEFAULT_ABOLITION_DATE = LocalDate.of(9999, 12, 31);

  private final CityRepository cityRepository;

  @Override
  public @Nullable City process(@NonNull CityCsvDto item) {
    checkNotNull(item, () -> new IllegalArgumentException("item must not be null"));

    String lgCode = item.getLgCode();
    if (lgCode == null || lgCode.isBlank()) {
      log.warn("Skipping record due to missing or invalid lgCode: {}", item);
      return null;
    }
    String trimmedLgCode = lgCode.trim();

    Long prefectureId = item.getPrefectureId();
    if (prefectureId == null) {
      log.warn("Skipping record due to missing or invalid prefectureId from lgCode: {}", item);
      return null;
    }

    City existing = cityRepository.findByLgCode(trimmedLgCode);
    City city = existing != null ? existing : new City();

    if (existing == null) {
      try {
        city.setId(Long.valueOf(trimmedLgCode));
      } catch (NumberFormatException e) {
        log.warn("Invalid lgCode format for ID: {}", trimmedLgCode);
        return null;
      }
      city.setLgCode(trimmedLgCode);
    }

    city.setPrefectureId(prefectureId);
    city.setCountryName(toNullIfEmpty(item.getCountryName()));
    city.setCountryKana(toNullIfEmpty(item.getCountryKana()));
    city.setCountryRoma(toNullIfEmpty(item.getCountryRoma()));
    city.setCityName(toNullIfEmpty(item.getCityName()));
    city.setCityKana(toNullIfEmpty(item.getCityKana()));
    city.setCityRoma(toNullIfEmpty(item.getCityRoma()));
    city.setWardName(toNullIfEmpty(item.getWardName()));
    city.setWardKana(toNullIfEmpty(item.getWardKana()));
    city.setWardRoma(toNullIfEmpty(item.getWardRoma()));
    city.setEffectiveDate(parseDate(item.getEffectiveDate(), DEFAULT_EFFECTIVE_DATE));
    city.setAbolitionData(parseDate(item.getAbolitionDate(), DEFAULT_ABOLITION_DATE));
    city.setRemarks(toNullIfEmpty(item.getRemarks()));

    return city;
  }

  private @Nullable String toNullIfEmpty(@Nullable String value) {
    if (value == null) {
      return null;
    }
    String trimmed = value.trim();
    return trimmed.isEmpty() ? null : trimmed;
  }

  private LocalDate parseDate(String dateStr, LocalDate defaultDate) {
    if (dateStr == null || dateStr.trim().isEmpty()) {
      return defaultDate;
    }
    String trimmed = dateStr.trim();
    if (trimmed.equals("9999-12-31")
        || trimmed.equals("9999/12/31")
        || trimmed.equals("99991231")) {
      return DEFAULT_ABOLITION_DATE;
    }
    try {
      return LocalDate.parse(trimmed.replace('/', '-'), DateTimeFormatter.ISO_LOCAL_DATE);
    } catch (DateTimeParseException e) {
      log.warn("Failed to parse date '{}', using default '{}'", dateStr, defaultDate);
      return defaultDate;
    }
  }
}
