package undecided.generic.addressReg.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 町字情報を管理するエンティティクラス。
 *
 * <p>このクラスは、データベースの「address_reg.chou_aza」テーブルに対応するものであり、町字に関する情報を保持します。
 * 各フィールドは、町字ID、市区町村ID、法定コード、町字コード、町字区分コード、大字・町名、丁目名、小字名、住居表示フラグ、有効日や廃止日などを表します。
 *
 * <p>エンティティとして、JPAやHibernateを利用してデータベースと連携するための定義が施されています。
 */
@Getter
@Setter
@ToString
@Entity
@Table(schema = "address_reg", name = "chou_aza", comment = "chou_aza table")
public class ChouAza {

  /**
   * 町字を一意に識別するためのIDを格納するフィールド。
   *
   * <p>データベース上では "chou_aza_id" カラムに対応しており、null 不可として定義されています。 主に町字レコードのプライマリキーとして使用されます。
   */
  @Id
  @Column(name = "chou_aza_id", comment = "Unique identifier for the chou aza", nullable = false)
  private Long id;

  /**
   * 所属する市区町村のIDを表すフィールド。
   *
   * <p>データベース上では "city_id" カラムに対応しており、null 不可として定義されています。
   */
  @NotNull
  @Column(name = "city_id", comment = "Unique identifier for the city", nullable = false)
  private Long cityId;

  /**
   * 法定コード（全国地方公共団体コード）を表すフィールド。
   *
   * <p>このフィールドは空であってはならず、最大6文字までの入力が可能です。 データベース上では "lg_code" カラムに対応しており、null 不可として定義されています。
   */
  @Size(max = 6)
  @NotNull
  @Column(name = "lg_code", comment = "Legal code", nullable = false, length = 6)
  private String lgCode;

  /**
   * 町字コードを表すフィールド。
   *
   * <p>このフィールドは空であってはならず、最大7文字までの入力が可能です。 データベース上では "machiaza_code" カラムに対応しており、null 不可として定義されています。
   */
  @Size(max = 7)
  @NotNull
  @Column(name = "machiaza_code", comment = "Machiaza code", nullable = false, length = 7)
  private String machiazaCode;

  /**
   * 町字区分コードを表すフィールド。
   *
   * <p>データベース上では "machiaza_type" カラムに対応しており、null 不可として定義されています。
   */
  @Size(max = 1)
  @NotNull
  @Column(name = "machiaza_type", comment = "Machiaza type code", nullable = false, length = 1)
  private String machiazaType;

  /**
   * 大字・町名を格納するフィールド。
   *
   * <p>データベース上では "oaza_cho_name" カラムに対応しています。
   */
  @Size(max = 120)
  @Column(name = "oaza_cho_name", comment = "Oaza cho name", length = 120)
  private String oazaChoName;

  /**
   * 大字・町名のカナ表記を格納するフィールド。
   *
   * <p>データベース上では "oaza_cho_kana" カラムに対応しています。
   */
  @Size(max = 240)
  @Column(name = "oaza_cho_kana", comment = "Oaza cho kana", length = 240)
  private String oazaChoKana;

  /**
   * 大字・町名のローマ字表記を格納するフィールド。
   *
   * <p>データベース上では "oaza_cho_roma" カラムに対応しています。
   */
  @Size(max = 180)
  @Column(name = "oaza_cho_roma", comment = "Oaza cho romaji", length = 180)
  private String oazaChoRoma;

  /**
   * 丁目名を格納するフィールド。
   *
   * <p>データベース上では "chome_name" カラムに対応しています。
   */
  @Size(max = 32)
  @Column(name = "chome_name", comment = "Chome name", length = 32)
  private String chomeName;

  /**
   * 丁目名のカナ表記を格納するフィールド。
   *
   * <p>データベース上では "chome_kana" カラムに対応しています。
   */
  @Size(max = 50)
  @Column(name = "chome_kana", comment = "Chome kana", length = 50)
  private String chomeKana;

  /**
   * 丁目名の数字表記を格納するフィールド。
   *
   * <p>データベース上では "chome_number" カラムに対応しています。
   */
  @Size(max = 2)
  @Column(name = "chome_number", comment = "Chome number", length = 2)
  private String chomeNumber;

  /**
   * 小字名を格納するフィールド。
   *
   * <p>データベース上では "koaza_name" カラムに対応しています。
   */
  @Size(max = 120)
  @Column(name = "koaza_name", comment = "Koaza name", length = 120)
  private String koazaName;

