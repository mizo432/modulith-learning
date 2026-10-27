package undecided.generic.bankReg.spi.branch;

import jakarta.persistence.Embeddable;

/**
 * 金融機関店舗コード（3桁、先頭ゼロを含む文字列）を表す値オブジェクト。
 *
 * <p>この値オブジェクトは、BankcodeJP の Master Export で定義される支店を一意に識別するコードを保持します。 例: "001"（本店）、"002"（東京営業部）など。
 */
@Embeddable
public record BranchCode(String value) {

  /** 支店コードの桁数（3桁）。 */
  public static final int LENGTH = 3;

  /**
   * 文字列から支店コードを作成します。
   *
   * @param code 支店コード（3桁の文字列）
   * @return 支店コードの値オブジェクト
   * @throws IllegalArgumentException コードがnull、空、または3桁でない場合
   */
  public static BranchCode of(String code) {
    if (code == null) {
      throw new IllegalArgumentException("branchCode must not be null");
    }
    String trimmed = code.trim();
    if (trimmed.isEmpty()) {
      throw new IllegalArgumentException("branchCode must not be empty");
    }
    if (trimmed.length() != LENGTH) {
      throw new IllegalArgumentException(
          "branchCode must be exactly " + LENGTH + " digits, but was: " + trimmed);
    }
    return new BranchCode(trimmed);
  }

  /**
   * 支店コードを文字列として返します。
   *
   * @return 支店コードの文字列表現
   */
  public String asString() {
    return value;
  }
}
