package undecided.erp.addressMgmt.infra.dao.city;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import undecided.erp.addressMgmt.adapters.dao.city.CityRecord;

class CityRecordTest {

  @Test
  void toEntity_ShouldReturnValidCity() {
    CityRecord cityRecord = new CityRecord();
    cityRecord.setId(1L);
    cityRecord.setLgCode("123");
    cityRecord.setCountyName("Tokyo");
    cityRecord.setCountyKana("Tokyo");
    cityRecord.setCountyRoma("Tokyo");
    cityRecord.setCityName("Tokyo");
    cityRecord.setCityKana("Tokyo");
    cityRecord.setCityRoma("Tokyo");
    cityRecord.setWardName("Shinjuku");
    cityRecord.setWardKana("Shinjuku");
    cityRecord.setWardRoma("Shinjuku");
    cityRecord.setEffectiveDate(LocalDate.now());
    cityRecord.setAbolitionDate(LocalDate.now());
    cityRecord.setRemarks("Due to administrative changes, previous record is abolished.");

    var actualCity = cityRecord.toEntity();
    System.out.println(actualCity);
  }
}