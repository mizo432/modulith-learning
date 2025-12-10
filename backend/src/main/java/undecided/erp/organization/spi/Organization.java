package undecided.erp.organization.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 * 組織を表すエンティティクラス。
 *
 * <p>このクラスはデータベースの「organizations」テーブルと対応しています。
 *
 * <p>複数階層の組織コードおよび名称を管理し、有効期間情報を含みます。
 *
 * <p>LEVEL0 から LEVEL4 までのコード長さに関する定数が定義されています。
 *
 * <p>
 *
 * <p>用途:
 *
 * <p>組織データの管理や検索、および他のエンティティ (たとえば RoleAssignmentForOrg) とのリレーションで使用されます。
 */
@Entity
@Table(name = "organizations")
@Getter
@Setter
public class Organization implements Serializable {
  /**
   * 組織コードのレベル0で使用されるコードの長さを表す定数。
   *
   * <p>組織コードは階層毎に分割されており、レベル0は最上位の階層に該当します。
   *
   * <p>この定数は、レベル0コードの文字数制限を指定します。
   *
   * <p>データベースの対応カラム（level0Code）に適用される長さ制約として使用されます。
   */
  public static final int LEVEL0_CODE_LENGTH = 4;

  /**
   * 組織コードのレベル1で使用されるコードの長さを表す定数。
   *
   * <p>組織コードは階層毎に分割されており、レベル1はレベル0の下位階層に該当します。
   *
   * <p>この定数は、レベル1コードの文字数制限を指定します。
   *
   * <p>データベースの対応カラム（level1Code）に適用される長さ制約として使用されます。
   */
  public static final int LEVEL1_CODE_LENGTH = 2;

  /**
   * 組織コードのレベル2で使用されるコードの長さを表す定数。
   *
   * <p>組織コードは階層毎に分割されており、レベル2は階層構造の中で レベル1の下位階層に該当します。
   *
   * <p>この定数は、レベル2コードの文字数制限を指定します。
   *
   * <p>データベースの対応カラム（level2Code）に適用される長さ制約として使用されます。
   */
  public static final int LEVEL2_CODE_LENGTH = 2;

  /**
   * 組織コードのレベル3で使用されるコードの長さを表す定数。
   *
   * <p>組織コードは階層構造に基づいて分割されており、レベル3はレベル2の下位階層に該当します。
   *
   * <p>この定数は、レベル3コードの文字数制限を指定します。
   *
   * <p>データベースの対応カラム（level3Code）に適用される長さ制約として使用されます。
   */
  public static final int LEVEL3_CODE_LENGTH = 2;

  /**
   * 組織コードのレベル4で使用されるコードの長さを表す定数。
   *
   * <p>組織コードは階層毎に分割されており、レベル4は階層構造の中で 最下位階層に該当します。
   *
   * <p>この定数は、レベル4コードの文字数制限を指定します。
   *
   * <p>データベースの対応カラム（level4Code）に適用される長さ制約として使用されます。
   */
  public static final int LEVEL4_CODE_LENGTH = 3;

  private static final int CODE_LENGTH = 100;

  /**
   * 組織を一意に識別するための識別子。
   *
   * <p>このフィールドは、UUID を使用して一意性を保証します。 データベース上では主キーとして機能し、@Id アノテーションによってマッピングされています。
   *
   * <p>用途: - 組織エンティティを識別するために使用されます。 - 他のエンティティとのリレーションで識別子として参照される場合があります。
   *
   * <p>制約: - null 値は許可されません (データベース制約としても設定されます)。 - UUID フォーマットである必要があります。
   */
  @Id private UUID organizationId;

  /**
   * 組織コードを表すフィールド。
   *
   * <p>このフィールドは「organizations」テーブルにおける組織の一意のコードを格納します。
   * 組織コードは、階層構造に基づいた複合コードであり、以下の階層のコードを結合する形で構成されています。 - レベル0: {@link #LEVEL0_CODE_LENGTH} - レベル1:
   * {@link #LEVEL1_CODE_LENGTH} - レベル2: {@link #LEVEL2_CODE_LENGTH} - レベル3: {@link
   * #LEVEL3_CODE_LENGTH} - レベル4: {@link #LEVEL4_CODE_LENGTH}
   *
   * <p>制約: - null 値は許可されません。 - 一意性が保証されます。 - 長さは、各階層のコード長さを合計した {@code LEVEL0_CODE_LENGTH +
   * LEVEL1_CODE_LENGTH + LEVEL2_CODE_LENGTH + LEVEL3_CODE_LENGTH + LEVEL4_CODE_LENGTH} に制限されます。
   *
   * <p>用途: - 各階層情報を統合した形での組織識別に使用されます。 - 組織情報の検索や参照に活用されます。
   */
  @Column(
      nullable = false,
      unique = true,
      length =
          LEVEL0_CODE_LENGTH
              + LEVEL1_CODE_LENGTH
              + LEVEL2_CODE_LENGTH
              + LEVEL3_CODE_LENGTH
              + LEVEL4_CODE_LENGTH)
  private String organizationCode;

