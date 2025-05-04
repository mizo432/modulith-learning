package undecided.erp.common.exception;

/**
 * このクラスは RuntimeException クラスのサブクラスであり、ExceptionCodeProvider インターフェースを実装する システム例外を表します。
 * <p>
 * SystemException クラスには3つのコンストラクタがあり、それぞれ例外コード、表示されるメッセージ、および 例外の根本的な原因を指定することができます。code パラメータは
 * ExceptionCodeProvider 型であり、 ExceptionCodeProvider インターフェースの実装である必要があります。message パラメータは例外がスロー
 * されたときに表示されるメッセージを表す文字列です。cause パラメータは例外の根本的な原因を表す Throwable オブジェクトです。
 * <p>
 * SystemException クラスは、例外に関連付けられた例外コードを返す ExceptionCodeProvider インターフェース の getCode()
 * メソッドをオーバーライドしています。
 */

public class SystemException extends RuntimeException implements
    ExceptionCodeProvider {

  private static final long serialVersionUID = 1L;

  /**
   * exception code.
   */
  private final String code;

  /**
   * コンストラクタ
   * <p>
   * {@link ExceptionCodeProvider}、表示されるメッセージ、および例外の根本原因を指定できます。
   * </p>
   *
   * @param code 例外コード {@link ExceptionCodeProvider}
   * @param message 表示されるメッセージ
   * @param cause 例外の根本原因
   */
  public SystemException(String code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
  }

  /**
   * コンストラクタ
   * <p>
   * {@link ExceptionCodeProvider} と表示されるメッセージを指定できます。
   * </p>
   *
   * @param code 例外コード {@link ExceptionCodeProvider}
   * @param message 表示されるメッセージ
   */
  public SystemException(String code, String message) {
    super(message);
    this.code = code;
  }

  /**
   * コンストラクタ
   * <p>
   * {@link ExceptionCodeProvider} と例外の原因を指定することができます。
   * </p>
   *
   * @param code 例外コード {@link ExceptionCodeProvider}
   * @param cause 例外の原因となる要素
   */
  public SystemException(String code, Throwable cause) {
    super(cause);
    this.code = code;
  }

  
  /**
   * Retrieves the exception code associated with the system exception.
   *
   * @return the exception code as a String
   */
  @Override
  public String getCode() {
    return code;

  }

}
