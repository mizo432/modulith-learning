package undecided.notification.internal;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import undecided.agiloom.requirementBacklog.spi.BacklogItemCommentAdded;
import undecided.agiloom.requirementBacklog.spi.BacklogItemCommentDeleted;
import undecided.notification.spi.NotificationEntity;
import undecided.notification.spi.NotificationType;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

/**
 * テストリスト項目14, 15: イベント経由での通知レコードDB永続化テスト（統合テスト）
 *
 * <p>- 項目14: コメントを追加したとき、BacklogItemCommentAddedイベントが発行され、
 *   CommentParticipantSyncListenerが参加者をNTスキーマに記録し、
 *   BacklogItemCommentEventListenerが通知レコードを作成してDBに保存する
 * <p>- 項目15: コメントを削除したとき、BacklogItemCommentDeletedイベントが発行され、
 *   BacklogItemCommentEventListenerが通知レコードを作成してDBに保存する
 *
 * <p>AG側のinternalパッケージクラスにアクセスできないため、
 * ApplicationEventPublisherを介してイベントを発行し、NT側のリスナーフローを検証する。
 */
@Tag("medium")
@DisplayName("通知モジュールの統合テスト")
@SpringBootTest
@Testcontainers
class NotificationIntegrationMediumTest {

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine")
      .withDatabaseName("test_nt")
      .withUsername("test")
      .withPassword("test");

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
  }

  @Autowired
  private NotificationRepository notificationRepository;

  @Autowired
  private CommentParticipantSubsetRepository commentParticipantSubsetRepository;

  @Autowired
  private ApplicationEventPublisher eventPublisher;

  @Autowired
  private SnowflakeIdGenerator snowflakeIdGenerator;

  private SnowflakeId backlogItemId;
  private SnowflakeId authorEmployeeId;
  private SnowflakeId participant1EmployeeId;
  private SnowflakeId participant2EmployeeId;

  @BeforeEach
  void setUp() {
    // IDを生成
    backlogItemId = snowflakeIdGenerator.nextIdValue();
    authorEmployeeId = snowflakeIdGenerator.nextIdValue();
    participant1EmployeeId = snowflakeIdGenerator.nextIdValue();
    participant2EmployeeId = snowflakeIdGenerator.nextIdValue();

    // 既存の参加者（通知対象）をNTスキーマに事前に投入
    commentParticipantSubsetRepository.save(
        CommentParticipantSubset.builder()
            .id(snowflakeIdGenerator.nextIdValue())
            .backlogItemId(backlogItemId)
            .employeeId(participant1EmployeeId)
            .employeeName("参加者1")
            .employeeEmail("participant1@example.com")
            .build());
    commentParticipantSubsetRepository.save(
        CommentParticipantSubset.builder()
            .id(snowflakeIdGenerator.nextIdValue())
            .backlogItemId(backlogItemId)
            .employeeId(participant2EmployeeId)
            .employeeName("参加者2")
            .employeeEmail("participant2@example.com")
            .build());
  }

  @Test
  @DisplayName("コメントを追加したとき、イベント経由で通知レコードがDBに保存される")
  void shouldPersistNotificationOnCommentAdded() {
    // テストリスト項目14
    // Arrange: 既存参加者が2人いる状態で、BacklogItemCommentAddedイベントを発行

    SnowflakeId commentId = snowflakeIdGenerator.nextIdValue();

    // Act: BacklogItemCommentAddedイベントを発行 →
    //   CommentParticipantSyncListenerが投稿者を参加者に記録 →
    //   BacklogItemCommentEventListenerが既存参加者（投稿者除く）に通知を作成
    eventPublisher.publishEvent(
        new BacklogItemCommentAdded(
            backlogItemId,
            commentId,
            authorEmployeeId,
            "投稿者",
            "",
            LocalDateTime.now()));

    // Assert: 既存参加者2人に通知レコードがDBに保存される
    List<NotificationEntity> notifications = StreamSupport.stream(notificationRepository.findAll().spliterator(), false)
        .filter(n -> n.getBacklogItemId().equals(backlogItemId))
        .toList();

    assertThat(notifications).hasSize(2);
    assertThat(notifications)
        .extracting(NotificationEntity::getRecipientId)
        .containsExactlyInAnyOrder(participant1EmployeeId, participant2EmployeeId);
    assertThat(notifications)
        .allSatisfy(n -> {
          assertThat(n.getType()).isEqualTo(NotificationType.COMMENT_ADDED);
          assertThat(n.getIsRead()).isFalse();
          assertThat(n.getTitle()).isEqualTo("コメントが追加されました");
          assertThat(n.getCreatedAt()).isNotNull();
          assertThat(n.getVersion()).isNotNull();
        });

    // 投稿者自身には通知されないことを確認
    assertThat(notifications)
        .noneSatisfy(n -> assertThat(n.getRecipientId()).isEqualTo(authorEmployeeId));
  }

  @Test
  @DisplayName("コメントを削除したとき、イベント経由で通知レコードがDBに保存される")
  void shouldPersistNotificationOnCommentDeleted() {
    // テストリスト項目15
    // Arrange: 既存参加者が2人いる状態で、操作者（participant1）がコメントを削除
    // BacklogItemCommentDeletedイベントを発行し、
    // BacklogItemCommentEventListenerが操作者以外の参加者に通知を作成

    SnowflakeId commentId = snowflakeIdGenerator.nextIdValue();

    // Act: BacklogItemCommentDeletedイベントを発行 →
    //   BacklogItemCommentEventListenerが操作者（participant1）以外の参加者（participant2）に通知を作成
    eventPublisher.publishEvent(
        new BacklogItemCommentDeleted(
            backlogItemId,
            commentId,
            participant1EmployeeId,
            LocalDateTime.now()));

    // Assert: 操作者（participant1）以外の参加者（participant2）に通知レコードが保存される
    List<NotificationEntity> notifications = StreamSupport.stream(notificationRepository.findAll().spliterator(), false)
        .filter(n -> n.getBacklogItemId().equals(backlogItemId))
        .toList();

    assertThat(notifications).hasSize(1);
    assertThat(notifications.get(0).getRecipientId()).isEqualTo(participant2EmployeeId);
    assertThat(notifications.get(0).getType()).isEqualTo(NotificationType.COMMENT_DELETED);
    assertThat(notifications.get(0).getIsRead()).isFalse();
    assertThat(notifications.get(0).getTitle()).isEqualTo("コメントが削除されました");
    assertThat(notifications.get(0).getCreatedAt()).isNotNull();
    assertThat(notifications.get(0).getVersion()).isNotNull();
  }
}
