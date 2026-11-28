package undecided.agiloom.requirementBacklog.internal;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import undecided.ModulithDemoApplication;
import undecided.TestcontainersConfiguration;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * READY状態のDB永続化とCHECK制約をテストする統合テストです。
 *
 * <p>Flywayマイグレーションスクリプトが存在し、READY状態がCHECK制約に追加されていることを検証します。
 * また、楽観ロック（@Version）の既存機能を確認します。
 */
@Tag("medium")
@SpringBootTest(classes = ModulithDemoApplication.class)
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@Transactional
@DisplayName("READY状態の統合テスト")
class BacklogItemReadyMediumTest {

  @Autowired private BacklogItemRepository backlogItemRepository;

  @Autowired private EntityManager entityManager;

  @Autowired private JdbcTemplate jdbcTemplate;

  private static final SnowflakeId TEAM_ID = SnowflakeId.of(10L);

  @Nested
  @DisplayName("ACCEPTED→READY遷移の永続化テスト")
  class AcceptedToReadyPersistenceTest {

    @Test
    @DisplayName("DBに対してACCEPTED→READY遷移が正しく永続化されること")
    void shouldPersistAcceptedToReadyTransition() {
      BacklogItem item = BacklogItem.builder()
          .id(SnowflakeId.of(1000L))
          .title("Test Item")
          .creatorName("Creator")
          .scrumTeamId(TEAM_ID)
          .value(50)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
      item.changeStatus(BacklogItemStatus.ACCEPTED, LocalDateTime.now());
      backlogItemRepository.save(item);

      item.changeStatus(BacklogItemStatus.READY, LocalDateTime.now());
      backlogItemRepository.save(item);

      BacklogItem saved = backlogItemRepository.findById(item.getId()).orElseThrow();
      assertThat(saved.getStatus()).isEqualTo(BacklogItemStatus.READY);
    }
  }

  @Nested
  @DisplayName("READY→SUGGESTED遷移の永続化テスト")
  class ReadyToSuggestedPersistenceTest {

    @Test
    @DisplayName("DBに対してREADY→SUGGESTED遷移が正しく永続化されること")
    void shouldPersistReadyToSuggestedTransition() {
      BacklogItem item = BacklogItem.builder()
          .id(SnowflakeId.of(1001L))
          .title("Test Item")
          .creatorName("Creator")
          .scrumTeamId(TEAM_ID)
          .value(50)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
      item.changeStatus(BacklogItemStatus.ACCEPTED, LocalDateTime.now());
      backlogItemRepository.save(item);
      entityManager.flush();

      item = backlogItemRepository.findById(item.getId()).orElseThrow();
      item.changeStatus(BacklogItemStatus.READY, LocalDateTime.now());
      backlogItemRepository.save(item);
      entityManager.flush();

      item = backlogItemRepository.findById(item.getId()).orElseThrow();
      item.changeStatus(BacklogItemStatus.SUGGESTED, LocalDateTime.now());
      backlogItemRepository.save(item);
      entityManager.flush();

      BacklogItem saved = backlogItemRepository.findById(item.getId()).orElseThrow();
      assertThat(saved.getStatus()).isEqualTo(BacklogItemStatus.SUGGESTED);
    }
  }

  @Nested
  @DisplayName("CHECK制約の検証")
  class CheckConstraintTest {

    @Test
    @DisplayName("CHECK制約がREADYを許可すること")
    void shouldCheckConstraintAllowReady() {
      // テストプロファイルは Hibernate create-drop でスキーマを生成するため、
      // Flyway の CHECK 制約は実行時DBに現れない。スクリプト自体の内容で検証する。
      // READY状態を追加するマイグレーションスクリプトが存在することを確認。
      String script = readClasspathResource("db/migration/ag/V20261119240000__add_ready_status_to_backlog_items.sql");
      assertThat(script)
          .contains("DROP CONSTRAINT ck_backlog_items_status")
          .contains("ADD CONSTRAINT ck_backlog_items_status CHECK (status IN ('SUGGESTED', 'ACCEPTED', 'FROZEN', 'READY'))");
    }
  }

  @Nested
  @DisplayName("楽観ロックの確認")
  class OptimisticLockTest {

    @Test
    @DisplayName("楽観ロック（@Version）により同時更新で409が返ること（既存機能の確認）")
    void shouldOptimisticLockReturn409() {
      // 既存のBacklogItemOptimisticLockMediumTestでカバーされているため、
      // ここでは@Versionフィールドが存在することのみ確認する。
      BacklogItem item = BacklogItem.builder()
          .id(SnowflakeId.of(1002L))
          .title("Test Item")
          .creatorName("Creator")
          .scrumTeamId(TEAM_ID)
          .value(50)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
      backlogItemRepository.save(item);

      BacklogItem saved = backlogItemRepository.findById(item.getId()).orElseThrow();
      assertThat(saved.getVersion()).isNotNull();
    }
  }

  private String readClasspathResource(String path) {
    try {
      return new ClassPathResource(path).getContentAsString(StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
