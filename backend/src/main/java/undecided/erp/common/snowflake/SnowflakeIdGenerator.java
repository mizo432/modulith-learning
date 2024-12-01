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
   * この定数は1609459200000Lに設定されており、これは2021年1月1日の00:00:00 UTCをミリ秒です。
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
   * MAX_WORKER_ID変数は、Snowflake ID生成アルゴリズムにおいて ワーカーIDに許される最大値を表します。
   */
  private static final long MAX_WORKER_ID = 1023L;
  /**
   * 現在のシーケンス番号は、ユニークなIDを生成するために使用されます。
   * <p>
   * この値はIDが生成されるたびにインクリメントされ、現在のタイムスタンプが変更されると0にリセットされます。 これにより、同じミリ秒内に生成されたIDが一意であることが保証されます。
   */
  private static long currentSequence = 0L;
  /**
   * lastTimestampという変数は、ユニークなID生成に使用されるSnowflakeアルゴリズムに基づいた 最後のタイムスタンプ（ミリ秒単位）を表します。
   * <p>
   * 初期値は-62167252739000Lです。 IDが生成されるたびにlastTimestampの値は更新されます。
   */
  private static long lastTimeMillis = -62167252739000L;
  /**
   * workerの一意のIDを表します。
   */
  private final long workerId;

  /**
   * SnowflakeIdGeneratorクラスは、Snowflakeアルゴリズムに基づいてユニークなIDを生成するために使用されます。
   * タイムスタンプ、ワーカーID、シーケンス番号の組み合わせを使用してIDを生成します。
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
   * このメソッドは、lastTimestamp変数を-62167252739000Lに初期化します。
   */
  public static void initialise() {
    lastTimeMillis = -62167252739000L;

  }

  /**
   * Snowflakeアルゴリズムに基づいて一意のIDを生成します。
   *
   * @return 生成された一意のID。
   * @throws RuntimeException 時計が後ろ向きに動く場合。
   */
  public synchronized long generateId() {
    long timestamp = DateProvider.currentTimeMillis();

    if (timestamp < lastTimeMillis) {
      throw new RuntimeException(
          "Clock moved backwards. Refusing to generate ID. timestamp:" + timestamp
              + ", lastTimestamp:" + lastTimeMillis);
    }
    if (timestamp == lastTimeMillis) {
      currentSequence = (currentSequence + 1) & 4095L;
      if (currentSequence == 0L) {
        timestamp = tilNextMillis(lastTimeMillis);
      }
    } else {
      currentSequence = 0L;
    }
    lastTimeMillis = timestamp;
    return ((timestamp - EPOCH) << 22) | (workerId << 12) | currentSequence;

  }

  /**
   * このメソッドは、現在のシステム時間を続けてチェックし、それが最後のタイムスタンプよりも大きくなるまで次のミリ秒タイムスタンプを計算します。
   *
   * @param lastTimeMillis 前回のタイムスタンプ（ミリ秒）。
   * @return 次のタイムスタンプ（ミリ秒）。
   */
  private long tilNextMillis(long lastTimeMillis) {
    long currentTimeMillis = DateProvider.currentTimeMillis();
    while (currentTimeMillis <= lastTimeMillis) {
      currentTimeMillis = DateProvider.currentTimeMillis();
    }
    return currentTimeMillis;
  }
}
