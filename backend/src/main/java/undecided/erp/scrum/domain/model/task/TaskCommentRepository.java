package undecided.erp.scrum.domain.model.task;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.erp.common.entity.SnowflakeId;

/**
 * タスクコメントに関連するデータアクセス操作を提供するリポジトリインターフェース。
 *
 * <p>このインターフェースはSpring Data JPAのJpaRepositoryを拡張し、 タスクコメントエンティティに対する標準的なCRUD操作と、カスタムクエリメソッドを提供します。
 */
@Repository
public interface TaskCommentRepository extends JpaRepository<TaskComment, SnowflakeId> {

  /**
   * 指定されたタスクに関連するすべてのコメントを取得します。
   *
   * @param task 検索対象のタスク
   * @return 指定されたタスクに関連するコメントのリスト
   */
  List<TaskComment> findByTask(Task task);

  /**
   * 指定されたタスクIDに関連するすべてのコメントを取得します。
   *
   * @param taskId 検索対象のタスクID
   * @return 指定されたタスクIDに関連するコメントのリスト
   */
  List<TaskComment> findByTaskTaskId(SnowflakeId taskId);

  /**
   * 指定された作成者IDによるコメントを検索します。
   *
   * @param authorId 検索対象の作成者ID
   * @return 指定された作成者IDによるコメントのリスト
   */
  List<TaskComment> findByAuthorId(Long authorId);

  /**
   * 指定されたタスクIDと作成者IDによるコメントを検索します。
   *
   * @param taskId 検索対象のタスクID
   * @param authorId 検索対象の作成者ID
   * @return 指定されたタスクIDと作成者IDによるコメントのリスト
   */
  List<TaskComment> findByTaskTaskIdAndAuthorId(SnowflakeId taskId, Long authorId);

  /**
   * 指定されたタスクに関連するすべてのコメントを作成日時の降順で取得します。
   *
   * @param task 検索対象のタスク
   * @return 指定されたタスクに関連するコメントのリスト（作成日時の降順）
   */
  List<TaskComment> findByTaskOrderByCreatedAtDesc(Task task);

  /**
   * 指定されたタスクIDに関連するすべてのコメントを作成日時の降順で取得します。
   *
   * @param taskId 検索対象のタスクID
   * @return 指定されたタスクIDに関連するコメントのリスト（作成日時の降順）
   */
  List<TaskComment> findByTaskTaskIdOrderByCreatedAtDesc(SnowflakeId taskId);
}
