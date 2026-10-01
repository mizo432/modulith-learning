package undecided.generic.bankReg.internal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 金融機関マスタの取り込み完了を記録するイベントエンティティ。
 *
 * <p>データベースの「bank_reg.bank_master_imports」テーブルに対応します。取り込みが完了するたびに 1 行を追加（INSERT のみ）し、
 * 次回の実行時に最新データセットを取り込み済みかどうかの判定に使用します。
 */
@Getter
@Setter
@ToString
@Entity
@Table(
    schema = "bank_reg",
    name = "bank_master_imports",
    comment = "bank master import event table")
public class BankMasterImport {

  /** 取り込んだ Master Export のデータセットID。 */
  @Id
  @Column(name = "dataset_id", comment = "Master Export dataset id", nullable = false, length = 128)
  private String datasetId;

  /** データセットの公開日時。 */
  @Column(name = "published_at", comment = "Dataset published time")
  private OffsetDateTime publishedAt;

  /** 取り込みが完了した日時。 */
  @Column(name = "occurred_at", comment = "Import completed time", nullable = false)
  private OffsetDateTime occurredAt;

  /** ダウンロードした ZIP の SHA-256。 */
  @Column(
      name = "zip_sha256",
      comment = "SHA-256 of the downloaded ZIP",
      nullable = false,
      length = 64)
  private String zipSha256;

  /** 取り込んだ金融機関の件数。 */
  @Column(name = "bank_row_count", comment = "Number of imported bank rows", nullable = false)
  private int bankRowCount;
}
