package undecided.erp.scrum.model;

/**
 * SprintRetrospectiveクラスは、Scrumプロジェクトのスプリントのレトロスペクティブ会議を表現します。
 * <p>
 * これには、何がうまくいったのか、何がうまくいかなかったのか、スプリントで何を改善できるのか、という議論が含まれます。
 */
public class SprintRetrospective {

  /**
   * スプリントレトロスペクティブミーティングでうまくいったことについての情報を表します。
   */
  private String whatWentWell;
  /**
   * スプリントレトロスペクティブミーティングでうまくいかなかったことについての情報を表します。
   */
  private String whatDidNotGoWell;

  /**
   * スプリントレトロスペクティブミーティングで改善可能な内容についての情報を表します。
   * <p>
   * {@code SprintRetrospective}クラスの{@code whatCanBeImproved}変数には、スプリントの改善可能な側面についての情報が含まれています。
   */
  private String whatCanBeImproved;
}
