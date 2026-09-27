package undecided.supporting.snowflake.spi;

import undecided.generic.entity.SnowflakeId;

public interface SnowflakeIdGenerator {

  public long nextId();

  public SnowflakeId nextIdValue();
}