  /**
   * 組織の正式名称を表すフィールド。
   *
   * <p>このフィールドには、組織の完全な名称を格納します。
   *
   * <p>制約: - null 値は許可されません。
   *
   * <p>用途: - 組織名の表示や検索に使用されます。 - 他のエンティティやシステムでの組織識別時に役立ちます。
   */
  @Column(nullable = false)
  private String fullName;

  /**
   * 最上位階層の組織コード (レベル0) を表すフィールド。
   *
   * <p>このフィールドは、組織コードのうちレベル0に該当する部分を格納します。 レベル0は階層構造の中で最上位に位置するコードを指します。
   *
   * <p>制約: - null 値は許可されません。 - 長さは {@code LEVEL0_CODE_LENGTH} で定義された最大長に制限されます。
   *
   * <p>用途: - 組織の階層構造を表現するために使用されます。 - レベル0の組織情報の検索や管理に活用されます。
   */
  @Column(nullable = false, length = LEVEL0_CODE_LENGTH)
  private String level0Code;

  /**
   * 組織コードのレベル1を表すフィールド。
   *
   * <p>このフィールドは、組織コードの階層構造におけるレベル1に該当する部分を格納します。 レベル1はレベル0の下位階層であり、組織構造を詳細に分割・表現する目的で使用されます。
   *
   * <p>制約: - null 値は許可されません。 - 長さは {@code LEVEL1_CODE_LENGTH} で定められた最大値に制限されます。
   *
   * <p>用途: - 組織の階層的な識別情報の一部として使用されます。 - レベル1の組織情報の検索やフィルタリングに活用されます。
   */
  @Column(nullable = false, length = LEVEL1_CODE_LENGTH)
  private String level1Code;

  /**
   * 組織コードのレベル2を表すフィールド。
   *
   * <p>このフィールドは、組織コードの階層構造におけるレベル2に該当する部分を格納します。 レベル2は、レベル1の下位階層として位置づけられ、組織構造をより詳細に表現するために使用されます。
   *
   * <p>制約: - null 値は許可されません。 - 長さは {@code LEVEL2_CODE_LENGTH} で定義された最大値に制限されます。
   *
   * <p>用途: - 組織階層内での識別情報の一部として使用されます。 - レベル2に該当する組織情報の検索やフィルタリングに活用されます。
   */
  @Column(nullable = false, length = LEVEL2_CODE_LENGTH)
  private String level2Code;

  /**
   * 組織コードのレベル3を表すフィールド。
   *
   * <p>このフィールドは、組織コードの階層構造におけるレベル3に該当する部分を格納します。 レベル3は、レベル2の下位階層として位置づけられ、組織構造をさらに詳細に表現するために使用されます。
   *
   * <p>制約: - null 値は許可されません。 - 長さは {@code LEVEL3_CODE_LENGTH} で定義された最大値に制限されます。
   *
   * <p>用途: - 組織階層内での識別情報の一部として使用されます。 - レベル3に該当する組織情報の検索やフィルタリングに活用されます。
   */
  @Column(nullable = false, length = LEVEL2_CODE_LENGTH)
  private String level3Code;

  /**
   * 組織コードのレベル4を表すフィールド。
   *
   * <p>このフィールドは、組織コードの階層構造におけるレベル4に該当する部分を格納します。 レベル4は、階層構造の最下位に該当し、組織を詳細に識別するために使用されます。
   *
   * <p>制約: - null 値は許可されません。 - 長さは {@code LEVEL4_CODE_LENGTH} で定義された最大長に制限されます。
   *
   * <p>用途: - 組織の階層的な識別情報の一部として使用されます。 - レベル4に該当する組織情報の検索や管理に活用されます。
   */
  @Column(nullable = false, length = LEVEL4_CODE_LENGTH)
  private String level4Code;

