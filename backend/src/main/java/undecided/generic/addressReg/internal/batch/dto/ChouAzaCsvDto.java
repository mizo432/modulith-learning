package undecided.generic.addressReg.internal.batch.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jspecify.annotations.Nullable;

/** デジタル庁アドレス・ベース・レジストリ 町字マスター CSV データ用 DTO クラス。 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ChouAzaCsvDto implements Serializable {

  /** 全国地方公共団体コード (例: "011002") */
  private @Nullable String lgCode;

  /** 町字id / 町字コード (例: "0001000") */
  private @Nullable String machiazaCode;

  /** 町字区分コード (例: "1") */
  private @Nullable String machiazaType;

  /** 大字・町名 (例: "大通西") */
  private @Nullable String oazaChoName;

  /** 大字・町名_カナ (例: "オオドオリニシ") */
  private @Nullable String oazaChoKana;

  /** 大字・町名_英字 (例: "Odorinishi") */
  private @Nullable String oazaChoRoma;

  /** 丁目名 (例: "１丁目") */
  private @Nullable String chomeName;

  /** 丁目名_カナ (例: "１チョウメ") */
  private @Nullable String chomeKana;

  /** 丁目名_数字 (例: "1") */
  private @Nullable String chomeNumber;

  /** 小字名 */
  private @Nullable String koazaName;

  /** 小字名_カナ */
  private @Nullable String koazaKana;

  /** 小字名_英字 */
  private @Nullable String koazaRoma;

  /** 同名町字識別コード */
  private @Nullable String machiazaDist;

  /** 住居表示フラグ (例: "1" または "0") */
  private @Nullable String rsdtAddrFlg;

  /** 住居表示方式コード (例: "1") */
  private @Nullable String rsdtAddrMtdCode;

  /** 大字・町名_通称フラグ (例: "0") */
  private @Nullable String oazaChoAkaFlg;

  /** 小字_通称フラグ (例: "0") */
  private @Nullable String koazaAkaCode;

  /** 大字・町名_電子国土基本図外字 */
  private @Nullable String oazaChoGsiUncmn;

  /** 小字_電子国土基本図外字 */
  private @Nullable String koazaGsiUncmn;

  /** 状態フラグ (例: "0") */
  private @Nullable String status;

  /** 起番フラグ (例: "0") */
  private @Nullable String wakeNumFlg;

  /** 原典資料コード */
  private @Nullable String srcCode;

  /** 効力発生日 (例: "1947-04-17") */
  private @Nullable String effectiveDate;

  /** 廃止日 (例: "" または "9999-12-31") */
  private @Nullable String abolitionDate;

  /** 備考 */
  private @Nullable String remarks;

  /**
   * 全国地方公共団体コードから市区町村IDを取得します。
   *
   * @return 市区町村ID。取得できない場合は null
   */
  public @Nullable Long getCityId() {
    if (lgCode == null || lgCode.trim().isEmpty()) {
      return null;
    }
    try {
      return Long.valueOf(lgCode.trim());
    } catch (NumberFormatException e) {
      return null;
    }
  }

  /**
   * 全国地方公共団体コードと町字コードから町字IDを取得します。
   *
   * @return 町字ID。取得できない場合は null
   */
  public @Nullable Long getChouAzaId() {
    if (lgCode == null
        || lgCode.trim().isEmpty()
        || machiazaCode == null
        || machiazaCode.trim().isEmpty()) {
      return null;
    }
    try {
      return Long.valueOf(lgCode.trim() + machiazaCode.trim());
    } catch (NumberFormatException e) {
      return null;
    }
  }
}
