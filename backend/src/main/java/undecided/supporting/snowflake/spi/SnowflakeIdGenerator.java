package undecided.supporting.snowflake.spi;

import undecided.supporting.entity.SnowflakeId;

public interface SnowflakeIdGenerator {

  public long nextId();

  public SnowflakeId nextIdValue();
}
