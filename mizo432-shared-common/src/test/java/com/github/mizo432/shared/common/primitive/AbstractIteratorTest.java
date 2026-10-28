package com.github.mizo432.shared.common.primitive;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("AbstractIteratorのテスト")
class AbstractIteratorTest {

  @Nested
  @DisplayName("基本的なイテレーションのテスト")
  class BasicIterationTests {

    @Test
    @DisplayName("要素を順番に返す")
    void shouldReturnElementsInOrder() {
      AbstractIterator<Integer> iterator =
          new AbstractIterator<Integer>() {
            int index = 0;

            @Override
            protected Integer computeNext() {
              if (index >= 3) {
                return endOfData();
              }
              return index++;
            }
          };

      assertThat(iterator.hasNext()).isTrue();
      assertThat(iterator.next()).isEqualTo(0);
      assertThat(iterator.hasNext()).isTrue();
      assertThat(iterator.next()).isEqualTo(1);
      assertThat(iterator.hasNext()).isTrue();
      assertThat(iterator.next()).isEqualTo(2);
      assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    @DisplayName("空のイテレーターの場合hasNextはfalseを返す")
    void shouldReturnFalseForEmptyIterator() {
      AbstractIterator<Integer> iterator =
          new AbstractIterator<Integer>() {
            @Override
            protected Integer computeNext() {
              return endOfData();
            }
          };

      assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    @DisplayName("データ終了後にnextを呼び出すとNoSuchElementExceptionをスローする")
    void shouldThrowExceptionWhenNextAfterEnd() {
      AbstractIterator<Integer> iterator =
          new AbstractIterator<Integer>() {
            @Override
            protected Integer computeNext() {
              return endOfData();
            }
          };

      assertThat(iterator.hasNext()).isFalse();
      assertThatThrownBy(iterator::next).isInstanceOf(NoSuchElementException.class);
    }
  }

  @Nested
  @DisplayName("peekメソッドのテスト")
  class PeekTests {

    @Test
    @DisplayName("peekは次の要素を返すがイテレーターを進めない")
    void shouldPeekWithoutAdvancing() {
      AbstractIterator<Integer> iterator =
          new AbstractIterator<Integer>() {
            int index = 0;

            @Override
            protected Integer computeNext() {
              if (index >= 2) {
                return endOfData();
              }
              return index++;
            }
          };

      assertThat(iterator.peek()).isEqualTo(0);
      assertThat(iterator.peek()).isEqualTo(0);
      assertThat(iterator.next()).isEqualTo(0);
    }

    @Test
    @DisplayName("空のイテレーターでpeekを呼び出すとNoSuchElementExceptionをスローする")
    void shouldThrowExceptionWhenPeekEmpty() {
      AbstractIterator<Integer> iterator =
          new AbstractIterator<Integer>() {
            @Override
            protected Integer computeNext() {
              return endOfData();
            }
          };

      assertThatThrownBy(iterator::peek).isInstanceOf(NoSuchElementException.class);
    }
  }
}
