package undecided.erp.shared.value;

public record NonNullObject(Object value) {

  /**
   * 与えられた値からNonNullObjectを生成します。
   *
   * @param value NonNullObjectを生成するための値（NonNull）
   * @return NonNullObject
   * @throws IllegalArgumentException 値がnullの場合
   */
  public static NonNullObject of(Object value) {
    if (value == null) {
      throw new IllegalArgumentException("Value cannot be null");
    }
    return new NonNullObject(value);
  }

}
