package undecided.erp.shared.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import undecided.erp.common.dateProvider.StaticDateTimeProvider;
import undecided.erp.common.ipaddress.StaticIpAddressProvider;
import undecided.erp.common.snowflake.SnowflakeIdGenerator;

class SnowflakeIdTest {

  @BeforeEach
  void setUo() {
    StaticDateTimeProvider.initialize(LocalDateTime.of(2024, 2, 3, 4, 5, 6, 7000000));
    StaticIpAddressProvider.initialize("1.2.3.4");
    SnowflakeIdGenerator.initialise();

  }

  @AfterEach
  void tearDown() {
    StaticIpAddressProvider.clear();
    StaticDateTimeProvider.clear();
    SnowflakeIdGenerator.initialise();

  }

  @Test
  void testNewInstance_notNull() {
    assertThat(SnowflakeId.newInstance()).isNotNull();
  }

  @Test
  void testNewInstance_unique() {
    assertThat(SnowflakeId.newInstance()).isNotEqualTo(SnowflakeId.newInstance());
  }

  @Test
  void testToString_persistency() {
    SnowflakeId<?> snowflakeId = SnowflakeId.newInstance();
    System.out.println(snowflakeId);
    assertThat(snowflakeId.toString()).isEqualTo("408699298413449216");
  }
}
