package undecided.generic.bankReg.internal.batch.processor;

import static undecided.supporting.precondition.ObjectPrecondition.checkNotNull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import undecided.generic.bankReg.internal.BranchRepository;
import undecided.generic.bankReg.internal.batch.dto.BranchCsvDto;
import undecided.generic.bankReg.spi.bank.BankCode;
import undecided.generic.bankReg.spi.branch.Branch;
import undecided.generic.bankReg.spi.branch.BranchCode;

/**
 * CSV から読み込んだ {@link BranchCsvDto} を検証し、既存データと照合して {@link Branch} エンティティを生成・更新する {@link
 * ItemProcessor} 実装。
 */
@Slf4j
@RequiredArgsConstructor
public class BranchItemProcessor implements ItemProcessor<BranchCsvDto, Branch> {

  private final BranchRepository branchRepository;

  /** 最新のデータセットID（ジョブパラメータから注入） */
  private String datasetId;

  public void setDatasetId(String datasetId) {
    this.datasetId = datasetId;
  }

  @Override
  public @Nullable Branch process(@NonNull BranchCsvDto item) {
    checkNotNull(item, () -> new IllegalArgumentException("item must not be null"));

    String rawBankCode = item.getBankCode();
    String rawBranchCode = item.getBranchCode();

    if (rawBankCode == null || rawBankCode.isBlank()) {
      log.warn("Skipping record due to missing or invalid bank code: {}", item);
      return null;
    }
    if (rawBranchCode == null || rawBranchCode.isBlank()) {
      log.warn("Skipping record due to missing or invalid branch code: {}", item);
      return null;
    }

    BankCode bankCode;
    try {
      bankCode = BankCode.of(rawBankCode);
    } catch (IllegalArgumentException ex) {
      log.warn("Skipping record due to invalid bank code format: {}", item);
      return null;
    }

    BranchCode branchCode;
    try {
      branchCode = BranchCode.of(rawBranchCode);
    } catch (IllegalArgumentException ex) {
      log.warn("Skipping record due to invalid branch code format: {}", item);
      return null;
    }

    Branch.BranchId branchId = new Branch.BranchId();
    branchId.setBankCode(bankCode.asString());
    branchId.setBranchCode(branchCode.asString());

    Branch existing = branchRepository.findById(branchId).orElse(null);
    Branch branch = existing != null ? existing : new Branch();

    if (existing == null) {
      branch.setBankCodeValue(bankCode);
      branch.setBranchCodeValue(branchCode);
    }

    branch.setBranchName(item.getBranchName() != null ? item.getBranchName().trim() : "");
    branch.setBranchHalfKana(
        item.getBranchHalfKana() != null ? item.getBranchHalfKana().trim() : null);
    branch.setBranchFullKana(
        item.getBranchFullKana() != null ? item.getBranchFullKana().trim() : null);
    branch.setBranchHiragana(
        item.getBranchHiragana() != null ? item.getBranchHiragana().trim() : null);
    branch.setDatasetId(datasetId != null ? datasetId : "");

    return branch;
  }
}
