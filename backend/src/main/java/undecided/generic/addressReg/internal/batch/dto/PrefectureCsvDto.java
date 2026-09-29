package undecided.generic.addressReg.internal.batch.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jspecify.annotations.Nullable;

/** デジタル庁アドレス・ベース・レジストリ 都道府県マスター CSV データ用 DTO クラス。 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PrefectureCsvDto implements Serializable {

  /** 全国地方公共団体コード (例: "010006") */
  private @Nullable String lgCode;

  /** 都道府県名 (例: "北海道") */
  private @Nullable String prefName;

  /** 都道府県名_カナ (例: "ホッカイドウ") */
  private @Nullable String prefKana;

  /** 都道府県名_英字 (例: "Hokkaido") */
  private @Nullable String prefRoma;

  /** 効力発生日 (例: "1947-04-17") */
  private @Nullable String effectiveDate;

  /** 廃止日 (例: "" または "9999-12-31") */
  private @Nullable String abolitionDate;

  /** 備考 */
  private @Nullable String remarks;

  /**
   * 全国地方公共団体コードの先頭2桁から都道府県コードを取得します。
   *
   * @return 都道府県コード (2桁)。取得できない場合は null
   */
  public @Nullable String getPrefectureCode() {
    if (lgCode == null || lgCode.trim().length() < 2) {
      return null;
    }
    return lgCode.trim().substring(0, 2);
  }
}
