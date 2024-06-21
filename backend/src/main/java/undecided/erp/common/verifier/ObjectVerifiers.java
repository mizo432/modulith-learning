package undecided.erp.common.verifier;

import java.util.function.Supplier;
import lombok.NonNull;

/**
 * ObjectVerifiersクラスはオブジェクトの状態や引数を検証するためのユーティリティメソッドを提供します。
 */
public class ObjectVerifiers {

  /**
   * 指定されたオブジェクトの参照がnullでないことを確認します。もし参照がnullならば、 supplierから提供される例外がスローされます。
   *
   * @param refer 検証するためのオブジェクト参照
   * @param supplier 参照がnullだった場合にスローされる例外の供給者
   * @param <T> オブジェクト参照の型
   * @throws RuntimeException 参照がnullの場合
   */
  public static <T> T verifyNotNull(T refer,
      @NonNull Supplier<? extends RuntimeException> supplier) {
    if (refer == null) {
      throw supplier.get();
    }
    return refer;
  }

  /**
   * 指定したオブジェクト参照がnullでないことを確認します.
   *
   * @param refer 確認するオブジェクト参照
   * @param label 例外メッセージで使用されるラベル
   * @param <T> オブジェクト参照のタイプ
   * @throws NullPointerException 参照がnullの場合
   */
  public static <T> T verifyNotNull(T refer, @NonNull String label) {
    if (refer == null) {
      throw new NullPointerException(String.format("%s がnullです。", label));
    }
    return refer;
  }

  /**
   * ブール表現を検証し、その表現が偽であればランタイム例外をスローします。
   *
   * @param expression 検証するブール表現
   * @param supplier 表現が偽であった場合に投げられる例外を提供するサプライヤ
   * @throws RuntimeException 表現が偽であった場合
   */
  public static void verifyArgument(boolean expression,
      @NonNull Supplier<? extends RuntimeException> supplier) {
    if (!expression) {
      throw supplier.get();
    }
  }

  /**
   * 論理式を検証し、その論理式が偽であれば例外を投げます
   *
   * @param expression 検証する論理式
   * @param label 例外メッセージで使用されるラベル
   * @throws IllegalArgumentException 論理式が偽である場合
   */
  public static void verifyArgument(boolean expression,
      @NonNull String label) {
    if (!expression) {
      throw new IllegalArgumentException(String.format("引数: %s が不正です。", label));
    }
  }

  /**
   * 指定されたブール型の式を評価し、その結果がfalseであった場合にはランタイム例外をスローするメソッドです。
   *
   * @param expression 検証するブール表現
   * @param supplier 式が偽であった場合にスローされる例外を提供するサプライヤ
   * @throws RuntimeException 式の評価結果が偽であった場合にスローされます
   */
  public static void verifyState(boolean expression,
      @NonNull Supplier<? extends RuntimeException> supplier) {
    if (!expression) {
      throw supplier.get();
    }
  }

  /**
   * boolean式に基づいてオブジェクトの状態を検証します。
   * <p>
   * もし式がfalseの場合、エラーメッセージを含むIllegalStateExceptionがスローされます。
   *
   * @param expression 検証するためのboolean式
   * @param label エラーメッセージで使用するラベル
   * @throws IllegalStateException 式がfalseと評価された場合
   */
  public static <T> void verifyState(boolean expression,
      @NonNull String label) {
    if (!expression) {
      throw new IllegalStateException(String.format("%s の状態が不正です。", label));
    }
  }

}
