package undecided.generic.documentMgmt.internal.batch.reader;

import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemReader;
import undecided.generic.documentMgmt.internal.DocumentRepository;
import undecided.generic.documentMgmt.spi.Document;

/**
 * 物理削除対象の帳票を読み込むリーダー。
 *
 * <p>論理削除から1年が経過した帳票を対象とし、ステップ実行ごとに新しいインスタンスが使われる（{@code @StepScope}）。
 */
public class DocumentPhysicalDeleteReader implements ItemReader<Document> {

  private final DocumentRepository documentRepository;

  public DocumentPhysicalDeleteReader(DocumentRepository documentRepository) {
    this.documentRepository = documentRepository;
  }

  @Override
  public @Nullable Document read() {
    throw new UnsupportedOperationException("not implemented");
  }
}
