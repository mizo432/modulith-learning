package undecided.shared.common.exception;

/**
 * 例外のレベルを解決するためのインタフェース。
 *
 * <p>このインタフェースを実装することで、指定された例外に応じて適切な例外のレベルを判定し、 {@link ExceptionLevel} を返す機能を提供します。
 *
 * <p>実装クラスは、カスタムのルールに基づいて例外レベルを解決することが可能です。
 */
public interface ExceptionLevelResolver {

  /**
   * 指定された例外に基づいて例外のレベルを解決します。
   *
   * @param exception 解決の対象となる例外
   * @return 解決された例外のレベルを表す {@link ExceptionLevel}
   */
  ExceptionLevel resolveExceptionLevel(Exception exception);
}
