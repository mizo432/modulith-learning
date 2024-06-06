package undecided.erp.scrum.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * SprintReviewクラスは、Scrumプロジェクトのスプリントレビューミーティングを表します。
 * <p>
 * スプリントレビューミーティングには、ミーティングの日付、参加者のリスト、話し合った問題、特定の改善点が含まれます。
 */
public class SprintReview {

  public static final SprintReview EMPTY = new SprintReview();
  private LocalDate date;
  private final List<String> participants = new ArrayList<>();
  /**
   * issuesDiscussed変数は、スプリントレビューミーティングで議論された問題のリストを格納します。
   * <p>
   * 議論された問題は、セッターとゲッターメソッドを使用してアクセスおよび変更することができます。
   */
  private String issuesDiscussed;
  /**
   * <code>improvements</code>変数は、スプリントレビューミーティングで議論された改善項目を表す文字列です。
   * <p>
   * この改善項目は、getterとsetterメソッドを使用してアクセスおよび変更することができます。
   */
  private String improvements;
}
