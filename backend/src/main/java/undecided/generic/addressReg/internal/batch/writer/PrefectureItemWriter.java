package undecided.generic.addressReg.internal.batch.writer;

import static undecided.shared.precondition.ObjectPrecondition.checkNotNull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;
import undecided.generic.addressReg.internal.PrefectureRepository;
import undecided.generic.addressReg.spi.Prefecture;

/**
 * 処理された {@link Prefecture} エンティティを {@link PrefectureRepository} を介して一括永続化する {@link ItemWriter} 実装。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PrefectureItemWriter implements ItemWriter<Prefecture> {

  private final PrefectureRepository prefectureRepository;

  @Override
  public void write(@NonNull Chunk<? extends Prefecture> chunk) {
    checkNotNull(chunk, () -> new IllegalArgumentException("chunk must not be null"));
    log.info("Persisting {} prefecture records", chunk.size());
    prefectureRepository.saveAll(chunk.getItems());
  }
}
