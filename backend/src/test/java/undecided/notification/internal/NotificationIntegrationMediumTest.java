package undecided.notification.internal;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import undecided.ModulithDemoApplication;
import undecided.TestcontainersConfiguration;
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
@SpringBootTest(classes = ModulithDemoApplication.class)
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
class NotificationIntegrationMediumTest {

  @Autowired
  private NotificationEntityRepository notificationRepository;

  @Autowired
  private NotificationCommentParticipantSubsetRepository commentParticipantSubsetRepository;

  @Autowired
  private ApplicationEventPublisher eventPublisher;

  @Autowired
  private SnowflakeIdGenerator snowflakeIdGenerator;

  @Autowired
  private PlatformTransactionManager transactionManager;

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
        NotificationCommentParticipantSubset.builder()
            .id(snowflakeIdGenerator.nextIdValue())
            .backlogItemId(backlogItemId)
            .employeeId(participant1EmployeeId)
            .employeeName("参加者1")
            .employeeEmail("participant1@example.com")
            .build());
    commentParticipantSubsetRepository.save(
        NotificationCommentParticipantSubset.builder()
            .id(snowflakeIdGenerator.nextIdValue())
            .backlogItemId(backlogItemId)
            .employeeId(participant2EmployeeId)
            .employeeName("参加者2")
            .employeeEmail("participant2@example.com")
            .build());
  }

  /**
   * 非同期のモジュールリスナーによる通知作成を待ち、対象アイテムの通知一覧を返す。
   *
   * <p>@ApplicationModuleListener はトランザクションコミット後に非同期で実行されるため、
   * 期待件数に達するまでポーリングする（最大15秒）。
   */
  private List<NotificationEntity> awaitNotifications(int expectedSize)
      throws InterruptedException {
    long deadline = System.currentTimeMillis() + 15000;
    List<NotificationEntity> notifications = List.of();
    while (System.currentTimeMillis() < deadline) {
      notifications =
          StreamSupport.stream(notificationRepository.findAll().spliterator(), false)
              .filter(n -> n.getBacklogItemId().equals(backlogItemId))
              .toList();
      if (notifications.size() >= expectedSize) {
        return notifications;
      }
      Thread.sleep(200);
    }
    return notifications;
  }

  @Test
  @DisplayName("コメントを追加したとき、イベント経由で通知レコードがDBに保存される")
  void shouldPersistNotificationOnCommentAdded() throws InterruptedException {
    // テストリスト項目14
    // Arrange: 既存参加者が2人いる状態で、BacklogItemCommentAddedイベントを発行

    SnowflakeId commentId = snowflakeIdGenerator.nextIdValue();

    // Act: BacklogItemCommentAddedイベントを発行 →
    //   CommentParticipantSyncListenerが投稿者を参加者に記録 →
    //   BacklogItemCommentEventListenerが既存参加者（投稿者除く）に通知を作成
    new TransactionTemplate(transactionManager)
        .executeWithoutResult(
            status ->
                eventPublisher.publishEvent(
                    new BacklogItemCommentAdded(
                        backlogItemId,
                        commentId,
                        authorEmployeeId,
                        "投稿者",
                        "",
                        LocalDateTime.now())));

    // Assert: 既存参加者2人に通知レコードがDBに保存される
    List<NotificationEntity> notifications = awaitNotifications(2);

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
  @DisplayName("同じ投稿者が同じアイテムに2回目のコメントを追加したとき、参加者レコードが重複しない")
  void shouldNotDuplicateParticipantOnSecondComment() throws InterruptedException {
    // テストリスト項目5
    // Arrange: 投稿者の参加者レコードが既に存在する状態（1回目のコメントで登録済み）

    // 1回目のコメントイベントを発行（投稿者が参加者に記録される）
    SnowflakeId commentId1 = snowflakeIdGenerator.nextIdValue();
    new TransactionTemplate(transactionManager)
        .executeWithoutResult(
            status ->
                eventPublisher.publishEvent(
                    new BacklogItemCommentAdded(
                        backlogItemId,
                        commentId1,
                        authorEmployeeId,
                        "投稿者",
                        "",
                        LocalDateTime.now())));

    // 1回目のイベント処理を待つ
    Thread.sleep(2000);

    // Act: 2回目のコメントイベントを発行（同じ投稿者、同じアイテム）
    SnowflakeId commentId2 = snowflakeIdGenerator.nextIdValue();
    new TransactionTemplate(transactionManager)
        .executeWithoutResult(
            status ->
                eventPublisher.publishEvent(
                    new BacklogItemCommentAdded(
                        backlogItemId,
                        commentId2,
                        authorEmployeeId,
                        "投稿者",
                        "",
                        LocalDateTime.now())));

    // 2回目のイベント処理を待つ
    Thread.sleep(2000);

    // Assert: 投稿者の参加者レコードは1件のみ（重複しない）
    Set<NotificationCommentParticipantSubset> participants =
        commentParticipantSubsetRepository.findByBacklogItemId(backlogItemId);

    long authorCount =
        participants.stream()
            .filter(p -> p.getEmployeeId().equals(authorEmployeeId))
            .count();
    assertThat(authorCount).isEqualTo(1);
  }

  @Test
  @DisplayName("コメントを削除したとき、イベント経由で通知レコードがDBに保存される")
  void shouldPersistNotificationOnCommentDeleted() throws InterruptedException {
    // テストリスト項目15
    // Arrange: 既存参加者が2人いる状態で、操作者（participant1）がコメントを削除
    // BacklogItemCommentDeletedイベントを発行し、
    // BacklogItemCommentEventListenerが操作者以外の参加者に通知を作成

    SnowflakeId commentId = snowflakeIdGenerator.nextIdValue();

    // Act: BacklogItemCommentDeletedイベントを発行 →
    //   BacklogItemCommentEventListenerが操作者（participant1）以外の参加者（participant2）に通知を作成
    new TransactionTemplate(transactionManager)
        .executeWithoutResult(
            status ->
                eventPublisher.publishEvent(
                    new BacklogItemCommentDeleted(
                        backlogItemId,
                        commentId,
                        participant1EmployeeId,
                        LocalDateTime.now())));

    // Assert: 操作者（participant1）以外の参加者（participant2）に通知レコードが保存される
    List<NotificationEntity> notifications = awaitNotifications(1);

    assertThat(notifications).hasSize(1);
    assertThat(notifications.get(0).getRecipientId()).isEqualTo(participant2EmployeeId);
    assertThat(notifications.get(0).getType()).isEqualTo(NotificationType.COMMENT_DELETED);
    assertThat(notifications.get(0).getIsRead()).isFalse();
    assertThat(notifications.get(0).getTitle()).isEqualTo("コメントが削除されました");
    assertThat(notifications.get(0).getCreatedAt()).isNotNull();
    assertThat(notifications.get(0).getVersion()).isNotNull();
  }
}
