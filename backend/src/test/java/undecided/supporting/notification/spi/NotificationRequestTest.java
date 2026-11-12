package undecided.supporting.notification.spi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.mizo432.shared.common.exception.BusinessException;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("NotificationRequestのテスト")
class NotificationRequestTest {

  private static final UUID RECIPIENT_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
  private static final UUID SOURCE_ID = UUID.fromString("44444444-4444-4444-4444-444444444444");
  private static final String SOURCE_DOMAIN = "agiloom";
  private static final String TITLE = "Test notification";
  private static final String CONTENT = "This is a test notification";
  private static final NotificationType TYPE = NotificationType.SYSTEM;

  @Nested
  @DisplayName("ビルダーのテスト")
  class BuilderTest {

    @Test
    @DisplayName("必須フィールドを設定するとビルドできる")
    void shouldBuildWithRequiredFields() {
      NotificationRequest request =
          NotificationRequest.builder(SOURCE_DOMAIN)
              .recipientId(RECIPIENT_ID)
              .sourceId(SOURCE_ID)
              .title(TITLE)
              .content(CONTENT)
              .type(TYPE)
              .build();

      assertThat(request).isNotNull();
      assertThat(request.getRecipientId()).isEqualTo(RECIPIENT_ID);
      assertThat(request.getSourceId()).isEqualTo(SOURCE_ID);
      assertThat(request.getSourceDomain()).isEqualTo(SOURCE_DOMAIN);
      assertThat(request.getTitle()).isEqualTo(TITLE);
      assertThat(request.getContent()).isEqualTo(CONTENT);
      assertThat(request.getType()).isEqualTo(TYPE);
    }

    @Test
    @DisplayName("sourceIdはオプションである")
    void shouldAcceptOptionalSourceId() {
      NotificationRequest request =
          NotificationRequest.builder(SOURCE_DOMAIN)
              .recipientId(RECIPIENT_ID)
              .title(TITLE)
              .content(CONTENT)
              .type(TYPE)
              .build();

      assertThat(request.getSourceId()).isNull();
    }

    @Test
    @DisplayName("recipientIdがnullの場合、BusinessExceptionがスローされる")
    void shouldThrowWhenRecipientIdIsNull() {
      assertThatThrownBy(() ->
              NotificationRequest.builder(SOURCE_DOMAIN)
                  .title(TITLE)
                  .content(CONTENT)
                  .type(TYPE)
                  .build())
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("recipientId");
    }

    @Test
    @DisplayName("titleがnullの場合、BusinessExceptionがスローされる")
    void shouldThrowWhenTitleIsNull() {
      assertThatThrownBy(() ->
              NotificationRequest.builder(SOURCE_DOMAIN)
                  .recipientId(RECIPIENT_ID)
                  .content(CONTENT)
                  .type(TYPE)
                  .build())
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("title");
    }

    @Test
    @DisplayName("contentがnullの場合、BusinessExceptionがスローされる")
    void shouldThrowWhenContentIsNull() {
      assertThatThrownBy(() ->
              NotificationRequest.builder(SOURCE_DOMAIN)
                  .recipientId(RECIPIENT_ID)
                  .title(TITLE)
                  .type(TYPE)
                  .build())
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("content");
    }

    @Test
    @DisplayName("typeがnullの場合、BusinessExceptionがスローされる")
    void shouldThrowWhenTypeIsNull() {
      assertThatThrownBy(() ->
              NotificationRequest.builder(SOURCE_DOMAIN)
                  .recipientId(RECIPIENT_ID)
                  .title(TITLE)
                  .content(CONTENT)
                  .build())
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("type");
    }
  }
}
