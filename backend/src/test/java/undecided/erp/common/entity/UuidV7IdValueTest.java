package undecided.erp.common.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import undecided.shared.common.uuidV7Provider.UuidV7Provider;

@DisplayName("UuidV7IdValueのテスト")
class UuidV7IdValueTest {

  @Nested
  @DisplayName("newInstanceのテスト")
  class NewInstanceTest {

    @Test
    @DisplayName("正常ケース: インスタンスが生成されるべき")
    void shouldCreateNewInstanceSuccessfully() {
      try (var mockedProvider = mockStatic(UuidV7Provider.class)) {
        UUID mockUuid = UUID.randomUUID();
        mockedProvider.when(UuidV7Provider::newInstance).thenReturn(mockUuid);

        UuidValue<UuidV7IdValue> result = UuidV7IdValue.newInstance();

        assertThat(result).isNotNull();
        assertThat(result.value()).isEqualTo(mockUuid);
      }
    }

    @Test
    @DisplayName("異常ケース: UUIDがnullの場合、例外は発生しないべき")
    void shouldHandleNullUuidGracefully() {
      try (var mockedProvider = mockStatic(UuidV7Provider.class)) {
        mockedProvider.when(UuidV7Provider::newInstance).thenReturn(null);

        UuidValue<UuidV7IdValue> result = UuidV7IdValue.newInstance();

        assertThat(result).isNotNull();
        assertThat(result.value()).isNull();
      }
    }
  }
}
