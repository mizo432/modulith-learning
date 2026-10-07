package undecided.generic.bankReg.internal.batch.writer;

import static undecided.supporting.precondition.ObjectPrecondition.checkNotNull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;
import undecided.generic.bankReg.internal.BranchRepository;
import undecided.generic.bankReg.spi.branch.Branch;

/** 処理された {@link Branch} エンティティを {@link BranchRepository} を介して一括永続化する {@link ItemWriter} 実装。 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BranchItemWriter implements ItemWriter<Branch> {

  private final BranchRepository branchRepository;

  @Override
  public void write(@NonNull Chunk<? extends Branch> chunk) {
    checkNotNull(chunk, () -> new IllegalArgumentException("chunk must not be null"));
    log.info("Persisting {} branch records", chunk.size());
    branchRepository.saveAll(chunk.getItems());
  }
}
