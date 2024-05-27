package undecided.erp.shared.value;

public record LengthCode(String value) {

  public static LengthCode of(String value, int length) {
    if (value.length() != length) {
      throw new IllegalArgumentException("Length code must be equal to length code");

    }
    return new LengthCode(value);
  }

}
