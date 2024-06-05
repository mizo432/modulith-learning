package undecided.erp.common.primitive;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.NonNull;

public class ConditionalExecutor {

  /**
   * 与えられた述語の結果に基づいて指定された消費者関数を実行します。
   *
   * @param testingObject 評価述語と消費者関数で使用するオブジェクト
   * @param testingPredicate オブジェクトをテストするための述語
   * @param truthyConsumer 述語がtrueを返した場合に実行される消費者関数
   * @param falsyConsumer 述語がfalseを返した場合に実行される消費者関数
   * @param <T> テストおよび消費の対象となる入力オブジェクトのタイプ
   */
  public static <T> void executeBasedOnPredicate(T testingObject,
      @NonNull Predicate<T> testingPredicate,
      @NonNull Consumer<T> truthyConsumer,
      @NonNull Consumer<T> falsyConsumer) {
    if (testingPredicate.test(testingObject)) {
      truthyConsumer.accept(testingObject);
    } else {
      falsyConsumer.accept(testingObject);
    }

  }

  /**
   * Executes the specified consumer function based on the evaluation of the given predicate.
   *
   * @param testingObject the object to test and be passed as an argument to the consumer function
   * @param testingPredicate the predicate used to test the object
   * @param truthyConsumer the consumer function to execute if the predicate returns true
   * @param <T> the type of the input object to test and consume
   */
  public static <T> void executeBasedOnPredicate(T testingObject,
      @NonNull Predicate<T> testingPredicate,
      @NonNull Consumer<T> truthyConsumer) {
    if (testingPredicate.test(testingObject)) {
      truthyConsumer.accept(testingObject);
    }

  }

  /**
   * 指定されたオブジェクトがpredicateによって定義されたテストに合格した場合は指定されたtrueFunctionを実行し、
   * 合格しない場合は指定されたfalseFunctionを実行します。
   *
   * @param <T> 入力オブジェクトの型
   * @param <ReturnValue> 戻り値の型
   * @param testingObject テストされるオブジェクト
   * @param testingPredicate オブジェクトをテストするために使用される述語
   * @param truthyFunction 述語がtrueを返した場合に実行される関数
   * @param falsyFunction 述語がfalseを返した場合に実行される関数
   * @return 実行した関数の結果
   */
  public static <T, ReturnValue> ReturnValue executeAndReturnBasedOnPredicate(T testingObject,
      @NonNull Predicate<T> testingPredicate,
      @NonNull Function<T, ReturnValue> truthyFunction,
      @NonNull Function<T, ReturnValue> falsyFunction) {
    if (testingPredicate.test(testingObject)) {
      return truthyFunction.apply(testingObject);
    } else {
      return falsyFunction.apply(testingObject);
    }

  }

  /**
   * 指定されたコンシュマーを、与えられた式に基づいて実行します。
   *
   * @param testingObject 消費者関数に引数として渡されるオブジェクト
   * @param expression 評価される式
   * @param truthyConsumer 式が真である場合に実行されるコンシュマー
   * @param falsyConsumer 式が偽である場合に実行されるコンシュマー
   * @param <T> 消費者関数に渡されるオブジェクトの型
   */
  public static <T> void executeBasedOnExpression(T testingObject, boolean expression,
      @NonNull Consumer<T> truthyConsumer,
      @NonNull Consumer<T> falsyConsumer) {
    if (expression) {
      truthyConsumer.accept(testingObject);
    } else {
      falsyConsumer.accept(testingObject);
    }

  }

  public static <T> void executeConsumerIfExpressionTrue(T testingObject, boolean expression,
      @NonNull Consumer<T> truthyConsumer) {
    if (expression) {
      truthyConsumer.accept(testingObject);
    }

  }

  /**
   * 指定された関数を、与えられた式に基づいて実行します。
   *
   * @param testingObject 消費者関数への引数として渡されるオブジェクト。
   * @param expression 評価される式。
   * @param truthyFunction 式が真である場合に実行される関数。
   * @param falsyFunction 式が偽である場合に実行される関数。
   * @param <T> 消費者関数に渡されるオブジェクトの型。
   */
  public static <T, ReturnValue> ReturnValue executeAndReturnBasedOnExpression(T testingObject,
      boolean expression, @NonNull Function<T, ReturnValue> truthyFunction,
      @NonNull Function<T, ReturnValue> falsyFunction) {
    if (expression) {
      return truthyFunction.apply(testingObject);
    } else {
      return falsyFunction.apply(testingObject);
    }

  }
}
