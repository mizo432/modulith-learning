package undecided.shared.exception;

/**
 * I/O 操作に関連するランタイム例外です。
 *
 * <p>ファイル入出力、ネットワーク通信、データベースアクセスなどの I/O 操作中に発生した
 * エラーを {@link SystemException} としてラップするために使用されます。
 *
 * <p>チェック例外である {@code IOException} をランタイム例外として扱うことで、
 * 例外伝播の負担を軽減し、適切なレイヤーで一括処理できるようにします。
 */
public class IORuntimeException extends SystemException {
  /**
   * I/O 操作に関連するランタイム例外を生成します。
   *
   * @param code 例外コード
   * @param message 例外メッセージ
   * @param cause 根本原因となった例外
   */
  public IORuntimeException(String code, String message, Throwable cause) {
    super(code, message, cause);
  }
}
