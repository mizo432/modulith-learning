package undecided.notification.internal;

import java.util.Set;
import org.springframework.data.repository.CrudRepository;
import undecided.supporting.snowflake.SnowflakeId;

/** コメント参加者のサブセットリポジトリ（NTスキーマ）。 */
interface CommentParticipantSubsetRepository extends CrudRepository<CommentParticipantSubset, SnowflakeId> {

  /** 指定されたバックログアイテムの全参加者レコードを取得する。 */
  Set<CommentParticipantSubset> findByBacklogItemId(SnowflakeId backlogItemId);
}
