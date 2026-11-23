package undecided.generic.documentMgmt.internal;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import undecided.generic.documentMgmt.spi.Document;
import undecided.supporting.snowflake.SnowflakeId;

/** 帳票の永続化リポジトリ。 */
interface DocumentRepository extends CrudRepository<Document, SnowflakeId> {

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
}
