package undecided.erp.addressMgmt.domain.model.city;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * LG コード、群名、市名、区名からなる都市の特性を表します。
 */
@AllArgsConstructor
@Getter
@ToString
public class CityAttribute {

  /**
   * 市のLGコードを表す変数。
   */
  private CityLgCode lgCode;
  /**
   * この変数は郡の名前を表します。
   * <p>
   * これは、郡を含む都市の情報を保存するためのCityAttributeクラスの一部として使用されます。
   */
  private CountyNames countryNames;
  /**
   * 表現の名前、カタカナ表現、及びローマ字表現を含む都市の名前を表現します。
   */
  private CityNames cityNames;
  /**
   * 市の区名を表します。
   */
  private WardNames wardNames;

  public static CityAttribute create(@NonNull CityLgCode lgCode,
      @NonNull CountyNames countryNames,
      @NonNull CityNames cityNames,
      @NonNull WardNames wardNames) {
    return new CityAttribute(lgCode, countryNames, cityNames, wardNames);

  }

  public static CityAttribute reconstruct(@NonNull CityLgCode lgCode,
      @NonNull CountyNames countryNames,
      @NonNull CityNames cityNames,
      @NonNull WardNames wardNames) {
    return new CityAttribute(lgCode, countryNames, cityNames, wardNames);

  }


}
