package undecided.erp.common.primitive;

import static undecided.erp.common.primitive.Objects2.isNull;

import com.google.common.collect.ImmutableSet;
import jakarta.validation.constraints.NotNull;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import undecided.erp.common.annotation.CanIgnoreReturnValue;
import undecided.erp.common.annotation.DoNotCall;
import undecided.erp.common.precondition.ObjectPrecondition;

@UtilityClass
public class Sets2 {

  /**
   * 与えられたセットが空であるかどうかをチェックします。
   *
   * @param <T> セット内の要素の型
   * @param set 空であるかどうかをチェックするセット（nullであってはなりません）
   * @return セットが空の場合はtrue、それ以外の場合はfalse
   */
  public static <T> boolean isEmpty(@NonNull Set<T> set) {
    return set.isEmpty();
  }

  /**
   * 指定されたセットが空であるかどうかを静かに確認します。
   *
   * @param <T> セット内の要素のタイプ
   * @param set 空であるかどうかを確認するセット（nullであってはならない）
   * @return セットが空またはnullの場合は{@code true}、それ以外の場合は{@code false}
   */
  public static <T> boolean isEmptySilently(Set<T> set) {
    return (isNull(set) || set.isEmpty());
  }

  /**
   * 与えられたセット内のすべての要素がnullでないかどうかを確認します。
   *
   * @param <E> セット内の要素の型
   * @param set nullでない要素を確認するためのセット（nullであってはならない）
   * @return セット内のすべての要素がnullでなければ {@code true}、そうでなければ {@code false} を返します
   */
  public static <E> boolean isAllElementsNotNull(@NonNull Set<E> set) {
    for (E e : set) {
      if (isNull(e)) {
        return false;
      }
    }
    return true;
  }

  /**
   * 指定された{@link Set}から要素の逐次的な {@link Stream} を返します。
   *
   * @param <T> セット内の要素の型
   * @param set セット作成のストリーム（nullであってはならない）
   * @return 指定されたセットからの要素の逐次的なストリーム
   */
  public static <T> Stream<@NonNull T> stream(@NonNull Set<T> set) {
    return set.stream();
  }

  /**
   * 2つのセットの共通部分（交差）を返します。
   *
   * @param <E> セット内の要素の型
   * @param set1 最初のセット（nullであってはならない）
   * @param set2 二番目のセット（nullであってはならない）
   * @return set1とset2の共通要素を含む新しいSetView
   */
  public static <E> Set<E> intersection(@NonNull Set<E> set1, @NonNull Set<E> set2) {
    return new SetView<>() {
      @Override
      @NotNull
      public UnmodifiableIterator<E> iterator() {
        return new AbstractIterator<>() {
          final Iterator<E> itr = set1.iterator();

          @Override
          protected E computeNext() {
            while (itr.hasNext()) {
              E e = itr.next();
              if (set2.contains(e)) {
                return e;
              }
            }
            return endOfData();
          }
        };
      }
    };
  }

  /**
   * 指定された列挙型に対する指定されたコレクションの補集合を返します。
   *
   * @param <E> セット内の要素の型
   * @param collection 補集合を検索するコレクション（nullであってはならない）
   * @param type 列挙型
   * @return コレクションの補集合
   */
  public static <E extends Enum<E>> EnumSet<E> complementOf(
      @NonNull Collection<E> collection, Class<E> type) {
    return (collection instanceof EnumSet)
        ? EnumSet.complementOf((EnumSet<E>) collection)
        : makeComplementByHand(collection, type);
  }

  /**
   * 指定された列挙型に対する与えられたコレクションの補集合を生成します。
   *
   * @param <E> コレクション内と列挙型内での要素の型
   * @param collection 補集合を生成するためのコレクション（nullであってはならない）
   * @param type 列挙型
   * @return コレクションの補集合を表す{@link EnumSet}
   */
  private static <E extends Enum<E>> EnumSet<E> makeComplementByHand(
      Collection<E> collection, Class<E> type) {
    EnumSet<E> result = EnumSet.allOf(type);
    result.removeAll(collection);
    return result;
  }

