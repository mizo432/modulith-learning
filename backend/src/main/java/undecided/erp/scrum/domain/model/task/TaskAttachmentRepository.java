package undecided.erp.scrum.domain.model.task;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.erp.common.entity.SnowflakeId;

/**
 * タスク添付ファイルに関連するデータアクセス操作を提供するリポジトリインターフェース。
 *
 * <p>このインターフェースはSpring Data JPAのJpaRepositoryを拡張し、
 * タスク添付ファイルエンティティに対する標準的なCRUD操作と、カスタムクエリメソッドを提供します。
 */
@Repository
public interface TaskAttachmentRepository extends JpaRepository<TaskAttachment, SnowflakeId> {

  /**
   * 指定されたタスクに関連するすべての添付ファイルを取得します。
   *
   * @param task 検索対象のタスク
   * @return 指定されたタスクに関連する添付ファイルのリスト
   */
  List<TaskAttachment> findByTask(Task task);

  /**
   * 指定されたタスクIDに関連するすべての添付ファイルを取得します。
   *
   * @param taskId 検索対象のタスクID
   * @return 指定されたタスクIDに関連する添付ファイルのリスト
   */
  List<TaskAttachment> findByTaskTaskId(SnowflakeId taskId);

  /**
   * 指定されたファイル名を持つ添付ファイルを検索します。
   *
   * @param fileName 検索対象のファイル名
   * @return 指定されたファイル名を持つ添付ファイルのリスト
   */
  List<TaskAttachment> findByFileName(String fileName);

  /**
   * 指定されたファイル名を含む添付ファイルを検索します。
   *
   * @param fileName 検索対象のファイル名の一部
   * @return 指定されたファイル名を含む添付ファイルのリスト
   */
  List<TaskAttachment> findByFileNameContaining(String fileName);

  /**
   * 指定されたファイルタイプを持つ添付ファイルを検索します。
   *
   * @param fileType 検索対象のファイルタイプ
   * @return 指定されたファイルタイプを持つ添付ファイルのリスト
   */
  List<TaskAttachment> findByFileType(String fileType);

  /**
   * 指定されたアップロードユーザーIDによる添付ファイルを検索します。
   *
   * @param uploadedBy 検索対象のアップロードユーザーID
   * @return 指定されたアップロードユーザーIDによる添付ファイルのリスト
   */
  List<TaskAttachment> findByUploadedBy(Long uploadedBy);

  /**
   * 指定されたタスクIDとアップロードユーザーIDによる添付ファイルを検索します。
   *
   * @param taskId 検索対象のタスクID
   * @param uploadedBy 検索対象のアップロードユーザーID
   * @return 指定されたタスクIDとアップロードユーザーIDによる添付ファイルのリスト
   */
  List<TaskAttachment> findByTaskTaskIdAndUploadedBy(SnowflakeId taskId, Long uploadedBy);

  /**
   * 指定された期間内にアップロードされた添付ファイルを検索します。
   *
   * @param startDate 検索対象の開始日時
   * @param endDate 検索対象の終了日時
   * @return 指定された期間内にアップロードされた添付ファイルのリスト
   */
  List<TaskAttachment> findByUploadedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

  /**
   * 指定されたタスクIDと期間内にアップロードされた添付ファイルを検索します。
   *
   * @param taskId 検索対象のタスクID
   * @param startDate 検索対象の開始日時
   * @param endDate 検索対象の終了日時
   * @return 指定されたタスクIDと期間内にアップロードされた添付ファイルのリスト
   */
  List<TaskAttachment> findByTaskTaskIdAndUploadedAtBetween(
      SnowflakeId taskId, LocalDateTime startDate, LocalDateTime endDate);

  /**
   * 指定されたタスクに関連するすべての添付ファイルをアップロード日時の降順で取得します。
   *
   * @param task 検索対象のタスク
   * @return 指定されたタスクに関連する添付ファイルのリスト（アップロード日時の降順）
   */
  List<TaskAttachment> findByTaskOrderByUploadedAtDesc(Task task);

  /**
   * 指定されたタスクIDに関連するすべての添付ファイルをアップロード日時の降順で取得します。
   *
   * @param taskId 検索対象のタスクID
   * @return 指定されたタスクIDに関連する添付ファイルのリスト（アップロード日時の降順）
   */
  List<TaskAttachment> findByTaskTaskIdOrderByUploadedAtDesc(SnowflakeId taskId);
}
