package undecided.erp.common.snowflake;

import java.net.UnknownHostException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import org.jspecify.annotations.NonNull;
import undecided.erp.common.application.ApplicationInfo;
import undecided.erp.common.ipaddress.IpAddressProvider;

public class NodeIdProvider {

  private static final AtomicReference<NodeIdProvider> nodeIdProvider =
      new AtomicReference<>(new NodeIdProvider());

  private Long nodeId;

  NodeIdProvider() {
    nodeId = null;
    try {
      String hostName = IpAddressProvider.ipAddress();
      String applicationName = ApplicationInfo.name();
      Long serverPort = ApplicationInfo.port();
      nodeId = Math.abs((long) Objects.hash(hostName, applicationName, serverPort));
    } catch (UnknownHostException e) {
      nodeId = (long) (Math.random() * (Math.pow(2, 10) - 1));
    }
    nodeId = nodeId & 1023;
  }

  NodeIdProvider(@NonNull NodeIdProvider nodeIdProvider) {
    NodeIdProvider.setNodeIdProvider(nodeIdProvider);
  }

  public static Long getNodeId() {
    return NodeIdProvider.nodeIdProvider.get().nodeId();
  }

  public static void setNodeIdProvider(NodeIdProvider nodeIdProvider) {
    NodeIdProvider.nodeIdProvider.set(nodeIdProvider);
  }

  /** DateProviderを初期化する */
  public static void clear() {
    IpAddressProvider.clear();
    ApplicationInfo.clear();
    NodeIdProvider.nodeIdProvider.set(new NodeIdProvider());
  }

  protected Long nodeId() {
    return nodeId;
  }
}
