package undecided.erp.common.snowflake;

public class StaticSnowflakeIdProvider extends SnowflakeIdProvider {

  private final Long value;

  private StaticSnowflakeIdProvider(Long value) {
    this.value = value;
  }

  public static void initialize(Long snowflakeId) {
    SnowflakeIdProvider.setSnowflakeIdProvider(new StaticSnowflakeIdProvider(snowflakeId));

  }

  /**
   * Snowflakeアルゴリズムに基づいて一意のIDを生成します。
   *
   * @return 生成された一意のID。
   * @throws RuntimeException 時計が後ろ向きに動く場合。
   */
  @Override
  protected synchronized long snowflakeId() {
    return value;

  }

  /**
   * このメソッドは、lastTimestamp変数を-62167252739000Lに初期化します。
   */
  public static void clear() {
    SnowflakeIdProvider.clear();

  }
}
