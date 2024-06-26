package undecided.erp.addressMgmt.domain.model.city;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import undecided.erp.shared.entity.SnowflakeId;

/**
 * City クラスは、様々な属性を持つ都市を表現します。
 */
@AllArgsConstructor
@Getter
@ToString
public class City {

  private SnowflakeId<City> id;
  private CityAttribute attribute;
  private CityEffectiveDate effectiveDate;
  private CityAbolitionDate abolitionDate;
  private String remarks;

  /**
   * 指定されたパラメータでCityを再構築します。
   *
   * @param id CityのID
   * @param attribute Cityの属性
   * @param effectiveDate Cityの有効日
   * @param abolitionDate Cityの廃止日
   * @param remarks Cityについての追加的な注釈
   * @return 指定されたパラメータで新たに作られたCity
   * @throws NullPointerException どれかの非ヌルパラメータがnullの場合
   */
  public static City reconstruct(@NonNull Long id, @NonNull CityAttribute attribute,
      @NonNull LocalDate effectiveDate, @NonNull LocalDate abolitionDate, String remarks) {
    return new City(SnowflakeId.of(id), attribute, CityEffectiveDate.of(effectiveDate),
        CityAbolitionDate.of(abolitionDate), remarks);

  }


}
