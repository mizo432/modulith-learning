package undecided.apigateway.config.ratelimit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("RateLimitPropertiesのテスト")
class RateLimitPropertiesTest {

  @Nested
  @DisplayName("デフォルト値のテスト")
  @SuppressWarnings("deprecation")
  class DefaultValuesTest {

    @Test
    @DisplayName("デフォルト値が正しく設定されていること")
    void shouldHaveCorrectDefaultValues() {
      // Act
      var properties = new RateLimitProperties();

      // Assert
      assertThat(properties.getCapacity())
          .as("デフォルトcapacityは100であること")
          .isEqualTo(100);
      assertThat(properties.getBurstCapacity())
          .as("デフォルトburstCapacityは200であること")
          .isEqualTo(200);
      assertThat(properties.getPeriodSeconds())
          .as("デフォルトperiodSecondsは60であること")
          .isEqualTo(60);
      assertThat(properties.getAuthCapacity())
          .as("デフォルトauthCapacityは5であること")
          .isEqualTo(5);
      assertThat(properties.getAuthBurstCapacity())
          .as("デフォルトauthBurstCapacityは10であること")
          .isEqualTo(10);
      assertThat(properties.getAuthRefreshCapacity())
          .as("デフォルトauthRefreshCapacityは10であること")
          .isEqualTo(10);
      assertThat(properties.getWriteCapacity())
          .as("デフォルトwriteCapacityは30であること")
          .isEqualTo(30);
      assertThat(properties.getWriteBurstCapacity())
          .as("デフォルトwriteBurstCapacityは60であること")
          .isEqualTo(60);
    }
  }

  @Nested
  @DisplayName("カスタム値設定のテスト")
  @SuppressWarnings("deprecation")
  class CustomValuesTest {

    @Test
    @DisplayName("カスタム値が設定できること")
    void shouldSetCustomValues() {
      // Arrange
      var properties = new RateLimitProperties();

      // Act
      properties.setCapacity(50);
      properties.setBurstCapacity(100);
      properties.setPeriodSeconds(30);
      properties.setAuthCapacity(3);
      properties.setAuthBurstCapacity(5);
      properties.setAuthRefreshCapacity(8);
      properties.setWriteCapacity(15);
      properties.setWriteBurstCapacity(30);

      // Assert
      assertThat(properties.getCapacity())
          .as("capacityは50に設定されること")
          .isEqualTo(50);
      assertThat(properties.getBurstCapacity())
          .as("burstCapacityは100に設定されること")
          .isEqualTo(100);
      assertThat(properties.getPeriodSeconds())
          .as("periodSecondsは30に設定されること")
          .isEqualTo(30);
      assertThat(properties.getAuthCapacity())
          .as("authCapacityは3に設定されること")
          .isEqualTo(3);
      assertThat(properties.getAuthBurstCapacity())
          .as("authBurstCapacityは5に設定されること")
          .isEqualTo(5);
      assertThat(properties.getAuthRefreshCapacity())
          .as("authRefreshCapacityは8に設定されること")
          .isEqualTo(8);
      assertThat(properties.getWriteCapacity())
          .as("writeCapacityは15に設定されること")
          .isEqualTo(15);
      assertThat(properties.getWriteBurstCapacity())
          .as("writeBurstCapacityは30に設定されること")
          .isEqualTo(30);
    }
  }

  @Nested
  @DisplayName("getPeriodのテスト")
  class GetPeriodTest {

    @Test
    @DisplayName("getPeriod()が正しいDurationを返すこと")
    void shouldReturnCorrectDuration() {
      // Arrange
      var properties = new RateLimitProperties();
      properties.setPeriodSeconds(120);

      // Act
      long seconds = properties.getPeriod().getSeconds();

      // Assert
      assertThat(seconds)
          .as("Durationの秒数は120であること")
          .isEqualTo(120);
    }
  }

  @Nested
  @DisplayName("境界値のテスト")
  class BoundaryValuesTest {

    @Test
    @DisplayName("負の値を設定した場合、IllegalArgumentExceptionがスローされること")
    void shouldRejectNegativeCapacity() {
      // Arrange
      var properties = new RateLimitProperties();

      // Act & Assert
      assertThatThrownBy(() -> properties.setCapacity(-1))
          .as("負のcapacityはIllegalArgumentExceptionをスローすること")
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("0を設定した場合、IllegalArgumentExceptionがスローされること")
    void shouldRejectZeroPeriodSeconds() {
      // Arrange
      var properties = new RateLimitProperties();

      // Act & Assert
      assertThatThrownBy(() -> properties.setPeriodSeconds(0))
          .as("0のperiodSecondsはIllegalArgumentExceptionをスローすること")
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("負の値をauthCapacityに設定した場合、IllegalArgumentExceptionがスローされること")
    void shouldRejectNegativeAuthCapacity() {
      // Arrange
      var properties = new RateLimitProperties();

      // Act & Assert
      assertThatThrownBy(() -> properties.setAuthCapacity(-5))
          .as("負のauthCapacityはIllegalArgumentExceptionをスローすること")
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("0をwriteCapacityに設定した場合、IllegalArgumentExceptionがスローされること")
    void shouldRejectZeroWriteCapacity() {
      // Arrange
      var properties = new RateLimitProperties();

      // Act & Assert
      assertThatThrownBy(() -> properties.setWriteCapacity(0))
          .as("0のwriteCapacityはIllegalArgumentExceptionをスローすること")
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("0をauthRefreshCapacityに設定した場合、IllegalArgumentExceptionがスローされること")
    void shouldRejectZeroAuthRefreshCapacity() {
      // Arrange
      var properties = new RateLimitProperties();

      // Act & Assert
      assertThatThrownBy(() -> properties.setAuthRefreshCapacity(0))
          .as("0のauthRefreshCapacityはIllegalArgumentExceptionをスローすること")
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("負の値をburstCapacityに設定した場合、IllegalArgumentExceptionがスローされること")
    @SuppressWarnings("deprecation")
    void shouldRejectNegativeBurstCapacity() {
      // Arrange
      var properties = new RateLimitProperties();

      // Act & Assert
      assertThatThrownBy(() -> properties.setBurstCapacity(-1))
          .as("負のburstCapacityはIllegalArgumentExceptionをスローすること")
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("負の値をauthBurstCapacityに設定した場合、IllegalArgumentExceptionがスローされること")
    @SuppressWarnings("deprecation")
    void shouldRejectNegativeAuthBurstCapacity() {
      // Arrange
      var properties = new RateLimitProperties();

      // Act & Assert
      assertThatThrownBy(() -> properties.setAuthBurstCapacity(-1))
          .as("負のauthBurstCapacityはIllegalArgumentExceptionをスローすること")
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("負の値をwriteBurstCapacityに設定した場合、IllegalArgumentExceptionがスローされること")
    @SuppressWarnings("deprecation")
    void shouldRejectNegativeWriteBurstCapacity() {
      // Arrange
      var properties = new RateLimitProperties();

      // Act & Assert
      assertThatThrownBy(() -> properties.setWriteBurstCapacity(-1))
          .as("負のwriteBurstCapacityはIllegalArgumentExceptionをスローすること")
          .isInstanceOf(IllegalArgumentException.class);
    }
  }
}
