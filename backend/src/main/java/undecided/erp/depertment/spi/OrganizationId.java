package undecided.erp.depertment.spi;

import static undecided.erp.common.precondition.ObjectPrecondition.checkNotNull;

import org.jspecify.annotations.NonNull;
import undecided.erp.common.precondition.StringPrecondition;

public class OrganizationId {
  private static final String LEVEL_1_SUFFIX = "000000";
  private static final String LEVEL_2_SUFFIX = "000";

  private static final int DEPTH_LEVEL_0 = 0;
  private static final int DEPTH_LEVEL_1 = 1;
  private static final int DEPTH_LEVEL_2 = 2;
  private static final int DEPTH_LEVEL_3 = 3;

  private final String value;

  /**
   * 組織IDを表すオブジェクトを生成します。
   *
   * @param value 組織IDの文字列。nullであってはならず、必ず10桁の半角数字である必要があります。 入力が条件を満たさない場合、例外がスローされます。
   * @throws IllegalArgumentException 組織IDがnullの場合、または10桁の半角数字でない場合
   */
  public OrganizationId(@NonNull String value) {
    checkNotNull(value, () -> new IllegalArgumentException("value must not be null!"));
    StringPrecondition.checkHalfWidthFixedLength(
        value, () -> new IllegalArgumentException("value must be 10 digits!"), 10);
    this.value = value;
  }

  /**
   * 組織IDの値を返します。
   *
   * @return 組織ID
   */
  public String getValue() {
    return value;
  }

  // ... existing code ...
  /**
   * 階層の深さを返す。
   *
   * <p>階層の深さは、組織IDの文字列の長さを表します。 Depth = 0 の場合は、組織IDが0を10桁です。 Depth = 1 の場合は、組織IDが000000です。 Depth =
   * 2 の場合は、組織IDの末尾が000です。 Depth = 3 の場合は、組織IDがそれ以外です。
   *
   * @return 階層の深さ
   */
  public Integer getLevel() {
    if (isAllZeros()) {
      return DEPTH_LEVEL_0;
    }
    if (value.endsWith(LEVEL_1_SUFFIX)) {
      return DEPTH_LEVEL_1;
    }
    if (value.endsWith(LEVEL_2_SUFFIX)) {
      return DEPTH_LEVEL_2;
    }
    return DEPTH_LEVEL_3;
  }

  private boolean isAllZeros() {
    return value.chars().allMatch(c -> c == '0');
  }

  /**
   * このメソッドは、現在の組織IDに基づいて親組織のIDを取得します。 組織の階層レベルに応じて異なる計算を行い、適切な親組織IDを返却します。
   * 階層レベルが0の場合は親組織が存在しないため、nullを返します。
   *
   * @return 親組織ID。階層レベルが0の場合はnullを返します。それ以外の場合は計算済みの親組織IDを返します。
   */
  public String getParentOrganizationId() {
    return switch (getLevel()) {
      case DEPTH_LEVEL_1 -> "0".repeat(value.length());
      case DEPTH_LEVEL_2 ->
          value.substring(0, value.length() - LEVEL_1_SUFFIX.length()) + LEVEL_1_SUFFIX;
      case DEPTH_LEVEL_3 ->
          value.substring(0, value.length() - LEVEL_2_SUFFIX.length()) + LEVEL_2_SUFFIX;
      default -> null;
    };
  }
}
