package undecided.agiloom.requirementBacklog.spi;

import org.jspecify.annotations.Nullable;

/**
 * 狩野式品質モデルに基づく品質カテゴリを表す区分値。
 *
 * <p>バックログアイテムの Value（0〜100）から品質カテゴリを自動分類します。
 * 区分値は処理の分岐条件として使用するものであり、DBにはコード値のみを保持し、
 * 日本語ラベルはアプリ側で対応付ける（ADR-003）。
 *
 * <ul>
 *   <li>{@link #ATTRACTIVE}: 魅力的品質（Value 76〜100）
 *   <li>{@link #MUST_BE}: 当たり前品質（Value 51〜75）
 *   <li>{@link #ONE_DIMENSIONAL}: 一元的品質（Value 26〜50）
 *   <li>{@link #INDIFFERENT}: 無関心品質（Value 0〜25）
 * </ul>
 *
 * <p>Value が null の場合は品質カテゴリも null となります。
 */
public enum QualityCategory {
  ATTRACTIVE("魅力的品質"),
  MUST_BE("当たり前品質"),
  ONE_DIMENSIONAL("一元的品質"),
  INDIFFERENT("無関心品質");

  private final String label;

  QualityCategory(String label) {
    this.label = label;
  }

  /** 表示用ラベル（日本語）を返します。DBや分岐条件には使用しません。 */
  public String label() {
    return label;
  }

  /**
   * Value から品質カテゴリを決定します。
   *
   * <p>境界値は以下の通りです：
   * <ul>
   *   <li>76 以上 → {@link #ATTRACTIVE}（魅力的品質）
   *   <li>51 以上 → {@link #MUST_BE}（当たり前品質）
   *   <li>26 以上 → {@link #ONE_DIMENSIONAL}（一元的品質）
   *   <li>0 以上 → {@link #INDIFFERENT}（無関心品質）
   *   <li>null → null
   * </ul>
   *
   * @param value バックログアイテムの Value（0〜100、null可）
   * @return 対応する品質カテゴリ。Value が null の場合は null
   */
  @Nullable
  public static QualityCategory fromValue(@Nullable Integer value) {
    if (value == null) {
      return null;
    }
    if (value >= 76) {
      return ATTRACTIVE;
    }
    if (value >= 51) {
      return MUST_BE;
    }
    if (value >= 26) {
      return ONE_DIMENSIONAL;
    }
    if (value >= 0) {
      return INDIFFERENT;
    }
    return null;
  }
}
