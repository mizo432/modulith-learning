package undecided.erp.relationship.domain.model.party.person;

import static undecided.erp.common.primitive.Strings2.emptyIfNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import undecided.erp.common.primitive.Strings2;

/**
 * FullNameクラスは、人のフルネームを表します。
 */
@EqualsAndHashCode
@Getter
public class FullName {

  private final String value;

  private FullName(@NonNull FirstName firstName, @NonNull LastName lastName) {
    this.value = Strings2.ltrim(emptyIfNull(lastName.getValue()) + Strings2.BLANK) +
        emptyIfNull(firstName.getValue());
  }

  /**
   * 指定された姓（FirstName）と名（LastName）を使って FullNameオブジェクトを作成します。
   *
   * @param firstName 人の名（NonNull）
   * @param lastName 人の姓（NonNull）
   * @return FullNameオブジェクト
   */
  public static FullName create(@NonNull FirstName firstName, @NonNull LastName lastName) {
    return new FullName(firstName, lastName);
  }

  /**
   * このメソッドは、指定されたFirstNameとLastNameを使用してFullNameオブジェクトを構築します。
   *
   * @param firstName 人の名前。nullであってはなりません。
   * @param lastName 人の姓。nullであってはなりません。
   * @return 指定されたFirstNameとLastNameを使用して構築されたFullNameオブジェクト。
   */
  public static FullName reconstruct(@NonNull FirstName firstName, @NonNull LastName lastName) {
    return new FullName(firstName, lastName);

  }

  @Override
  public String toString() {
    return String.valueOf(value);

  }
}
