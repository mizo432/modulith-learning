package undecided.generic.bankReg.spi.branch;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jspecify.annotations.NonNull;
import undecided.generic.bankReg.spi.bank.BankCode;

/**
 * 金融機関の支店情報を管理するエンティティクラス。
 *
 * <p>このクラスは、データベースの「bank_reg.branches」テーブルに対応し、 BankcodeJP の Master Export から取り込んだ支店の情報を保持します。
 */
@Getter
@Setter
@ToString
@Entity
@IdClass(Branch.BranchId.class)
@Table(schema = "bank_reg", name = "branches", comment = "bank branch table")
public class Branch {

  /**
   * 支店コード（3桁、先頭ゼロを含む文字列）。
   *
   * <p>データベース上では "branch_code" カラムに対応し、金融機関コードと組み合わせてプライマリキーとして使用されます（例: "001"）。
   */
  @Id
  @Size(min = 3, max = 3)
  @Column(name = "branch_code", comment = "Branch code (3 digits)", nullable = false, length = 3)
  private String branchCode;

  /**
   * 所属する金融機関のコード（4桁）。
   *
   * <p>データベース上では "bank_code" カラムに対応し、複合プライマリキーの一部です。
   */
  @Id
  @NotNull
  @Size(min = 4, max = 4)
  @Column(name = "bank_code", comment = "Bank code (4 digits)", nullable = false, length = 4)
  private String bankCode;
  /** 支店名（例: "東京営業部"）。 */
  @NotNull
  @Size(max = 128)
  @Column(name = "branch_name", comment = "Branch name", nullable = false, length = 128)
  private String branchName;
  /** 支店名の半角カナ表記。 */
  @Size(max = 256)
  @Column(name = "branch_half_kana", comment = "Branch name in half-width katakana", length = 256)
  private String branchHalfKana;
  /** 支店名の全角カナ表記。 */
  @Size(max = 256)
  @Column(name = "branch_full_kana", comment = "Branch name in full-width katakana", length = 256)
  private String branchFullKana;
  /** 支店名のひらがな表記。 */
  @Size(max = 256)
  @Column(name = "branch_hiragana", comment = "Branch name in hiragana", length = 256)
  private String branchHiragana;
  /**
   * この行を最後に更新した Master Export のデータセットID。
   *
   * <p>最新のデータセットに含まれなくなった支店（廃止・統合など）を判定するために使用します。
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
   * 所属する金融機関のコードを値オブジェクトとして取得します。
   *
   * @return 金融機関コードの値オブジェクト
   */
  public @NonNull BankCode bankCodeValue() {
    return BankCode.of(bankCode);
  }

  /**
   * 所属する金融機関のコードを値オブジェクトから設定します。
   *
   * @param code 金融機関コードの値オブジェクト
   */
  public void setBankCodeValue(@NonNull BankCode code) {
    this.bankCode = code.asString();
  }

  /** 支店と金融機関の複合キー。 */
  @Getter
  @Setter
  @EqualsAndHashCode
  public static class BranchId implements Serializable {
    @Column(name = "bank_code", nullable = false, length = 4)
    private String bankCode;

    @Column(name = "branch_code", nullable = false, length = 3)
    private String branchCode;
  }
}
