package undecided.erp.common.precondition;

import java.util.function.Supplier;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * Boolean 値を検証するためのユーティリティクラス。
 * <p>
 * 引数として {@link Boolean} 型を受け取り、値が {@code true} または {@code false} であることを確認し、
 * 必要に応じて例外をスローします。特に、メソッドレベルでの検証や安全性のチェックに適しています。
 *
 * <p>このクラスは、条件付きの検証や防御的プログラミングに役立ちます。
 * 例えば、特定の条件が満たされない場合に例外をスローし、プログラムの安全性を確保します。</p>
 *
 * @see java.util.function.Supplier
 */
@UtilityClass
public class BooleanPrecondition {

  /**
   * 指定された Boolean 参照が {@code true} であるかをチェックします。
   * <p>
   * 参照が {@code null} の場合、{@code null} を返します。 参照が {@code true} の場合、{@code true} を返します。 参照が
   * {@code false} の場合、exceptionSupplierによって提供される例外をスローします。
   *
   * @param ref チェックされるBooleanの参照（null可能）。
   * @param exceptionSupplier {@code ref} が {@code false} の場合にスローされる例外を提供するSupplier（非null）。
   * @return {@code ref} が {@code null} または {@code true} の場合、元のBoolean参照。
   * @throws RuntimeException {@code ref} が {@code false} の場合、exceptionSupplierによって提供される例外をスロー。
   */
  public static Boolean checkTrue(Boolean ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    if (ref == null) {
      return ref;
    }
    if (ref) {
      return ref;
    }
    throw exceptionSupplier.get();
  }

  /**
   * 指定された Boolean 参照が {@code false} であるかをチェックします。
   * <p>
   * 参照が {@code null} の場合、{@code null} を返します。 参照が {@code false} の場合、{@code false} を返します。 参照が
   * {@code true} の場合、exceptionSupplierによって提供される例外をスローします。
   *
   * @param ref チェックされるBooleanの参照（null可能）。
   * @param exceptionSupplier {@code ref} が {@code true} の場合にスローされる例外を提供するSupplier（非null）。
   * @return {@code ref} が {@code null} または {@code false} の場合、元のBoolean参照。
   * @throws RuntimeException {@code ref} が {@code true} の場合、exceptionSupplierによって提供される例外をスロー。
   */
  public static Boolean checkFalse(Boolean ref,
      @NonNull Supplier<? extends RuntimeException> exceptionSupplier) {
    if (ref == null) {
      return ref;
    }
    if (!ref) {
      return ref;
    }
    throw exceptionSupplier.get();
  }


}
