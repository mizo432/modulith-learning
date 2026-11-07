package undecided.apigateway.config.ratelimit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RateLimitPropertiesTest {

  @Test
  @DisplayName("デフォルト値が正しく設定されている")
  void defaultValues() {
    var properties = new RateLimitProperties();

    assertThat(properties.getCapacity()).isEqualTo(100);
    assertThat(properties.getBurstCapacity()).isEqualTo(200);
    assertThat(properties.getPeriodSeconds()).isEqualTo(60);
    assertThat(properties.getAuthCapacity()).isEqualTo(5);
    assertThat(properties.getAuthBurstCapacity()).isEqualTo(10);
    assertThat(properties.getWriteCapacity()).isEqualTo(30);
    assertThat(properties.getWriteBurstCapacity()).isEqualTo(60);
  }

  @Test
  @DisplayName("カスタム値が設定できる")
  void customValues() {
    var properties = new RateLimitProperties();
    properties.setCapacity(50);
    properties.setBurstCapacity(100);
    properties.setPeriodSeconds(30);
    properties.setAuthCapacity(3);
    properties.setAuthBurstCapacity(5);
    properties.setWriteCapacity(15);
    properties.setWriteBurstCapacity(30);

    assertThat(properties.getCapacity()).isEqualTo(50);
    assertThat(properties.getBurstCapacity()).isEqualTo(100);
    assertThat(properties.getPeriodSeconds()).isEqualTo(30);
    assertThat(properties.getAuthCapacity()).isEqualTo(3);
    assertThat(properties.getAuthBurstCapacity()).isEqualTo(5);
    assertThat(properties.getWriteCapacity()).isEqualTo(15);
    assertThat(properties.getWriteBurstCapacity()).isEqualTo(30);
  }

  @Test
  @DisplayName("getPeriod()が正しいDurationを返す")
  void getPeriod() {
    var properties = new RateLimitProperties();
    properties.setPeriodSeconds(120);

    assertThat(properties.getPeriod().getSeconds()).isEqualTo(120);
  }
}
