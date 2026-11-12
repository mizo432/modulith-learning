package com.github.mizo432.shared.message;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("ResultMessagesのテスト")
class ResultMessagesTest {

  @Test
  @DisplayName("successファクトリメソッドでSUCCESSタイプのインスタンスが作成される")
  void shouldCreateSuccessTypeInstance() {
    ResultMessages resultMessages = ResultMessages.success();
    assertThat(resultMessages).isNotNull();
    assertThat(resultMessages.getType()).isEqualTo(StandardResultMessageType.SUCCESS);
  }

  @Test
  @DisplayName("infoファクトリメソッドでINFOタイプのインスタンスが作成される")
  void shouldCreateInfoTypeInstance() {
    ResultMessages resultMessages = ResultMessages.info();
    assertThat(resultMessages).isNotNull();
    assertThat(resultMessages.getType()).isEqualTo(StandardResultMessageType.INFO);
  }

  @Test
  @DisplayName("addメソッドでメッセージが追加される")
  void shouldAddMessage() {
    ResultMessages resultMessages = ResultMessages.success();
    resultMessages.add("Test");
    assertThat(resultMessages).isNotNull();
    assertThat(resultMessages.isNotEmpty()).isTrue();
    assertThat(resultMessages.getList().getFirst().code()).isEqualTo("Test");
  }

  @Test
  @DisplayName("addAllメソッドで複数のメッセージが追加される")
  void shouldAddMultipleMessages() {
    ResultMessage res1 = new ResultMessage("Test 1", null, null);
    ResultMessage res2 = new ResultMessage("Test 2", null, null);
    ResultMessages resultMessages = ResultMessages.success();
    resultMessages.addAll(res1, res2);
    assertThat(resultMessages).isNotNull();
    assertThat(resultMessages.isNotEmpty()).isTrue();
    assertThat(resultMessages.getList().get(0).code()).isEqualTo("Test 1");
    assertThat(resultMessages.getList().get(1).code()).isEqualTo("Test 2");
  }

  @Test
  @DisplayName("addAllメソッドで3つのメッセージが追加される")
  void shouldAddThreeMessages() {
    ResultMessage res1 = new ResultMessage("Test 1", null, null);
    ResultMessage res2 = new ResultMessage("Test 2", null, null);
    ResultMessage res3 = new ResultMessage("Test 3", null, null);
    ResultMessages resultMessages = ResultMessages.success();
    resultMessages.addAll(res1, res2, res3);
    assertThat(resultMessages).isNotNull();
    assertThat(resultMessages.isNotEmpty()).isTrue();
    assertThat(resultMessages.getList().get(0).code()).isEqualTo("Test 1");
    assertThat(resultMessages.getList().get(1).code()).isEqualTo("Test 2");
    assertThat(resultMessages.getList().get(2).code()).isEqualTo("Test 3");
  }

  @Test
  @DisplayName("warningファクトリメソッドでWARNINGタイプのインスタンスが作成される")
  void shouldCreateWarningTypeInstance() {
    ResultMessages resultMessages = ResultMessages.warning();
    assertThat(resultMessages).isNotNull();
    assertThat(resultMessages.getType()).isEqualTo(StandardResultMessageType.WARNING);
  }

  @Test
  @DisplayName("secondaryファクトリメソッドでSECONDARYタイプのインスタンスが作成される")
  void shouldCreateSecondaryTypeInstance() {
    ResultMessages resultMessages = ResultMessages.secondary();
    assertThat(resultMessages).isNotNull();
    assertThat(resultMessages.getType()).isEqualTo(StandardResultMessageType.SECONDARY);
  }

  @Test
  @DisplayName("lightファクトリメソッドでLIGHTタイプのインスタンスが作成される")
  void shouldCreateLightTypeInstance() {
    ResultMessages resultMessages = ResultMessages.light();
    assertThat(resultMessages).isNotNull();
    assertThat(resultMessages.getType()).isEqualTo(StandardResultMessageType.LIGHT);
  }

  @Test
  @DisplayName("darkファクトリメソッドでDARKタイプのインスタンスが作成される")
  void shouldCreateDarkTypeInstance() {
    ResultMessages resultMessages = ResultMessages.dark();
    assertThat(resultMessages).isNotNull();
    assertThat(resultMessages.getType()).isEqualTo(StandardResultMessageType.DARK);
  }

  @Test
  @DisplayName("addAllメソッドでCollectionのメッセージが追加される")
  void shouldAddMessageCollection() {
    Collection<ResultMessage> messages = new ArrayList<>();
    messages.add(new ResultMessage("Test 1", null, null));
    messages.add(new ResultMessage("Test 2", null, null));
    ResultMessages resultMessages = ResultMessages.success();
    resultMessages.addAll(messages);
    assertThat(resultMessages).isNotNull();
    assertThat(resultMessages.isNotEmpty()).isTrue();
    assertThat(resultMessages.getList().get(0).code()).isEqualTo("Test 1");
    assertThat(resultMessages.getList().get(1).code()).isEqualTo("Test 2");
  }
}
