package undecided.notification.internal;

import java.util.function.Function;

/**
 * メール通知サービス。
 *
 * <p>非同期でメールを送信します。送信失敗はログ出力のみで、通知レコードの作成には影響しません。
 */
public class EmailNotificationService {

  /** メール送信の機能インターフェース。 */
  @FunctionalInterface
  public interface MailSender {
    void send(String to, String from, String subject, String text);
  }

  private final MailSender mailSender;

  public EmailNotificationService(MailSender mailSender) {
    this.mailSender = mailSender;
  }

  /**
   * 指定されたメールアドレスへ通知メールを送信します。
   *
   * @param to 宛先メールアドレス
   * @param title メール件名
   * @param content メール本文
   */
  public void send(String to, String title, String content) {
    // TODO: 実装
    throw new UnsupportedOperationException("Not yet implemented");
  }
}