  /**
   * 小字名のカナ表記を格納するフィールド。
   *
   * <p>データベース上では "koaza_kana" カラムに対応しています。
   */
  @Size(max = 240)
  @Column(name = "koaza_kana", comment = "Koaza kana", length = 240)
  private String koazaKana;

  /**
   * 小字名のローマ字表記を格納するフィールド。
   *
   * <p>データベース上では "koaza_roma" カラムに対応しています。
   */
  @Size(max = 180)
  @Column(name = "koaza_roma", comment = "Koaza romaji", length = 180)
  private String koazaRoma;

  /**
   * 同名町字識別コードを格納するフィールド。
   *
   * <p>データベース上では "machiaza_dist" カラムに対応しています。
   */
  @Size(max = 120)
  @Column(name = "machiaza_dist", comment = "Machiaza distinction code", length = 120)
  private String machiazaDist;

  /**
   * 住居表示フラグを表すフィールド。
   *
   * <p>データベース上では "rsdt_addr_flg" カラムに対応しており、null 不可として定義されています。
   */
  @NotNull
  @Column(name = "rsdt_addr_flg", comment = "Residential address flag", nullable = false)
  private Boolean rsdtAddrFlg;

  /**
   * 住居表示方式コードを格納するフィールド。
   *
   * <p>データベース上では "rsdt_addr_mtd_code" カラムに対応しています。
   */
  @Size(max = 1)
  @Column(name = "rsdt_addr_mtd_code", comment = "Residential address method code", length = 1)
  private String rsdtAddrMtdCode;

  /**
   * 大字・町名_通称フラグを表すフィールド。
   *
   * <p>データベース上では "oaza_cho_aka_flg" カラムに対応しており、null 不可として定義されています。
   */
  @NotNull
  @Column(name = "oaza_cho_aka_flg", comment = "Oaza cho alias flag", nullable = false)
  private Boolean oazaChoAkaFlg;

  /**
   * 小字_通称フラグ/コ���ドを格納するフィールド。
   *
   * <p>データベース上では "koaza_aka_code" カラムに対応しています。
   */
  @Size(max = 1)
  @Column(name = "koaza_aka_code", comment = "Koaza alias code", length = 1)
  private String koazaAkaCode;

  /**
   * 大字・町名_電子国土基本図外字を格納するフィールド。
   *
   * <p>データベース上では "oaza_cho_gsi_uncmn" カラムに対応しています。
   */
  @Size(max = 50)
  @Column(name = "oaza_cho_gsi_uncmn", comment = "Oaza cho GSI uncommon characters", length = 50)
  private String oazaChoGsiUncmn;

  /**
   * 小字_電子国土基本図外字を格納するフィールド。
   *
   * <p>データベース上では "koaza_gsi_uncmn" カラムに対応しています。
   */
  @Size(max = 50)
  @Column(name = "koaza_gsi_uncmn", comment = "Koaza GSI uncommon characters", length = 50)
  private String koazaGsiUncmn;

  /**
   * 状態フラグを表すフィールド。
   *
   * <p>データベース上では "status" カラムに対応しており、null 不可として定義されています。
   */
  @NotNull
  @Column(name = "status", comment = "Status flag", nullable = false)
  private Integer status;

  /**
   * 起番フラグを表すフィールド。
   *
   * <p>データベース上では "wake_num_flg" カラムに対応しており、null 不可として定義されています。
   */
  @NotNull
  @Column(name = "wake_num_flg", comment = "Wake num flag", nullable = false)
  private Boolean wakeNumFlg;

  /**
   * 原典資料コードを格納するフィールド。
   *
   * <p>データベース上では "src_code" カラムに対応しています。
   */
  @Size(max = 2)
  @Column(name = "src_code", comment = "Source code", length = 2)
  private String srcCode;

  /**
   * 町字が有効となる日を表すフィールド。
   *
   * <p>このフィールドは必須です。町字の有効開始日として使用されます。
   */
  @NotNull
  @Column(name = "effective_date", comment = "Effective date", nullable = false)
  private LocalDate effectiveDate;

  /**
   * 廃止日を表すフィールド。
   *
   * <p>このフィールドは必須です。町字が廃止された日付を管理します。
   */
  @NotNull
  @Column(name = "abolition_data", comment = "Abolition date", nullable = false)
  private LocalDate abolitionData;

  /**
   * 備考を表すフィールド。
   *
   * <p>このフィールドには、関連する補足情報や自��形式のコメントを保存できます。
   */
  @Size(max = 256)
  @Column(name = "remarks", comment = "Remarks", length = 256)
  private String remarks;
}
