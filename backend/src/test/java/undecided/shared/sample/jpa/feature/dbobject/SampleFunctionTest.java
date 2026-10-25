package undecided.shared.sample.jpa.feature.dbobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.Arrays;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("small")
@DisplayName("SampleFunctionのテスト")
@ExtendWith(MockitoExtension.class)
class SampleFunctionTest {

  @Mock private EntityManager entityManager;

  @Mock private Query query;

  @InjectMocks private SampleFunction sampleFunction;

  @Nested
  @DisplayName("countPartiesByTypeメソッドのテスト")
  class CountPartiesByTypeTest {

    @Test
    @DisplayName("PERSONタイプのパーティ数が正常に取得される")
    void shouldCountPersonParties() {
      // Arrange
      when(entityManager.createNativeQuery(anyString())).thenReturn(query);
      when(query.setParameter(anyString(), "PERSON")).thenReturn(query);
      when(query.getSingleResult()).thenReturn(100L);

      // Act
      Long count = sampleFunction.countPartiesByType("PERSON");

      // Assert
      assertThat(count).isEqualTo(100L);
    }

    @Test
    @DisplayName("COMPANYタイプのパーティ数が正常に取得される")
    void shouldCountCompanyParties() {
      // Arrange
      when(entityManager.createNativeQuery(anyString())).thenReturn(query);
      when(query.setParameter(anyString(), "COMPANY")).thenReturn(query);
      when(query.getSingleResult()).thenReturn(50L);

      // Act
      Long count = sampleFunction.countPartiesByType("COMPANY");

      // Assert
      assertThat(count).isEqualTo(50L);
    }

    @Test
    @DisplayName("存在しないタイプのパーティ数は0を返す")
    void shouldReturnZeroForNonExistentType() {
      // Arrange
      when(entityManager.createNativeQuery(anyString())).thenReturn(query);
      when(query.setParameter(anyString(), "UNKNOWN")).thenReturn(query);
      when(query.getSingleResult()).thenReturn(0L);

      // Act
      Long count = sampleFunction.countPartiesByType("UNKNOWN");

      // Assert
      assertThat(count).isEqualTo(0L);
    }
  }

  @Nested
  @DisplayName("countAllPartiesByTypeメソッドのテスト")
  class CountAllPartiesByTypeTest {

    @Test
    @DisplayName("全パーティタイプごとのカウントが正常に取得される")
    void shouldCountAllPartiesByType() {
      // Arrange
      Object[] row1 = new Object[]{"PERSON", 100L};
      Object[] row2 = new Object[]{"COMPANY", 50L};
      when(entityManager.createNativeQuery(anyString())).thenReturn(query);
      when(query.getResultList()).thenReturn(Arrays.asList(row1, row2));

      // Act
      Map<String, Long> result = sampleFunction.countAllPartiesByType();

      // Assert
      assertThat(result).isNotNull();
      assertThat(result).hasSize(2);
      assertThat(result.get("PERSON")).isEqualTo(100L);
      assertThat(result.get("COMPANY")).isEqualTo(50L);
    }

    @Test
    @DisplayName("パーティが存在しない場合は空のマップを返す")
    void shouldReturnEmptyMapWhenNoParties() {
      // Arrange
      when(entityManager.createNativeQuery(anyString())).thenReturn(query);
      when(query.getResultList()).thenReturn(Arrays.asList());

      // Act
      Map<String, Long> result = sampleFunction.countAllPartiesByType();

      // Assert
      assertThat(result).isNotNull();
      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("単一のパーティタイプのみが存在する場合")
    void shouldReturnSingleTypeWhenOnlyOneExists() {
      // Arrange
      Object[] row1 = new Object[]{"PERSON", 200L};
      when(entityManager.createNativeQuery(anyString())).thenReturn(query);
      when(query.getResultList()).thenReturn(Arrays.asList(row1));

      // Act
      Map<String, Long> result = sampleFunction.countAllPartiesByType();

      // Assert
      assertThat(result).isNotNull();
      assertThat(result).hasSize(1);
      assertThat(result.get("PERSON")).isEqualTo(200L);
    }
  }
}
