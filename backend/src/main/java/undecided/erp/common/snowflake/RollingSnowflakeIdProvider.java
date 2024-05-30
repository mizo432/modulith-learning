package undecided.erp.common.snowflake;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.springframework.lang.NonNull;

/**
 * RollingListSnowflakeIdProviderは、Snowflakeアルゴリズムに基づいてユニークなIDを生成するSnowflakeIdProviderのサブクラスです。
 * <p>
 * ユニークなIDを生成するために、Snowflake IDのローリングリストを維持します。
 */
public class RollingSnowflakeIdProvider extends SnowflakeIdProvider {

  private final List<Long> idList = new ArrayList<>();

  private int index;

  private RollingSnowflakeIdProvider(Collection<Long> idList) {
    this.idList.addAll(idList);
    index = 0;

  }

  public static void initialize(@NonNull Long... snowflakeIds) {
    if (snowflakeIds.length == 0) {
      throw new IllegalArgumentException("Snowflake IDs must not be empty");
    }
    SnowflakeIdProvider.setSnowflakeIdProvider(
        new RollingSnowflakeIdProvider(Arrays.asList(snowflakeIds)));

  }

  /**
   * Snowflakeアルゴリズムに基づいて一意のIDを生成します。
   *
   * @return 生成された一意のID。
   * @throws RuntimeException 時計が後ろ向きに動く場合。
   */
  @Override
  protected synchronized long snowflakeId() {
    Long result = idList.get(index);
    if (index == idList.size() - 1) {
      index = 0;
    } else {
      index++;
    }
    return result;

  }

  /**
   * このメソッドは、lastTimestamp変数を-62167252739000Lに初期化します。
   */
  public static void clear() {
    SnowflakeIdProvider.clear();

  }
}
