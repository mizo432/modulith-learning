package com.github.mizo432.shared.sample.jpa.feature.dbobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("small")
@DisplayName("SampleProcedureのテスト")
@ExtendWith(MockitoExtension.class)
class SampleProcedureTest {

  @Mock private EntityManager entityManager;

  @Mock private Query query;

  @InjectMocks private SampleProcedure sampleProcedure;

  @Nested
  @DisplayName("getNextOrderIdメソッドのテスト")
  class GetNextOrderIdTest {

    @Test
    @DisplayName("次の注文番号が正常に取得される")
    void shouldGetNextOrderId() {
      // Arrange
      when(entityManager.createNativeQuery(anyString())).thenReturn(query);
      when(query.getSingleResult()).thenReturn(123L);

      // Act
      Long orderId = sampleProcedure.getNextOrderId();

      // Assert
      assertThat(orderId).isEqualTo(123L);
    }

    @Test
    @DisplayName("シーケンス値がインクリメントされて取得される")
    void shouldGetIncrementedOrderId() {
      // Arrange
      when(entityManager.createNativeQuery(anyString())).thenReturn(query);
      when(query.getSingleResult()).thenReturn(1001L);

      // Act
      Long orderId = sampleProcedure.getNextOrderId();

      // Assert
      assertThat(orderId).isEqualTo(1001L);
    }
  }

  @Nested
  @DisplayName("getNextPartyIdメソッドのテスト")
  class GetNextPartyIdTest {

    @Test
    @DisplayName("次のパーティ番号が正常に取得される")
    void shouldGetNextPartyId() {
      // Arrange
      when(entityManager.createNativeQuery(anyString())).thenReturn(query);
      when(query.getSingleResult()).thenReturn(456L);

      // Act
      Long partyId = sampleProcedure.getNextPartyId();

      // Assert
      assertThat(partyId).isEqualTo(456L);
    }

    @Test
    @DisplayName("パーティシーケンス値が正常に取得される")
    void shouldGetPartySequenceValue() {
      // Arrange
      when(entityManager.createNativeQuery(anyString())).thenReturn(query);
      when(query.getSingleResult()).thenReturn(2001L);

      // Act
      Long partyId = sampleProcedure.getNextPartyId();

      // Assert
      assertThat(partyId).isEqualTo(2001L);
    }
  }
}
