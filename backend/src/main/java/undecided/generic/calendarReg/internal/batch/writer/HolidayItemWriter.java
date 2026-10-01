package undecided.generic.calendarReg.internal.batch.writer;

import static undecided.supporting.precondition.ObjectPrecondition.checkNotNull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;
import undecided.generic.calendarReg.internal.HolidayRepository;
import undecided.generic.calendarReg.spi.Holiday;

/** 処理された {@link Holiday} エンティティを {@link HolidayRepository} を介して一括永続化する {@link ItemWriter} 実装。 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HolidayItemWriter implements ItemWriter<Holiday> {

  private final HolidayRepository holidayRepository;

  @Override
  public void write(@NonNull Chunk<? extends Holiday> chunk) {
    checkNotNull(chunk, () -> new IllegalArgumentException("chunk must not be null"));
    log.info("Persisting {} holiday records", chunk.size());
    holidayRepository.saveAll(chunk.getItems());
  }
}
