package undecided.shared.common.primitiveOld;

import static undecided.shared.common.precondition.IntegerPrecondition.checkNonNegative;
import static undecided.shared.common.primitiveOld.Objects2.isNull;

import com.google.common.primitives.Ints;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Maps2 {

  public static <K, V> boolean isEmpty(@NonNull Map<K, V> map) {
    return map.isEmpty();
  }

  public static <K, V> boolean isEmptySilently(Map<K, V> map) {
    return (isNull(map) || map.isEmpty());
  }

  public static <K, V> boolean isAllElementsNotNull(@NonNull Map<K, V> map) {
    for (V v : map.values()) {
      if (isNull(v)) {
        return false;
      }
    }
    return true;
  }

  public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(int expectedSize) {
    return new HashMap<>(capacity(expectedSize));
  }

  /**
   * データ構造の期待されるサイズに基づいて容量を計算します。
   *
   * @param expectedSize データ構造が保持することを期待される要素の数。 この数値が0未満の場合は、IllegalArgumentExceptionがスローされます。
   * @return 計算された容量。これは、ロードファクターを考慮して期待されるサイズより大きくなるように保証されます。
   *     期待されるサイズが3未満の場合、返される容量は期待されるサイズより少し大きくなります。
   *     大きな期待されるサイズの場合、容量は75％のロードファクターを許可するための係数を使用して計算されます。
   *     期待されるサイズが非常に大きい場合、容量としてInteger.MAX_VALUEが返されます。
   */
  static int capacity(int expectedSize) {
    if (expectedSize < 3) {
      checkNonNegative(
          expectedSize, () -> new IllegalArgumentException("expectedSize must positive or zero."));
      return expectedSize + 1;
    }
    if (expectedSize < Ints.MAX_POWER_OF_TWO) {
      return (int) Math.ceil(expectedSize / 0.75);
    }
    return Integer.MAX_VALUE; // any large value
  }

  /**
   * 新しい空のHashMapを作成します。
   *
   * @param <K> このマップで管理されるキーの種類
   * @param <V> マップされた値の種類
   * @return 新しい空のHashMapのインスタンス
   */
  public static <K, V> HashMap<K, V> newHashMap() {
    return new HashMap<>();
  }

  /**
   * 指定したマップを初期内容とする新しいHashMapを作成します。
   *
   * @param <K> このマップが保持するキーの型
   * @param <V> マッピングされる値の型
   * @param map 初期内容として使用されるマップ
   * @return 新しいHashMapのインスタンス
   */
  public static <K, V> HashMap<K, V> newHashMap(Map<? extends K, ? extends V> map) {
    return new HashMap<>(map);
  }

  /**
   * 初期マッピングがない新しい{@code LinkedHashMap}のインスタンスを作成します。
   *
   * @param <K> このマップが維持するキーの型
   * @param <V> マップされた値の型
   * @return {@code LinkedHashMap}の新しいインスタンス
   */
  public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
    return new LinkedHashMap<>();
  }

  /**
   * 指定されたマップと同じマッピングを持つ{@link LinkedHashMap}の新しいインスタンスを作成します。
   *
   * @param <K> このマップが保持するキーの型
   * @param <V> マッピングされる値の型
   * @param map マッピングを新しいLinkedHashMapにコピーするマップ
   * @return 指定されたマップと同じマッピングを持つLinkedHashMapの新しいインスタンス
   */
  public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(Map<? extends K, ? extends V> map) {
    return new LinkedHashMap<>(map);
  }

  /**
   * 指定した期待サイズの新しい LinkedHashMap を作成します。
   *
   * @param expectedSize LinkedHashMapの期待サイズ
   * @param <K> LinkedHashMap が管理するキーのタイプ
   * @param <V> 　マップされた値のタイプ
   * @return 指定した期待サイズを持つ新しい LinkedHashMap
   */
  public static <K, V> LinkedHashMap<K, V> newLinkedHashMapWithExpectedSize(int expectedSize) {
    return new LinkedHashMap<>(capacity(expectedSize));
  }

  /**
   * 指定したマップの変更不能なビューを返します。
   *
   * <p>変更不能なマップを変更しようとするとUnsupportedOperationExceptionがスローされます。
   *
   * @param <K> マップが保持するキーの型
   * @param <V> マップにマップされた値の型
   * @param map 変更不能なビューが必要なマップ
   * @return 指定したマップの変更不能なビュー
   */
  public static <K, V> Map<K, V> unmodifiableMap(@NonNull Map<K, ? extends V> map) {

    if (map instanceof SortedMap) {
      return Collections.unmodifiableSortedMap((SortedMap<K, ? extends V>) map);
    } else {
      return Collections.unmodifiableMap(map);
    }
  }
}
