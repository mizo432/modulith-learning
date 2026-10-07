package undecided.generic.bankReg.spi.bank;

import jakarta.persistence.Embeddable;

/**
 * 金融機関コード（4桁、先頭ゼロを含む文字列）を表す値オブジェクト。
 *
 * <p>この値オブジェクトは、BankcodeJP の Master Export で定義される金融機関を一意に識別するコードを保持します。 例:
 * "0001"（みずほ銀行）、"0004"（三井住友銀行）など。
 */
@Embeddable
public record BankCode(String value) {

  /** 金融機関コードの桁数（4桁）。 */
  public static final int LENGTH = 4;

  /**
   * 文字列から金融機関コードを作成します。
   *
   * @param code 金融機関コード（4桁の文字列）
   * @return 金融機関コードの値オブジェクト
   * @throws IllegalArgumentException コードがnull、空、または4桁でない場合
   */
  public static BankCode of(String code) {
    if (code == null) {
      throw new IllegalArgumentException("bankCode must not be null");
    }
    String trimmed = code.trim();
    if (trimmed.isEmpty()) {
      throw new IllegalArgumentException("bankCode must not be empty");
    }
    if (trimmed.length() != LENGTH) {
      throw new IllegalArgumentException(
          "bankCode must be exactly " + LENGTH + " digits, but was: " + trimmed);
    }
    return new BankCode(trimmed);
  }

  /**
   * 金融機関コードを文字列として返します。
   *
   * @return 金融機関コードの文字列表現
   */
  public String asString() {
    return value;
  }
}
