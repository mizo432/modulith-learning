package undecided.erp.common.message;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import org.junit.jupiter.api.Test;

class ResultMessagesTest {

  @Test
  void testSuccessFactoryMethod() {
    ResultMessages resultMessages = ResultMessages.success();
    assertThat(resultMessages).isNotNull();
    assertThat(resultMessages.getType()).isEqualTo(StandardResultMessageType.SUCCESS);
  }

  @Test
  void testInfoFactoryMethod() {
    ResultMessages resultMessages = ResultMessages.info();
    assertThat(resultMessages).isNotNull();
    assertThat(resultMessages.getType()).isEqualTo(StandardResultMessageType.INFO);
  }

  // And so on for the rest of the factory methods

  @Test
  void testAddResultMessage() {
    ResultMessages resultMessages = ResultMessages.success();
    resultMessages.add("Test");
    assertThat(resultMessages).isNotNull();
    assertThat(resultMessages.isNotEmpty()).isTrue();
    assertThat(resultMessages.getList().getFirst().getCode()).isEqualTo("Test");
  }

  @Test
  void testAddAllResultMessages() {
    ResultMessage res1 = new ResultMessage("Test 1", null, null);
    ResultMessage res2 = new ResultMessage("Test 2", null, null);
    ResultMessages resultMessages = ResultMessages.success();
    resultMessages.addAll(res1, res2);
    assertThat(resultMessages).isNotNull();
    assertThat(resultMessages.isNotEmpty()).isTrue();
    assertThat(resultMessages.getList().get(0).getCode()).isEqualTo("Test 1");
    assertThat(resultMessages.getList().get(1).getCode()).isEqualTo("Test 2");
  }

  @Test
  void testAddAllResultMessagesMultiple() {
    ResultMessage res1 = new ResultMessage("Test 1", null, null);
    ResultMessage res2 = new ResultMessage("Test 2", null, null);
    ResultMessage res3 = new ResultMessage("Test 3", null, null);
    ResultMessages resultMessages = ResultMessages.success();
    resultMessages.addAll(res1, res2, res3);
    assertThat(resultMessages).isNotNull();
    assertThat(resultMessages.isNotEmpty()).isTrue();
    assertThat(resultMessages.getList().get(0).getCode()).isEqualTo("Test 1");
    assertThat(resultMessages.getList().get(1).getCode()).isEqualTo("Test 2");
    assertThat(resultMessages.getList().get(2).getCode()).isEqualTo("Test 3");
  }

  @Test
  void testWarningFactoryMethod() {
    ResultMessages resultMessages = ResultMessages.warning();
    assertThat(resultMessages).isNotNull();
    assertThat(resultMessages.getType()).isEqualTo(StandardResultMessageType.WARNING);
  }

  @Test
  void testSecondaryFactoryMethod() {
    ResultMessages resultMessages = ResultMessages.secondary();
    assertThat(resultMessages).isNotNull();
    assertThat(resultMessages.getType()).isEqualTo(StandardResultMessageType.SECONDARY);
  }

  @Test
  void testLightFactoryMethod() {
    ResultMessages resultMessages = ResultMessages.light();
    assertThat(resultMessages).isNotNull();
    assertThat(resultMessages.getType()).isEqualTo(StandardResultMessageType.LIGHT);
  }

  @Test
  void testDarkFactoryMethod() {
    ResultMessages resultMessages = ResultMessages.dark();
    assertThat(resultMessages).isNotNull();
  }

  @Test
  void testAddAllResultMessagesCollection() {
    Collection<ResultMessage> messages = new ArrayList<>();
    messages.add(new ResultMessage("Test 1", null, null));
    messages.add(new ResultMessage("Test 2", null, null));
    ResultMessages resultMessages = ResultMessages.success();
    resultMessages.addAll(messages);
    assertThat(resultMessages).isNotNull();
    assertThat(resultMessages.isNotEmpty()).isTrue();
    assertThat(resultMessages.getList().get(0).getCode()).isEqualTo("Test 1");
    assertThat(resultMessages.getList().get(1).getCode()).isEqualTo("Test 2");
  }
}
