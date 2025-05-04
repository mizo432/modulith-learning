package undecided.erp.common.stack;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

/**
 * 単純なスタックデータ構造を表します。
 *
 * <p>
 * このクラスはジェネリッククラスであり、任意の型の要素を格納することができます。 スタックは「後入れ先出し（LIFO: Last-In-First-Out）」の原則に従います。
 * スタックの一番上には最も最近追加された要素があり、一番下には最も古く追加された要素があります。
 * </p>
 *
 * @param <E> スタックに格納される要素の型
 */
public class Stack<E> {

  private final List<E> elements;

  /**
   * 単純なスタックデータ構造を表します。
   *
   * <p>
   * このクラスはジェネリッククラスであり、任意の型の要素を格納することができます。
   * <p>
   * スタックは「後入れ先出し（LIFO: Last-In-First-Out）」の原則に従います。
   * <p>
   * スタックの一番上には最も最近追加された要素があり、一番下には最も古く追加された要素があります。
   * </p>
   */
  public Stack() {
    this.elements = new ArrayList<>();
  }

  /**
   * スタックの一番上に要素を追加します。
   *
   * @param element 追加する要素
   */
  public void push(E element) {
    elements.add(element);
  }

  /**
   * スタックの一番上の要素を削除し、その要素を返します。
   *
   * @return 削除されたスタックの一番上の要素
   * @throws EmptyStackException スタックが空の場合
   */
  public E pop() {
    if (isEmpty()) {
      throw new EmptyStackException();
    }
    return elements.removeLast();
  }

  /**
   * スタックの一番上の要素を返しますが、スタックからは削除しません。
   *
   * @return スタックの一番上の要素
   * @throws EmptyStackException スタックが空の場合
   */
  public E peek() {
    if (isEmpty()) {
      throw new EmptyStackException();
    }
    return elements.getLast();
  }

  /**
   * スタックが空かどうかを確認します。
   *
   * @return スタックが空の場合はtrue、そうでない場合はfalse
   */
  public boolean isEmpty() {
    return elements.isEmpty();
  }

  /**
   * スタック内の要素の数を返します。
   *
   * @return スタック内の要素の数
   */
  public int size() {
    return elements.size();
  }

  @Override
  public String toString() {
    return elements.toString();
  }
}
