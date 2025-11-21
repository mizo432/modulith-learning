package undecided.erp.common.uuidV7Provider;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import undecided.erp.common.precondition.IndexedRuntimeException;

@DisplayName("RollingUuidV7IdProviderのテスト")
class RollingUuidV7IdProviderTest {

  @Nested
  @DisplayName("internalNewInstanceメソッドのテスト")
  class InternalNewInstanceTests {

    @Test
    @DisplayName("事前定義済みUUIDが順番に返されるべき")
    void shouldReturnUUIDsInOrder() {
      // Arrange
      String[] predefinedUUIDs = {
        "123e4567-e89b-12d3-a456-426614174000",
        "123e4567-e89b-12d3-a456-426614174001",
        "123e4567-e89b-12d3-a456-426614174002"
      };
      RollingUuidV7IdProvider.initialize(predefinedUUIDs);

      // Act & Assert
      assertThat(UuidV7Provider.newInstance().toString()).isEqualTo(predefinedUUIDs[0]);
      assertThat(UuidV7Provider.newInstance().toString()).isEqualTo(predefinedUUIDs[1]);
      assertThat(UuidV7Provider.newInstance().toString()).isEqualTo(predefinedUUIDs[2]);
    }

    @Test
    @DisplayName("UUIDリストが環状に処理されるべき")
    void shouldCycleThroughUUIDs() {
      // Arrange
      String[] predefinedUUIDs = {
        "123e4567-e89b-12d3-a456-426614174000", "123e4567-e89b-12d3-a456-426614174001"
      };
      RollingUuidV7IdProvider.initialize(predefinedUUIDs);

      // Act & Assert
      assertThat(UuidV7Provider.newInstance().toString()).isEqualTo(predefinedUUIDs[0]);
      assertThat(UuidV7Provider.newInstance().toString()).isEqualTo(predefinedUUIDs[1]);
      assertThat(UuidV7Provider.newInstance().toString()).isEqualTo(predefinedUUIDs[0]);
      assertThat(UuidV7Provider.newInstance().toString()).isEqualTo(predefinedUUIDs[1]);
    }

    @Test
    @DisplayName("UUIDリストにnullを含む場合は例外がスローされるべき")
    void shouldThrowExceptionWhenUUIDListContainsNull() {
      // Arrange
      String[] invalidUUIDs = {
        "123e4567-e89b-12d3-a456-426614174000", null, "123e4567-e89b-12d3-a456-426614174002"
      };

      // Assert
      assertThatThrownBy(() -> RollingUuidV7IdProvider.initialize(invalidUUIDs))
          .isInstanceOf(IndexedRuntimeException.class)
          .hasMessageContaining("UUIDs must not contain null");
    }

    @Test
    @DisplayName("UUIDリストが空の場合は例外がスローされるべき")
    void shouldThrowExceptionWhenUUIDListIsEmpty() {
      // Arrange & Act & Assert
      assertThatThrownBy(() -> RollingUuidV7IdProvider.initialize())
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("UUIDs must not be empty");
    }

    @Test
    @DisplayName("UUIDリストが初期化されていない場合、デフォルト生成にフォールバックされるべき")
    void shouldFallbackToDefaultWhenNotInitialized() {
      // Arrange
      RollingUuidV7IdProvider.clear();

      // Act
      UUID generatedUUID = UuidV7Provider.newInstance();

      // Assert
      assertThat(generatedUUID).isNotNull();
      assertThat(generatedUUID.version())
          .isEqualTo(7); // Ensures it's a randomly generated UUID (version 4)
      RollingUuidV7IdProvider.clear();
    }
  }
}
