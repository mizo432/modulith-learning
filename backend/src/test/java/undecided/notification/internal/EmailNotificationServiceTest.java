package undecided.notification.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * テストリスト項目7, 8: メール送信サービスのテスト
 *
 * <p>- 項目7: メール送信サービスが起動したとき、JavaMailSenderが呼び出される
 * <p>- 項目8: メール送信で例外が発生したとき、通知レコード作成は失敗しない（ログ出力のみ）
 */
@Tag("small")
@DisplayName("EmailNotificationServiceの単体テスト")
class EmailNotificationServiceTest {

  private static final String TO_EMAIL = "test@example.com";
  private static final String TITLE = "コメントが追加されました";
  private static final String CONTENT = "バックログアイテムに新しいコメントが追加されました";

  @Nested
  @DisplayName("send メソッドのテスト")
  class SendTest {

    @Test
    @DisplayName("メール送信サービスが起動したとき、JavaMailSenderが呼び出される")
    void shouldCallMailSender() {
      // テストリスト項目7
      int[] callCount = {0};
      String[] capturedTo = {null};
      String[] capturedFrom = {null};
      String[] capturedSubject = {null};
      String[] capturedText = {null};
      EmailNotificationService service =
          new EmailNotificationService(
              (to, from, subject, text) -> {
                callCount[0]++;
                capturedTo[0] = to;
                capturedFrom[0] = from;
                capturedSubject[0] = subject;
                capturedText[0] = text;
              });

      service.send(TO_EMAIL, TITLE, CONTENT);

      // MailSenderが1回呼び出され、正しい引数が渡されることを検証
      assertThat(callCount[0]).isEqualTo(1);
      assertThat(capturedTo[0]).isEqualTo(TO_EMAIL);
      assertThat(capturedFrom[0]).isEqualTo("noreply@modulith-learning.local");
      assertThat(capturedSubject[0]).isEqualTo(TITLE);
      assertThat(capturedText[0]).isEqualTo(CONTENT);
    }

    @Test
    @DisplayName("メール送信で例外が発生したとき、通知レコード作成は失敗しない（ログ出力のみ）")
    void shouldNotFailNotificationCreationWhenMailFails() {
      // テストリスト項目8
      EmailNotificationService service =
          new EmailNotificationService(
              (to, from, subject, text) -> {
                throw new RuntimeException("Mail send failed");
              });

      // 例外がスローされないこと（try-catchでキャッチされる）
      assertThatCode(() -> service.send(TO_EMAIL, TITLE, CONTENT)).doesNotThrowAnyException();
    }
  }
}
