package undecided.generic.bankReg.internal.batch.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jspecify.annotations.Nullable;

/**
 * BankcodeJP Master Export の banks.csv 1 行分の DTO。
 *
 * <p>Super CSV によるマッピング用であり、カラム名は CSV のヘッダーに合わせる。
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BankCsvDto implements Serializable {

  /** 金融機関コード（4桁） */
  private @Nullable String bankCode;

  /** 金融機関名 */
  private @Nullable String bankName;

  /** 金融機関名の半角カナ表記 */
  private @Nullable String bankHalfKana;

  /** 金融機関名の全角カナ表記 */
  private @Nullable String bankFullKana;

  /** 金融機関名のひらがな表記 */
  private @Nullable String bankFullHira;

  /** 業態コード */
  private @Nullable String businessTypeCode;

  /** 業態名 */
  private @Nullable String businessType;
}
