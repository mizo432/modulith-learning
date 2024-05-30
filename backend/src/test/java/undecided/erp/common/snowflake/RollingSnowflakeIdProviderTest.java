package undecided.erp.common.snowflake;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class RollingSnowflakeIdProviderTest {

  @Test
  void testInitializeWithValidArray() {
    RollingSnowflakeIdProvider.initialize(1L, 2L, 3L);
    assertThat(SnowflakeIdProvider.generateId()).isEqualTo(1L);
    assertThat(SnowflakeIdProvider.generateId()).isEqualTo(2L);
    assertThat(SnowflakeIdProvider.generateId()).isEqualTo(3L);
    assertThat(SnowflakeIdProvider.generateId()).isEqualTo(1L);
  }

  @Test
  void testInitializeWithSingleValue() {
    assertThatThrownBy(RollingSnowflakeIdProvider::initialize).isInstanceOf(
        IllegalArgumentException.class);
    ;
  }


  @Test
  void testClear() {
    RollingSnowflakeIdProvider.initialize(1L, 2L, 3L);
    RollingSnowflakeIdProvider.clear();

    assertThat(SnowflakeIdProvider.generateId())
        .isNotEqualTo(1L);
  }
}
