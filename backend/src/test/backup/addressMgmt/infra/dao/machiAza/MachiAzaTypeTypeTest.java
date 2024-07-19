package undecided.erp.addressMgmt.infra.dao.machiAza;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import undecided.erp.addressMgmt.adapters.dao.machiAza.MachiAzaTypeType;
import undecided.erp.addressMgmt.domain.model.machiAza.MachiAzaType;

@SuppressWarnings("NonAsciiCharacters")
class MachiAzaTypeTypeTest {

  private final MachiAzaTypeType machiAzaTypeType = new MachiAzaTypeType();

  @Test
  void convertToDatabaseColumnTests_for_大字町() {
    String convertedValue = machiAzaTypeType.convertToDatabaseColumn(MachiAzaType.大字町);
    MatcherAssert.assertThat(convertedValue, Matchers.is("1"));
  }

  @Test
  void convertToDatabaseColumnTests_for_丁目() {
    String convertedValue = machiAzaTypeType.convertToDatabaseColumn(MachiAzaType.丁目);
    MatcherAssert.assertThat(convertedValue, Matchers.is("2"));
  }

  @Test
  void convertToDatabaseColumnTests_for_小字() {
    String convertedValue = machiAzaTypeType.convertToDatabaseColumn(MachiAzaType.小字);
    MatcherAssert.assertThat(convertedValue, Matchers.is("3"));
  }

  @Test
  void convertToDatabaseColumnTests_for_大字町または丁目または小字なし() {
    String convertedValue = machiAzaTypeType.convertToDatabaseColumn(
        MachiAzaType.大字町または丁目または小字なし);
    MatcherAssert.assertThat(convertedValue, Matchers.is("4"));
  }

  @Test
  void convertToDatabaseColumnTests_for_道路方式の住居表示における道路名() {
    String convertedValue = machiAzaTypeType.convertToDatabaseColumn(
        MachiAzaType.道路方式の住居表示における道路名);
    MatcherAssert.assertThat(convertedValue, Matchers.is("5"));
  }

  @Test
  void convertToEntityAttributeTest_for_1() {
    MachiAzaType convertedValue = machiAzaTypeType.convertToEntityAttribute("1");
    MatcherAssert.assertThat(convertedValue, Matchers.is(MachiAzaType.大字町));
  }

  @Test
  void convertToEntityAttributeTest_for_2() {
    MachiAzaType convertedValue = machiAzaTypeType.convertToEntityAttribute("2");
    MatcherAssert.assertThat(convertedValue, Matchers.is(MachiAzaType.丁目));
  }

  @Test
  void convertToEntityAttributeTest_for_3() {
    MachiAzaType convertedValue = machiAzaTypeType.convertToEntityAttribute("3");
    MatcherAssert.assertThat(convertedValue, Matchers.is(MachiAzaType.小字));
  }

  @Test
  void convertToEntityAttributeTest_for_4() {
    MachiAzaType convertedValue = machiAzaTypeType.convertToEntityAttribute("4");
    MatcherAssert.assertThat(convertedValue,
        Matchers.is(MachiAzaType.大字町または丁目または小字なし));
  }

  @Test
  void convertToEntityAttributeTest_for_5() {
    MachiAzaType convertedValue = machiAzaTypeType.convertToEntityAttribute("5");
    MatcherAssert.assertThat(convertedValue,
        Matchers.is(MachiAzaType.道路方式の住居表示における道路名));
  }
}
