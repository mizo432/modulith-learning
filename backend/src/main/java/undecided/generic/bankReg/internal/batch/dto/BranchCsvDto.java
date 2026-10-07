package undecided.generic.bankReg.internal.batch.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jspecify.annotations.Nullable;

/** BankcodeJP Master Export の branches.csv データ用 DTO クラス。 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BranchCsvDto implements Serializable {

  /** 金融機関コード（4桁） */
  private @Nullable String bankCode;

  /** 支店コード（3桁） */
  private @Nullable String branchCode;

  /** 支店名 */
  private @Nullable String branchName;

  /** 支店名の半角カナ表記 */
  private @Nullable String branchHalfKana;

  /** 支店名の全角カナ表記 */
  private @Nullable String branchFullKana;

  /** 支店名のひらがな表記 */
  private @Nullable String branchHiragana;
}
