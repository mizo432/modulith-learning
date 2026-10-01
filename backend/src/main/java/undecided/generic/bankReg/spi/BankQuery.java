package undecided.generic.bankReg.spi;

import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;

/** 金融機関情報を検索するためのクエリインターフェース。 */
public interface BankQuery {

  /**
   * 金融機関コードを指定して金融機関を検索します。
   *
   * @param bankCode 金融機関コード（4桁）
   * @return 該当する金融機関の Optional
   */
  @NonNull Optional<Bank> findByBankCode(@NonNull String bankCode);

  /**
   * すべての金融機関を金融機関コードの昇順で取得します。
   *
   * @return 金融機関のリスト
   */
  @NonNull List<Bank> findAll();
}
