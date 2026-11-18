package undecided.agiloom.requirementBacklog.internal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import undecided.ModulithDemoApplication;
import undecided.TestcontainersConfiguration;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * BacklogItemの楽観ロック動作をテストする統合テスト。
 *
 * <p>Testcontainersを使用して、実際のDBトランザクションで楽観ロックの動作を検証する。
 */
@Tag("medium")
@SpringBootTest(classes = ModulithDemoApplication.class)
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@DisplayName("BacklogItem楽観ロックの統合テスト")
class BacklogItemOptimisticLockMediumTest {

  @Autowired private BacklogItemRepository repository;

  @PersistenceContext private EntityManager entityManager;

  @Test
  @DisplayName("状態変更後の再コミットで楽観ロック例外が発生すること")
  @Transactional
  void shouldThrowOptimisticLockingFailureOnStaleVersionAfterStatusChange() {
    // Arrange
    SnowflakeId itemId = SnowflakeId.of(2000L);
    BacklogItem item = BacklogItem.builder()
        .id(itemId)
        .title("Stale Version Test")
        .creatorName("Creator")
        .scrumTeamId(SnowflakeId.of(10L))
        .build();
    repository.save(item);
    entityManager.flush();
    entityManager.clear();

    // 古いバージョンのエンティティを読み込む
    BacklogItem staleItem = entityManager.find(BacklogItem.class, itemId);

    // 1次キャッシュをクリアして、別の管理インスタンスとして再度読み込む
    entityManager.detach(staleItem);
    BacklogItem freshItem = entityManager.find(BacklogItem.class, itemId);
    freshItem.changeStatus(BacklogItemStatus.FROZEN);
    entityManager.flush();

    // 古いバージョンのエンティティを再アタッチすると楽観ロック例外が発生する
    assertThatThrownBy(() -> entityManager.merge(staleItem))
        .isInstanceOf(OptimisticLockException.class);
  }

  @Test
  @DisplayName("内容更新後の再コミットで楽観ロック例外が発生すること")
  @Transactional
  void shouldThrowOptimisticLockingFailureOnStaleVersionAfterUpdate() {
    // Arrange
    SnowflakeId itemId = SnowflakeId.of(3000L);
    BacklogItem item = BacklogItem.builder()
        .id(itemId)
        .title("Stale Update Test")
        .creatorName("Creator")
        .scrumTeamId(SnowflakeId.of(10L))
        .build();
    repository.save(item);
    entityManager.flush();
    entityManager.clear();

    // 古いバージョンのエンティティを読み込む
    BacklogItem staleItem = entityManager.find(BacklogItem.class, itemId);

    // 1次キャッシュをクリアして、別の管理インスタンスとして再度読み込む
    entityManager.detach(staleItem);
    BacklogItem freshItem = entityManager.find(BacklogItem.class, itemId);
    freshItem.update("Fresh Title", null, null, null, null);
    entityManager.flush();

    // 古いバージョンのエンティティを再アタッチすると楽観ロック例外が発生する
    assertThatThrownBy(() -> entityManager.merge(staleItem))
        .isInstanceOf(OptimisticLockException.class);
  }
}
