package undecided.erp.shared.value;

/**
 * 最大長を持つ文字列を表現します。
 */
public record MaxLengthString(String value) {

  /**
   * 与えられた値と最大の長さのMaxLengthStringオブジェクトを作成します。
   *
   * @param value Stringの値。
   * @param length 値の許容最大長。
   * @return 新しいMaxLengthStringオブジェクト。
   * @throws IllegalArgumentException 値が長すぎる場合。
   */
  public static MaxLengthString of(String value, int length) {
    if (value.length() > length) {
      throw new IllegalArgumentException("Value is too long");


    }
    return new MaxLengthString(value);
  }

}
