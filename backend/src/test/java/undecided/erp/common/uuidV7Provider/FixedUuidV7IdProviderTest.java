package undecided.erp.common.uuidV7Provider;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("FixedUuidV7IdProviderのテスト")
class FixedUuidV7IdProviderTest {

  @Nested
  @DisplayName("initializeメソッドのテスト")
  class InitializeMethodTests {

    @Test
    @DisplayName("固定UUIDが正常に設定されるべき")
    void shouldSetFixedUUIDSuccessfully() {
      // Arrange
      UUID fixedUuid = UUID.randomUUID();

      // Act
      FixedUuidV7IdProvider.initialize(fixedUuid);
      UUID result = UuidV7Provider.newInstanse();

      // Assert
      assertThat(result).isNotNull().isEqualTo(fixedUuid);

      // Cleanup
      UuidV7Provider.clear();
    }
  }
}
