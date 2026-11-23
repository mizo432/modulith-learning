package undecided.generic.documentMgmt.internal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import undecided.generic.documentMgmt.spi.Document;
import undecided.supporting.snowflake.SnowflakeId;

/** 帳票の永続化リポジトリ。バッチサブパッケージからも使うため public とする（bankReg の BankRepository と同じ扱い）。 */
public interface DocumentRepository extends CrudRepository<Document, SnowflakeId> {

  /**
   * ファイル名で帳票を検索する（論理削除済みを含む）。
   *
   * @param fileName ファイル名
   * @return ファイル名に一致する帳票の一覧
   */
  List<Document> findByFileName(String fileName);

  /**
   * バージョン系列 ID で帳票を検索する（全バージョン、論理削除済みを含む）。
   *
   * @param seriesId バージョン系列 ID
   * @return 系列に属する帳票の一覧
   */
  List<Document> findBySeriesId(SnowflakeId seriesId);

  /**
   * 論理削除されていない帳票を ID で検索する。
   *
   * @param id 帳票 ID
   * @return 論理削除されていない帳票
   */
  Optional<Document> findByIdAndDeletedFalse(SnowflakeId id);

  /**
   * 論理削除済みで、削除日時が指定日時より前の帳票を検索する（物理削除バッチの対象抽出用）。
   *
   * @param deletedAt 削除日時の上限（この日時より前に論理削除された帳票が対象）
   * @return 物理削除対象の帳票の一覧
   */
  List<Document> findByDeletedTrueAndDeletedAtBefore(LocalDateTime deletedAt);
}
