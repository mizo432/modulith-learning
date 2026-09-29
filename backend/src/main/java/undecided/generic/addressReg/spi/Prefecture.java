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
 * 都道府県情報を管理するエンティティクラス。
 *
 * <p>このクラスは、データベースの「address_reg.prefectures」テーブルに対応するものであり、 都道府県に関する情報を保持します。各フィールドは、都道府県のID、コード、
 * 名称、法定コード、有効日や廃止日などを表します。
 *
 * <p>エンティティとして、JPAやHibernateを利用してデータベースと連携するための 定義が施されています。
 */
@Getter
@Setter
@ToString
@Entity
@Table(schema = "address_reg", name = "prefectures", comment = "prefecture table")
public class Prefecture {

  /**
   * 都道府県を一意に識別するためのIDを格納するフィールド。
   *
   * <p>データベース上では "prefecture_id" カラムに対応しており、null 不可として定義されています。 主に都道府県レコードのプライマリキーとして使用されます。
   */
  @Id
  @Column(
      name = "prefecture_id",
      comment = "Unique identifier for the prefecture",
      nullable = false)
  private Long id;

  /**
   * 都道府県コードを格納するフィールド。
   *
   * <p>このフィールドは空であってはならず、最大2文字までの入力が可能です。 データベース上では "prefecture_code" カラムに対応しており、null
   * 不可として定義されています。 主に都道府県を一意に識別するために使用されます。
   */
  @Size(max = 2)
  @NotNull
  @Column(name = "prefecture_code", comment = "Prefecture code", nullable = false, length = 2)
  private String prefectureCode;

  /**
   * 法定コードを表すフィールド。
   *
   * <p>このフィールドは空であってはならず、最大6文字までの入力が可能です。 データベース上では "lg_code" カラムに対応しており、null 不可として定義されています。
   * 主に行政区分などを識別するために使用されます。
   */
  @Size(max = 6)
  @NotNull
  @Column(name = "lg_code", comment = "Legal code", nullable = false, length = 6)
  private String lgCode;

  /**
   * 都道府県名を格納するフィールド。
   *
   * <p>このフィールドは空であってはならず、最大10文字までの入力が可能です。 データベース上では "pref_name" カラムに対応し、null 不可として定義されています。
   * 主に都道府県名を保持するために使用されます。
   */
  @Size(max = 10)
  @NotNull
  @Column(name = "pref_name", comment = "Prefecture name", nullable = false, length = 10)
  private String prefName;

  /**
   * 都道府県名のカナ表記を格納するフィールド。
   *
   * <p>このフィールドは空であってはならず、最大50文字までの入力が可能です。 データベース上で "pref_kana" カラムに対応し、null 不可として定義されています。
   * 主に都道府県名の読み仮名を保持するために使用されます。
   */
  @Size(max = 50)
  @NotNull
  @Column(name = "pref_kana", comment = "Prefecture kana", nullable = false, length = 50)
  private String prefKana;

  /**
   * ローマ字表記で都道府県名を格納するフィールド。
   *
   * <p>このフィールドは空であってはならず、最大50文字の制限があります。 データベース上では "pref_roma" カラムに対応し、null 不可として定義されています。
   * 主に都道府県名のローマ字表記を保持するために使用されます。
   */
  @Size(max = 50)
  @NotNull
  @Column(name = "pref_roma", comment = "Prefecture romaji", nullable = false, length = 50)
  private String prefRoma;

  /**
   * 都道府県が有効となる日を表すフィールド。
   *
   * <p>このフィールドは必須です。 都道府県の有効開始日として使用されます。
   */
  @NotNull
  @Column(name = "effective_date", comment = "Effective date", nullable = false)
  private LocalDate effectiveDate;

  /**
   * 廃止日を表すフィールド。
   *
   * <p>このフィールドは必須です。 都道府県が廃止された日付を管理します。
   */
  @NotNull
  @Column(name = "abolition_data", comment = "Abolition date", nullable = false)
  private LocalDate abolitionData;

  /**
   * 備考を表すフィールド。
   *
   * <p>このフィールドには、関連する補足情報や自由形式のコメントを保存できます。 値は最大256文字まで格納可能です。
   */
  @Size(max = 256)
  @Column(name = "remarks", comment = "Remarks", length = 256)
  private String remarks;
}
