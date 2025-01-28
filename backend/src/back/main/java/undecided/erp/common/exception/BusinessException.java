package undecided.erp.common.exception;

import undecided.erp.common.message.ResultMessage;
import undecided.erp.common.message.ResultMessages;

/**
 * BusinessException クラスは、業務関連のエラーを処理するためのカスタム例外です。
 * <p>
 * このクラスは ResultMessagesNotificationException を拡張しており、 業務ロジックに関する問題の結果メッセージをカプセル化し、通知する機能を提供します。
 * <p>
 * このクラスは、特定のメッセージや例外の原因に基づいてインスタンスを作成するための 複数のコンストラクタを提供します。
 * <p>
 * - 最初のコンストラクタは、単一のメッセージを指定してインスタンスを作成するために使用されます。 このコンストラクタはエラータイプの ResultMessages
 * インスタンスを生成し、指定されたメッセージを追加します。
 * <p>
 * - 2 番目のコンストラクタは、ResultMessages オブジェクトを通じて複数のメッセージを指定して インスタンスを作成するために使用されます。
 * <p>
 * - 3 番目のコンストラクタは、複数のメッセージと例外の原因の両方を指定してインスタンスを作成するために使用され、 詳細な例外処理のアプローチを可能にします。
 */
public class BusinessException extends ResultMessagesNotificationException {

  /**
   * メッセージを指定するためのコンストラクタ。
   * <p>
   * エラータイプの{@link ResultMessages}インスタンスを生成し、メッセージを追加します。
   * </p>
   *
   * @param message 結果メッセージ
   */
  public BusinessException(String message) {
    super(ResultMessages.error().add(ResultMessage.fromText(message)));
  }

  /**
   * メッセージを指定するためのコンストラクタ。
   * <p>
   * 複数の{@code String}形式のメッセージを引数として受け取ります。
   * </p>
   *
   * @param messages {@link ResultMessages}のインスタンス
   */
  public BusinessException(ResultMessages messages) {
    super(messages);
  }

  /**
   * メッセージと例外を指定するためのコンストラクタ。
   * <p>
   * 複数の{@code String}メッセージと例外の原因を引数として受け取ります。
   * </p>
   *
   * @param messages {@link ResultMessages} のインスタンス
   * @param cause {@link Throwable} のインスタンス
   */
  public BusinessException(ResultMessages messages, Throwable cause) {
    super(messages, cause);
  }

}
