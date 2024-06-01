package undecided.erp.addressMgmt.model.prefecture;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * 都道府県の名前を表すクラスです。
 */
@AllArgsConstructor
@Getter
public class PrefectureNames {

  private PrefectureName name;
  private PrefectureKana kana;
  private PrefectureRoma roma;

  /**
   * 提供されたname、nameKana、nameRomaの値を用いて、PrefectureNameオブジェクトを再構築します。
   *
   * @param name 県の名前。
   * @param kana 県の名前のカナ表現。
   * @param nameRoma 県の名前のローマ字表現。
   * @return 再構築された値を持つ新しいPrefectureNameオブジェクト。
   */
  public static PrefectureNames reconstruct(String name, String kana, String nameRoma) {
    return new PrefectureNames(PrefectureName.reconstruct(name),
        PrefectureKana.reconstruct(kana),
        PrefectureRoma.reconstruct(nameRoma));

  }

  public static PrefectureNames create(@NonNull String name, String kana, String roma) {
    return new PrefectureNames(PrefectureName.of(name),
        PrefectureKana.of(kana),
        PrefectureRoma.of(roma));
  }

  @Override
  public String toString() {
    return "PrefectureNames{" +
        "name=" + name +
        ", kana=" + kana +
        ", roma=" + roma +
        '}';
  }
}
