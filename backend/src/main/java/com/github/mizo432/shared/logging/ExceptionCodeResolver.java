package com.github.mizo432.shared.logging;

import com.github.mizo432.shared.exception.ExceptionCodeProvider;

/**
 * 例外から例外コードを解決するためのインターフェースです。
 *
 * <p>このインターフェースを実装することで、指定された例外から一意の例外コードを
 * 取得するロジックを提供します。例外コードはログ出力やエラーハンドリングにおいて
 * 例外の種類を識別するために使用されます。
 *
 * <p>{@link ExceptionCodeProvider} を実装した例外クラスからコードを取得したり、
 * 例外のマッピングに基づいてコードを解決したりするために使用されます。
 *
 * @see ExceptionCodeProvider
 * @see SimpleMappingExceptionCodeResolver
 */
public interface ExceptionCodeResolver {

  /**
   * 指定された例外から例外コードを解決します。
   *
   * @param exception 解決対象の例外
   * @return 解決された例外コード。解決できない場合は {@code null} を返す可能性があります。
   */
  String resolveExceptionCode(Exception exception);
}
