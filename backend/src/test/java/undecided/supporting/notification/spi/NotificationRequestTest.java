package undecided.supporting.notification.spi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("NotificationRequestのテスト")
class NotificationRequestTest {

  UUID recipientId = UUID.randomUUID();
  UUID sourceId = UUID.randomUUID();
  String sourceDomain = "agiloom";
  String title = "Test notification";
  String content = "This is a test notification";
  NotificationType type = NotificationType.SYSTEM;

  @Nested
  @DisplayName("ビルダーのテスト")
  class BuilderTest {

    @Test
    @DisplayName("必須フィールドを設定するとビルドできる")
    void shouldBuildWithRequiredFields() {
      NotificationRequest request = NotificationRequest.builder(sourceDomain)
          .recipientId(recipientId)
          .sourceId(sourceId)
          .title(title)
          .content(content)
          .type(type)
          .build();

      assertThat(request).isNotNull();
      assertThat(request.getRecipientId()).isEqualTo(recipientId);
      assertThat(request.getSourceId()).isEqualTo(sourceId);
      assertThat(request.getSourceDomain()).isEqualTo(sourceDomain);
      assertThat(request.getTitle()).isEqualTo(title);
      assertThat(request.getContent()).isEqualTo(content);
      assertThat(request.getType()).isEqualTo(type);
    }

    @Test
    @DisplayName("sourceIdはオプションである")
    void sourceIdIsOptional() {
      NotificationRequest request = NotificationRequest.builder(sourceDomain)
          .recipientId(recipientId)
          .title(title)
          .content(content)
          .type(type)
          .build();

      assertThat(request.getSourceId()).isNull();
    }

    @Test
    @DisplayName("recipientIdがnullの場合、例外がスローされる")
    void shouldThrowWhenRecipientIdIsNull() {
      assertThatThrownBy(() -> NotificationRequest.builder(sourceDomain)
          .title(title)
          .content(content)
          .type(type)
          .build())
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("recipientId");
    }

    @Test
    @DisplayName("titleがnullの場合、例外がスローされる")
    void shouldThrowWhenTitleIsNull() {
      assertThatThrownBy(() -> NotificationRequest.builder(sourceDomain)
          .recipientId(recipientId)
          .content(content)
          .type(type)
          .build())
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("title");
    }

    @Test
    @DisplayName("contentがnullの場合、例外がスローされる")
    void shouldThrowWhenContentIsNull() {
      assertThatThrownBy(() -> NotificationRequest.builder(sourceDomain)
          .recipientId(recipientId)
          .title(title)
          .type(type)
          .build())
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("content");
    }

    @Test
    @DisplayName("typeがnullの場合、例外がスローされる")
    void shouldThrowWhenTypeIsNull() {
      assertThatThrownBy(() -> NotificationRequest.builder(sourceDomain)
          .recipientId(recipientId)
          .title(title)
          .content(content)
          .build())
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("type");
    }
  }

  @Nested
  @DisplayName("NotificationTypeのテスト")
  class NotificationTypeTest {

    @Test
    @DisplayName("すべてのタイプが定義されている")
    void shouldHaveAllTypes() {
      NotificationType[] types = NotificationType.values();
      assertThat(types).hasSize(8)
          .contains(NotificationType.MENTION)
          .contains(NotificationType.ASSIGNEE_CHANGE)
          .contains(NotificationType.DUE_DATE)
          .contains(NotificationType.AWAITING_DECISION)
          .contains(NotificationType.APPROVAL_RESULT)
          .contains(NotificationType.JOB_EXECUTION_RESULT)
          .contains(NotificationType.SYSTEM)
          .contains(NotificationType.CUSTOM);
    }
  }
}
