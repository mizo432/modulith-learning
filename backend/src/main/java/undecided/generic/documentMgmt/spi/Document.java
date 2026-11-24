package undecided.generic.documentMgmt.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 帳票エンティティ。
 *
 * <p>ファイルのメタデータ（ファイル名、Content-Type、サイズ、ファイル種別、バージョン、MinIO のキー）と
 * 論理削除フラグを管理する。バックログとの紐付けは、利用側（requirementBacklog）が関連付けテーブルで持つ。
 *
 * <p>同一ファイル名の再アップロードは新バージョンとして保存する。
 */
@Entity
@Table(schema = "undecided_ef", name = "documents")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Document {

  /** ファイル名の最大長（項目制約の単一情報源 ADR-002）。 */
  public static final int FILE_NAME_MAX_LENGTH = 255;

  /** Content-Type の最大長（項目制約の単一情報源 ADR-002）。 */
  public static final int CONTENT_TYPE_MAX_LENGTH = 100;

  /** S3 キーの最大長（項目制約の単一情報源 ADR-002）。 */
  public static final int S3_KEY_MAX_LENGTH = 1024;

  /** アップローダー名の最大長（項目制約の単一情報源 ADR-002）。 */
  public static final int UPLOADER_NAME_MAX_LENGTH = 100;

  /** 帳票の一意な識別子（Snowflake ID）。 */
  @Id
  @NonNull
  private SnowflakeId id;

  /** ファイル名（元のファイル名）。 */
  @Column(name = "file_name", nullable = false, length = FILE_NAME_MAX_LENGTH)
  @NonNull
  private String fileName;

  /** Content-Type（MIME タイプ）。 */
  @Column(name = "content_type", nullable = false, length = CONTENT_TYPE_MAX_LENGTH)
  @NonNull
  private String contentType;

  /** ファイルサイズ（バイト数）。 */
  @Column(name = "file_size", nullable = false)
  private long fileSize;

  /** ファイル種別区分値（IMAGE/PDF/TEXT/DOCUMENT）。 */
  @Enumerated(EnumType.STRING)
  @Column(name = "file_type", nullable = false, length = 20)
  @NonNull
  private DocumentFileType fileType;

  /** バージョン番号（1 始まり）。 */
  @Column(name = "version", nullable = false)
  private int version;

  /** MinIO 上のオブジェクトキー。{@code {documentId}/v{version}/{fileName}} 形式。 */
  @Column(name = "s3_key", nullable = false, length = S3_KEY_MAX_LENGTH)
  @NonNull
  private String s3Key;

  /**
   * バージョン系列 ID。
   *
   * <p>同じ系列内の帳票は同一ファイルのバージョンとして扱う。利用側（requirementBacklog）が
   * 系列を採番してアップロード時に指定する。段階2までの帳票（系列なし）は null。
   */
  @Column(name = "series_id")
  @Nullable
  private SnowflakeId seriesId;

  /** アップローダーの従業員 ID。 */
  @Column(name = "uploader_employee_id")
  @Nullable
  private SnowflakeId uploaderEmployeeId;

  /** アップローダー名。 */
  @Column(name = "uploader_name", length = UPLOADER_NAME_MAX_LENGTH)
  @Nullable
  private String uploaderName;

  /** 論理削除フラグ。true のとき論理削除済み。 */
  @Column(name = "deleted", nullable = false)
  private boolean deleted;

  /** 論理削除日時。論理削除されていない場合は null。 */
  @Column(name = "deleted_at")
  @Nullable
  private LocalDateTime deletedAt;

  /** 楽観ロック用バージョン列。 */
  @Version
  @Column(name = "version_col")
  private Long versionCol;

  /** 作成日時。 */
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  /** 更新日時。 */
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  /**
   * 論理削除を実行する。
   *
   * <p>already deleted の場合は {@link IllegalStateException} をスローする。
   *
   * @param now 論理削除日時
   * @throws IllegalStateException 既に論理削除済みの場合
   */
  public void softDelete(@NonNull LocalDateTime now) {
    if (this.deleted) {
      throw new IllegalStateException("既に論理削除済みの帳票です: " + this.id);
    }
    this.deleted = true;
    this.deletedAt = now;
    this.updatedAt = now;
  }
}
