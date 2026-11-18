package undecided.notification.internal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * テストリスト項目7, 8: メール送信サービスのテスト
 *
 * <p>- 項目7: メール送信サービスが起動したとき、JavaMailSenderが呼び出される
 * <p>- 項目8: メール送信で例外が発生したとき、通知レコード作成は失敗しない（ログ出力のみ）
 *
 * <p>spring-boot-starter-mail が依存関係に追加されるまで、JavaMailSenderは利用できない。
 * このテストはメール送信基盤の新設が必要であることを示す（Red）。
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
      // EmailNotificationService の send() が JavaMailSender を呼び出すことを検証する。
      // 現在の実装では UnsupportedOperationException がスローされる（Red）。
      // 実装後は mailSender.send() が呼び出されることを確認する。
      EmailNotificationService service =
          new EmailNotificationService(
              (to, from, subject, text) -> {
                // no-op
              });
      assertThatThrownBy(() -> service.send(TO_EMAIL, TITLE, CONTENT))
          .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("メール送信で例外が発生したとき、通知レコード作成は失敗しない（ログ出力のみ）")
    void shouldNotFailNotificationCreationWhenMailFails() {
      // テストリスト項目8
      // メール送信の失敗が通知レコード作成に影響しないことを検証
      // 現在 spring-boot-starter-mail が依存関係にないため、
      // EmailNotificationService の send() は UnsupportedOperationException をスローする
      // 実装後は try-catch で例外をキャッチし、ログ出力のみとする
      EmailNotificationService service =
          new EmailNotificationService(
              (to, from, subject, text) -> {
                throw new RuntimeException("Mail send failed");
              });
      // Red: 現在の実装では例外がスローされる
      assertThatThrownBy(() -> service.send(TO_EMAIL, TITLE, CONTENT))
          .isInstanceOf(UnsupportedOperationException.class);
    }
  }
}
