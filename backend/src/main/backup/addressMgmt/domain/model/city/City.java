package undecided.erp.addressMgmt.domain.model.city;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import undecided.erp.shared.entity.SnowflakeId;

/**
 * City クラスは、様々な属性を持つ都市を表現します。
 */
@AllArgsConstructor(staticName = "reconstruct")
@NoArgsConstructor
@Getter
@ToString
@Entity
@Table(schema = "address_info", name = "cities")
public class City {

  @EmbeddedId
//  @AttributeOverride(name = "value", column = @Column(name = "city_id"))
  @Column(name = "city_id")
  private SnowflakeId<City> id;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "lgCode.value", column = @Column(name = "lg_code")),
      @AttributeOverride(name = "countryNames.name.value", column = @Column(name = "country_name")),
      @AttributeOverride(name = "countryNames.kana.value", column = @Column(name = "country_kana")),
      @AttributeOverride(name = "countryNames.roma.value", column = @Column(name = "country_roma")),
      @AttributeOverride(name = "cityNames.name.value", column = @Column(name = "city_name")),
      @AttributeOverride(name = "cityNames.kana.value", column = @Column(name = "city_kana")),
      @AttributeOverride(name = "cityNames.roma.value", column = @Column(name = "city_roma")),
      @AttributeOverride(name = "wardNames.name.value", column = @Column(name = "ward_name")),
      @AttributeOverride(name = "wardNames.kana.value", column = @Column(name = "ward_kana")),
      @AttributeOverride(name = "wardNames.roma.value", column = @Column(name = "ward_roma"))
  })
  private CityAttribute attribute = CityAttribute.EMPTY;


  @Embedded
  @AttributeOverride(name = "value.value", column = @Column(name = "effective_date"))
  private CityEffectiveDate effectiveDate = CityEffectiveDate.EMPTY;

  @Embedded
  @AttributeOverride(name = "value.value", column = @Column(name = "abolition_data"))
  private CityAbolitionDate abolitionDate = CityAbolitionDate.EMPTY;

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
  public static City reconstruct(
      @NonNull Long id,
      @NonNull CityAttribute attribute,
      @NonNull LocalDate effectiveDate,
      @NonNull LocalDate abolitionDate,
      String remarks) {
    return new City(
        SnowflakeId.reconstruct(id),
        attribute,
        CityEffectiveDate.reconstruct(effectiveDate),
        CityAbolitionDate.reconstruct(abolitionDate),
        remarks);

  }

  public static City create(
      @NonNull SnowflakeId<City> id,
      @NonNull CityAttribute attribute,
      @NonNull CityEffectiveDate effectiveDate,
      @NonNull CityAbolitionDate abolitionDate,
      String remarks) {
    return new City(id, attribute, effectiveDate, abolitionDate, remarks);

  }

}
