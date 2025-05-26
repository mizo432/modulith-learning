package undecided.erp.scrum.domain.model.project;

/**
 * プロジェクトの状態を表す列挙型です。
 *
 * <p>このクラスは以下の状態を定義します:
 *
 * <ul>
 *   <li>ACTIVE: アクティブなプロジェクト。現在進行中で作業可能な状態。
 *   <li>ARCHIVED: アーカイブされたプロジェクト。完了または一時停止され、変更不可の状態。
 * </ul>
 */
public enum ProjectStatus {
  /**
   * アクティブなプロジェクト。
   *
   * <p>現在進行中で作業可能な状態を表します。
   */
  ACTIVE,

  /**
   * アーカイブされたプロジェクト。
   *
   * <p>完了または一時停止され、変更不可の状態を表します。
   */
  ARCHIVED
}
