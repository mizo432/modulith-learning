package undecided.erp.common.uuidV7Provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import undecided.shared.common.precondition.IndexedRuntimeException;

@DisplayName("RollingUuidV7IdProviderTest 内部ロジックのテスト")
class RollingUuidV7IdProviderTest {
  @AfterEach
  void tearEach() {
    UuidV7Provider.clear();
  }

  @Nested
  @DisplayName("internalNewInstanceメソッドのテスト")
  class InternalNewInstanceTests {

    @Test
    @DisplayName("指定されたUUID順に生成されること")
    void shouldGenerateUuidsInProvidedOrder() {
      // Arrange
      String[] uuids = {
        "f47ac10b-58cc-4372-a567-0e02b2c3d479",
        "a8098c1a-f86e-11da-bd1a-00112444be1e",
        "16fd2706-8baf-433b-82eb-8c7fada847da"
      };
      RollingUuidV7IdProvider.initialize(uuids);

      // Act & Assert
      assertThat(UuidV7Provider.newInstance().toString()).isEqualTo(uuids[0]);
      assertThat(UuidV7Provider.newInstance().toString()).isEqualTo(uuids[1]);
      assertThat(UuidV7Provider.newInstance().toString()).isEqualTo(uuids[2]);
      // Circular behavior check
      assertThat(UuidV7Provider.newInstance().toString()).isEqualTo(uuids[0]);
    }

    @Test
    @DisplayName("UUIDリストが1つの場合でも正しく循環すること")
    void shouldCycleCorrectlyWithSingleUuid() {
      // Arrange
      String uuid = "f47ac10b-58cc-4372-a567-0e02b2c3d479";
      RollingUuidV7IdProvider.initialize(uuid);

      // Act & Assert
      assertThat(UuidV7Provider.newInstance().toString()).isEqualTo(uuid);
      assertThat(UuidV7Provider.newInstance().toString()).isEqualTo(uuid);
    }

    @Test
    @DisplayName("引数がnullの場合はNullPointerExceptionがスローされること")
    void shouldThrowIllegalArgumentExceptionWhenArgumentIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> RollingUuidV7IdProvider.initialize((String[]) null))
          .isInstanceOf(NullPointerException.class)
          .hasMessage("uuids must not be null.");
    }

    @Test
    @DisplayName("空のリストが引数の場合はIllegalArgumentExceptionがスローされること")
    void shouldThrowIllegalArgumentExceptionWhenArgumentIsEmpty() {
      // Act & Assert
      assertThatThrownBy(() -> RollingUuidV7IdProvider.initialize())
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("UUIDs must not be empty");
    }

    @Test
    @DisplayName("nullを含むリストが渡された場合にIndexedRuntimeExceptionがスローされること")
    void shouldThrowIndexedRuntimeExceptionWhenListContainsNull() {
      // Arrange
      String[] uuids = {"f47ac10b-58cc-4372-a567-0e02b2c3d479", null};

      // Act & Assert
      assertThatThrownBy(() -> RollingUuidV7IdProvider.initialize(uuids))
          .isInstanceOf(IndexedRuntimeException.class)
          .hasCauseInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("UUIDs must not contain null");
    }

    @Test
    @DisplayName("clearメソッドで状態がリセットされること")
    void shouldResetStateWhenCleared() {
      // Arrange
      String[] uuids = {
        "f47ac10b-58cc-4372-a567-0e02b2c3d479", "a8098c1a-f86e-11da-bd1a-00112444be1e"
      };
      RollingUuidV7IdProvider.initialize(uuids);

      // Act
      assertThat(UuidV7Provider.newInstance().toString()).isEqualTo(uuids[0]);
      RollingUuidV7IdProvider.clear();

      // Assert
      UUID newUuid = UuidV7Provider.newInstance(); // Should use the fallback generator
      assertThat(newUuid).isNotNull();
      assertThat(newUuid.toString()).isNotEqualTo(uuids[0]);
      assertThat(newUuid.toString()).isNotEqualTo(uuids[1]);
    }
  }
}
