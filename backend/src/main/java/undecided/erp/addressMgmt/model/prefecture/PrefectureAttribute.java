package undecided.erp.addressMgmt.model.prefecture;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * PrefectureAttributeは、lgCodeやnameなどの県の属性を表すクラスです。
 */
@AllArgsConstructor
@Getter
@ToString
public class PrefectureAttribute {

  private PrefectureLgCode lgCode;
  private PrefectureName name;

  /**
   * 提供されたlgCodeと名前を使用して新しいPrefectureAttributeオブジェクトを作成します。
   *
   * @param lgCode 県を表すlgCode
   * @param name 県の名前
   * @return 提供されたlgCodeと名前をもつ新しいPrefectureAttributeオブジェクト
   */
  public static PrefectureAttribute create(@NonNull PrefectureLgCode lgCode,
      @NonNull PrefectureName name) {
    return new PrefectureAttribute(lgCode, name);

  }

  /**
   * 与えられた都道府県LgコードとPerfNameを使用して、PrefectureAttributeを再構築します。
   *
   * @param lgCode PrefectureAttributeに設定する都道府県Lgコード
   * @param name PrefectureAttributeに設定するPerfName
   * @return 提供されたlgCodeとnameを持つ新しいPrefectureAttributeのインスタンス
   */
  public static PrefectureAttribute reconstruct(String lgCode, String name, String nameKana,
      String nameRoma) {
    PrefectureLgCode lgCode1 = PrefectureLgCode.reconstruct(lgCode);
    return new PrefectureAttribute(lgCode1, PrefectureName.reconstruct(name, nameKana, nameRoma));

  }

}
