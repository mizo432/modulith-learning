package undecided.shared.common.snowflake;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FixedSnowflakeIdValueProviderTest {

  private long testSnowflakeId;

  @BeforeEach
  public void setUp() {
    FixedSnowflakeIdProvider.clear();
    testSnowflakeId = 12345L; // Assign your mock/test value here
  }

  @AfterEach
  public void tearDown() {
    FixedSnowflakeIdProvider.clear();
  }

  @Test
  public void testInitialize() {
    FixedSnowflakeIdProvider.initialize(testSnowflakeId);
    long expectedSnowflakeId = testSnowflakeId;
    long actualSnowflakeId = SnowflakeIdProvider.generateId();
    assertEquals(expectedSnowflakeId, actualSnowflakeId);
  }

  // More Test cases

}
