package undecided.erp.common.stack;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.EmptyStackException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * テストクラス: {@link Stack}
 */
@DisplayName("Stackクラス")
public class StackTest {

  /**
   * テスト対象メソッド: {@link Stack#peek()}
   */
  @Nested
  @DisplayName("peekメソッド")
  class PeekMethodTest {

    /**
     * テストケース: Stackが空の場合
     */
    @Test
    @DisplayName("shouldスタックが空の場合、EmptyStackExceptionをスローします")
    void shouldThrowEmptyStackExceptionWhenStackIsEmpty() {
      Stack<String> stack = new Stack<>();
      assertThatThrownBy(stack::peek).isInstanceOf(EmptyStackException.class);
    }

    /**
     * テストケース: Stackに要素が含まれている場合
     */
    @Test
    @DisplayName("shouldスタックが空でない場合、最後の要素を返す")
    void shouldReturnLastElementWhenStackIsNotEmpty() {
      Stack<String> stack = new Stack<>();
      String lastElement = "last";
      stack.push("first");
      stack.push(lastElement);

      String peekedElement = stack.peek();

      assertThat(peekedElement).isEqualTo(lastElement);
    }
  }

  /**
   * テスト対象メソッド: pushメソッド
   */
  @Nested
  @DisplayName("pushメソッド")
  class PushMethodTest {

    /**
     * テストケース: Stackが空の場合
     */
    @Test
    @DisplayName("should最後に追加された要素を返す")
    void shouldReturnTheElementThatIsAdded() {
      Stack<String> stack = new Stack<>();
      String addedElement = "added";
      stack.push(addedElement);
      String peekedElement = stack.peek();

      assertThat(peekedElement).isEqualTo(addedElement);
    }

    /**
     * テストケース: Stackが既に要素を持つ
     */
    @Test
    @DisplayName("should最後に追加された要素を返す")
    void shouldReturnTheLastElementThatIsAdded() {
      Stack<String> stack = new Stack<>();
      stack.push("first");
      String lastAddedElement = "last";
      stack.push(lastAddedElement);
      String peekedElement = stack.peek();

      assertThat(peekedElement).isEqualTo(lastAddedElement);
    }
  }

  /**
   * テスト対象メソッド: {@link Stack#pop()}
   */
  @Nested
  @DisplayName("popメソッド")
  class PopMethodTest {

    /**
     * テストケース: Stackが空の場合
     */
    @Test
    @DisplayName("shouldスタックが空の場合、EmptyStackExceptionをスローします")
    void shouldThrowEmptyStackExceptionWhenStackIsEmpty() {
      Stack<String> stack = new Stack<>();
      assertThatThrownBy(stack::pop).isInstanceOf(EmptyStackException.class);
    }

    /**
     * テストケース: Stackに要素が含まれている場合
     */
    @Test
    @DisplayName("shouldスタックが空でない場合、最後の要素を取り出して削除する")
    void shouldPeekAndRemoveLastElementWhenStackIsNotEmpty() {
      Stack<String> stack = new Stack<>();
      String lastElement = "last";
      stack.push("first");
      stack.push(lastElement);

      String popedElement = stack.pop();

      assertThat(popedElement)
          .isEqualTo(lastElement);
      assertThat(stack.size())
          .isEqualTo(1);
    }
  }

  /**
   * テスト対象メソッド: {@link Stack#toString()}
   */
  @Nested
  @DisplayName("toStringメソッド")
  class ToStringMethodTest {

    /**
     * テストケース: Stackが空の場合
     */
    @Test
    @DisplayName("shouldスタックが空の場合、空の配列を返す")
    void shouldReturnEmptyArrayWhenStackIsEmpty() {
      Stack<String> stack = new Stack<>();
      assertThat(stack.toString()).isEqualTo("[]");
    }

    /**
     * テストケース: Stackに要素が含まれている場合
     */
    @Test
    @DisplayName("shouldスタックが空でない場合、要素を含む配列を返す")
    void shouldReturnArrayWithElementsWhenStackIsNotEmpty() {
      Stack<String> stack = new Stack<>();
      stack.push("first");
      stack.push("second");
      assertThat(stack.toString()).isEqualTo("[first, second]");
    }
  }
}
