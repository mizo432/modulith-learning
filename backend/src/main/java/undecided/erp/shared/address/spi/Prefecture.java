package undecided.erp.shared.address.spi;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import undecided.erp.shared.entity.AuditResource;
import undecided.shared.common.precondition.ObjectPrecondition;
import undecided.shared.common.primitiveOld.Strings2;
import undecided.shared.common.uuidV7Provider.UuidV7Provider;

/**
 * Prefectureクラスは、日本の地理的な都道府県を表現するエンティティクラスです。
 *
 * <p>このクラスは、都道府県の識別情報や関連するデータを管理するために使用されます。 また、AuditResourceクラスを継承しており、作成・更新時の監査情報を保持します。
 * 主に永続化対象のデータを処理するため、JPAエンティティとして定義されています。
 *
 * <p>【フィールドの概要】 - prefectureId: 都道府県を一意に識別するUUID - code: 都道府県コード - name: 都道府県名 - kana: 都道府県名のフリガナ
 *
 * <p>このクラスは次のアノテーションが付与されています: - @Entity: このクラスがJPAエンティティとしてマッピングされることを示します。 - @AllArgsConstructor:
 * 全てのフィールドを初期化するためのコンストラクタを生成します。 - @NoArgsConstructor: デフォルトの引数なしコンストラクタを生成します。
 *
 * <p>メソッド: - create: 指定されたコード、名前、フリガナを基に都道府県のインスタンスを生成します。
 */
@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Getter
public class Prefecture extends AuditResource implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   * 都道府県を一意に識別するためのUUID。
   *
   * <p>このフィールドは、Prefectureエンティティの主キーとして使用されます。 都道府県ごとに固有の識別子を持つことで、データベース内で一意性を保ち、
   * 他のエンティティや操作から識別しやすくします。
   *
   * <p>特徴: - UUID形式を採用し、一意性を保証。 - JPAの@Idアノテーションにより、主キーとしてマッピング。 -
   * UuidV7Provider.newInstance()メソッドを使用して生成。
   *
   * <p>主に、永続化データの参照や関連付けなどで利用されます。
   */
  @Id private UUID prefectureId;

  /**
   * 都道府県を識別するためのコードを表します。
   *
   * <p>このフィールドは、都道府県に対応する一意の識別子を文字列形式で保持します。 具体的には、日本の行政区域を一意に特定するための情報となります。
   * 主に、永続化や検索処理、データ連携などの場面で使用されます。
   */
  private String code;

  /**
   * 都道府県の名称を表します。
   *
   * <p>この変数は、Prefectureエンティティ内で都道府県の正式な名称を管理するために使用されます。 例として、「東京都」や「大阪府」といった名称が格納されます。
   *
   * <p>主に表示やデータの永続化、検索などの目的で利用されます。
   */
  private String name;

  /**
   * 都道府県名のフリガナを表すフィールド。
   *
   * <p>このフィールドは、都道府県名称のフリガナ表記（カタカナ）を文字列形式で保持します。 主に、検索や表示時に読みやすい形で都道府県名を示す際に使用されます。
   *
   * <p>利用例: - 「東京都」の場合は「トウキョウト」と格納されます。 - 「大阪府」の場合は「オオサカフ」と格納されます。
   *
   * <p>主な用途: - 都道府県名のユーザーフレンドリーな検索対応。 - データベースや外部システムへの投影時のキーとしての利用。
   */
  private String kana;

  /**
   * 指定されたコード、名前、フリガナを利用して新しいPrefectureインスタンスを生成します。
   *
   * @param code 都道府県を一意に識別するためのコード
   * @param name 都道府県の名称
   * @param kana 都道府県名のフリガナ
   * @return 新しく生成されたPrefectureインスタンス
   */
  public static Prefecture create(
      @NonNull String code, @NonNull String name, @NonNull String kana) {
    ObjectPrecondition.checkNotNull(code, () -> new NullPointerException("code must not null"));
    ObjectPrecondition.checkNotNull(name, () -> new NullPointerException("name must not null"));
    ObjectPrecondition.checkNotNull(kana, () -> new NullPointerException("kana must not null"));

    return new Prefecture(UuidV7Provider.newInstance(), code, name, kana);
  }

  /**
   * 指定されたコード、名称、カナを用いてPrefectureインスタンスを更新します。
   *
   * <p>既存のインスタンスと同じ値が指定された場合は、現在のインスタンスをそのまま返します。 値に変更があった場合は、新しいPrefectureインスタンスを生成して返します。
   *
   * @param code 都道府県を識別するための一意のコード
   * @param name 都道府県の名称
   * @param kana 都道府県名のフリガナ
   * @return 更新されたPrefectureインスタンス。または変更がない場合は現在のインスタンス
   */
  public Prefecture update(@NonNull String code, @NonNull String name, @NonNull String kana) {
    ObjectPrecondition.checkNotNull(code, () -> new NullPointerException("code must not null"));
    ObjectPrecondition.checkNotNull(name, () -> new NullPointerException("name must not null"));
    ObjectPrecondition.checkNotNull(kana, () -> new NullPointerException("kana must not null"));
    if (Strings2.equal(this.code, code)
        && Strings2.equal(this.name, name)
        && Strings2.equal(this.kana, kana)) return this;

    return new Prefecture(prefectureId, code, name, kana);
  }
}
