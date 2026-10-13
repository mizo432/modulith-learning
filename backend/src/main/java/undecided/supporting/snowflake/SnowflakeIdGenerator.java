package undecided.supporting.snowflake;

public interface SnowflakeIdGenerator {

  public long nextId();

  public SnowflakeId nextIdValue();
}
