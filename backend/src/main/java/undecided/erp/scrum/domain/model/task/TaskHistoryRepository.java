package undecided.erp.scrum.domain.model.task;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.erp.common.entity.SnowflakeId;

/**
 * タスク履歴に関連するデータアクセス操作を提供するリポジトリインターフェース。
 *
 * <p>このインターフェースはSpring Data JPAのJpaRepositoryを拡張し、 タスク履歴エンティティに対する標準的なCRUD操作と、カスタムクエリメソッドを提供します。
 */
@Repository
public interface TaskHistoryRepository extends JpaRepository<TaskHistory, SnowflakeId> {

  /**
   * 指定されたタスクに関連するすべての履歴を取得します。
   *
   * @param task 検索対象のタスク
   * @return 指定されたタスクに関連する履歴のリスト
   */
  List<TaskHistory> findByTask(Task task);

  /**
   * 指定されたタスクIDに関連するすべての履歴を取得します。
   *
   * @param taskId 検索対象のタスクID
   * @return 指定されたタスクIDに関連する履歴のリスト
   */
  List<TaskHistory> findByTaskTaskId(SnowflakeId taskId);

  /**
   * 指定された変更種類の履歴を検索します。
   *
   * @param changeType 検索対象の変更種類
   * @return 指定された変更種類の履歴のリスト
   */
  List<TaskHistory> findByChangeType(TaskHistory.ChangeType changeType);

  /**
   * 指定された変更者IDによる履歴を検索します。
   *
   * @param changedBy 検索対象の変更者ID
   * @return 指定された変更者IDによる履歴のリスト
   */
  List<TaskHistory> findByChangedBy(Long changedBy);

  /**
   * 指定されたタスクIDと変更種類の履歴を検索します。
   *
   * @param taskId 検索対象のタスクID
   * @param changeType 検索対象の変更種類
   * @return 指定されたタスクIDと変更種類の履歴のリスト
   */
  List<TaskHistory> findByTaskTaskIdAndChangeType(
      SnowflakeId taskId, TaskHistory.ChangeType changeType);

  /**
   * 指定されたタスクIDと変更者IDによる履歴を検索します。
   *
   * @param taskId 検索対象のタスクID
   * @param changedBy 検索対象の変更者ID
   * @return 指定されたタスクIDと変更者IDによる履歴のリスト
   */
  List<TaskHistory> findByTaskTaskIdAndChangedBy(SnowflakeId taskId, Long changedBy);

  /**
   * 指定された期間内の履歴を検索します。
   *
   * @param startDate 検索対象の開始日時
   * @param endDate 検索対象の終了日時
   * @return 指定された期間内の履歴のリスト
   */
  List<TaskHistory> findByChangedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

  /**
   * 指定されたタスクIDと期間内の履歴を検索します。
   *
   * @param taskId 検索対象のタスクID
   * @param startDate 検索対象の開始日時
   * @param endDate 検索対象の終了日時
   * @return 指定されたタスクIDと期間内の履歴のリスト
   */
  List<TaskHistory> findByTaskTaskIdAndChangedAtBetween(
      SnowflakeId taskId, LocalDateTime startDate, LocalDateTime endDate);

  /**
   * 指定されたタスクに関連するすべての履歴を変更日時の降順で取得します。
   *
   * @param task 検索対象のタスク
   * @return 指定されたタスクに関連する履歴のリスト（変更日時の降順）
   */
  List<TaskHistory> findByTaskOrderByChangedAtDesc(Task task);

  /**
   * 指定されたタスクIDに関連するすべての履歴を変更日時の降順で取得します。
   *
   * @param taskId 検索対象のタスクID
   * @return 指定されたタスクIDに関連する履歴のリスト（変更日時の降順）
   */
  List<TaskHistory> findByTaskTaskIdOrderByChangedAtDesc(SnowflakeId taskId);
}
