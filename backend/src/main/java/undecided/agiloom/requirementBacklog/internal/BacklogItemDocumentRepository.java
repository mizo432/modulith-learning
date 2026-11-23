package undecided.agiloom.requirementBacklog.internal;

import undecided.agiloom.requirementBacklog.spi.BacklogItemDocument;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import undecided.supporting.snowflake.SnowflakeId;

/** バックログアイテムと帳票の関連付けのリポジトリ。 */
interface BacklogItemDocumentRepository extends CrudRepository<BacklogItemDocument, SnowflakeId> {

  /** バックログアイテムに紐づく関連付けの一覧を取得する。 */
  List<BacklogItemDocument> findByBacklogItemId(SnowflakeId backlogItemId);

  /** バックログアイテムとバージョン系列で関連付けを取得する。 */
  Optional<BacklogItemDocument> findByBacklogItemIdAndSeriesId(
      SnowflakeId backlogItemId, SnowflakeId seriesId);

  /** バックログアイテムと帳票IDで関連付けを取得する。 */
  Optional<BacklogItemDocument> findByBacklogItemIdAndDocumentId(
      SnowflakeId backlogItemId, SnowflakeId documentId);
}
