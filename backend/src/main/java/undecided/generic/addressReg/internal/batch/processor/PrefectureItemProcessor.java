package undecided.generic.addressReg.internal.batch.processor;

import static undecided.shared.precondition.ObjectPrecondition.checkNotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;
import undecided.generic.addressReg.internal.PrefectureRepository;
import undecided.generic.addressReg.internal.batch.dto.PrefectureCsvDto;
import undecided.generic.addressReg.spi.Prefecture;

/**
 * CSV から読み込んだ {@link PrefectureCsvDto} を検証し、既存データと照合して {@link Prefecture} エンティティを生成・更新する {@link
 * ItemProcessor} 実装。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PrefectureItemProcessor implements ItemProcessor<PrefectureCsvDto, Prefecture> {

  private static final LocalDate DEFAULT_EFFECTIVE_DATE = LocalDate.of(1947, 4, 17);
  private static final LocalDate DEFAULT_ABOLITION_DATE = LocalDate.of(9999, 12, 31);

  private final PrefectureRepository prefectureRepository;

  @Override
  public @Nullable Prefecture process(@NonNull PrefectureCsvDto item) {
    checkNotNull(item, () -> new IllegalArgumentException("item must not be null"));

    String prefectureCode = item.getPrefectureCode();
    if (prefectureCode == null || prefectureCode.isBlank()) {
      log.warn("Skipping record due to missing or invalid prefecture code: {}", item);
      return null;
    }

    Prefecture existing = prefectureRepository.findByPrefectureCode(prefectureCode);
    Prefecture prefecture = existing != null ? existing : new Prefecture();

    if (existing == null) {
      try {
        prefecture.setId(Long.valueOf(prefectureCode));
      } catch (NumberFormatException e) {
        log.warn("Invalid prefecture code format for ID: {}", prefectureCode);
        return null;
      }
      prefecture.setPrefectureCode(prefectureCode);
    }

    prefecture.setLgCode(item.getLgCode() != null ? item.getLgCode().trim() : "");
    prefecture.setPrefName(item.getPrefName() != null ? item.getPrefName().trim() : "");
    prefecture.setPrefKana(item.getPrefKana() != null ? item.getPrefKana().trim() : "");
    prefecture.setPrefRoma(item.getPrefRoma() != null ? item.getPrefRoma().trim() : "");
    prefecture.setEffectiveDate(parseDate(item.getEffectiveDate(), DEFAULT_EFFECTIVE_DATE));
    prefecture.setAbolitionData(parseDate(item.getAbolitionDate(), DEFAULT_ABOLITION_DATE));
    prefecture.setRemarks(item.getRemarks() != null ? item.getRemarks().trim() : null);

    return prefecture;
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
