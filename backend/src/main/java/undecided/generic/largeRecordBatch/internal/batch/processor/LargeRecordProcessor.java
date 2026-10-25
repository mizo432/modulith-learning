package undecided.generic.largeRecordBatch.internal.batch.processor;

import static undecided.shared.precondition.ObjectPrecondition.checkNotNull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;
import undecided.generic.largeRecordBatch.internal.batch.dto.LargeRecordCsvDto;
import undecided.generic.largeRecordBatch.spi.LargeRecord;

/**
 * CSVから読み込んだ{@link LargeRecordCsvDto}を検証し、{@link LargeRecord}エンティティに変換する
 * {@link ItemProcessor}実装。
 *
 * <p>大量データ処理パターンとして、以下の機能をデモンストレーションします：
 * <ul>
 *   <li>入力データのバリデーション（必須フィールドチェック）</li>
 *   <li>数値変換のフォールトトレランス（パース失敗時のデフォルト値）</li>
 *   <li>不正データのログ出力とスキップ処理</li>
 *   <li>ステータス値の正規化</li>
 * </ul>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LargeRecordProcessor implements ItemProcessor<LargeRecordCsvDto, LargeRecord> {

  @Override
  public @Nullable LargeRecord process(@NonNull LargeRecordCsvDto item) {
    checkNotNull(item, () -> new IllegalArgumentException("item must not be null"));

    // 必須フィールド: recordCode の検証
    String recordCode = item.getRecordCode();
    if (recordCode == null || recordCode.isBlank()) {
      log.warn("Skipping record due to missing or invalid record code: {}", item);
      return null;
    }

    // 必須フィールド: recordName の検証
    String recordName = item.getRecordName();
    if (recordName == null || recordName.isBlank()) {
      log.warn("Skipping record due to missing or invalid record name: {}", item);
      return null;
    }

    LargeRecord largeRecord = new LargeRecord();
    largeRecord.setRecordCode(recordCode.trim());
    largeRecord.setRecordName(recordName.trim());

    // カテゴリの設定（null許容）
    String category = item.getCategory();
    largeRecord.setCategory(category != null ? category.trim() : "");

    // サブカテゴリの設定（null許容）
    String subCategory = item.getSubCategory();
    largeRecord.setSubCategory(subCategory != null ? subCategory.trim() : "");

    // 金額のフォールトトレラントなパース
    String amountStr = item.getAmount();
    if (amountStr != null && !amountStr.isBlank()) {
      try {
        largeRecord.setAmount(Long.parseLong(amountStr.trim()));
      } catch (NumberFormatException e) {
        log.warn("Invalid amount format: '{}', setting to null for record: {}", amountStr, recordCode);
        largeRecord.setAmount(null);
      }
    }

    // 数量のフォールトトレラントなパース
    String quantityStr = item.getQuantity();
    if (quantityStr != null && !quantityStr.isBlank()) {
      try {
        largeRecord.setQuantity(Integer.parseInt(quantityStr.trim()));
      } catch (NumberFormatException e) {
        log.warn("Invalid quantity format: '{}', setting to null for record: {}", quantityStr, recordCode);
        largeRecord.setQuantity(null);
      }
    }

    // ステータスの正規化
    String status = item.getStatus();
    if (status != null && !status.isBlank()) {
      String normalizedStatus = status.trim().toUpperCase();
      if ("ACTIVE".equals(normalizedStatus) || "INACTIVE".equals(normalizedStatus)) {
        largeRecord.setStatus(normalizedStatus);
      } else {
        log.warn("Invalid status: '{}', defaulting to ACTIVE for record: {}", status, recordCode);
        largeRecord.setStatus("ACTIVE");
      }
    } else {
      largeRecord.setStatus("ACTIVE");
    }

    return largeRecord;
  }
}
