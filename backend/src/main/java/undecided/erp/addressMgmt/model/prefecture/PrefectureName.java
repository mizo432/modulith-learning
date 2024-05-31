package undecided.erp.addressMgmt.model.prefecture;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * 都道府県の名前を表すクラスです。
 */
@AllArgsConstructor
@Getter
public class PrefectureName {

  private Name name;
  private NameKana kana;
  private NameRoma roma;

  /**
   * 提供されたname、nameKana、nameRomaの値を用いて、PrefectureNameオブジェクトを再構築します。
   *
   * @param name 県の名前。
   * @param nameKana 県の名前のカナ表現。
   * @param nameRoma 県の名前のローマ字表現。
   * @return 再構築された値を持つ新しいPrefectureNameオブジェクト。
   */
  public static PrefectureName reconstruct(String name, String nameKana, String nameRoma) {
    return new PrefectureName(Name.reconstruct(name), NameKana.reconstruct(nameKana),
        NameRoma.reconstruct(nameRoma));

  }

  public static PrefectureName create(@NonNull String name, String nameKana, String nameRoma) {
    return new PrefectureName(Name.reconstruct(name), NameKana.reconstruct(nameKana),
        NameRoma.reconstruct(nameRoma));
  }

  @Override
  public String toString() {
    return "PerfName{" +
        "name=" + name +
        ", kana=" + kana +
        ", roma=" + roma +
        '}';
  }
}
