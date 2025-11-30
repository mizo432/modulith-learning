package undecided.erp.common.exception;

import undecided.erp.common.message.ResultMessages;

/**
 * NotFoundBusinessException クラスは、指定されたリソースが見つからない場合にスローされる業務例外です。
 * 業務ロジックにおいて、検索結果が存在しない場合に適切なエラー通知を行うために使用されます。
 *
 * <p>この例外は、{@link BusinessException} を拡張しており、特定のメッセージコードと引数を使用して例外を構成します。
 */
public class NotFoundBusinessException extends BusinessException {
  private static final long serialVersionUID = 1L;
  private static final String MESSAGE_CODE = "error.not.found";

  /**
   * 指定されたリソースが見つからない場合にスローされる例外を初期化します。
   *
   * @param resourceName リソースの名前
   * @param searchColumnName 検索に使用されたカラム名
   * @param searchKey 検索キーの値
   */
  public NotFoundBusinessException(String resourceName, String searchColumnName, String searchKey) {
    super(ResultMessages.info().add(MESSAGE_CODE, resourceName, searchColumnName, searchKey));
  }
}
