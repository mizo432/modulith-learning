package undecided.generic.documentMgmt.spi;

/**
 * 帳票のバージョン値オブジェクト。
 *
 * @param value バージョン番号
 */
public record DocumentVersion(int value) {

  /** バージョンの初期値。 */
  public static final int INITIAL_VALUE = 1;

  /**
   * 初期バージョンを返す。
   *
   * @return 値が {@link #INITIAL_VALUE} のバージョン
   */
  public static DocumentVersion initial() {
    return new DocumentVersion(INITIAL_VALUE);
  }

  /**
   * 初期バージョンかどうかを返す。
   *
   * @return 値が {@link #INITIAL_VALUE} のとき true
   */
  public boolean isInitial() {
    return value == INITIAL_VALUE;
  }

  /**
   * 次のバージョンを返す。
   *
   * @return 値を 1 増やしたバージョン
   */
  public DocumentVersion increment() {
    return new DocumentVersion(value + 1);
  }
}
