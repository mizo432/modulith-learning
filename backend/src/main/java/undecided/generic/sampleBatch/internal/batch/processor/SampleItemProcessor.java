package undecided.generic.sampleBatch.internal.batch.processor;

import static undecided.shared.precondition.ObjectPrecondition.checkNotNull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;
import undecided.generic.sampleBatch.internal.SampleItemRepository;
import undecided.generic.sampleBatch.internal.batch.dto.SampleItemCsvDto;
import undecided.generic.sampleBatch.spi.SampleItem;

/**
 * CSVから読み込んだ{@link SampleItemCsvDto}を検証し、既存データと照合して{@link SampleItem}エンティティを生成・更新する{@link ItemProcessor}実装。
 *
 * <p>バッチ基本構成サンプルとして、入力データのバリデーションと変換ロジックの標準パターンを示します。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SampleItemProcessor implements ItemProcessor<SampleItemCsvDto, SampleItem> {

  private final SampleItemRepository sampleItemRepository;

  @Override
  public @Nullable SampleItem process(@NonNull SampleItemCsvDto item) {
    checkNotNull(item, () -> new IllegalArgumentException("item must not be null"));

    String itemCode = item.getItemCode();
    if (itemCode == null || itemCode.isBlank()) {
      log.warn("Skipping record due to missing or invalid item code: {}", item);
      return null;
    }

    String trimmedItemCode = itemCode.trim();
    SampleItem existing = sampleItemRepository.findByItemCode(trimmedItemCode);
    SampleItem sampleItem = existing != null ? existing : new SampleItem();

    if (existing == null) {
      sampleItem.setItemCode(trimmedItemCode);
    }

    sampleItem.setItemName(item.getItemName() != null ? item.getItemName().trim() : "");

    String category = item.getCategory();
    sampleItem.setCategory(category != null ? category.trim() : "");

    String priceStr = item.getPrice();
    if (priceStr != null && !priceStr.isBlank()) {
      try {
        sampleItem.setPrice(Long.parseLong(priceStr.trim()));
      } catch (NumberFormatException e) {
        log.warn("Invalid price format: '{}', skipping record", priceStr);
        return null;
      }
    } else {
      sampleItem.setPrice(null);
    }

    return sampleItem;
  }
}
