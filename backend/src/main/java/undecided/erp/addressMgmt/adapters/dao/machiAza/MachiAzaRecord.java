package undecided.erp.addressMgmt.adapters.dao.machiAza;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import undecided.erp.addressMgmt.domain.model.machiAza.MachiAzaStatus;
import undecided.erp.addressMgmt.domain.model.machiAza.MachiAzaType;

/**
 * A class representing a MachiAzaRecord entity.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Entity
@Table(name = "machi_azas", schema = "address_info")
public class MachiAzaRecord {

  @Id
  @Column(name = "machi_aza_id", nullable = false)
  private Long id;

  @Column(name = "city_id", nullable = false)
  private Long cityId;
  @Column(name = "lg_code", nullable = false)
  private String lgCode;
  @Column(name = "machiaza_code", nullable = false)
  private String machiazaCode;

  /** 町字区分 */
  @Convert(converter = MachiAzaTypeType.class)
  @Column(name = "machiaza_type", nullable = false, length = 1)
  private MachiAzaType type;
  @Column(name = "oaza_cho_name")
  private String oazaChoName;
  @Column(name = "oaza_cho_kana")
  private String oazaChoKana;
  @Column(name = "oaza_cho_roma")
  private String oazaChoRoma;
  @Column(name = "chome_name")
  private String chomeName;
  @Column(name = "chome_kana")
  private String chomeKana;
  @Column(name = "chome_number")
  private String chomeNumber;
  @Column(name = "koaza_name")
  private String koazaName;
  @Column(name = "koaza_kana")
  private String koazaKana;
  @Column(name = "koaza_roma")
  private String koazaRoma;
  /**
   * 同一町字識別情報
   * <p>
   * 町字（大字・町）の名称が同一で、別の町字IDを付与してレコードを複数に分ける場合に識別する名称を収録。
   */
  @Column(name = "machiaza_dist")
  private String machiazaDist;

  /**
   * 住居表示フラグ。
   * <p>
   * 当該大字町丁目の全部または一部において住居表示を実施しているか否かを示す。
   * なお、同一大字町丁目に住居表示実施区域と未実施区域が併存する場合には、同一町字IDに対し実施と非実施の2レコードを収録する。
   * <p>
   * （1:住居表示実施 0:住居表示非実施）
   */
  @Column(name = "rsdt_addr_flg")
  private Boolean rsdtAddrFlg;

  /**
   * 住居表示方式コード。
   * <p>
   * 住居表示の方式を示す。（1:街区方式　2:道路方式　0:住居表示でない）
   */
  @Column(name = "rsdt_addr_mtd_code")
  private String rsdtAddrMtdCode;

  /**
   * 大字・町名_通称フラグ。
   * <p>
   * 住居表示の方式を示す。（1:街区方式　2:道路方式　0:住居表示でない）
   */
  @Column(name = "oaza_cho_aka_flg")
  private Boolean oazaChoAkaFlg;

  /**
   * 大字・町名_通称フラグ。
   * <p>
   * 大字・町名に通称名を収録する場合を判別するフラグ。（0:通称でない　1:大字・町名に通称名を収録）　（注3）
   */
  @Column(name = "koaza_aka_code")
  private String koazaAkaCode;

  /**
   * 小字名_通称コード
   */
  @Column(name = "oaza_cho_gsi_uncmn")
  private String oazaChoGsiUncmn;
  @Column(name = "koaza_gsi_uncmn")
  private String koazaGsiUncmn;
  @Convert(converter = MachiAzaStatusType.class)
  @Column(name = "status")
  private MachiAzaStatus status;
  @Column(name = "wake_num_flg")
  private Boolean wakeNumFlg;
  /**
   * 原典資料コード。
   * <p>
   * 原典資料を表すコード。（1:自治体資料　11:位置参照情報・自治体資料　12:位置参照情報・街区レベル　13:位置参照情報・1/2500地形図　10:位置参照情報・その他資料　0:その他資料）。※10～13は大字・町丁目レベル位置参照情報の原典資料コードの0～3に対応
   */
  @Column(name = "src_code")
  private String srcCode;
  private LocalDate effectiveDate;
  private LocalDate abolitionDate;
  private String remarks;


}