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
import undecided.generic.addressReg.internal.ChouAzaRepository;
import undecided.generic.addressReg.internal.batch.dto.ChouAzaCsvDto;
import undecided.generic.addressReg.spi.ChouAza;

/**
 * CSV から読み込んだ {@link ChouAzaCsvDto} を検証し、既存データと照合して {@link ChouAza} エンティティを生成・更新する {@link
 * ItemProcessor} 実装。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChouAzaItemProcessor implements ItemProcessor<ChouAzaCsvDto, ChouAza> {

  private static final LocalDate DEFAULT_EFFECTIVE_DATE = LocalDate.of(1947, 4, 17);
  private static final LocalDate DEFAULT_ABOLITION_DATE = LocalDate.of(9999, 12, 31);

  private final ChouAzaRepository chouAzaRepository;

  @Override
  public @Nullable ChouAza process(@NonNull ChouAzaCsvDto item) {
    checkNotNull(item, () -> new IllegalArgumentException("item must not be null"));

    String lgCode = item.getLgCode();
    if (lgCode == null || lgCode.isBlank()) {
      log.warn("Skipping record due to missing or invalid lgCode: {}", item);
      return null;
    }
    String trimmedLgCode = lgCode.trim();

    String machiazaCode = item.getMachiazaCode();
    if (machiazaCode == null || machiazaCode.isBlank()) {
      log.warn("Skipping record due to missing or invalid machiazaCode: {}", item);
      return null;
    }
    String trimmedMachiazaCode = machiazaCode.trim();

    Long cityId = item.getCityId();
    if (cityId == null) {
      log.warn("Skipping record due to missing or invalid cityId from lgCode: {}", item);
      return null;
    }

    Long chouAzaId = item.getChouAzaId();
    if (chouAzaId == null) {
      log.warn("Skipping record due to invalid chouAzaId: {}", item);
      return null;
    }

    ChouAza existing =
        chouAzaRepository.findByLgCodeAndMachiazaCode(trimmedLgCode, trimmedMachiazaCode);
    ChouAza chouAza = existing != null ? existing : new ChouAza();

    if (existing == null) {
      chouAza.setId(chouAzaId);
      chouAza.setCityId(cityId);
      chouAza.setLgCode(trimmedLgCode);
      chouAza.setMachiazaCode(trimmedMachiazaCode);
    } else {
      chouAza.setCityId(cityId);
      chouAza.setLgCode(trimmedLgCode);
      chouAza.setMachiazaCode(trimmedMachiazaCode);
    }

    String machiazaType = item.getMachiazaType();
    chouAza.setMachiazaType(
        machiazaType != null && !machiazaType.isBlank() ? machiazaType.trim() : "1");

    chouAza.setOazaChoName(toNullIfEmpty(item.getOazaChoName()));
    chouAza.setOazaChoKana(toNullIfEmpty(item.getOazaChoKana()));
    chouAza.setOazaChoRoma(toNullIfEmpty(item.getOazaChoRoma()));
    chouAza.setChomeName(toNullIfEmpty(item.getChomeName()));
    chouAza.setChomeKana(toNullIfEmpty(item.getChomeKana()));
    chouAza.setChomeNumber(toNullIfEmpty(item.getChomeNumber()));
    chouAza.setKoazaName(toNullIfEmpty(item.getKoazaName()));
    chouAza.setKoazaKana(toNullIfEmpty(item.getKoazaKana()));
    chouAza.setKoazaRoma(toNullIfEmpty(item.getKoazaRoma()));
    chouAza.setMachiazaDist(toNullIfEmpty(item.getMachiazaDist()));
    chouAza.setRsdtAddrFlg(parseBoolean(item.getRsdtAddrFlg(), false));
    chouAza.setRsdtAddrMtdCode(toNullIfEmpty(item.getRsdtAddrMtdCode()));
    chouAza.setOazaChoAkaFlg(parseBoolean(item.getOazaChoAkaFlg(), false));
    chouAza.setKoazaAkaCode(toNullIfEmpty(item.getKoazaAkaCode()));
    chouAza.setOazaChoGsiUncmn(toNullIfEmpty(item.getOazaChoGsiUncmn()));
    chouAza.setKoazaGsiUncmn(toNullIfEmpty(item.getKoazaGsiUncmn()));
    chouAza.setStatus(parseInteger(item.getStatus(), 0));
    chouAza.setWakeNumFlg(parseBoolean(item.getWakeNumFlg(), false));
    chouAza.setSrcCode(toNullIfEmpty(item.getSrcCode()));
    chouAza.setEffectiveDate(parseDate(item.getEffectiveDate(), DEFAULT_EFFECTIVE_DATE));
    chouAza.setAbolitionData(parseDate(item.getAbolitionDate(), DEFAULT_ABOLITION_DATE));
    chouAza.setRemarks(toNullIfEmpty(item.getRemarks()));

    return chouAza;
  }

  private @Nullable String toNullIfEmpty(@Nullable String value) {
    if (value == null) {
      return null;
    }
    String trimmed = value.trim();
    return trimmed.isEmpty() ? null : trimmed;
  }

  private boolean parseBoolean(@Nullable String value, boolean defaultValue) {
    if (value == null || value.trim().isEmpty()) {
      return defaultValue;
    }
    String trimmed = value.trim();
    if ("1".equals(trimmed)
        || "true".equalsIgnoreCase(trimmed)
        || "t".equalsIgnoreCase(trimmed)
        || "y".equalsIgnoreCase(trimmed)
        || "yes".equalsIgnoreCase(trimmed)) {
      return true;
    }
    if ("0".equals(trimmed)
        || "false".equalsIgnoreCase(trimmed)
        || "f".equalsIgnoreCase(trimmed)
        || "n".equalsIgnoreCase(trimmed)
        || "no".equalsIgnoreCase(trimmed)) {
      return false;
    }
    return defaultValue;
  }

  private int parseInteger(@Nullable String value, int defaultValue) {
    if (value == null || value.trim().isEmpty()) {
      return defaultValue;
    }
    try {
      return Integer.parseInt(value.trim());
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  private @NonNull LocalDate parseDate(@Nullable String dateStr, @NonNull LocalDate defaultDate) {
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
