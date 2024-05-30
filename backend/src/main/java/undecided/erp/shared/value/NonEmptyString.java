package undecided.erp.shared.value;

/**
 * 非空の文字列値を表します。
 */
public record NonEmptyString(String value) {

  /**
   * 与えられた値で新しいNonEmptyStringのインスタンスを作成します。
   *
   * @param value NonEmptyStringに使用する値です。
   * @return 新しいNonEmptyStringのインスタンスを返します。
   * @throws IllegalArgumentException 値が空の場合に発生します。
   */
  public static NonEmptyString of(String value) {
    if (value.isEmpty()) {
      throw new IllegalArgumentException("Empty string");
    }
    return new NonEmptyString(value);
  }

}
