package undecided.agiloom.requirementBacklog.spi;

import java.util.List;
import org.jspecify.annotations.NonNull;

/**
 * ストーリーポイントの値オブジェクト。
 *
 * <p>許容値はフィボナッチ数列 {@code 0, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89} のみ。
 * 項目制約の値は値オブジェクトの定数として定義し、単一の情報源とする（ADR-002）。
 * StoryPoint 0を許可する（#645で決定：見積もり不要なタスク）。
 */
public final class StoryPoint {

  /** 許容されるストーリーポイント値のリスト（フィボナッチ数列）。単一の情報源（ADR-002）。 */
  public static final List<Integer> ALLOWED_VALUES = List.of(0, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89);

  /** 最小値。 */
  public static final int MIN_VALUE = 0;

  /** 最大値。 */
  public static final int MAX_VALUE = 89;

  private final int value;

  private StoryPoint(int value) {
    this.value = value;
  }

  /**
   * 指定された値からStoryPointを作成します。
   *
   * @param value ストーリーポイント値（フィボナッチ数列の値であること）
   * @return 作成されたStoryPoint
   * @throws IllegalArgumentException 値がフィボナッチ数列にない場合
   */
  public static StoryPoint of(@NonNull Integer value) {
    throw new UnsupportedOperationException("StoryPoint validation is not yet implemented");
  }

  /** ストーリーポイントの値を返します。 */
  public int getValue() {
    return value;
  }

  /** 許容されるストーリーポイント値の一覧を返します。 */
  public static List<Integer> allowedValues() {
    return ALLOWED_VALUES;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    StoryPoint that = (StoryPoint) o;
    return value == that.value;
  }

  @Override
  public int hashCode() {
    return Integer.hashCode(value);
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
