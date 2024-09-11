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
   * Constructor
   * <p>
   * {@link ExceptionCodeProvider}, message to be displayed and underlying cause of exception can be
   * specified.
   * </p>
   *
   * @param code ExceptionCode {@link ExceptionCodeProvider}
   * @param message message to be displayed
   * @param cause underlying cause of exception
   */
  public SystemException(String code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
  }

  /**
   * Constructor<br>
   * <p>
   * {@link ExceptionCodeProvider}, message to be displayed can be specified.
   * </p>
   *
   * @param code ExceptionCode {@link ExceptionCodeProvider}
   * @param message message to be displayed
   */
  public SystemException(String code, String message) {
    super(message);
    this.code = code;
  }

  /**
   * Constructor<br>
   * <p>
   * {@link ExceptionCodeProvider} and underlying cause of exception can be specified.
   * </p>
   *
   * @param code ExceptionCode {@link ExceptionCodeProvider}
   * @param cause underlying cause of exception
   */
  public SystemException(String code, Throwable cause) {
    super(cause);
    this.code = code;
  }

  @Override
  public String getCode() {
    return code;
  }

}
