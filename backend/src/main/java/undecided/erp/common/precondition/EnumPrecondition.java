package undecided.erp.common.precondition;

import static undecided.shared.common.primitiveOld.Objects2.isNull;

import java.util.EnumSet;
import java.util.function.Supplier;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * 列挙型に関する事前条件を検証するためのユーティリティクラス。
 *
 * <p>このクラスは、指定されたEnumSetや列挙型に基づいた入力検証を行うための静的メソッドを提供します。
 * これらのメソッドは、入力された条件に基づき、例外をスローすることで不正な値を防ぎます。
 *
 * <p>業務的な観点での条件チェックではなく、主にメソッドレベルでの入力値検証を目的としています。
 */
@UtilityClass
public class EnumPrecondition {

  /**
   * 指定されたEnumSetに与えられた列挙型の定数が含まれているかどうかを確認します。含まれていない場合、サプライヤーによって指定された例外がスローされます。
   *
   * @param ref 確認する列挙型の定数
   * @param enumSet 確認するEnumSet
   * @param supplier 列挙型の定数が含まれていない場合にスローする例外を提供するサプライヤー
   * @param <E> 列挙型のタイプ
   * @return EnumSetに列挙型の定数が含まれている場合、同じ列挙型の定数
   * @throws RuntimeException 列挙型の定数がEnumSetに含まれていない場合
   */
  public static <E extends Enum<E>> E checkContains(
      E ref, @NonNull EnumSet<E> enumSet, @NonNull Supplier<? extends RuntimeException> supplier) {
    if (!enumSet.contains(ref)) {
      throw supplier.get();
    }
    return ref;
  }

  /**
   * 指定された列挙型の定数が与えられたEnumSetに含まれていないことを確認します。
   *
   * <p>もし含まれている場合、指定された例外がスローされます。
   *
   * @param ref 列挙型の定数の参照
   * @param enumSet 確認するEnumSet
   * @param supplier 列挙型の定数が含まれている場合にスローする例外を提供するサプライヤー
   * @param <EnumType> 列挙型のタイプ
   * @param <ExceptionType> 例外のタイプ
   * @return EnumSetに列挙型の定数が含まれていない場合、同じ列挙型の定数
   * @throws RuntimeException 列挙型の定数がEnumSetに含まれている場合
   */
  public static <EnumType extends Enum<EnumType>, ExceptionType extends RuntimeException>
      EnumType checkNotContains(
          EnumType ref,
          @NonNull EnumSet<EnumType> enumSet,
          @NonNull Supplier<ExceptionType> supplier) {
    if (isNull(ref)) {
      return null;
    }
    if (enumSet.contains(ref)) {
      throw supplier.get();
    }
    return ref;
  }

  /**
   * テスト値が基準値と等しくないことを検証します。
   *
   * <p>これらが等しい場合、例外がスローされます。
   *
   * @param referenceValue 比較のための基準値
   * @param testValue 比較のためのテスト値
   * @param exceptionSupplier 値が等しい場合にスローされる例外を提供するサプライヤー
   * @param <EnumType> Enumの型
   * @param <ExceptionType> スローされる例外の型
   * @return テスト値が基準値と等しくない場合の基準値
   * @throws ExceptionType テスト値が基準値と等しい場合
   */
  public static <EnumType extends Enum<EnumType>, ExceptionType extends RuntimeException>
      EnumType checkNotEqual(
          EnumType referenceValue,
          @NonNull EnumType testValue,
          @NonNull Supplier<ExceptionType> exceptionSupplier) {
    if (testValue == referenceValue) {
      throw exceptionSupplier.get();
    }
    return referenceValue;
  }
}
