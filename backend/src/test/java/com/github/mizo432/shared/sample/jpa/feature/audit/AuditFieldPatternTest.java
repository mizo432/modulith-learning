package com.github.mizo432.shared.sample.jpa.feature.audit;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.lang.reflect.Field;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("監査フィールドパターンの単体テスト")
class AuditFieldPatternTest {

  @Nested
  @DisplayName("@CreatedDateパターンのテスト")
  class CreatedDatePatternTest {

    @Test
    @DisplayName("SampleAuditCreatedDateはAuditingEntityListenerを登録している")
    void shouldHaveAuditingEntityListener() {
      // Arrange & Act
      EntityListeners entityListeners =
          SampleAuditCreatedDate.class.getAnnotation(EntityListeners.class);

      // Assert
      assertThat(entityListeners).isNotNull();
      assertThat(entityListeners.value()).contains(AuditingEntityListener.class);
    }

    @Test
    @DisplayName("@CreatedDateアノテーションがcreatedAtフィールドに付与されている")
    void shouldHaveCreatedDateAnnotation() throws NoSuchFieldException {
      // Arrange
      Field createdAtField = SampleAuditCreatedDate.class.getDeclaredField("createdAt");

      // Act & Assert
      assertThat(createdAtField.getAnnotation(CreatedDate.class)).isNotNull();
    }

    @Test
    @DisplayName("SampleAuditCreatedDateが正常に作成される")
    void shouldCreateSampleAuditCreatedDate() {
      // Arrange
      SnowflakeId id = SnowflakeId.of(1L);
      String name = "テスト";

      // Act
      SampleAuditCreatedDate entity = new SampleAuditCreatedDate(id, name);

      // Assert
      assertThat(entity).isNotNull();
      assertThat(entity.getId()).isEqualTo(id);
      assertThat(entity.getName()).isEqualTo(name);
      assertThat(entity.getCreatedBy()).isEqualTo("system");
      assertThat(entity.getCreatedAt()).isNull();
    }
  }

  @Nested
  @DisplayName("@PrePersist/@PreUpdateパターンのテスト")
  class CallbackPatternTest {

    @Test
    @DisplayName("SampleAuditCallbackはAuditingEntityListenerを登録していない")
    void shouldNotHaveAuditingEntityListener() {
      // Arrange & Act
      EntityListeners entityListeners =
          SampleAuditCallback.class.getAnnotation(EntityListeners.class);

      // Assert
      assertThat(entityListeners).isNull();
    }

    @Test
    @DisplayName("@PrePersistアノテーションが付与されたメソッドが存在する")
    void shouldHavePrePersistMethod() {
      // Arrange & Act
      boolean hasPrePersist =
          Arrays.stream(SampleAuditCallback.class.getDeclaredMethods())
              .anyMatch(m -> m.isAnnotationPresent(PrePersist.class));

      // Assert
      assertThat(hasPrePersist).isTrue();
    }

    @Test
    @DisplayName("@PreUpdateアノテーションが付与されたメソッドが存在する")
    void shouldHavePreUpdateMethod() {
      // Arrange & Act
      boolean hasPreUpdate =
          Arrays.stream(SampleAuditCallback.class.getDeclaredMethods())
              .anyMatch(m -> m.isAnnotationPresent(PreUpdate.class));

      // Assert
      assertThat(hasPreUpdate).isTrue();
    }

    @Test
    @DisplayName("SampleAuditCallbackが正常に作成される")
    void shouldCreateSampleAuditCallback() {
      // Arrange
      SnowflakeId id = SnowflakeId.of(1L);
      String name = "テスト";

      // Act
      SampleAuditCallback entity = new SampleAuditCallback(id, name);

      // Assert
      assertThat(entity).isNotNull();
      assertThat(entity.getId()).isEqualTo(id);
      assertThat(entity.getName()).isEqualTo(name);
      assertThat(entity.getCreatedAt()).isNull();
      assertThat(entity.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("prePersistメソッドが監査フィールドを設定する")
    void shouldPrePersistSetAuditFields() {
      // Arrange
      SampleAuditCallback entity = new SampleAuditCallback(SnowflakeId.of(1L), "テスト");

      // Act
      entity.prePersist();

      // Assert
      assertThat(entity.getCreatedAt()).isNotNull();
      assertThat(entity.getCreatedBy()).isEqualTo("system");
    }

    @Test
    @DisplayName("preUpdateメソッドが更新フィールドを設定する")
    void shouldPreUpdateSetUpdateFields() {
      // Arrange
      SampleAuditCallback entity = new SampleAuditCallback(SnowflakeId.of(1L), "テスト");

      // Act
      entity.preUpdate();

      // Assert
      assertThat(entity.getUpdatedAt()).isNotNull();
      assertThat(entity.getUpdatedBy()).isEqualTo("system");
    }
  }
}
