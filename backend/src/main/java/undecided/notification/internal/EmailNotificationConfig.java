package undecided.notification.internal;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * メール通知の設定。
 *
 * <p>Spring Boot が自動生成する {@link JavaMailSender} を
 * {@link EmailNotificationService.MailSender} にアダプトするBeanを定義する。
 * {@code @Async} を有効にするため {@code @EnableAsync} を付与する。
 */
@Configuration
@EnableAsync
class EmailNotificationConfig {

  /**
   * JavaMailSender を MailSender 機能インターフェースにアダプトするBean。
   *
   * @param javaMailSender Spring Boot が自動生成するJavaMailSender
   * @return MailSenderの実装
   */
  @Bean
  EmailNotificationService.MailSender mailSenderAdapter(JavaMailSender javaMailSender) {
    return (to, from, subject, text) -> {
      MimeMessage message = javaMailSender.createMimeMessage();
      try {
        message.setFrom(from);
        message.setRecipients(jakarta.mail.Message.RecipientType.TO, to);
        message.setSubject(subject);
        message.setText(text, "utf-8", "html");
      } catch (MessagingException e) {
        throw new RuntimeException("メールメッセージの作成に失敗しました", e);
      }
      javaMailSender.send(message);
    };
  }
}
