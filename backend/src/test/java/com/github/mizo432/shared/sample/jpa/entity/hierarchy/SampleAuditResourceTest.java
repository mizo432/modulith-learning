package com.github.mizo432.shared.sample.jpa.entity.hierarchy;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("SampleAuditResourceの単体テスト")
class SampleAuditResourceTest {

  /** テスト用の具象サブクラスです。 SampleAuditResourceはabstractなので、テストのために具象クラスを作成します。 */
  static class TestAuditEntity extends SampleAuditResource {
    // テスト用の空の実装
  }

  @Nested
  @DisplayName("prePersistメソッドのテスト")
  class PrePersistTest {

    @Test
    @DisplayName("prePersist呼び出しでcreatedAtとcreatedByが設定される")
    void shouldSetCreatedAtAndCreatedByWhenPrePersist() {
      // Arrange
      TestAuditEntity entity = new TestAuditEntity();

      // Act
      entity.prePersist();

      // Assert
      assertThat(entity.getCreatedAt()).isNotNull();
      assertThat(entity.getCreatedBy()).isEqualTo("system");
    }

    @Test
    @DisplayName("createdAtは現在時刻に近い値が設定される")
    void shouldSetCreatedAtToCurrentTime() {
      // Arrange
      TestAuditEntity entity = new TestAuditEntity();
      LocalDateTime before = LocalDateTime.now().minusSeconds(1);

      // Act
      entity.prePersist();

      // Assert
      LocalDateTime after = LocalDateTime.now().plusSeconds(1);
      assertThat(entity.getCreatedAt()).isAfter(before).isBefore(after);
    }
  }

  @Nested
  @DisplayName("preUpdateメソッドのテスト")
  class PreUpdateTest {

    @Test
    @DisplayName("preUpdate呼び出しでupdatedAtとupdatedByが設定される")
    void shouldSetUpdatedAtAndUpdatedByWhenPreUpdate() {
      // Arrange
      TestAuditEntity entity = new TestAuditEntity();

      // Act
      entity.preUpdate();

      // Assert
      assertThat(entity.getUpdatedAt()).isNotNull();
      assertThat(entity.getUpdatedBy()).isEqualTo("system");
    }

    @Test
    @DisplayName("updatedAtは現在時刻に近い値が設定される")
    void shouldSetUpdatedAtToCurrentTime() {
      // Arrange
      TestAuditEntity entity = new TestAuditEntity();
      LocalDateTime before = LocalDateTime.now().minusSeconds(1);

      // Act
      entity.preUpdate();

      // Assert
      LocalDateTime after = LocalDateTime.now().plusSeconds(1);
      assertThat(entity.getUpdatedAt()).isAfter(before).isBefore(after);
    }
  }

  @Nested
  @DisplayName("初期状態のテスト")
  class InitialStateTest {

    @Test
    @DisplayName("新規作成時はすべての監査フィールドがnullである")
    void shouldHaveNullAuditFieldsInitially() {
      // Arrange & Act
      TestAuditEntity entity = new TestAuditEntity();

      // Assert
      assertThat(entity.getCreatedAt()).isNull();
      assertThat(entity.getCreatedBy()).isNull();
      assertThat(entity.getUpdatedAt()).isNull();
      assertThat(entity.getUpdatedBy()).isNull();
    }
  }

  @Nested
  @DisplayName("prePersistとpreUpdateの組み合わせテスト")
  class CombinedTest {

    @Test
    @DisplayName("prePersist後にpreUpdateを実行すると両方のフィールドが設定される")
    void shouldSetAllFieldsWhenBothCalled() {
      // Arrange
      TestAuditEntity entity = new TestAuditEntity();

      // Act
      entity.prePersist();
      entity.preUpdate();

      // Assert
      assertThat(entity.getCreatedAt()).isNotNull();
      assertThat(entity.getCreatedBy()).isEqualTo("system");
      assertThat(entity.getUpdatedAt()).isNotNull();
      assertThat(entity.getUpdatedBy()).isEqualTo("system");
    }
  }
}
