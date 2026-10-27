package undecided.generic.largeRecordBatch.internal.batch.writer;

import static undecided.shared.precondition.ObjectPrecondition.checkNotNull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;
import undecided.generic.largeRecordBatch.internal.LargeRecordRepository;
import undecided.generic.largeRecordBatch.spi.LargeRecord;

/**
 * 処理された{@link LargeRecord}エンティティを{@link LargeRecordRepository}を介して一括永続化する
 * {@link ItemWriter}実装。
 *
 * <p>大量データ処理パターンとして、以下の機能をデモンストレーションします：
 * <ul>
 *   <li>チャンク単位の一括保存（saveAll）によるパフォーマンス最適化</li>
 *   <li>処理件数のログ出力によるパフォーマンス監視</li>
 * </ul>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LargeRecordWriter implements ItemWriter<LargeRecord> {

  private final LargeRecordRepository largeRecordRepository;

  @Override
  public void write(@NonNull Chunk<? extends LargeRecord> chunk) {
    checkNotNull(chunk, () -> new IllegalArgumentException("chunk must not be null"));
    log.info("Persisting {} large record items in this chunk", chunk.size());
    largeRecordRepository.saveAll(chunk.getItems());
  }
}
