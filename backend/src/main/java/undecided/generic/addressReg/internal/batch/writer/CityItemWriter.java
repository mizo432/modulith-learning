package undecided.generic.addressReg.internal.batch.writer;

import static undecided.shared.precondition.ObjectPrecondition.checkNotNull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;
import undecided.generic.addressReg.internal.CityRepository;
import undecided.generic.addressReg.spi.City;

/** 処理された {@link City} エンティティを {@link CityRepository} を介して一括永続化する {@link ItemWriter} 実装。 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CityItemWriter implements ItemWriter<City> {

  private final CityRepository cityRepository;

  @Override
  public void write(@NonNull Chunk<? extends City> chunk) {
    checkNotNull(chunk, () -> new IllegalArgumentException("chunk must not be null"));
    log.info("Persisting {} city records", chunk.size());
    cityRepository.saveAll(chunk.getItems());
  }
}
