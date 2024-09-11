package undecided.erp.common.message;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


class ResultMessageTest {

  @Test
  public void testFromCode() {
    ResultMessage rm = ResultMessage.fromCode("code123", "arg1", "arg2", "arg3");
    assertThat(rm.getCode()).isEqualTo("code123");
    assertThat(rm.getArgs()).isNotEqualTo(null);
  }

  @Test
  public void testFromText() {
    ResultMessage rm = ResultMessage.fromText("text123");
    assertThat(rm.getText()).isEqualTo("text123");
  }

  @Test
  public void testEquals() {
    ResultMessage rm1 = ResultMessage.fromCode("sameCode",
        new Object[]{"sameArg1", "sameArg2", "sameArg3"});
    ResultMessage rm2 = ResultMessage.fromCode("sameCode",
        new Object[]{"sameArg1", "sameArg2", "sameArg3"});
    assertThat(rm1.equals(rm2)).isTrue();
  }


  @Test
  public void testEqualsWithDifferentCodes() {
    ResultMessage rm1 = ResultMessage.fromCode("code1",
        new Object[]{"sameArg1", "sameArg2", "sameArg3"});
    ResultMessage rm2 = ResultMessage.fromCode("code2",
        new Object[]{"sameArg1", "sameArg2", "sameArg3"});
    assertThat(rm1.equals(rm2)).isFalse();
  }

  @Test
  public void testEqualsWithDifferentArgs() {
    ResultMessage rm1 = ResultMessage.fromCode("sameCode", "arg1");
    ResultMessage rm2 = ResultMessage.fromCode("sameCode", "arg2");
    assertThat(rm1.equals(rm2)).isFalse();
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
