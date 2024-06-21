package undecided.erp.addressMgmt.infra.dao.prefecture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import undecided.erp.addressMgmt.adapters.dao.prefecture.PrefectureRecord;
import undecided.erp.addressMgmt.model.prefecture.Prefecture;
import undecided.erp.addressMgmt.model.prefecture.PrefectureAttribute;

public class PrefectureRecordTest {

  @Test
  public void testToPrefabecture() {
    // Given:
    Long id = 1L;
    String lgCode = "01";
    String prefName = "Hokkaido";
    String prefKana = "ほっかいどう";
    String prefRoma = "Hokkaido";
    LocalDate efctDate = LocalDate.of(1947, 8, 15);
    LocalDate abltDate = LocalDate.of(9999, 12, 30);
    String remarks = "The northernmost prefecture of Japan";

    PrefectureRecord prefectureRecord = new PrefectureRecord();
    prefectureRecord.setId(id);
    prefectureRecord.setLgCode(lgCode);
    prefectureRecord.setPrefName(prefName);
    prefectureRecord.setPrefKana(prefKana);
    prefectureRecord.setPrefRoma(prefRoma);
    prefectureRecord.setEfctDate(efctDate);
    prefectureRecord.setAbltDate(abltDate);
    prefectureRecord.setRemarks(remarks);

    // When:
    Prefecture prefecture = prefectureRecord.toPrefecture();

    // Then:
    assertNotNull(prefecture);

    assertEquals(id, prefecture.getId().getValue());
    assertEquals(lgCode, prefecture.getAttribute().getLgCode().getValue());
    assertEquals(prefName, prefecture.getAttribute().getNames().getName().getValue());
    assertEquals(prefKana, prefecture.getAttribute().getNames().getKana().getValue());
    assertEquals(prefRoma, prefecture.getAttribute().getNames().getRoma().getValue());
    assertEquals(efctDate, prefecture.getEffectiveDate().getValue().getValue());
    assertEquals(abltDate, prefecture.getAbolitionDate().getValue().getValue());
    assertEquals(remarks, prefecture.getRemarks());
  }

  @Test
  public void testFromPrefecture() {
    // Given:
    Long id = 1L;
    String lgCode = "01";
    String prefName = "Hokkaido";
    String prefKana = "ほっかいどう";
    String prefRoma = "Hokkaido";
    LocalDate efctDate = LocalDate.of(1947, 8, 15);
    LocalDate abltDate = LocalDate.of(9999, 12, 30);
    String remarks = "The northernmost prefecture of Japan";
    Prefecture prefecture = Prefecture.reconstruct(id,
        PrefectureAttribute.reconstruct(lgCode, prefName, prefKana, prefRoma), efctDate, abltDate,
        remarks);

    // When:
    PrefectureRecord prefectureRecord = PrefectureRecord.from(prefecture);

    // Then:
    assertNotNull(prefectureRecord);

    assertEquals(id, prefectureRecord.getId());
    assertEquals(lgCode, prefectureRecord.getLgCode());
    assertEquals(prefName, prefectureRecord.getPrefName());
    assertEquals(prefKana, prefectureRecord.getPrefKana());
    assertEquals(prefRoma, prefectureRecord.getPrefRoma());
    assertEquals(efctDate, prefectureRecord.getEfctDate());
    assertEquals(abltDate, prefectureRecord.getAbltDate());
    assertEquals(remarks, prefectureRecord.getRemarks());
  }
}
