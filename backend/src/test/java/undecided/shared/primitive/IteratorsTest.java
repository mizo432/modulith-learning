package undecided.shared.primitive;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Iteratorsのテスト")
class IteratorsTest {

  @Nested
  @DisplayName("addAllメソッドのテスト")
  class AddAllTests {

    @Test
    @DisplayName("イテレーターの要素をコレクションに追加する")
    void shouldAddAllElementsFromIterator() {
      List<Integer> addTo = new ArrayList<>();
      Iterator<Integer> iterator = Arrays.asList(1, 2, 3).iterator();

      boolean result = Iterators.addAll(addTo, iterator);

      assertThat(result).isTrue();
      assertThat(addTo).containsExactly(1, 2, 3);
    }

    @Test
    @DisplayName("空のイテレーターの場合falseを返す")
    void shouldReturnFalseForEmptyIterator() {
      List<Integer> addTo = new ArrayList<>();
      Iterator<Integer> iterator = new ArrayList<Integer>().iterator();

      boolean result = Iterators.addAll(addTo, iterator);

      assertThat(result).isFalse();
      assertThat(addTo).isEmpty();
    }

    @Test
    @DisplayName("既存の要素があるコレクションに追加する")
    void shouldAddToExistingCollection() {
      List<Integer> addTo = new ArrayList<>(List.of(0));
      Iterator<Integer> iterator = Arrays.asList(1, 2).iterator();

      boolean result = Iterators.addAll(addTo, iterator);

      assertThat(result).isTrue();
      assertThat(addTo).containsExactly(0, 1, 2);
    }
  }
}
