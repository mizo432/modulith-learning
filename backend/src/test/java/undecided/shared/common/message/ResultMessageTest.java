package undecided.shared.common.message;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@DisplayName("ResultMessage のテスト")
@Tag( "small")
class ResultMessageTest {

  @Nested
  @DisplayName("fromCode メソッドのテスト")
  class FromCodeTest {
    @Test
    @DisplayName("codeからResultMessageを生成できること")
    void shouldCreateResultMessageFromCode() {
      ResultMessage rm = ResultMessage.fromCode("code123", "arg1", "arg2", "arg3");
      assertThat(rm.code()).isEqualTo("code123");
      assertThat(rm.args()).isNotNull();
    }

    @Test
    @DisplayName("codeとargsが文字列表現に整形されること")
    void shouldReturnFormattedStringForCodeAndArgs() {
      // Given
      ResultMessage rm = ResultMessage.fromCode("code123", "arg1", "arg2");

      // When
      String result = rm.toString();

      // Then
      assertThat(result).isEqualTo("ResultMessage{code='code123', args=[arg1, arg2]}");
    }

  }

  @Nested
  @DisplayName("fromTextメソッドのテスト")
  class FromTextTest {
    @Test
    @DisplayName("textのみを持つ場合はtextをそのまま返すこと")
    void shouldReturnTextStringWhenOnlyTextExists() {
      // Given
      ResultMessage rm = ResultMessage.fromText("Some descriptive text.");

      // When
      String result = rm.toString();

      // Then
      assertThat(result).isEqualTo("Some descriptive text.");
    }

  }

  @Nested
  @DisplayName("equalsメソッドのテスト")
  class EqualsTest {
  @Test
    @DisplayName("textが異なる場合はfalseを返すこと")
    void shouldReturnFalseWhenTextsAreDifferent() {
      ResultMessage rm1 = ResultMessage.fromText("text1");
      ResultMessage rm2 = ResultMessage.fromText("text2");
      assertThat(rm1.equals(rm2)).isFalse();
    }

    @Test
    @DisplayName("textが同じ場合はtrueを返すこと")
    void shouldReturnTrueWhenTextsAreSame() {
      ResultMessage rm1 = ResultMessage.fromText("sameText");
      ResultMessage rm2 = ResultMessage.fromText("sameText");
      assertThat(rm1.equals(rm2)).isTrue();
    }

  }



}
