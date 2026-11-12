package com.github.mizo432.shared.logging;

import static org.springframework.util.StringUtils.hasText;

import com.github.mizo432.shared.exception.ExceptionLevel;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * 例外のレベルを解決するデフォルトの実装クラス。
 *
 * <p>このクラスは {@link ExceptionLevelResolver} インターフェースを実装しており、 指定された例外から適切な例外レベルを判定して返します。
 * 必要に応じて、{@link ExceptionCodeResolver} を利用して例外コードを解決できます。
 */
public class DefaultExceptionLevelResolver implements ExceptionLevelResolver {

  private @Nullable ExceptionCodeResolver exceptionCodeResolver;

  /**
   * デフォルトの例外レベルを解決するためのクラスのデフォルトコンストラクタ。
   *
   * <p>このコンストラクタは、特定の {@link ExceptionCodeResolver} を使用せずにインスタンスを生成します。 例外の分類に必要なコードの解決機能を持たせる場合は、
   * パラメータ付きコンストラクタを使用してください。
   */
  public DefaultExceptionLevelResolver() {}

  /**
   * {@code DefaultExceptionLevelResolver} クラスのコンストラクタ。
   *
   * <p>指定された {@code ExceptionCodeResolver} を使用して例外コードを解決します。
   *
   * @param exceptionCodeResolver 例外コードを解決するための {@code ExceptionCodeResolver} の実装
   */
  public DefaultExceptionLevelResolver(ExceptionCodeResolver exceptionCodeResolver) {
    this.exceptionCodeResolver = exceptionCodeResolver;
  }

  /**
   * 指定された例外オブジェクトから例外レベルを解決します。
   *
   * @param ex 判定対象の例外オブジェクト
   * @return 解決された例外レベル
   */
  @Override
  public @NonNull ExceptionLevel resolveExceptionLevel(@NonNull Exception ex) {
    String exceptionCode = this.resolveExceptionCode(ex);
    if (!hasText(exceptionCode)) {
      return ExceptionLevel.ERROR;
    }
    
    char prefix = Character.toLowerCase(exceptionCode.charAt(0));
    return switch (prefix) {
      case 'e' -> ExceptionLevel.ERROR;
      case 'w' -> ExceptionLevel.WARN;
      case 'i' -> ExceptionLevel.INFO;
      default -> ExceptionLevel.ERROR;
    };
  }

  /**
   * 指定された例外オブジェクトから例外コードを解決します。
   *
   * @param ex 解決対象の例外オブジェクト
   * @return 解決された例外コード。{@code null} が返される場合もあります。
   */
  protected @Nullable String resolveExceptionCode(@NonNull Exception ex) {
    return this.exceptionCodeResolver != null
        ? this.exceptionCodeResolver.resolveExceptionCode(ex)
        : null;
  }
}
