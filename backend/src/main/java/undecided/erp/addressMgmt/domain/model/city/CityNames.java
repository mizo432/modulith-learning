package undecided.erp.addressMgmt.domain.model.city;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * 都道府県の名前を表すクラスです。
 */
@AllArgsConstructor
@Getter
public class CityNames {

  private CityName name;
  private CityKana kana;
  private CityRoma roma;

  /**
   * 提供されたname、nameKana、nameRomaの値を用いて、PrefectureNameオブジェクトを再構築します。
   *
   * @param name 県の名前。
   * @param kana 県の名前のカナ表現。
   * @param roma 県の名前のローマ字表現。
   * @return 再構築された値を持つ新しいPrefectureNameオブジェクト。
   */
  public static CityNames reconstruct(String name, String kana, String roma) {
    return new CityNames(CityName.reconstruct(name),
        CityKana.reconstruct(kana),
        CityRoma.reconstruct(roma));

  }

  public static CityNames create(@NonNull String name, String kana, String roma) {
    return new CityNames(CityName.of(name),
        CityKana.of(kana),
        CityRoma.of(roma));
  }

  @Override
  public String toString() {
    return "CityNames{" +
        "name=" + name +
        ", kana=" + kana +
        ", roma=" + roma +
        '}';
  }
}
