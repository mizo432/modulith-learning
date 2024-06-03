package undecided.erp.shared.value;

/**
 * 非空の文字列値を表します。
 */
public record NonEmptyString(String value) {

  public static NonEmptyString of(String value) {
    if (value == null) {
      return new NonEmptyString(null);

    }
    if (value.isEmpty()) {
      throw new IllegalArgumentException("Empty string");
    }
    return new NonEmptyString(value);
  }

}
