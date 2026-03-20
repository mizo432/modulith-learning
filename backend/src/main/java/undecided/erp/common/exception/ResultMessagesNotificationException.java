package undecided.erp.common.exception;

import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import undecided.shared.common.message.ResultMessage;
import undecided.shared.common.message.ResultMessageType;
import undecided.shared.common.message.ResultMessages;

/**
 * {@code ResultMessagesNotificationException} クラスは、{@code RuntimeException} クラスの抽象サブクラスです。
 *
 * <p>このクラスは、通知すべき結果メッセージが存在する場合にスローされる例外を表します。 このクラスは、結果メッセージをカプセル化し、それを呼び出し元に渡す手段を提供します。
 *
 * <p>このクラスには、1つの引数を取るコンストラクタと、2つの引数を取るコンストラクタがあります。 1つの引数を取るコンストラクタは、{@link ResultMessages}
 * のインスタンスをパラメータとして受け取ります。 2つの引数を取るコンストラクタは、{@link ResultMessages} と {@link Throwable}
 * のインスタンスをパラメータとして受け取ります。
 *
 * <p>このクラスのインスタンスには、{@code resultMessages} というプロパティがあります。このプロパティは、 {@link ResultMessages}
 * 型で、カプセル化された結果メッセージを保持します。
 *
 * <p>このクラスのインスタンスでは、{@link #getMessage()} メソッドを呼び出すことで、 カプセル化された結果メッセージを文字列形式で取得できます。
 */
@Getter
public abstract class ResultMessagesNotificationException extends RuntimeException {

  /**
   * 例外内でカプセル化された結果メッセージを表します。
   *
   * <p>このフィールドは、呼び出し側に通知されるメッセージを保持する {@link ResultMessages} のインスタンスを格納します。 このフィールドは final
   * フィールドであり、 コンストラクタを通じて設定された後は不変であることが保証されています。
   */
  private final ResultMessages resultMessages;

  /**
   * 引数が1つのコンストラクタ
   *
   * @param messages {@link ResultMessages} のインスタンス
   */
  protected ResultMessagesNotificationException(@Nullable ResultMessages messages) {
    this(messages, null);
  }

  /**
   * 2つの引数を取るコンストラクタ。
   *
   * @param messages {@link ResultMessages} のインスタンス
   * @param cause {@link Throwable} のインスタンス
   */
  public ResultMessagesNotificationException(
      @NonNull ResultMessages messages, @NonNull Throwable cause) {
    super(cause);
    if (messages == null) {
      throw new IllegalArgumentException("messages must not be null");
    }
    this.resultMessages = messages;
  }

  /**
   * {@code ResultMessagesNotificationException} クラスの3つの引数を持つコンストラクタ。
   *
   * @param resultMessageType 結果メッセージのタイプを表す {@link ResultMessageType} のインスタンス
   * @param message 結果メッセージを表す {@link ResultMessage} のインスタンス
   * @param cause この例外を発生させた原因となった {@link Throwable} のインスタンス
   */
  public ResultMessagesNotificationException(
      @NonNull ResultMessageType resultMessageType,
      @NonNull ResultMessage message,
      @NonNull Throwable cause) {
    this(new ResultMessages(resultMessageType, message), cause);
  }

  /**
   * {@code ResultMessagesNotificationException} クラスの2つの引数を持つコンストラクタ。
   *
   * @param resultMessageType 結果メッセージのタイプを表す {@link ResultMessageType} のインスタンス。null ではない必要があります。
   * @param message 結果メッセージを表す {@link ResultMessage} のインスタンス。null ではない必要があります。
   */
  public ResultMessagesNotificationException(
      @NonNull ResultMessageType resultMessageType, @NonNull ResultMessage message) {
    this(new ResultMessages(resultMessageType, message));
  }

  /**
   * {@code ResultMessagesNotificationException} クラスの4つの引数を持つコンストラクタ。
   *
   * @param resultMessageType 結果メッセージのタイプを表す {@link ResultMessageType} のインスタンス。null ではない必要があります。
   * @param errorCode エラーメッセージを識別するためのコード。null ではない必要があります。
   * @param verrorArgs メッセージの引数。null ではない必要があります。
   * @param cause この例外を発生させた原因となった {@link Throwable} のインスタンス。null ではない必要があります。
   */
  public ResultMessagesNotificationException(
      @NonNull ResultMessageType resultMessageType,
      @NonNull String errorCode,
      @NonNull Object[] verrorArgs,
      @NonNull Throwable cause) {
    this(
        new ResultMessages(resultMessageType, ResultMessage.fromCode(errorCode, verrorArgs)),
        cause);
  }

  /**
   * {@code ResultMessagesNotificationException} クラスの3つの引数を持つコンストラクタ。
   *
   * @param resultMessageType 結果メッセージのタイプを表す {@link ResultMessageType} のインスタンス。null ではない必要があります。
   * @param errorCode エラーメッセージを識別するためのコード。null ではない必要があります。
   * @param verrorArgs メッセージの引数。null ではない必要があります。
   */
  public ResultMessagesNotificationException(
      @NonNull ResultMessageType resultMessageType,
      @NonNull String errorCode,
      @NonNull Object... verrorArgs) {
    this(new ResultMessages(resultMessageType, ResultMessage.fromCode(errorCode, verrorArgs)));
  }

  /**
   * メッセージを文字列形式で返します。
   *
   * @return メッセージの文字列
   */
  @Override
  public String getMessage() {
    return resultMessages.toString();
  }
}
