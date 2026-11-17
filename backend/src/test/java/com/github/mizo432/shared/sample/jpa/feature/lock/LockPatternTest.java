package com.github.mizo432.shared.sample.jpa.feature.lock;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.LockModeType;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

@Tag("small")
@DisplayName("@Lockパターンの単体テスト")
class LockPatternTest {

  @Nested
  @DisplayName("SampleOrderSequenceエンティティのテスト")
  class SampleOrderSequenceTest {

    @Test
    @DisplayName("SampleOrderSequenceが正常に作成される")
    void shouldCreateSampleOrderSequence() {
      // Arrange
      Long id = 1L;
      String sequenceName = "ORDER_NO";
      Long currentValue = 100L;

      // Act
      SampleOrderSequence sequence = new SampleOrderSequence(id, sequenceName, currentValue);

      // Assert
      assertThat(sequence).isNotNull();
      assertThat(sequence.getId()).isEqualTo(id);
      assertThat(sequence.getSequenceName()).isEqualTo(sequenceName);
      assertThat(sequence.getCurrentValue()).isEqualTo(currentValue);
    }
  }

  @Nested
  @DisplayName("SampleOrderSequenceRepositoryの@Lockメソッドのテスト")
  class LockMethodTest {

    @Test
    @DisplayName("findBySequenceNameWithLockメソッドに@Lockアノテーションが付与されている")
    void shouldHaveLockAnnotationOnFindBySequenceNameWithLock() throws NoSuchMethodException {
      // Arrange
      Method method =
          SampleOrderSequenceRepository.class.getMethod("findBySequenceNameWithLock", String.class);

      // Act & Assert
      Lock lock = method.getAnnotation(Lock.class);
      assertThat(lock).isNotNull();
      assertThat(lock.value()).isEqualTo(LockModeType.PESSIMISTIC_WRITE);
    }

    @Test
    @DisplayName("findByIdWithLockメソッドに@Lockアノテーションが付与されている")
    void shouldHaveLockAnnotationOnFindByIdWithLock() throws NoSuchMethodException {
      // Arrange
      Method method = SampleOrderSequenceRepository.class.getMethod("findByIdWithLock", Long.class);

      // Act & Assert
      Lock lock = method.getAnnotation(Lock.class);
      assertThat(lock).isNotNull();
      assertThat(lock.value()).isEqualTo(LockModeType.PESSIMISTIC_WRITE);
    }

    @Test
    @DisplayName("@Lockメソッドは@Queryアノテーションも持つ")
    void shouldLockMethodsHaveQueryAnnotation() throws NoSuchMethodException {
      // Arrange
      Method method1 =
          SampleOrderSequenceRepository.class.getMethod("findBySequenceNameWithLock", String.class);
      Method method2 =
          SampleOrderSequenceRepository.class.getMethod("findByIdWithLock", Long.class);

      // Assert
      assertThat(method1.getAnnotation(Query.class)).isNotNull();
      assertThat(method2.getAnnotation(Query.class)).isNotNull();
    }
  }
}
