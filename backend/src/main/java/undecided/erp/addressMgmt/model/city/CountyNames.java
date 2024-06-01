package undecided.erp.addressMgmt.model.city;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * 都道府県の名前を表すクラスです。
 */
@AllArgsConstructor
@Getter
public class CountyNames {

  private CountryName name;
  private CountryKana kana;
  private CountryRoma roma;

  /**
   * 提供されたname、nameKana、nameRomaの値を用いて、PrefectureNameオブジェクトを再構築します。
   *
   * @param name 県の名前。
   * @param kana 県の名前のカナ表現。
   * @param nameRoma 県の名前のローマ字表現。
   * @return 再構築された値を持つ新しいPrefectureNameオブジェクト。
   */
  public static CountyNames reconstruct(String name, String kana, String nameRoma) {
    return new CountyNames(CountryName.reconstruct(name),
        CountryKana.reconstruct(kana),
        CountryRoma.reconstruct(nameRoma));

  }

  public static CountyNames create(@NonNull String name, String kana, String roma) {
    return new CountyNames(CountryName.of(name),
        CountryKana.of(kana),
        CountryRoma.of(roma));
  }

  @Override
  public String toString() {
    return "CountyNames{" +
        "name=" + name +
        ", kana=" + kana +
        ", roma=" + roma +
        '}';
  }
}
