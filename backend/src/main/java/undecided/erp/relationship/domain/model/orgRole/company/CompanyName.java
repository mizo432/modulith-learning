package undecided.erp.relationship.domain.model.orgRole.company;

import static undecided.erp.common.primitive.Objects2.isNull;
import static undecided.erp.common.verifier.ObjectVerifiers.verifyNotNull;
import static undecided.erp.common.verifier.StringVerifiers.verifyHalfWidthLengthAtMost;
import static undecided.erp.common.verifier.StringVerifiers.verifyNonEmpty;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Embeddable
public class CompanyName {

  private static final CompanyName EMPTY = new CompanyName();

  private static final int LENGTH = 128;
  private String value;

  /**
   * 指定した値でCompanyNameクラスの新しいインスタンスを作成します。
   *
   * @param value CompanyNameの値
   * @return 新しいCompanyNameインスタンス
   * @throws IllegalArgumentException 値がnull、空、または最大長を超える場合
   */
  public static CompanyName of(String value) {
    verifyNotNull(value, () -> new IllegalArgumentException("value cannot be null"));
    verifyNonEmpty(value, () -> new IllegalArgumentException("value cannot be empty"));
    verifyHalfWidthLengthAtMost(value,
        () -> new IllegalArgumentException("value must be at most 128 characters"), LENGTH);
    return new CompanyName(value);

  }

  public static CompanyName reconstruct(String value) {
    if (isNull(value)) {
      return EMPTY;
    }
    return new CompanyName(value);

  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

}
