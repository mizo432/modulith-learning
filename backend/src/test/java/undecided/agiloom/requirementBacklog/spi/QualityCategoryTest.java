package undecided.agiloom.requirementBacklog.spi;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("QualityCategoryの単体テスト")
class QualityCategoryTest {

  @Test
  @DisplayName("value=0はINDIFFERENT（無関心品質）を返すこと")
  void shouldReturnIndifferentForValueZero() {
    assertThat(QualityCategory.fromValue(0)).isEqualTo(QualityCategory.INDIFFERENT);
  }

  @Test
  @DisplayName("value=25はINDIFFERENT（無関心品質）を返すこと")
  void shouldReturnIndifferentForValue25() {
    assertThat(QualityCategory.fromValue(25)).isEqualTo(QualityCategory.INDIFFERENT);
  }

  @Test
  @DisplayName("value=26はONE_DIMENSIONAL（一元的品質）を返すこと")
  void shouldReturnOneDimensionalForValue26() {
    assertThat(QualityCategory.fromValue(26)).isEqualTo(QualityCategory.ONE_DIMENSIONAL);
  }

  @Test
  @DisplayName("value=50はONE_DIMENSIONAL（一元的品質）を返すこと")
  void shouldReturnOneDimensionalForValue50() {
    assertThat(QualityCategory.fromValue(50)).isEqualTo(QualityCategory.ONE_DIMENSIONAL);
  }

  @Test
  @DisplayName("value=51はMUST_BE（当たり前品質）を返すこと")
  void shouldReturnMustBeForValue51() {
    assertThat(QualityCategory.fromValue(51)).isEqualTo(QualityCategory.MUST_BE);
  }

  @Test
  @DisplayName("value=75はMUST_BE（当たり前品質）を返すこと")
  void shouldReturnMustBeForValue75() {
    assertThat(QualityCategory.fromValue(75)).isEqualTo(QualityCategory.MUST_BE);
  }

  @Test
  @DisplayName("value=76はATTRACTIVE（魅力的品質）を返すこと")
  void shouldReturnAttractiveForValue76() {
    assertThat(QualityCategory.fromValue(76)).isEqualTo(QualityCategory.ATTRACTIVE);
  }

  @Test
  @DisplayName("value=100はATTRACTIVE（魅力的品質）を返すこと")
  void shouldReturnAttractiveForValue100() {
    assertThat(QualityCategory.fromValue(100)).isEqualTo(QualityCategory.ATTRACTIVE);
  }

  @Test
  @DisplayName("value=nullはnullを返すこと")
  void shouldReturnNullForNullValue() {
    assertThat(QualityCategory.fromValue(null)).isNull();
  }

  @Test
  @DisplayName("ATTRACTIVEのlabelは「魅力的品質」を返すこと")
  void shouldReturnLabelForAttractive() {
    assertThat(QualityCategory.ATTRACTIVE.label()).isEqualTo("魅力的品質");
  }

  @Test
  @DisplayName("MUST_BEのlabelは「当たり前品質」を返すこと")
  void shouldReturnLabelForMustBe() {
    assertThat(QualityCategory.MUST_BE.label()).isEqualTo("当たり前品質");
  }

  @Test
  @DisplayName("ONE_DIMENSIONALのlabelは「一元的品質」を返すこと")
  void shouldReturnLabelForOneDimensional() {
    assertThat(QualityCategory.ONE_DIMENSIONAL.label()).isEqualTo("一元的品質");
  }

  @Test
  @DisplayName("INDIFFERENTのlabelは「無関心品質」を返すこと")
  void shouldReturnLabelForIndifferent() {
    assertThat(QualityCategory.INDIFFERENT.label()).isEqualTo("無関心品質");
  }
}
