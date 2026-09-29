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
 * 市区町村情報を管理するエンティティクラス。
 *
 * <p>このクラスは、データベースの「address_reg.cities」テーブルに対応するものであり、市区町村に関する情報を保持します。
 * 各フィールドは、市区町村のID、都道府県ID、法定コード、郡名、市区町村名、政令市区名、有効日や廃止日などを表します。
 *
 * <p>エンティティとして、JPAやHibernateを利用してデータベースと連携するための定義が施されています。
 */
@Getter
@Setter
@ToString
@Entity
@Table(schema = "address_reg", name = "cities", comment = "city table")
public class City {

  /**
   * 市区町村を一意に識別するためのIDを格納するフィールド。
   *
   * <p>データベース上では "city_id" カラムに対応しており、null 不可として定義されています。 主に市区町村レコードのプライマリキーとして使用されます。
   */
  @Id
  @Column(name = "city_id", comment = "Unique identifier for the city", nullable = false)
  private Long id;

  /**
   * 所属する都道府県のIDを表すフィールド。
   *
   * <p>データベース上では "prefecture_id" カラムに対応しており、null 不可として定義されています。
   */
  @NotNull
  @Column(
      name = "prefecture_id",
      comment = "Unique identifier for the prefecture",
      nullable = false)
  private Long prefectureId;

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
   * 郡名を格納するフィールド。
   *
   * <p>データベース上では "country_name" カラムに対応しています。
   */
  @Size(max = 24)
  @Column(name = "country_name", comment = "County name", length = 24)
  private String countryName;

  /**
   * 郡名のカナ表記を格納するフィールド。
   *
   * <p>データベース上では "country_kana" カラムに対応しています。
   */
  @Size(max = 50)
  @Column(name = "country_kana", comment = "County kana", length = 50)
  private String countryKana;

  /**
   * 郡名のローマ字表記を格納するフィールド。
   *
   * <p>データベース上では "country_roma" カラムに対応しています。
   */
  @Size(max = 100)
  @Column(name = "country_roma", comment = "County romaji", length = 100)
  private String countryRoma;

  /**
   * 市区町村名を格納するフィールド。
   *
   * <p>データベース上では "city_name" カラムに対応しています。
   */
  @Size(max = 24)
  @Column(name = "city_name", comment = "City name", length = 24)
  private String cityName;

  /**
   * 市区町村名のカナ表記を格納するフィールド。
   *
   * <p>データベース上では "city_kana" カラムに対応しています。
   */
  @Size(max = 50)
  @Column(name = "city_kana", comment = "City kana", length = 50)
  private String cityKana;

  /**
   * 市区町村名のローマ字表記を格納するフィールド。
   *
   * <p>データベース上では "city_roma" カラムに対応しています。
   */
  @Size(max = 100)
  @Column(name = "city_roma", comment = "City romaji", length = 100)
  private String cityRoma;

  /**
   * 政令市区名を格納するフィールド。
   *
   * <p>データベース上では "ward_name" カラムに対応しています。
   */
  @Size(max = 24)
  @Column(name = "ward_name", comment = "Ward name", length = 24)
  private String wardName;

  /**
   * 政令市区名のカナ表記を格納するフィールド。
   *
   * <p>データベース上では "ward_kana" カラムに対応しています。
   */
  @Size(max = 50)
  @Column(name = "ward_kana", comment = "Ward kana", length = 50)
  private String wardKana;

  /**
   * 政令市区名のローマ字表記を格納するフィールド。
   *
   * <p>データベース上では "ward_roma" カラムに対応しています。
   */
  @Size(max = 100)
  @Column(name = "ward_roma", comment = "Ward romaji", length = 100)
  private String wardRoma;

  /**
   * 市区町村が有効となる日を表すフィールド。
   *
   * <p>このフィールドは必須です。市区町村の有効開始日として使用されます。
   */
  @NotNull
  @Column(name = "effective_date", comment = "Effective date", nullable = false)
  private LocalDate effectiveDate;

  /**
   * 廃止日を表すフィールド。
   *
   * <p>このフィールドは必須です。市区町村が廃止された日付を管理します。
   */
  @NotNull
  @Column(name = "abolition_data", comment = "Abolition date", nullable = false)
  private LocalDate abolitionData;

  /**
   * 備考を表すフィールド。
   *
   * <p>このフィールドには、関連する補足情報や自由形式のコメントを保存できます。
   */
  @Size(max = 256)
  @Column(name = "remarks", comment = "Remarks", length = 256)
  private String remarks;
}
