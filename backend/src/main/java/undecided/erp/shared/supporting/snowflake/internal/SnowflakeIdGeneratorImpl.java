package undecided.erp.shared.supporting.snowflake.internal;

import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.shared.supporting.snowflake.spi.SnowflakeIdGenerator;
import undecided.shared.common.dateProvider.DateProvider;

public final class SnowflakeIdGeneratorImpl implements SnowflakeIdGenerator {

  private static final long MAX_SEQUENCE = 4095;

  private static final int NODE_ID_SHIFT = 12;
  private static final int TIMESTAMP_SHIFT = 12 + 10;

  private final long epochMillis;
  private final long nodeId;

  private long lastTimestamp = -1;
  private long sequence = 0;

  public SnowflakeIdGeneratorImpl(long nodeId, long epochMillis) {
    if (nodeId < 0 || nodeId > NodeId.MAX) {
      throw new IllegalArgumentException("invalid nodeId: " + nodeId);
    }
    this.epochMillis = epochMillis;
    this.nodeId = nodeId;
  }

  public SnowflakeIdGeneratorImpl(NodeId nodeId, long epochMillis) {
    this(nodeId.getValue(), epochMillis);
  }

  public synchronized long nextId() {
    long timestamp = DateProvider.currentTimeMillis();
    if (timestamp < lastTimestamp) {
      throw new IllegalStateException("clock moved backwards");
    }

    if (timestamp == lastTimestamp) {
      sequence++;
      if (sequence > MAX_SEQUENCE) {
        timestamp = withNextMillis(timestamp);
        sequence = 0;
      }
    } else {
      sequence = 0;
    }
    lastTimestamp = timestamp;
    return ((timestamp - epochMillis) << TIMESTAMP_SHIFT) | (nodeId << NODE_ID_SHIFT) | sequence;
  }

  @Override
  public synchronized SnowflakeId nextIdValue() {
    return SnowflakeId.of(nextId());

  }

  private long withNextMillis(long lastTimestamp) {
    long timestamp;
    do {
      Thread.onSpinWait();
      timestamp = DateProvider.currentTimeMillis();
    } while (timestamp <= lastTimestamp);
    return timestamp;
  }

}
