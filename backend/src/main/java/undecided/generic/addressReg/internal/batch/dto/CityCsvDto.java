package undecided.generic.addressReg.internal.batch.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jspecify.annotations.Nullable;

/** デジタル庁アドレス・ベース・レジストリ 市区町村マスター CSV データ用 DTO クラス。 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CityCsvDto implements Serializable {

  /** 全国地方公共団体コード (例: "011002") */
  private @Nullable String lgCode;

  /** 都道府県名 (例: "北海道") */
  private @Nullable String prefName;

  /** 都道府県名_カナ (例: "ホッカイドウ") */
  private @Nullable String prefKana;

  /** 都道府県名_英字 (例: "Hokkaido") */
  private @Nullable String prefRoma;

  /** 郡名 (例: "空知郡") */
  private @Nullable String countryName;

  /** 郡名_カナ (例: "ソラチグン") */
  private @Nullable String countryKana;

  /** 郡名_英字 (例: "Sorachi-gun") */
  private @Nullable String countryRoma;

  /** 市区町��名 (例: "札幌市") */
  private @Nullable String cityName;

  /** 市区町村名_カナ (例: "サッポロシ") */
  private @Nullable String cityKana;

  /** 市区町村名_英字 (例: "Sapporo-shi") */
  private @Nullable String cityRoma;

  /** 政令市区名 (例: "中央区") */
  private @Nullable String wardName;

  /** 政令市区名_カナ (例: "チュウオウク") */
  private @Nullable String wardKana;

  /** 政令市区名_英字 (例: "Chuo-ku") */
  private @Nullable String wardRoma;

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

  /**
   * 全国地方公共団体コードの先頭2桁から都道府県IDを取得します。
   *
   * @return 都道府県ID。取得できない場合は null
   */
  public @Nullable Long getPrefectureId() {
    String prefCode = getPrefectureCode();
    if (prefCode == null) {
      return null;
    }
    try {
      return Long.valueOf(prefCode);
    } catch (NumberFormatException e) {
      return null;
    }
  }
}
