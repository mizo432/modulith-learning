package undecided.erp.shared.supporting.snowflake.spi;

import undecided.erp.common.entity.SnowflakeId;

public interface SnowflakeIdGenerator {


  public long nextId();

  public SnowflakeId nextIdValue();
}
