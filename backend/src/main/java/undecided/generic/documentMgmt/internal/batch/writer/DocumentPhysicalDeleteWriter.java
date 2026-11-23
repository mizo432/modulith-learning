package undecided.generic.documentMgmt.internal.batch.writer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;
import undecided.generic.documentMgmt.internal.DocumentRepository;
import undecided.generic.documentMgmt.spi.Document;

/** ストレージから物理削除済みの帳票レコードを DB から削除するライター。 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentPhysicalDeleteWriter implements ItemWriter<Document> {

  private final DocumentRepository documentRepository;

  @Override
  public void write(@NonNull Chunk<? extends Document> chunk) {
    documentRepository.deleteAll(chunk.getItems());
    log.debug("帳票レコードをDBから物理削除しました: count={}", chunk.size());
  }
}
