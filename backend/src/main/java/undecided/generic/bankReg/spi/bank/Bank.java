package undecided.generic.bankReg.spi.bank;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 金融機関情報を管理するエンティティクラス。
 *
 * <p>このクラスは、データベースの「bank_reg.banks」テーブルに対応するものであり、 BankcodeJP の Master Export から取り込んだ金融機関の情報を保持します。
 */
@Getter
@Setter
@ToString
@Entity
@Table(schema = "bank_reg", name = "banks", comment = "bank table")
public class Bank {

  /**
   * 金融機関コード（4桁、先頭ゼロを含む文字列）。
   *
   * <p>データベース上では "bank_code" カラムに対応し、プライマリキーとして使用されます（例: "0001"）。
   */
  @Id
  @Size(min = BankCode.LENGTH, max = BankCode.LENGTH)
  @Column(
      name = "bank_code",
      comment = "Bank code (4 digits)",
      nullable = false,
      length = BankCode.LENGTH)
  private BankCode bankCode;
  /** 金融機関名（例: "みずほ銀行"）。 */
  @NotNull
  @Size(max = 128)
  @Column(name = "bank_name", comment = "Bank name", nullable = false, length = 128)
  private String bankName;
  /** 金融機関名の半角カナ表記。 */
  @Size(max = 256)
  @Column(name = "bank_half_kana", comment = "Bank name in half-width katakana", length = 256)
  private String bankHalfKana;
  /** 金融機関名の全角カナ表記。 */
  @Size(max = 256)
  @Column(name = "bank_full_kana", comment = "Bank name in full-width katakana", length = 256)
  private String bankFullKana;
  /** 金融機関名のひらがな表記。 */
  @Size(max = 256)
  @Column(name = "bank_full_hira", comment = "Bank name in hiragana", length = 256)
  private String bankFullHira;
  /** 業態コード。 */
  @Size(max = 8)
  @Column(name = "business_type_code", comment = "Business type code", length = 8)
  private String businessTypeCode;
  /** 業態名（例: "都市銀行"）。 */
  @Size(max = 64)
  @Column(name = "business_type", comment = "Business type", length = 64)
  private String businessType;
  /**
   * この行を最後に更新した Master Export のデータセットID。
   *
   * <p>最新のデータセットに含まれなくなった金融機関（廃止・合併など）を判定するために使用します。
   */
  @NotNull
  @Size(max = 128)
  @Column(
      name = "dataset_id",
      comment = "Master Export dataset id that last refreshed this row",
      nullable = false,
      length = 128)
  private String datasetId;

  /**
   * 金融機関コードを文字列として返します。
   *
   * @return 金融機関コードの文字列（例: "0001"）
   */
  public String getBankCode() {
    return bankCode.value();
  }

  /**
   * 金融機関コードの値オブジェクトを返します。
   *
   * @return 金融機関コードの値オブジェクト
   */
  public BankCode getBankCodeValue() {
    return bankCode;
  }
}
