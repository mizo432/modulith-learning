package undecided.erp.scrum.domain.model.task;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.erp.common.entity.PptEntity;
import undecided.erp.common.entity.SnowflakeId;

/**
 * TaskAttachmentクラスは、エンティティとしてタスクに添付されるファイルを表現します。
 *
 * <p>このクラスは以下の特性を持ちます:
 *
 * <ul>
 *   <li>`attachmentId`: 添付ファイル固有の識別子を表す。SnowflakeIdを使用。
 *   <li>`task`: この添付ファイルが属するタスク。
 *   <li>`fileName`: ファイル名。
 *   <li>`fileType`: ファイルの種類（MIMEタイプ）。
 *   <li>`fileSize`: ファイルサイズ（バイト単位）。
 *   <li>`filePath`: ファイルの保存パス。
 *   <li>`uploadedBy`: ファイルをアップロードしたユーザーのID。
 *   <li>`uploadedAt`: ファイルがアップロードされた日時。
 *   <li>`description`: ファイルの説明。
 * </ul>
 */
@AllArgsConstructor
@Entity
@Table(name = "task_attachments")
@NoArgsConstructor
public class TaskAttachment extends PptEntity<TaskAttachment> implements Serializable {

  /**
   * ユニークな添付ファイル識別子を表す変数。
   *
   * <p>アプリケーションにおける各添付ファイルを一意に識別するために使用されます。 データベース上の "attachment_id" カラムに対応し、null 値は許可されていません。
   */
  @Id
  @Column(name = "attachment_id", columnDefinition = "BIGINT", nullable = false)
  @Convert(converter = SnowflakeId.SnowflakeIdConverter.class)
  private SnowflakeId attachmentId;

  /**
   * この添付ファイルが属するタスクを表す変数。
   *
   * <p>タスクと添付ファイルの関連付けを表します。 データベース上の "task_id" カラムに対応します。
   */
  @ManyToOne
  @JoinColumn(name = "task_id", nullable = false)
  @Getter
  private Task task;

  /**
   * ファイル名を表す変数。
   *
   * <p>添付ファイルの名前を示します。 データベース上の "file_name" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "file_name", nullable = false)
  private String fileName;

  /**
   * ファイルの種類を表す変数。
   *
   * <p>添付ファイルのMIMEタイプを示します。 データベース上の "file_type" カラムに対応します。
   */
  @Getter
  @Column(name = "file_type", length = 100)
  private String fileType;

  /**
   * ファイルサイズを表す変数。
   *
   * <p>添付ファイルのサイズをバイト単位で示します。 データベース上の "file_size" カラムに対応します。
   */
  @Getter
  @Column(name = "file_size")
  private Long fileSize;

  /**
   * ファイルの保存パスを表す変数。
   *
   * <p>添付ファイルが保存されている場所のパスを示します。 データベース上の "file_path" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "file_path", nullable = false, length = 1000)
  private String filePath;

  /**
   * ファイルをアップロードしたユーザーのIDを表す変数。
   *
   * <p>添付ファイルをアップロードしたユーザーのIDを示します。 データベース上の "uploaded_by" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "uploaded_by", nullable = false)
  private Long uploadedBy;

  /**
   * ファイルがアップロードされた日時を表す変数。
   *
   * <p>添付ファイルがアップロードされた日時を示します。 データベース上の "uploaded_at" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "uploaded_at", nullable = false)
  private LocalDateTime uploadedAt;

  /**
   * ファイルの説明を表す変数。
   *
   * <p>添付ファイルの説明を示します。 データベース上の "description" カラムに対応します。
   */
  @Getter
  @Column(name = "description", length = 1000)
  private String description;

  /**
   * 添付ファイルIDを設定します。
   *
   * @param attachmentId 設定する添付ファイルID
   */
  public void setAttachmentId(SnowflakeId attachmentId) {
    this.attachmentId = attachmentId;
  }

  /**
   * この添付ファイルが属するタスクを設定します。
   *
   * @param task 設定するタスク
   */
  public void setTask(Task task) {
    this.task = task;
  }

  /**
   * ファイル名を設定します。
   *
   * @param fileName 設定するファイル名
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   * ファイルの種類を設定します。
   *
   * @param fileType 設定するファイルの種類
   */
  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  /**
   * ファイルサイズを設定します。
   *
   * @param fileSize 設定するファイルサイズ
   */
  public void setFileSize(Long fileSize) {
    this.fileSize = fileSize;
  }

  /**
   * ファイルの保存パスを設定します。
   *
   * @param filePath 設定するファイルの保存パス
   */
  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  /**
   * ファイルをアップロードしたユーザーのIDを設定します。
   *
   * @param uploadedBy 設定するアップロードユーザーID
   */
  public void setUploadedBy(Long uploadedBy) {
    this.uploadedBy = uploadedBy;
  }

  /**
   * ファイルがアップロードされた日時を設定します。
   *
   * @param uploadedAt 設定するアップロード日時
   */
  public void setUploadedAt(LocalDateTime uploadedAt) {
    this.uploadedAt = uploadedAt;
  }

  /**
   * ファイルの説明を設定します。
   *
   * @param description 設定するファイルの説明
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * このメソッドはTaskAttachmentクラスの文字列表現を生成します。
   *
   * <p>各フィールドの値を連結して構成された文字列を返します。
   *
   * @return TaskAttachmentオブジェクトのフィールド情報を含む文字列表現
   */
  @Override
  public String toString() {
    return "TaskAttachment{"
        + "attachmentId="
        + attachmentId
        + ", task="
        + (task != null ? task.toString() : "null")
        + ", fileName='"
        + fileName
        + '\''
        + ", fileType='"
        + fileType
        + '\''
        + ", fileSize="
        + fileSize
        + ", filePath='"
        + filePath
        + '\''
        + ", uploadedBy="
        + uploadedBy
        + ", uploadedAt="
        + uploadedAt
        + ", description='"
        + description
        + '\''
        + '}';
  }
}
