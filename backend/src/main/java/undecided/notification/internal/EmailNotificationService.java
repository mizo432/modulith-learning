package undecided.notification.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * メール通知サービス。
 *
 * <p>非同期でメールを送信します。送信失敗はログ出力のみで、通知レコードの作成には影響しません。
 */
@Service
class EmailNotificationService {

  private static final Logger log = LoggerFactory.getLogger(EmailNotificationService.class);

  /** メール送信の機能インターフェース。 */
  @FunctionalInterface
  interface MailSender {
    void send(String to, String from, String subject, String text);
  }

  private final MailSender mailSender;

  public EmailNotificationService(MailSender mailSender) {
    this.mailSender = mailSender;
  }

  /**
   * 指定されたメールアドレスへ通知メールを送信します（同期）。
   * 送信失敗はログ出力のみで、例外はスローしません。
   *
   * @param to 宛先メールアドレス
   * @param title メール件名
   * @param content メール本文
   */
  public void send(String to, String title, String content) {
    try {
      mailSender.send(to, "noreply@modulith-learning.local", title, content);
    } catch (Exception ex) {
      log.warn("メール送信に失敗しました (to={}, title={})", to, title, ex);
    }
  }

  /**
   * 指定されたメールアドレスへ通知メールを非同期で送信します。
   * 送信失敗はログ出力のみで、例外はスローしません。
   *
   * @param to 宛先メールアドレス
   * @param title メール件名
   * @param content メール本文
   */
  @Async
  public void sendAsync(String to, String title, String content) {
    try {
      mailSender.send(to, "noreply@modulith-learning.local", title, content);
    } catch (Exception ex) {
      log.warn("メール送信に失敗しました (to={}, title={})", to, title, ex);
    }
  }
}
