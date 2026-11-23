package undecided.notification.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import undecided.agiloom.requirementBacklog.spi.BacklogItemSplit;
import undecided.notification.spi.NotificationEntity;
import undecided.notification.spi.NotificationType;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

@Tag("small")
@DisplayName("バックログアイテム分割通知リスナーの単体テスト")
@ExtendWith(MockitoExtension.class)
class BacklogItemSplitEventListenerTest {

  private static final SnowflakeId ITEM_ID = SnowflakeId.of(10L);
  private static final SnowflakeId TEAM_ID = SnowflakeId.of(20L);
  private static final SnowflakeId ACTOR_ID = SnowflakeId.of(100L);
  private static final SnowflakeId MEMBER_1_ID = SnowflakeId.of(200L);
  private static final SnowflakeId MEMBER_2_ID = SnowflakeId.of(300L);
  private static final SnowflakeId NOTIFICATION_1_ID = SnowflakeId.of(5000L);
  private static final SnowflakeId NOTIFICATION_2_ID = SnowflakeId.of(5001L);
  private static final BacklogItemSplit EVENT =
      new BacklogItemSplit(
          ITEM_ID,
          TEAM_ID,
          List.of(SnowflakeId.of(11L), SnowflakeId.of(12L)),
          ACTOR_ID,
          LocalDateTime.of(2026, 11, 23, 10, 0));

  @Mock private NotificationEntityRepository notificationRepository;
  @Mock private NotificationScrumTeamMemberRepository scrumTeamMemberRepository;
  @Mock private EmailNotificationService emailNotificationService;
  @Mock private SnowflakeIdGenerator snowflakeIdGenerator;

  private BacklogItemSplitEventListener listener;

  @BeforeEach
  void setUp() {
    listener =
        new BacklogItemSplitEventListener(
            notificationRepository,
            scrumTeamMemberRepository,
            emailNotificationService,
            snowflakeIdGenerator);
  }

  @Nested
  @DisplayName("分割イベントの受信")
  class OnBacklogItemSplitTest {

    @Test
    @DisplayName("操作者以外のチームメンバーそれぞれに未読通知を保存しメールを送る")
    void shouldSaveAndSendNotificationsToTeamMembersExceptActor() {
      when(scrumTeamMemberRepository.findByScrumTeamId(TEAM_ID))
          .thenReturn(
              Set.of(
                  new NotificationScrumTeamMember(ACTOR_ID, "actor@example.com"),
                  new NotificationScrumTeamMember(MEMBER_1_ID, "member1@example.com"),
                  new NotificationScrumTeamMember(MEMBER_2_ID, "member2@example.com")));
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(NOTIFICATION_1_ID, NOTIFICATION_2_ID);

      listener.onBacklogItemSplit(EVENT);

      ArgumentCaptor<NotificationEntity> captor = ArgumentCaptor.forClass(NotificationEntity.class);
      verify(notificationRepository, times(2)).save(captor.capture());
      List<NotificationEntity> saved = captor.getAllValues();
      assertThat(saved)
          .extracting(NotificationEntity::getRecipientId)
          .containsExactlyInAnyOrder(MEMBER_1_ID, MEMBER_2_ID);
      assertThat(saved)
          .extracting(NotificationEntity::getId)
          .containsExactlyInAnyOrder(NOTIFICATION_1_ID, NOTIFICATION_2_ID);
      for (NotificationEntity notification : saved) {
        assertThat(notification.getBacklogItemId()).isEqualTo(ITEM_ID);
        assertThat(notification.getCommentId()).isNull();
        assertThat(notification.getType()).isEqualTo(NotificationType.BACKLOG_ITEM_SPLIT);
        assertThat(notification.getIsRead()).isFalse();
        assertThat(notification.getTitle()).contains("分割");
        assertThat(notification.getContent()).contains("分割");
        String address =
            notification.getRecipientId().equals(MEMBER_1_ID)
                ? "member1@example.com"
                : "member2@example.com";
        verify(emailNotificationService)
            .sendAsync(address, notification.getTitle(), notification.getContent());
      }
      verifyNoMoreInteractions(emailNotificationService);
    }

