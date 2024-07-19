package undecided.erp.addressMgmt.domain.model.city;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * LG コード、群名、市名、区名からなる都市の特性を表します。
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Embeddable
public class CityAttribute {

  public static final CityAttribute EMPTY = new CityAttribute();
  /**
   * 市のLGコードを表す変数。
   */
  @Embedded
  private CityLgCode lgCode = CityLgCode.EMPTY;
  /**
   * この変数は郡の名前を表します。
   * <p>
   * これは、郡を含む都市の情報を保存するためのCityAttributeクラスの一部として使用されます。
   */
  @Embedded
  private CountyNames countryNames = CountyNames.EMPTY;
  /**
   * 表現の名前、カタカナ表現、及びローマ字表現を含む都市の名前を表現します。
   */
  @Embedded
  private CityNames cityNames = CityNames.EMPTY;
  /**
   * 市の区名を表します。
   */
  @Embedded
  private WardNames wardNames = WardNames.EMPTY;

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
