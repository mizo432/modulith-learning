package undecided.generic.{module}.internal.batch.processor;

import static undecided.shared.precondition.ObjectPrecondition.checkNotNull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;
import undecided.generic.{module}.internal.{Entity}Repository;
import undecided.generic.{module}.internal.batch.dto.{Entity}CsvDto;
import undecided.generic.{module}.spi.{Entity};

/**
 * CSV から読み込んだ {@link {Entity}CsvDto} を検証し、既存データと照合して {@link {Entity}} エンティティを生成・更新する {@link
 * ItemProcessor} 実装。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class {Entity}ItemProcessor implements ItemProcessor<{Entity}CsvDto, {Entity}> {

  private final {Entity}Repository {entity}Repository;

  @Override
  public @Nullable {Entity} process(@NonNull {Entity}CsvDto item) {
    checkNotNull(item, () -> new IllegalArgumentException("item must not be null"));

    // TODO: 必須項目の検証
    // 不正なデータはログに記録して null を返す（スキップ）
    String requiredField = item.getRequiredField();
    if (requiredField == null || requiredField.isBlank()) {
      log.warn("Skipping record due to missing or invalid requiredField: {}", item);
      return null;
    }

    // TODO: 既存データとの整合性確認
    {Entity} existing = {entity}Repository.findByUniqueKey(requiredField.trim());
    {Entity} {entity} = existing != null ? existing : new {Entity}();

    // TODO: DTO からエンティティへの変換
    if (existing == null) {
      // 新規登録時の処理
      {entity}.setId(item.getId());
    }
    // 共通のフィールド設定
    {entity}.setRequiredField(requiredField.trim());

    return {entity};
  }

  /**
   * 空文字列または空白のみの文字列を null に変換します。
   */
  private @Nullable String toNullIfEmpty(@Nullable String value) {
    if (value == null) {
      return null;
    }
    String trimmed = value.trim();
    return trimmed.isEmpty() ? null : trimmed;
  }
}
