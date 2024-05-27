package undecided.erp.common.snowflake;

import undecided.erp.common.dateProvider.DateProvider;

/**
 * SnowflakeIdGeneratorクラスは、Snowflakeアルゴリズムに基づいてユニークなIDを生成するために使われます。
 * これは、タイムスタンプ、ワーカーID、シーケンス番号の組み合わせを使用してIDを生成します。
 */
public class SnowflakeIdGenerator {

  /**
   * EPOCH変数は、Snowflakeアルゴリズムを使用して一意のIDを生成する際の時間の起点を表しています。
   * <p>
   * この定数は1609459200000Lに設定されており、これは2021年1月1日の00:00:00 UTCをミリ秒で表したものです。
   * <p>
   * この変数は{@link
   * SnowflakeIdGenerator}クラスで一意のIDを生成する際に使用されます。現在のタイムスタンプからEPOCHを引いてから経過時間を算出し、その結果の値は22ビット左シフトされてから、ワーカーIDとシーケンス番号と組み合わされて一意のIDが生成されます。
   * <p>
   * 使い方の例:
   * <pre>{@code
   *     long timestamp = System.currentTimeMillis();
   *     long id = ((timestamp - EPOCH) << 22) | (workerId << 12) | sequence;
   * }</pre>
   *
   * @see SnowflakeIdGenerator
   */
  private static final Long EPOCH = 1609459200000L;
  /**
   * The MAX_WORKER_ID variable represents the maximum value allowed for the worker ID in the
   * Snowflake ID generation algorithm.
   */
  private static final long MAX_WORKER_ID = 1023L;

  /**
   * Represents the unique ID of a worker.
   */
  private final long workerId;
  /**
   *
   */
  private static long sequence = 0L;

  /**
   * The lastTimestamp variable represents the last timestamp in milliseconds used for generating
   * unique IDs based on the Snowflake algorithm. It is initially set to -62167252739000L. The value
   * of lastTimestamp is updated every time an ID is generated.
   */
  private static long lastTimestamp = -62167252739000L;

  /**
   * SnowflakeIdGenerator is a class that generates unique IDs based on the Snowflake algorithm.
   */
  public SnowflakeIdGenerator(Long workerId) {
    if (workerId < 0 || workerId > MAX_WORKER_ID) {
      throw new IllegalArgumentException(
          "worker ID must be between 0 and " + MAX_WORKER_ID + " but workerId is "
              + workerId);
    }
    this.workerId = workerId;
  }

  /**
   * Snowflakeアルゴリズムに基づいて一意のIDを生成します。
   *
   * @return 生成された一意のID。
   * @throws RuntimeException 時計が後ろ向きに動く場合。
   */
  public synchronized long generateId() {
    long timestamp = DateProvider.currentTimeMillis();

    if (timestamp < lastTimestamp) {
      throw new RuntimeException(
          "Clock moved backwards. Refusing to generate ID. timestamp:" + timestamp
              + ", lastTimestamp:" + lastTimestamp);
    }
    if (timestamp == lastTimestamp) {
      sequence = (sequence + 1) & 4095L;
      if (sequence == 0L) {
        timestamp = tilNextMillis(lastTimestamp);
      }
    } else {
      sequence = 0L;
    }
    lastTimestamp = timestamp;
    return ((timestamp - EPOCH) << 22) | (workerId << 12) | sequence;

  }

  /**
   * このメソッドは、現在のシステム時間を続けてチェックし、それが最後のタイムスタンプよりも大きくなるまで次のミリ秒タイムスタンプを計算します。
   *
   * @param lastTimestamp 前回のタイムスタンプ（ミリ秒）。
   * @return 次のタイムスタンプ（ミリ秒）。
   */
  private long tilNextMillis(long lastTimestamp) {
    long timestamp = System.currentTimeMillis();
    while (timestamp <= lastTimestamp) {
      timestamp = System.currentTimeMillis();
    }
    return timestamp;
  }

  /**
   * このメソッドは、lastTimestamp変数を-62167252739000Lに初期化します。
   */
  public static void initialise() {
    lastTimestamp = -62167252739000L;

  }
}
