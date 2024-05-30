package undecided.erp.shared.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import undecided.erp.common.snowflake.SnowflakeIdProvider;
import undecided.erp.common.snowflake.StaticSnowflakeIdProvider;

class SnowflakeIdTest {

  @BeforeEach
  void setUo() {
    StaticSnowflakeIdProvider.initialize(10L);
    ;

  }

  @AfterEach
  void tearDown() {
    SnowflakeIdProvider.clear();

  }

  @Test
  void testNewInstance_notNull() {
    assertThat(SnowflakeId.newInstance()).isNotNull();
  }

  @Test
  void testToString_persistency() {
    SnowflakeId<?> snowflakeId = SnowflakeId.newInstance();
    assertThat(snowflakeId.toString()).isEqualTo("10");
  }

  @Test
  void testEquals_reflexivity() {
    SnowflakeId<?> snowflakeId = SnowflakeId.newInstance();
    assertThat(snowflakeId.equals(snowflakeId)).isTrue();
  }

  @Test
  void testEquals_symmetry() {
    SnowflakeId<?> snowflakeId1 = SnowflakeId.newInstance();
    SnowflakeId<?> snowflakeId2 = SnowflakeId.of(snowflakeId1.getValue());
    assertThat(snowflakeId1.equals(snowflakeId2)).isTrue();
    assertThat(snowflakeId2.equals(snowflakeId1)).isTrue();
  }

  @Test
  void testEquals_transitivity() {
    SnowflakeId<?> snowflakeId1 = SnowflakeId.newInstance();
    SnowflakeId<?> snowflakeId2 = SnowflakeId.of(snowflakeId1.getValue());
    SnowflakeId<?> snowflakeId3 = SnowflakeId.of(snowflakeId1.getValue());
    assertThat(snowflakeId1.equals(snowflakeId2)).isTrue();
    assertThat(snowflakeId2.equals(snowflakeId3)).isTrue();
    assertThat(snowflakeId1.equals(snowflakeId3)).isTrue();
  }

  @Test
  void testEquals_null() {
    SnowflakeId<?> snowflakeId = SnowflakeId.newInstance();
    assertThat(snowflakeId.equals(null)).isFalse();
  }
}
