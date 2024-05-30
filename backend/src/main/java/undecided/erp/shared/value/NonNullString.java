package undecided.erp.shared.value;

public record NonNullString(String value) {

  /**
   * Creates a new NonNullString object with the given value.
   *
   * @param value the String value. Cannot be null.
   * @return a new NonNullString object.
   * @throws IllegalArgumentException if the value is null.
   */
  public static NonNullString of(String value) {
    if (value == null) {
      throw new IllegalArgumentException("Value cannot be null");
    }
    return new NonNullString(value);
  }

}
