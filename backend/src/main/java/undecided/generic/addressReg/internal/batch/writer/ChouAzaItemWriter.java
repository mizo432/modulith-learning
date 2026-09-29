package undecided.generic.addressReg.internal.batch.writer;

import static undecided.supporting.precondition.ObjectPrecondition.checkNotNull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;
import undecided.generic.addressReg.internal.ChouAzaRepository;
import undecided.generic.addressReg.spi.ChouAza;

/** 処理された {@link ChouAza} エンティティを {@link ChouAzaRepository} を介して一括永続化する {@link ItemWriter} 実装。 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChouAzaItemWriter implements ItemWriter<ChouAza> {

  private final ChouAzaRepository chouAzaRepository;

  @Override
  public void write(@NonNull Chunk<? extends ChouAza> chunk) {
    checkNotNull(chunk, () -> new IllegalArgumentException("chunk must not be null"));
    log.info("Persisting {} chou aza records", chunk.size());
    chouAzaRepository.saveAll(chunk.getItems());
  }
}
