package undecided.erp.common.snowflake;

public class StaticNodeIdProvider extends NodeIdProvider {

  private final long nodeId;

  StaticNodeIdProvider(long nodeId) {
    this.nodeId = nodeId;
  }

  public static void initialize(long nodeId) {
    NodeIdProvider.setNodeIdProvider(new StaticNodeIdProvider(nodeId));
  }

  @Override
  protected Long nodeId() {
    return nodeId;
  }

  public static void clear() {
    NodeIdProvider.clear();

  }
}
