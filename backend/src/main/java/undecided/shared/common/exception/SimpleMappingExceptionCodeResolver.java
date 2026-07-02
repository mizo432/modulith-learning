package undecided.shared.common.exception;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 * SimpleMappingExceptionCodeResolverクラスは、例外に関連付けられたコードを解決するための処理を提供します。
 * <p>
 * このクラスはExceptionCodeResolverインターフェースを実装し、特定の例外に基づいたコードマッピングの解決を行います。
 *
 * <p>特定の例外に関連するコードが明示的に定義されている場合、それを返し、定義がない場合はデフォルトの例外コードを返します。
 * 例外コードは以下の2つの方法で解決されます: - 対象例外がExceptionCodeProviderインターフェースを実装している場合、そのgetCodeメソッドを利用してコードを取得。 -
 * 例外とそのスーパークラスを順次チェックし、定義された例外マッピングに一致する場合、そのコードを返す。
 *
 * <p>このクラスの責務は例外コードの解決に限定されており、具体的な例外のハンドリングは含まれません。
 */
@Setter
public class SimpleMappingExceptionCodeResolver implements ExceptionCodeResolver {

  private static final Logger logger =
      LoggerFactory.getLogger(SimpleMappingExceptionCodeResolver.class);
  private LinkedHashMap<String, String> exceptionMappings;
  private String defaultExceptionCode;

  public SimpleMappingExceptionCodeResolver() {
  }

  /**
   * 指定された例外クラスに関連付けられた例外コードを解決します。
   * <p>
   * このメソッドは以下の順序で例外コードを解決します: 1. 例外が{@link ExceptionCodeProvider}を実装している場合、
   * {@code getCode}メソッドの結果として返されるコードを使用します。 2.
   * クラス階層に基づいて設定されている例外マッピングを確認し、一致する場合はそのマッピングコードを使用します。 3. 上記のいずれにも一致しない場合は、デフォルトの例外コードを返します。
   *
   * @param ex 処理対象の例外。{@code null}の場合、デフォルトの例外コードを返します。
   * @return 解決された例外コード。該当するコードがない場合は、デフォルトの例外コードを返します。
   */
  public String resolveExceptionCode(Exception ex) {
    if (ex == null) {
      logger.warn("target exception is null. return defaultExceptionCode.");
    } else {
      if (ex instanceof ExceptionCodeProvider) {
        String code = ((ExceptionCodeProvider) ex).getCode();
        if (code != null) {

          return code;
        }
      }

      if (!CollectionUtils.isEmpty(this.exceptionMappings)) {
        for (Map.Entry<String, String> entry : this.exceptionMappings.entrySet()) {
          String targetException = entry.getKey();

          for (Class<?> exceptionClass = ex.getClass();
              exceptionClass != Object.class;
              exceptionClass = exceptionClass.getSuperclass()) {
            if (exceptionClass.getName().contains(targetException)) {
              return entry.getValue();
            }
          }
        }
      }
    }
    return this.defaultExceptionCode;
  }
}