    @Test
    @DisplayName("チームに操作者しかいないとき通知を保存せず送信しない")
    void shouldNotNotifyWhenActorIsOnlyTeamMember() {
      when(scrumTeamMemberRepository.findByScrumTeamId(TEAM_ID))
          .thenReturn(Set.of(new NotificationScrumTeamMember(ACTOR_ID, "actor@example.com")));

      listener.onBacklogItemSplit(EVENT);

      verifyNoInteractions(notificationRepository, emailNotificationService, snowflakeIdGenerator);
    }

    @Test
    @DisplayName("対象チームにメンバーがいないとき通知を保存せず送信しない")
    void shouldNotNotifyWhenTeamHasNoMembers() {
      when(scrumTeamMemberRepository.findByScrumTeamId(TEAM_ID)).thenReturn(Set.of());

      listener.onBacklogItemSplit(EVENT);

      verifyNoInteractions(notificationRepository, emailNotificationService, snowflakeIdGenerator);
    }

    @Test
    @DisplayName("メールアドレスが未設定でもアプリ内通知を保存しメールは送信しない")
    void shouldSaveNotificationsWithoutSendingMailWhenAddressIsMissing() {
      when(scrumTeamMemberRepository.findByScrumTeamId(TEAM_ID))
          .thenReturn(
              Set.of(
                  new NotificationScrumTeamMember(MEMBER_1_ID, null),
                  new NotificationScrumTeamMember(MEMBER_2_ID, "")));
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(NOTIFICATION_1_ID, NOTIFICATION_2_ID);

      listener.onBacklogItemSplit(EVENT);

      ArgumentCaptor<NotificationEntity> captor = ArgumentCaptor.forClass(NotificationEntity.class);
      verify(notificationRepository, times(2)).save(captor.capture());
      assertThat(captor.getAllValues())
          .extracting(NotificationEntity::getRecipientId)
          .containsExactlyInAnyOrder(MEMBER_1_ID, MEMBER_2_ID);
      verifyNoInteractions(emailNotificationService);
    }

    @Test
    @DisplayName("同じ従業員が重複しているとき通知は1件だけ保存し送信する")
    void shouldNotifyEachEmployeeOnlyOnce() {
      when(scrumTeamMemberRepository.findByScrumTeamId(TEAM_ID))
          .thenReturn(
              Set.of(
                  new NotificationScrumTeamMember(MEMBER_1_ID, "member1@example.com"),
                  new NotificationScrumTeamMember(MEMBER_1_ID, "other@example.com")));
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(NOTIFICATION_1_ID);

      listener.onBacklogItemSplit(EVENT);

      ArgumentCaptor<NotificationEntity> captor = ArgumentCaptor.forClass(NotificationEntity.class);
      verify(notificationRepository).save(captor.capture());
      assertThat(captor.getValue().getRecipientId()).isEqualTo(MEMBER_1_ID);
      ArgumentCaptor<String> address = ArgumentCaptor.forClass(String.class);
      verify(emailNotificationService)
          .sendAsync(
              address.capture(), eq(captor.getValue().getTitle()), eq(captor.getValue().getContent()));
      assertThat(address.getValue()).isIn("member1@example.com", "other@example.com");
      verifyNoMoreInteractions(emailNotificationService);
    }

    @Test
    @DisplayName("分割イベントのリスナーとして登録されている")
    void shouldBeRegisteredAsAnApplicationModuleListener() throws NoSuchMethodException {
      assertThat(BacklogItemSplitEventListener.class.getAnnotation(Component.class)).isNotNull();
      assertThat(
              BacklogItemSplitEventListener.class
                  .getDeclaredMethod("onBacklogItemSplit", BacklogItemSplit.class)
                  .getAnnotation(ApplicationModuleListener.class))
          .isNotNull();
    }
  }
}