package undecided.shared.common.snowflake;

public class StaticNodeIdProvider extends NodeIdProvider {

  private final long nodeId;

  StaticNodeIdProvider(long nodeId) {
    this.nodeId = nodeId;
  }

  public static void initialize(long nodeId) {
    NodeIdProvider.setNodeIdProvider(new StaticNodeIdProvider(nodeId));
  }

  public static void clear() {
    NodeIdProvider.clear();
  }

  @Override
  protected Long nodeId() {
    return nodeId;
  }
}
