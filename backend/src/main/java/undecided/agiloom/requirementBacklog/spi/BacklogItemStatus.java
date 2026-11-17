package undecided.agiloom.requirementBacklog.spi;

/**
 * バックログアイテムの状態を表す区分値。
 *
 * <p>区分値は処理の分岐条件として使用するものであり、DBにはコード値のみを保持し、
 * 日本語ラベルはアプリ側で対応付ける（ADR-003）。
 *
 * <ul>
 *   <li>{@link #SUGGESTED}: 提案済み（初期状態）。チームメンバーやステークホルダーが作成した直後の状態
 *   <li>{@link #ACCEPTED}: 受入済み。SMまたはPOがチームのバックログとして受け入れた状態
 *   <li>{@link #REJECTED}: 却下。SMまたはPOが受け入れないと判断した状態
 * </ul>
 *
 * <p>遷移ルールは SUGGESTED → ACCEPTED と SUGGESTED → REJECTED のみ許可する。
 */
public enum BacklogItemStatus {
  SUGGESTED("提案済み"),
  ACCEPTED("受入済み"),
  REJECTED("却下");

  private final String label;

  BacklogItemStatus(String label) {
    this.label = label;
  }

  /** 表示用ラベル（日本語）を返します。DBや分岐条件には使用しません。 */
  public String label() {
    return label;
  }
}
