package undecided.erp.common.uuidV7Provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("UuidV7Providerのテスト")
class UuidV7ProviderTest {

  @Nested
  @DisplayName("newInstanseメソッドのテスト")
  class NewInstanseTests {

    @Test
    @DisplayName("UUIDが正常に生成されるべき")
    void shouldGenerateValidUUID() {
      // Arrange & Act
      UUID generatedUUID = UuidV7Provider.newInstanse();

      // Assert
      assertThat(generatedUUID).isNotNull();
      assertThat(generatedUUID.version()).isEqualTo(4); // UUID.randomUUID() generates version 4
    }

    @Test
    @DisplayName("カスタムプロバイダーでUUIDが生成されるべき")
    void shouldGenerateUUIDFromCustomProvider() {
      // Arrange
      UuidV7Provider mockProvider = mock(UuidV7Provider.class);
      UUID mockUUID = UUID.randomUUID();
      when(mockProvider.internalNewInstance()).thenReturn(mockUUID);

      new UuidV7Provider(mockProvider); // Set custom provider

      // Act
      UUID generatedUUID = UuidV7Provider.newInstanse();

      // Assert
      assertThat(generatedUUID).isNotNull().isEqualTo(mockUUID);
      verify(mockProvider).internalNewInstance();
    }
  }
}
