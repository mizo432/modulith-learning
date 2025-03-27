package undecided.erp.common.exception;

/**
 * 例外のレベルを定義する列挙型。 それぞれの値は、例外の重大度を表します。
 * <p>
 * INFO: 情報として扱う例外。 WARN: 警告を示す例外。 ERROR: エラーを示す例外。 UNKNOWN: 不明なレベルの例外。
 */
public enum ExceptionLevel {
  INFO, WARN, ERROR, UNKNOWN
}
