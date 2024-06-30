package undecided.erp.relationship.domain.model.party.person;

import lombok.Getter;
import lombok.NonNull;
import undecided.erp.shared.value.NonNullObject;

/**
 * FullNameクラスは、人のフルネームを表します。
 */
@Getter
public class FullName {

  private static final String EMPTY_STRING = "";
  private static final String BLANK_STRING = " ";

  private final String value;

  private FullName(@NonNull FirstName firstName, @NonNull LastName lastName) {
    this.value = emptyIfNull(lastName.getValue()) + BLANK_STRING +
        emptyIfNull(firstName.getValue());
  }

  String emptyIfNull(String value) {
    if (value == null) {
      return EMPTY_STRING;
    }
    return value;

  }

  /**
   * 指定された姓（FirstName）と名（LastName）を使って FullNameオブジェクトを作成します。
   *
   * @param firstName 人の名（NonNull）
   * @param lastName 人の姓（NonNull）
   * @return FullNameオブジェクト
   */
  public static FullName create(@NonNull FirstName firstName, @NonNull LastName lastName) {
    NonNullObject.of(firstName);
    NonNullObject.of(lastName);
    return new FullName(firstName, lastName);
  }

  @Override
  public String toString() {
    return String.valueOf(value);

  }
}
