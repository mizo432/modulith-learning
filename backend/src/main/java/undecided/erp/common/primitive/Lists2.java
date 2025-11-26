package undecided.erp.common.primitive;

import static undecided.erp.common.precondition.IntegerPrecondition.checkNonNegative;
import static undecided.erp.common.precondition.ObjectPrecondition.checkNotNull;
import static undecided.erp.common.primitive.Objects2.isNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;

/**
 * Lists2クラスはリストに関するユーティリティメソッドを提供します。
 *
 * <p>このクラスはインスタンス化できないユーティリティクラスとして設計されています。
 */
@UtilityClass
public class Lists2 {

  /**
   * 指定されたリストが空であるかどうかを判定します。
   *
   * @param <T> リスト内の要素の型
   * @param list 判定対象のリスト（nullではないことを前提とします）
   * @return リストが空の場合は{@code true}、それ以外の場合は{@code false}
   */
  public static <T> boolean isEmpty(@NonNull List<T> list) {
    return list.isEmpty();
  }

  /**
   * 指定されたリストが空であるかどうかを、例外をスローすることなく静かに判定します。
   *
   * @param <T> リスト内の要素の型
   * @param list 判定対象のリスト。nullである場合も考慮されます
   * @return リストがnullまたは空の場合は{@code true}、それ以外の場合は{@code false}
   */
  public static <T> boolean isEmptySilently(List<T> list) {
    return (isNull(list) || list.isEmpty());
  }

  /**
   * 指定されたリストの全ての要素がnullではないかを判定します。
   *
   * @param <E> リスト内の要素の型
   * @param list nullではないかを確認するリスト（nullではないことを前提とします）
   * @return リスト内の全ての要素がnullではない場合は{@code true}、1つでもnullの要素がある場合は{@code false}
   */
  public static <E> boolean isAllElementsNotNull(@NonNull List<E> list) {
    for (E e : list) {
      if (isNull(e)) {
        return false;
      }
    }
    return true;
  }

  /**
   * 指定されたリストを元に非null要素の{@link Stream}を生成して返します。
   *
   * @param <T> リスト内の要素の型
   * @param list {@link Stream}を生成する元となるリスト（nullではないことを前提とします）
   * @return 指定されたリストをストリーム化した結果の{@link Stream}
   */
  public static <T> Stream<@NonNull T> stream(@NonNull List<T> list) {
    return list.stream();
  }

  public static <E> ArrayList<E> newArrayList() {
    return new ArrayList<>();
  }

  /**
   * 渡された可変長引数を元に、新しいArrayListを作成します。
   *
   * @param <E> ArrayListの要素の型
   * @param elements 可変長引数として渡されるArrayListに追加する要素の配列
   * @return 指定された要素を含む新しいArrayList
   * @throws IllegalArgumentException elementsがnullの場合
   */
  @SafeVarargs
  public static <E> ArrayList<E> newArrayList(E... elements) {
    checkNotNull(elements, () -> new IllegalArgumentException("elements is null"));
    int capacity = computeArrayListCapacity(elements.length);
    ArrayList<E> list = new ArrayList<>(capacity);
    Collections.addAll(list, elements);
    return list;
  }

  /**
   * 指定された要素を元に新しいArrayListを作成して返します。
   *
   * @param <E> ArrayList内の要素の型
   * @param elements 追加する要素を含むIterableインスタンス。nullであってはなりません。
   * @return 指定された要素を含む新しいArrayList
   * @throws IllegalArgumentException elementsがnullの場合
   */
  public static <E> ArrayList<E> newArrayList(Iterable<? extends E> elements) {
    checkNotNull(elements, () -> new IllegalArgumentException("elements is null")); // for GWT
    return (elements instanceof Collection)
        ? new ArrayList<>((Collection<? extends E>) elements)
        : newArrayList(elements.iterator());
  }

  /**
   * 指定されたイテレーターの要素を元に新しいArrayListを作成して返します。
   *
   * @param <E> ArrayListの要素の型
   * @param elements 要素を提供するイテレーター。nullであってはなりません。
   * @return 指定された要素を含む新しいArrayList
   * @throws IllegalArgumentException elementsがnullの場合
   */
  public static <E> ArrayList<E> newArrayList(Iterator<? extends E> elements) {
    ArrayList<E> list = newArrayList();
    Iterators.addAll(list, elements);
    return list;
  }

  /**
   * 指定された配列サイズに基づき、適切なArrayListの初期容量を計算します。
   *
   * @param arraySize 配列サイズ（0以上である必要があります）
   * @return 計算されたArrayListの初期容量
   * @throws IllegalArgumentException arraySizeが負の場合にスローされます
   */
  private static int computeArrayListCapacity(int arraySize) {
    checkNonNegative(
        arraySize, () -> new IllegalArgumentException("arraySize must positive or zero."));

    return Ints.saturatedCast(5L + arraySize + (arraySize / 10));
  }

  /**
   * 指定された初期容量を持つ新しいArrayListを作成します。
   *
   * @param <E> ArrayList内の要素の型
   * @param initialArraySize 作成するArrayListの初期容量（0以上である必要があります）
   * @return 指定された初期容量を持つ新しいArrayList
   * @throws IllegalArgumentException initialArraySizeが負の値である場合
   */
  public static <E> ArrayList<E> newArrayListWithCapacity(int initialArraySize) {
    checkNonNegative(
        initialArraySize,
        () -> new IllegalArgumentException("initialArraySize must positive or zero"));
    return new ArrayList<>(initialArraySize);
  }

  public static <E> E getLast(@NonNull List<E> list) {
    checkNotNull(list, () -> new IllegalArgumentException("list must not be null."));
    if (list.isEmpty()) {
      return null;
    } else {
      if (isEmpty(list)) return null;
      return list.getLast();
    }
  }
}
