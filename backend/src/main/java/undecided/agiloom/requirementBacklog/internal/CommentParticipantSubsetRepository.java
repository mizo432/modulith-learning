package undecided.agiloom.requirementBacklog.internal;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import undecided.supporting.snowflake.SnowflakeId;

/** コメント参加者のサブセットリポジトリ（AGスキーマ）。 */
interface CommentParticipantSubsetRepository
    extends CrudRepository<CommentParticipantSubset, SnowflakeId> {

  /** 指定されたバックログアイテムの全参加者レコードを取得する。 */
  List<CommentParticipantSubset> findByBacklogItemId(SnowflakeId backlogItemId);

  /** 指定されたバックログアイテムと従業員の組み合わせが存在するかを確認する。 */
  boolean existsByBacklogItemIdAndEmployeeId(
      SnowflakeId backlogItemId, SnowflakeId employeeId);
}
