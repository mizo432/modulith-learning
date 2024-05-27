package undecided.erp.shared.value;

public record NonNullCode(String value) {

  public static NonNullCode of(String value) {
    if (value == null) {
      throw new IllegalArgumentException("Value cannot be null");
    }
    return new NonNullCode(value);
  }

}
