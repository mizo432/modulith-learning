package com.github.mizo432.shared.sample.jpa.feature.transaction;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("@Transactionalパターンの単体テスト")
class TransactionPatternTest {

  @Nested
  @DisplayName("SampleTransactionServiceのテスト")
  class SampleTransactionServiceTest {

    @Test
    @DisplayName("SampleTransactionServiceには@Transactionalアノテーションが付与されている")
    void shouldHaveTransactionalAnnotation() {
      // Arrange & Act
      Transactional transactional =
          SampleTransactionService.class.getAnnotation(Transactional.class);

      // Assert
      assertThat(transactional).isNotNull();
    }

    @Test
    @DisplayName("findWithReadOnlyTransactionはreadOnly=trueである")
    void shouldFindWithReadOnlyTransactionBeReadOnly() throws NoSuchMethodException {
      // Arrange
      Method method =
          SampleTransactionService.class.getMethod(
              "findWithReadOnlyTransaction", SnowflakeId.class);

      // Act & Assert
      Transactional transactional = method.getAnnotation(Transactional.class);
      assertThat(transactional).isNotNull();
      assertThat(transactional.readOnly()).isTrue();
    }

    @Test
    @DisplayName("saveWithNewTransactionはPropagation.REQUIRES_NEWである")
    void shouldSaveWithNewTransactionBeRequiresNew() throws NoSuchMethodException {
      // Arrange
      Method method =
          SampleTransactionService.class.getMethod("saveWithNewTransaction", String.class);

      // Act & Assert
      Transactional transactional = method.getAnnotation(Transactional.class);
      assertThat(transactional).isNotNull();
      assertThat(transactional.propagation()).isEqualTo(Propagation.REQUIRES_NEW);
    }

    @Test
    @DisplayName("saveWithSerializableIsolationはIsolation.SERIALIZABLEである")
    void shouldSaveWithSerializableIsolationBeSerializable() throws NoSuchMethodException {
      // Arrange
      Method method =
          SampleTransactionService.class.getMethod("saveWithSerializableIsolation", String.class);

      // Act & Assert
      Transactional transactional = method.getAnnotation(Transactional.class);
      assertThat(transactional).isNotNull();
      assertThat(transactional.isolation()).isEqualTo(Isolation.SERIALIZABLE);
    }

    @Test
    @DisplayName("findWithOptionalTransactionはPropagation.SUPPORTSである")
    void shouldFindWithOptionalTransactionBeSupports() throws NoSuchMethodException {
      // Arrange
      Method method =
          SampleTransactionService.class.getMethod(
              "findWithOptionalTransaction", SnowflakeId.class);

      // Act & Assert
      Transactional transactional = method.getAnnotation(Transactional.class);
      assertThat(transactional).isNotNull();
      assertThat(transactional.propagation()).isEqualTo(Propagation.SUPPORTS);
    }

    @Test
    @DisplayName("saveWithRollbackForExceptionはrollbackFor=Exception.classである")
    void shouldSaveWithRollbackForExceptionHaveRollbackFor() throws NoSuchMethodException {
      // Arrange
      Method method =
          SampleTransactionService.class.getMethod("saveWithRollbackForException", String.class);

      // Act & Assert
      Transactional transactional = method.getAnnotation(Transactional.class);
      assertThat(transactional).isNotNull();
      assertThat(transactional.rollbackFor()).containsExactly(Exception.class);
    }
  }
}
