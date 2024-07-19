package undecided.erp.common.snowflake;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StaticNodeIdProviderTest {

  @BeforeEach
  void setUp() {
    StaticNodeIdProvider.clear();

  }

  @BeforeEach
  void tearDown() {
    StaticNodeIdProvider.clear();
  }

  @Test
  void testNodeIdMethod_withValidNodeId() {
    long testId = 12345L;
    StaticNodeIdProvider nodeProvider = new StaticNodeIdProvider(testId);
    assertThat(nodeProvider.nodeId()).isEqualTo(testId);
  }

  @Test
  void testNodeIdMethod_withInitialZeroId() {
    long testId = 0L;
    StaticNodeIdProvider nodeIdProvider = new StaticNodeIdProvider(testId);
    assertThat(nodeIdProvider.nodeId()).isEqualTo(testId);
  }

  @Test
  void testClearMethod_afterInitialization() {
    long testId = 12345L;
    StaticNodeIdProvider.initialize(testId);
    StaticNodeIdProvider.clear();
    assertThat(NodeIdProvider.getNodeId()).isNotEqualTo(testId);
  }
}
