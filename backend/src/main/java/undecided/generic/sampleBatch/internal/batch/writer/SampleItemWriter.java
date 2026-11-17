package undecided.generic.sampleBatch.internal.batch.writer;

import static com.github.mizo432.shared.common.precondition.ObjectPrecondition.checkNotNull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;
import undecided.generic.sampleBatch.internal.SampleItemRepository;
import undecided.generic.sampleBatch.spi.SampleItem;

/**
 * 処理された{@link SampleItem}エンティティを{@link SampleItemRepository}を介して一括永続化する{@link ItemWriter}実装。
 *
 * <p>バッチ基本構成サンプルとして、Writerの標準パターンを示します。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SampleItemWriter implements ItemWriter<SampleItem> {

  private final SampleItemRepository sampleItemRepository;

  @Override
  public void write(@NonNull Chunk<? extends SampleItem> chunk) {
    checkNotNull(chunk, () -> new IllegalArgumentException("chunk must not be null"));
    log.info("Persisting {} sample item records", chunk.size());
    sampleItemRepository.saveAll(chunk.getItems());
  }
}
