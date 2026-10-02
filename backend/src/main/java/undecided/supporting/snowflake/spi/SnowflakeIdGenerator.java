package undecided.supporting.snowflake.spi;

public interface SnowflakeIdGenerator {

  public long nextId();

  public SnowflakeId nextIdValue();
}
