package undecided.agiloom.sprintExecution.spi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.mizo432.shared.common.exception.BusinessException;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("Activityのテスト")
class ActivityTest {

  private static final UUID FIXED_ACTOR_ID =
      UUID.fromString("33333333-3333-3333-3333-333333333333");

  private Activity createActivity() {
    return Activity.of(
        SnowflakeId.of(1L),
        SnowflakeId.of(100L),
        FIXED_ACTOR_ID,
        ActivityType.STATUS_CHANGE,
        "Sprint status changed to ACTIVE",
        LocalDateTime.now());
  }

  @Nested
  @DisplayName("ファクトリーメソッドのテスト")
  class FactoryMethodTest {

    @Test
    @DisplayName("ファクトリーメソッドでインスタンスが作成される")
    void shouldCreateInstanceWithFactoryMethod() {
      Activity activity = createActivity();

      assertThat(activity).isNotNull();
      assertThat(activity.getId().value()).isEqualTo(1L);
      assertThat(activity.getSprintId().value()).isEqualTo(100L);
      assertThat(activity.getActorId()).isEqualTo(FIXED_ACTOR_ID);
      assertThat(activity.getActivityType()).isEqualTo(ActivityType.STATUS_CHANGE);
      assertThat(activity.getDescription()).isEqualTo("Sprint status changed to ACTIVE");
      assertThat(activity.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("descriptionはオプションである")
    void descriptionIsOptional() {
      Activity activity =
          Activity.of(
              SnowflakeId.of(2L),
              SnowflakeId.of(100L),
              FIXED_ACTOR_ID,
              ActivityType.COMMENT_ADDED,
              null,
              null);

      assertThat(activity.getDescription()).isNull();
    }
  }

  @Nested
  @DisplayName("アクティビティタイプのテスト")
  class ActivityTypeTest {

    @Test
    @DisplayName("すべてのアクティビティタイプでインスタンスが作成される")
    void shouldCreateWithAllActivityTypes() {
      for (ActivityType type : ActivityType.values()) {
        Activity activity =
            Activity.of(
                SnowflakeId.of(1L),
                SnowflakeId.of(100L),
                FIXED_ACTOR_ID,
                type,
                null,
                null);

        assertThat(activity.getActivityType()).isEqualTo(type);
      }
    }
  }

  @Nested
  @DisplayName("updateメソッドのテスト")
  class UpdateMethodTest {

    @Test
    @DisplayName("アクティビティタイプとdescriptionが更新される")
    void shouldUpdateActivityTypeAndDescription() {
      Activity activity = createActivity();

      activity.update(ActivityType.COMMENT_ADDED, "New description");

      assertThat(activity.getActivityType()).isEqualTo(ActivityType.COMMENT_ADDED);
      assertThat(activity.getDescription()).isEqualTo("New description");
    }

    @Test
    @DisplayName("descriptionはnullに更新できる")
    void shouldUpdateDescriptionToNull() {
      Activity activity = createActivity();

      activity.update(ActivityType.COMMENT_ADDED, null);

      assertThat(activity.getDescription()).isNull();
    }

    @Test
    @DisplayName("activityTypeがnullの場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenActivityTypeIsNull() {
      Activity activity = createActivity();

      assertThatThrownBy(() -> activity.update(null, "Description"))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("activityType must not be null");
    }

    @Test
    @DisplayName("descriptionが2000文字を超える場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenDescriptionExceedsLength() {
      Activity activity = createActivity();
      String longDescription = "A".repeat(2001);

      assertThatThrownBy(() -> activity.update(ActivityType.COMMENT_ADDED, longDescription))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("description must not exceed 2000 characters");
    }
  }
}
