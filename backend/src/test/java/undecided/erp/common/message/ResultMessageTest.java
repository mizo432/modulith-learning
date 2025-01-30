package undecided.erp.common.message;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


class ResultMessageTest {

  @Test
  public void testFromCode() {
    ResultMessage rm = ResultMessage.fromCode("code123", "arg1", "arg2", "arg3");
    assertThat(rm.code()).isEqualTo("code123");
    assertThat(rm.args()).isNotEqualTo(null);
  }

  @Test
  public void testFromText() {
    ResultMessage rm = ResultMessage.fromText("text123");
    assertThat(rm.text()).isEqualTo("text123");
  }


  @Test
  public void testEqualsWithDifferentText() {
    ResultMessage rm1 = ResultMessage.fromText("text1");
    ResultMessage rm2 = ResultMessage.fromText("text2");
    assertThat(rm1.equals(rm2)).isFalse();
  }

  @Test
  public void testEqualsWithSameText() {
    ResultMessage rm1 = ResultMessage.fromText("sameText");
    ResultMessage rm2 = ResultMessage.fromText("sameText");
    assertThat(rm1.equals(rm2)).isTrue();
  }

}
