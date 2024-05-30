package undecided.erp.common.snowflake;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Objects;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import undecided.erp.common.application.ApplicationInfo;

class NodeIdProviderTest {

  @BeforeEach
  void setUp() {
    NodeIdProvider.clear();
  }

  @AfterEach
  void tearDown() {
    NodeIdProvider.clear();
  }

  @Test
  void testNodeId_whenHostNameIsNotUnknown() throws UnknownHostException {
    String hostName = Inet4Address.getLocalHost().getHostAddress();
    Long nodeId = NodeIdProvider.getNodeId();
    assertThat(nodeId).isEqualTo(
        Math.abs((long) Objects.hash(hostName, ApplicationInfo.name(), ApplicationInfo.port()))
            & 1023);
  }

  @Test
  void testNodeId_whenHostNameIsUnknown() {
    Long nodeId = NodeIdProvider.getNodeId();
    System.out.println(nodeId);
    assertThat(nodeId >= 0 && nodeId < 1024).isTrue();
  }

}
