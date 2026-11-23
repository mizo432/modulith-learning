package undecided.notification.internal;

import java.util.Collection;
import java.util.Set;
import org.springframework.data.repository.CrudRepository;
import undecided.supporting.snowflake.SnowflakeId;

/** コメント参加者のサブセットリポジトリ（NTスキーマ）。 */
interface NotificationCommentParticipantSubsetRepository extends CrudRepository<NotificationCommentParticipantSubset, SnowflakeId> {

  /** 指定されたバックログアイテムの全参加者レコードを取得する。 */
  Set<NotificationCommentParticipantSubset> findByBacklogItemId(SnowflakeId backlogItemId);

  /** 指定された従業員の参加者レコードを取得する。 */
  Set<NotificationCommentParticipantSubset> findByEmployeeIdIn(Collection<SnowflakeId> employeeIds);

  /** 指定されたバックログアイテムと従業員の組み合わせが既に登録されているかを確認する。 */
  boolean existsByBacklogItemIdAndEmployeeId(SnowflakeId backlogItemId, SnowflakeId employeeId);
}
