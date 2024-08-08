package undecided.erp.common.message;

/**
 * {@code StandardResultMessageType} enumは {@code ResultMessageType} インターフェースを実装し、
 * さまざまな種類の結果メッセージを表します。
 *
 * <p>各タイプはenum内の定数として、その対応するメッセージタイプ文字列と共に表されます。
 * このenumは、指定されたタイプ文字列を使用してメッセージタイプのインスタンスを作成するための コンストラクタも提供します。</p>
 *
 * <p>このenumは、{@code ResultMessageType} インターフェースの {@code getType()} メソッドをオーバーライドして、
 * enum値のメッセージタイプ文字列を返します。また、{@code toString()} メソッドもオーバーライドして、 メッセージタイプ文字列を返します。</p>
 *
 * <p>enum値にはバージョンを示すために {@code @since} タグを使用して追加情報が指定されている場合があります。
 * 一部のenum値には、メッセージタイプの意味を説明するための追加の説明があります。</p>
 *
 * <p>使用例:</p>
 * <pre>{@code
 *   ResultMessages messages = new ResultMessages(StandardResultMessageType.ERROR);
 * }</pre>
 */
public enum StandardResultMessageType implements ResultMessageType {
  /**
   * message type is <code>success</code>.
   */
  SUCCESS("success"),
  /**
   * message type is <code>info</code>.
   */
  INFO("info"),
  /**
   * message type is <code>warning</code>.
   *
   * @since 5.0.0
   */
  WARNING("warning"),
  /**
   * message type is <code>error</code>.
   */
  ERROR("error"),
  /**
   * message type is <code>danger</code>.
   */
  DANGER("danger"),
  /**
   * message type is <code>primary</code>.
   *
   * @since 5.7.0
   */
  PRIMARY("primary"),
  /**
   * message type is <code>secondary</code>.
   *
   * @since 5.7.0
   */
  SECONDARY("secondary"),
  /**
   * message type is <code>light</code>.
   *
   * @since 5.7.0
   */
  LIGHT("light"),
  /**
   * message type is <code>dark</code>.
   *
   * @since 5.7.0
   */
  DARK("dark");

  /**
   * message type
   */
  private final String type;

  /**
   * Create ResultMessageType instance<br>
   *
   * @param type message type
   */
  private StandardResultMessageType(String type) {
    this.type = type;
  }

  @Override
  public String getType() {
    return this.type;
  }

  /**
   * <p>
   * returns message type
   * </p>
   *
   * @see java.lang.Enum#toString()
   */
  @Override
  public String toString() {
    return this.type;
  }
}
