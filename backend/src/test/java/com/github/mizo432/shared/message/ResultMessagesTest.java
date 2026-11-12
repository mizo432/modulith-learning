package com.github.mizo432.shared.message;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.mizo432.shared.common.message.ResultMessage;
import com.github.mizo432.shared.common.message.ResultMessages;
import com.github.mizo432.shared.common.message.StandardResultMessageType;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("ResultMessages のテスト")
class ResultMessagesTest {

  @Nested
  @DisplayName("ファクトリメソッドのテスト")
  class FactoryMethodsTest {

    @Test
    @DisplayName("successファクトリメソッドでSUCCESSタイプのResultMessagesを生成すること")
    void shouldCreateSuccessResultMessages() {
      ResultMessages resultMessages = ResultMessages.success();
      assertThat(resultMessages).isNotNull();
      assertThat(resultMessages.getType()).isEqualTo(StandardResultMessageType.SUCCESS);
    }

    @Test
    @DisplayName("infoファクトリメソッドでINFOタイプのResultMessagesを生成すること")
    void shouldCreateInfoResultMessages() {
      ResultMessages resultMessages = ResultMessages.info();
      assertThat(resultMessages).isNotNull();
      assertThat(resultMessages.getType()).isEqualTo(StandardResultMessageType.INFO);
    }

    @Test
    @DisplayName("warningファクトリメソッドでWARNINGタイプのResultMessagesを生成すること")
    void shouldCreateWarningResultMessages() {
      ResultMessages resultMessages = ResultMessages.warning();
      assertThat(resultMessages).isNotNull();
      assertThat(resultMessages.getType()).isEqualTo(StandardResultMessageType.WARNING);
    }

    @Test
    @DisplayName("secondaryファクトリメソッドでSECONDARYタイプのResultMessagesを生成すること")
    void shouldCreateSecondaryResultMessages() {
      ResultMessages resultMessages = ResultMessages.secondary();
      assertThat(resultMessages).isNotNull();
      assertThat(resultMessages.getType()).isEqualTo(StandardResultMessageType.SECONDARY);
    }

    @Test
    @DisplayName("lightファクトリメソッドでLIGHTタイプのResultMessagesを生成すること")
    void shouldCreateLightResultMessages() {
      ResultMessages resultMessages = ResultMessages.light();
      assertThat(resultMessages).isNotNull();
      assertThat(resultMessages.getType()).isEqualTo(StandardResultMessageType.LIGHT);
    }

    @Test
    @DisplayName("darkファクトリメソッドでResultMessagesを生成すること")
    void shouldCreateDarkResultMessages() {
      ResultMessages resultMessages = ResultMessages.dark();
      assertThat(resultMessages).isNotNull();
    }
  }

  @Nested
  @DisplayName("add メソッドのテスト")
  class AddMethodTest {

    @Test
    @DisplayName("addメソッドでResultMessageを追加できること")
    void shouldAddResultMessage() {
      ResultMessages resultMessages = ResultMessages.success();
      resultMessages.add("Test");
      assertThat(resultMessages).isNotNull();
      assertThat(resultMessages.isNotEmpty()).isTrue();
      assertThat(resultMessages.getList().getFirst().code()).isEqualTo("Test");
    }
  }

  @Nested
  @DisplayName("addAll メソッドのテスト")
  class AddAllMethodTest {

    @Test
    @DisplayName("addAllメソッドで複数のResultMessageを追加できること")
    void shouldAddAllResultMessages() {
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
    @DisplayName("addAllメソッドで3つのResultMessageを追加できること")
    void shouldAddAllResultMessagesMultiple() {
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
    @DisplayName("addAllメソッドでCollectionのResultMessageを追加できること")
    void shouldAddAllResultMessagesFromCollection() {
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
}
