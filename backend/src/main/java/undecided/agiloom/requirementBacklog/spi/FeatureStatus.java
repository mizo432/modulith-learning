package undecided.agiloom.requirementBacklog.spi;

/**
 * フィーチャーの状態を表す区分値。
 *
 * <p>区分値は処理の分岐条件として使用するものであり、DBにはコード値のみを保持し、
 * 日本語ラベルはアプリ側で対応付ける（ADR-003）。
 *
 * <ul>
 *   <li>{@link #PLANNED}: 計画済み（初期状態）
 *   <li>{@link #IN_PROGRESS}: 進行中
 *   <li>{@link #DONE}: 完了
 * </ul>
 *
 * <p>許可される遷移（決定7）：
 * <ul>
 *   <li>順方向: {@code Planned → InProgress}, {@code InProgress → Done}
 *   <li>差し戻し: {@code InProgress → Planned}, {@code Done → InProgress}
 *   <li>許可しない: {@code Planned → Done}, {@code Done → Planned}
 * </ul>
 */
public enum FeatureStatus {
  PLANNED("計画済み"),
  IN_PROGRESS("進行中"),
  DONE("完了");

  private final String label;

  FeatureStatus(String label) {
    this.label = label;
  }

  /** 表示用ラベル（日本語）を返します。DBや分岐条件には使用しません。 */
  public String label() {
    return label;
  }
}
