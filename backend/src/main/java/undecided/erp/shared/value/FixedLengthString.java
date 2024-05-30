package undecided.erp.shared.value;

/**
 * 指定された値を持つ、固定長の文字列を表します。
 */
public record FixedLengthString(String value) {

  /**
   * 指定された値と長さで新しいLengthStringオブジェクトを作成します。
   *
   * @param value LengthStringの文字列値
   * @param length LengthStringの長さ
   * @return 新しいLengthStringオブジェクト
   * @throws IllegalArgumentException 値の長さが指定された長さと一致しない場合
   */
  public static FixedLengthString of(String value, int length) {
    if (value.length() != length) {
      throw new IllegalArgumentException("Length code must be equal to length code");

    }
    return new FixedLengthString(value);
  }

}
