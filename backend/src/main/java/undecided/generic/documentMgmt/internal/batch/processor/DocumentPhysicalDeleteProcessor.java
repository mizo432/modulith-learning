package undecided.generic.documentMgmt.internal.batch.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;
import undecided.generic.documentMgmt.spi.Document;
import undecided.generic.documentMgmt.spi.DocumentStorageService;

/**
 * 帳票の実体をストレージから物理削除するプロセッサー。
 *
 * <p>ストレージ削除が成功した帳票だけをライターへ渡す。削除に失敗した場合は例外を伝播させ、
 * チャンク全体をロールバックすることで DB レコードが削除されないようにする。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentPhysicalDeleteProcessor implements ItemProcessor<Document, Document> {

  private final DocumentStorageService documentStorageService;

  @Override
  public @Nullable Document process(@NonNull Document document) {
    throw new UnsupportedOperationException("not implemented");
  }
}
