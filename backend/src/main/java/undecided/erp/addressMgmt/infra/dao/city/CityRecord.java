package undecided.erp.addressMgmt.infra.dao.city;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import undecided.erp.addressMgmt.model.city.City;
import undecided.erp.addressMgmt.model.city.CityAttribute;
import undecided.erp.addressMgmt.model.city.CityLgCode;
import undecided.erp.addressMgmt.model.city.CityNames;
import undecided.erp.addressMgmt.model.city.CountyNames;
import undecided.erp.addressMgmt.model.city.WardNames;

/**
 * CityRecordクラスは、address_infoデータベーススキーマにおける市区町村レコードを表します。
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "cities", schema = "address_info")
public class CityRecord {

  @Id
  @Column(name = "city_id", nullable = false)
  private Long id;

  @Column(name = "prefecture_id", nullable = false)
  private Long prefectureId;
  @Column(name = "lg_code", nullable = false)
  private String lgCode;
  @Column(name = "county_name", nullable = false)
  private String countyName;
  @Column(name = "county_kana", nullable = false)
  private String countyKana;
  @Column(name = "county_roma", nullable = false)
  private String countyRoma;
  @Column(name = "city_name", nullable = false)
  private String cityName;
  @Column(name = "city_kana", nullable = false)
  private String cityKana;
  @Column(name = "city_roma", nullable = false)
  private String cityRoma;
  @Column(name = "ward_name", nullable = false)
  private String wardName;
  @Column(name = "ward_kana", nullable = false)
  private String wardKana;
  @Column(name = "ward_roma", nullable = false)
  private String wardRoma;

  private LocalDate effectiveDate;
  private LocalDate abolitionDate;
  private String remarks;

  public City toEntity() {
    CityAttribute attribute = toCityAttribute();
    return City.reconstruct(id, attribute, effectiveDate, abolitionDate, remarks);
  }

  private CityAttribute toCityAttribute() {
    CountyNames countyNames = toCountyNames();
    CityNames cityNames = toCityNames();
    WardNames wardNames = toWardNames();
    return CityAttribute.reconstruct(CityLgCode.reconstruct(lgCode), countyNames, cityNames,
        wardNames);
  }

  private WardNames toWardNames() {
    return WardNames.reconstruct(countyRoma, cityRoma, wardRoma);
  }

  private CityNames toCityNames() {
    return CityNames.reconstruct(cityName, cityKana, cityRoma);
  }

  private CountyNames toCountyNames() {
    return CountyNames.reconstruct(countyName, cityKana, cityRoma);
  }
}