  /**
   * 2つのセット間の差を返す。
   *
   * <p><ui>
   * <li>set1に存在しset2に存在しない要素を返す。
   * <li>set2に存在しset1に存在しない要素は返さない。 </ui>
   *
   * @param <E> セット内の要素の型
   * @param set1 最初のセット（nullであってはならない）
   * @param set2 二番目のセット（nullであってはならない）
   * @return set1とset2間の差を表す新しいSetView
   */
  public static <E> SetView<E> difference(@NonNull final Set<E> set1, @NonNull final Set<?> set2) {
    ObjectPrecondition.checkNotNull(set1, () -> new IllegalArgumentException("set1 is null"));
    ObjectPrecondition.checkNotNull(set2, () -> new IllegalArgumentException("set2 is null"));

    return new SetView<>() {
      @Override
      @NotNull
      public UnmodifiableIterator<E> iterator() {
        return new AbstractIterator<>() {
          final Iterator<E> itr = set1.iterator();

          @Override
          protected E computeNext() {
            while (itr.hasNext()) {
              E e = itr.next();
              if (!set2.contains(e)) {
                return e;
              }
            }
            return endOfData();
          }
        };
      }

      @Override
      @NotNull
      public Stream<E> stream() {
        return set1.stream().filter(e -> !set2.contains(e));
      }

      @Override
      @NotNull
      public Stream<E> parallelStream() {
        return set1.parallelStream().filter(e -> !set2.contains(e));
      }

      @Override
      public int size() {
        int size = 0;
        for (E e : set1) {
          if (!set2.contains(e)) {
            size++;
          }
        }
        return size;
      }

      @Override
      public boolean isEmpty() {
        return set2.containsAll(set1);
      }

      @Override
      public boolean contains(Object element) {
        return set1.contains(element) && !set2.contains(element);
      }
    };
  }

  /**
   * SetViewクラスは、セットを操作するための種々の操作を提供するセットのビューを表します。
   *
   * <p>これはAbstractSetクラスを拡張し、セットでの作業用のユーティリティメソッドを提供します。
   *
   * @param <E> セット内の要素の型
   */
  public abstract static class SetView<E> extends AbstractSet<E> {

    private SetView() {}

    /**
     * このSetViewの不変コピーを作成します。
     *
     * @return このSetViewの要素を含む不変のセット
     */
    @SuppressWarnings("nullness") // Unsafe, but we can't fix it now.
    public ImmutableSet<E> immutableCopy() {
      return ImmutableSet.copyOf(this);
    }

    /**
     * このSetViewの要素を指定されたセットにコピーします。
     *
     * @param <S> コピー先のセットの型
     * @param set 要素をコピーする先のセット
     * @return 要素がコピーされた後の入力セット
     */
    @CanIgnoreReturnValue
    public <S extends Set<E>> S copyInto(S set) {
      set.addAll(this);
      return set;
    }

    @CanIgnoreReturnValue
    @Deprecated
    @Override
    @DoNotCall("Always throws UnsupportedOperationException")
    public final boolean add(E e) {
      throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    @Deprecated
    @Override
    @DoNotCall("Always throws UnsupportedOperationException")
    public final boolean remove(@NonNull Object object) {
      throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    @Deprecated
    @Override
    @DoNotCall("Always throws UnsupportedOperationException")
    public final boolean addAll(Collection<? extends E> newElements) {
      throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    @Deprecated
    @Override
    @DoNotCall("Always throws UnsupportedOperationException")
    public final boolean removeAll(Collection<?> oldElements) {
      throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    @Deprecated
    @Override
    @DoNotCall("Always throws UnsupportedOperationException")
    public final boolean removeIf(java.util.function.@NonNull Predicate<? super E> filter) {
      throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    @Deprecated
    @Override
    @DoNotCall("Always throws UnsupportedOperationException")
    public final boolean retainAll(Collection<?> elementsToKeep) {
      throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    @DoNotCall("Always throws UnsupportedOperationException")
    public final void clear() {
      throw new UnsupportedOperationException();
    }

    /**
     * このSetViewの要素を反復処理するための変更不可能なイテレータを返します。
     *
     * @return このSetViewの要素を反復処理するための変更不可能なイテレータ
     */
    @Override
    public abstract UnmodifiableIterator<E> iterator();

    /**
     * このSetViewに含まれる要素の数を返します。 デフォルトの実装では常に0を返します。
     *
     * @return このSetViewに含まれる要素の数
     */
    @Override
    public int size() {
      return 0;
    }
  }
}
