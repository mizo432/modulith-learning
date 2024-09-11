package undecided.erp.addressMgmt.domain.model.city;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 * 都道府県の名前を表すクラスです。
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class WardNames {

  public static final WardNames EMPTY = new WardNames();
  private WardName name;
  private WardKana kana;
  private WardRoma roma;

  /**
   * 提供されたname、nameKana、nameRomaの値を用いて、PrefectureNameオブジェクトを再構築します。
   *
   * @param name 県の名前。
   * @param kana 県の名前のカナ表現。
   * @param nameRoma 県の名前のローマ字表現。
   * @return 再構築された値を持つ新しいPrefectureNameオブジェクト。
   */
  public static WardNames reconstruct(String name, String kana, String nameRoma) {
    return new WardNames(WardName.reconstruct(name),
        WardKana.reconstruct(kana),
        WardRoma.reconstruct(nameRoma));

  }

  public static WardNames create(@NonNull String name, String kana, String roma) {
    return new WardNames(WardName.of(name),
        WardKana.of(kana),
        WardRoma.of(roma));
  }

  @Override
  public String toString() {
    return "WardNames{" +
        "name=" + name +
        ", kana=" + kana +
        ", roma=" + roma +
        '}';
  }
}
