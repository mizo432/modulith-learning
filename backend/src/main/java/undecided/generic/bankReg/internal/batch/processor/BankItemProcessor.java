package undecided.generic.bankReg.internal.batch.processor;

import static undecided.shared.precondition.ObjectPrecondition.checkNotNull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import undecided.generic.bankReg.internal.batch.dto.BankCsvDto;
import undecided.generic.bankReg.spi.bank.Bank;
import undecided.generic.bankReg.spi.bank.BankCode;

/**
 * CSV から読み込んだ {@link BankCsvDto} を検証し、{@link Bank} エンティティに変換する {@link ItemProcessor} 実装。
 *
 * <p>金融機関コードまたは金融機関名が欠落している行はスキップします。
 */
@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class BankItemProcessor implements ItemProcessor<BankCsvDto, Bank> {

  @Value("#{jobParameters['datasetId']}")
  String datasetId;

  @Override
  public @Nullable Bank process(@NonNull BankCsvDto item) {
    checkNotNull(item, () -> new IllegalArgumentException("item must not be null"));

    String rawBankCode = item.getBankCode();
    if (rawBankCode == null || rawBankCode.isBlank()) {
      log.warn("Skipping record due to missing or empty bank code: {}", item);
      return null;
    }

    BankCode bankCode;
    try {
      bankCode = BankCode.of(rawBankCode);
    } catch (IllegalArgumentException ex) {
      log.warn("Skipping record due to invalid bank code format: {}", item);
      return null;
    }

    String bankName = item.getBankName();
    if (bankName == null || bankName.isBlank()) {
      log.warn("Skipping record due to missing or empty bank name for code {}: {}", bankCode, item);
      return null;
    }

    Bank bank = new Bank();
    bank.setBankCode(bankCode);
    bank.setBankName(bankName.trim());
    bank.setBankHalfKana(trimOrNull(item.getBankHalfKana()));
    bank.setBankFullKana(trimOrNull(item.getBankFullKana()));
    bank.setBankFullHira(trimOrNull(item.getBankFullHira()));
    bank.setBusinessTypeCode(trimOrNull(item.getBusinessTypeCode()));
    bank.setBusinessType(trimOrNull(item.getBusinessType()));
    bank.setDatasetId(datasetId);
    return bank;
  }

  private @Nullable String trimOrNull(@Nullable String value) {
    if (value == null || value.isBlank()) {
      return null;
    }
    return value.trim();
  }
}
