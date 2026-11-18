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
 *   <li>{@link #FROZEN}: 凍結。SMまたはPOが一時的に保留とした状態
 * </ul>
 *
 * <p>遷移ルールは SUGGESTED → ACCEPTED、SUGGESTED → FROZEN、FROZEN → SUGGESTED のみ許可する。
 * FROZEN → SUGGESTED（復帰）は PO のみが実行できる。
 */
public enum BacklogItemStatus {
  SUGGESTED("提案済み"),
  ACCEPTED("受入済み"),
  FROZEN("凍結");

  private final String label;

  BacklogItemStatus(String label) {
    this.label = label;
  }

  /** 表示用ラベル（日本語）を返します。DBや分岐条件には使用しません。 */
  public String label() {
    return label;
  }
}