  /**
   * 組織階層の最上位に位置するレベル0の名称を表すフィールド。
   *
   * <p>このフィールドには、レベル0に該当する組織名称を格納します。 組織の階層構造を表現するために使用される名称であり、一般的には最上位階層の グループや組織単位の名称を表します。
   *
   * <p>制約: - null 値は禁止されています。 - 最大文字数は {@code CODE_LENGTH} によって制限されています。
   *
   * <p>用途: - 組織階層内でのレベル0の名称の管理や表示を目的として使用されます。 - 組織データの階層的検索や整理にも役立ちます。
   */
  @Column(nullable = false, length = CODE_LENGTH)
  private String level0Name;

  /**
   * 組織コードのレベル1に対応する名称を表すフィールド。
   *
   * <p>このフィールドは、組織階層構造の中でレベル1に該当する部分の名称を格納します。 レベル1はレベル0の下位階層として位置付けられ、組織構造の詳細化を目的とした名称管理に使用されます。
   *
   * <p>制約: - null 値は許可されません。 - 最大文字数は {@code CODE_LENGTH} によって制限されています。
   *
   * <p>用途: - レベル1の組織名称の表示や検索に使用されます。 - 階層的な組織データ構造の管理や整備に役立ちます。
   */
  @Column(nullable = false, length = CODE_LENGTH)
  private String level1Name;

  /**
   * 組織コードのレベル2に対応する名称を表すフィールド。
   *
   * <p>このフィールドは、組織階層構造の中でレベル2に該当する部分の名称を格納します。 レベル2は階層構造の中間部分として位置付けられ、組織を詳細に分類・表現するために使用されます。
   *
   * <p>制約: - null 値は許可されません。 - 最大文字数は {@code CODE_LENGTH} によって制限されています。
   *
   * <p>用途: - レベル2の組織名称の表示や検索に使用されます。 - 階層的な組織データ管理や構造的な整理に役立ちます。
   */
  @Column(nullable = false, length = CODE_LENGTH)
  private String level2Name;

  /**
   * 組織コードのレベル3に対応する名称を表すフィールド。
   *
   * <p>このフィールドは、組織階層構造の中でレベル3に該当する部分の名称を格納します。 レベル3は、レベル2の下位階層として位置付けられ、組織をさらに詳細に分類・表現する目的で使用されます。
   *
   * <p>制約: - null 値は禁止されています。 - 最大文字数は {@code CODE_LENGTH} によって制限されています。
   *
   * <p>用途: - レベル3の組織名称の表示や検索に使用されます。 - 階層的な組織データ管理や構造的な整理に役立ちます。
   */
  @Column(nullable = false, length = CODE_LENGTH)
  private String level3Name;

  /**
   * 組織コードのレベル4に対応する名称を表すフィールド。
   *
   * <p>このフィールドは、組織階層構造におけるレベル4に該当する部分の名称を格納します。 レベル4は階層構造の最下位階層に該当し、組織をさらに詳細に識別・表現するために使用されます。
   *
   * <p>制約: - null 値は許可されません。 - 最大文字数は {@code CODE_LENGTH} によって制限されています。
   *
   * <p>用途: - レベル4の組織名称の表示や検索に使用されます。 - 階層的な組織データ管理や詳細な分類に役立ちます。
   */
  @Column(nullable = false, length = CODE_LENGTH)
  private String level4Name;

  /**
   * 有効期間の開始日時を表すフィールド。
   *
   * <p>このフィールドには、組織の有効期間が開始する日時を格納します。 データベース上では「null」を許可しない制約が設定されています。
   *
   * <p>制約: - このフィールドは必須入力項目です。
   *
   * <p>用途: - 組織の有効期間管理に使用され、開始日時を明確にするために活用されます。 - 他のシステムやプロセスにおける有効期間の検証や検索時に参照されます。
   */
  @Column(nullable = false)
  private LocalDateTime validFrom;

  /**
   * 有効期間の終了日時を表すフィールド。
   *
   * <p>このフィールドには、組織の有効期間が終了する日時を格納します。
   *
   * <p>制約: - null 値は許可されます。null の場合、有効期間が無期限であることを意味します。
   *
   * <p>用途: - 組織データの有効期限管理に使用されます。 - 無効化された組織を判定するロジックや、終了日時を利用した検索において参照されます。
   */
  private LocalDateTime validTo;
}
