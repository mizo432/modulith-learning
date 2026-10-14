package undecided.generic.{module}.internal.batch.writer;

import static undecided.shared.precondition.ObjectPrecondition.checkNotNull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;
import undecided.generic.{module}.internal.{Entity}Repository;
import undecided.generic.{module}.spi.{Entity};

/** 処理された {@link {Entity}} エンティティを {@link {Entity}Repository} を介して一括永続化する {@link ItemWriter} 実装。 */
@Slf4j
@Component
@RequiredArgsConstructor
public class {Entity}ItemWriter implements ItemWriter<{Entity}> {

  private final {Entity}Repository {entity}Repository;

  @Override
  public void write(@NonNull Chunk<? extends {Entity}> chunk) {
    checkNotNull(chunk, () -> new IllegalArgumentException("chunk must not be null"));
    log.info("Persisting {} {entity} records", chunk.size());
    {entity}Repository.saveAll(chunk.getItems());
  }
}
