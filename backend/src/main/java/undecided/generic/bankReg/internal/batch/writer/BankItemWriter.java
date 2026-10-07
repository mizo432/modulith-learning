package undecided.generic.bankReg.internal.batch.writer;

import static undecided.supporting.precondition.ObjectPrecondition.checkNotNull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;
import undecided.generic.bankReg.internal.BankRepository;
import undecided.generic.bankReg.spi.bank.Bank;

/**
 * 処理された {@link Bank} エンティティを {@link BankRepository} を介して一括永続化する {@link ItemWriter} 実装。
 *
 * <p>金融機関コード（PK）が既存と一致する場合は更新（UPDATE）、不一致の場合は新規追加（INSERT）されます。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BankItemWriter implements ItemWriter<Bank> {

  private final BankRepository bankRepository;

  @Override
  public void write(@NonNull Chunk<? extends Bank> chunk) {
    checkNotNull(chunk, () -> new IllegalArgumentException("chunk must not be null"));
    log.info("Persisting {} bank records", chunk.size());
    bankRepository.saveAll(chunk.getItems());
  }
}
