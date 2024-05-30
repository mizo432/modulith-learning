package undecided.erp.common.snowflake;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StaticSnowflakeIdProviderTest {

  private long testSnowflakeId;

  @BeforeEach
  public void setUp() {
    testSnowflakeId = 12345L; // Assign your mock/test value here
  }

  @Test
  public void testInitialize() {
    StaticSnowflakeIdProvider.initialize(testSnowflakeId);
    long expectedSnowflakeId = testSnowflakeId;
    long actualSnowflakeId = SnowflakeIdProvider.generateId();
    assertEquals(expectedSnowflakeId, actualSnowflakeId);
  }

  // More Test cases

}
