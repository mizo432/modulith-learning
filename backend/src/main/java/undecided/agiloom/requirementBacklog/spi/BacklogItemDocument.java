package undecided.agiloom.requirementBacklog.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * バックログアイテムと帳票の関連付け。
 *
 * <p>帳票の実体とメタデータは帳票管理モジュール（documentMgmt）が持ち、
 * 自領域では紐付けだけを保持します（他領域のテーブルは直接参照しません）。
 * documentId は系列の最新バージョンの帳票を指し、再アップロード時に更新されます。
 */
@Entity
@Table(schema = "undecided_ag", name = "backlog_item_documents")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BacklogItemDocument {

  /** 関連付けの一意な識別子。 */
  @Id protected SnowflakeId id;

  /** バックログアイテムID。 */
  @Column(name = "backlog_item_id", nullable = false)
  protected SnowflakeId backlogItemId;

  /** 帳票ID（系列の最新バージョン）。 */
  @Setter
  @Column(name = "document_id", nullable = false)
  protected SnowflakeId documentId;

  /** バージョン系列ID。 */
  @Column(name = "series_id", nullable = false)
  protected SnowflakeId seriesId;

  /** 紐付け作成日時。 */
  @Column(name = "created_at", nullable = false, updatable = false)
  protected LocalDateTime createdAt;

  /** 楽観ロック用バージョン列。 */
  @Version
  @Column(name = "version_col")
  protected Long versionCol;
}
