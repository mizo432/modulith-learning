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
 *   <li>{@link #READY}: 準備完了。ACCEPTEDから遷移し、スプリント実行の準備が整った状態
 *   <li>{@link #ESTIMATED}: 見積もり済み。READYから遷移し、ストーリーポイントが設定された状態
 *   <li>{@link #IN_PROGRESS}: 進行中（#522で実装予定。本Issueではenum追加のみ）
 *   <li>{@link #DONE}: 完了（#525で実装予定。本Issueではenum追加のみ）
 * </ul>
 *
 * <p>遷移ルールは SUGGESTED → ACCEPTED、SUGGESTED → FROZEN、FROZEN → SUGGESTED、
 * ACCEPTED → READY、READY → SUGGESTED、READY → ESTIMATED、ESTIMATED → READY のみ許可する。
 * FROZEN → SUGGESTED（復帰）は PO のみが実行できる。
 * READY → ESTIMATED、ESTIMATED → READY はチームメンバー全員が実行できる。
 */
public enum BacklogItemStatus {
  SUGGESTED("提案済み"),
  ACCEPTED("受入済み"),
  FROZEN("凍結"),
  READY("準備完了"),
  ESTIMATED("見積もり済み"),
  IN_PROGRESS("進行中"),
  DONE("完了");

  private final String label;

  BacklogItemStatus(String label) {
    this.label = label;
  }

  /** 表示用ラベル（日本語）を返します。DBや分岐条件には使用しません。 */
  public String label() {
    return label;
  }
}
