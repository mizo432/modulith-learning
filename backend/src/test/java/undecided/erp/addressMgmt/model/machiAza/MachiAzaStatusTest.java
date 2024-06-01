package undecided.erp.addressMgmt.model.machiAza;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MachiAzaStatusTest {

  @ParameterizedTest
  @CsvSource({
      "1,自治体確認待ち",
      "2,地方自治法の町若しくは字に該当",
      "3,地方自治法の町若しくは字に非該当",
      "9,不明",
      "32,不明",
      "999,不明"})
  void testValueOfId(int id, String machiAzaStatus) {
    MachiAzaStatus expectedStatus = MachiAzaStatus.valueOf(machiAzaStatus);
    assertThat(MachiAzaStatus.valueOfId(id)).isEqualTo(expectedStatus);
  }

}