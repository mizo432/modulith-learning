package undecided.erp.relationship.domain.model.orgRole.company;

import static undecided.erp.common.verifier.ObjectVerifiers.verifyNotNull;
import static undecided.erp.common.verifier.StringVerifiers.verifyAllDecimal;
import static undecided.erp.common.verifier.StringVerifiers.verifyHalfWidthFixedLength;
import static undecided.erp.common.verifier.StringVerifiers.verifyNonEmpty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CompanyCode {

  private static final int LENGTH = 3;

  @JsonValue
  private String value;

  /**
   * 与えられた値からCompanyCodeオブジェクトを作成します。
   *
   * @param value CompanyCodeオブジェクトを作成するための値
   * @return 作成されたCompanyCodeオブジェクト
   * @throws IllegalArgumentException 値がnull、空、数字、または固定長でない場合
   */
  @JsonCreator
  public static CompanyCode of(String value) {
    verifyNotNull(value,
        () -> new IllegalArgumentException("value cannot be null"));
    verifyNonEmpty(value,
        () -> new IllegalArgumentException("value cannot be empty"));
    verifyAllDecimal(value,
        () -> new IllegalArgumentException("value cannot be a number"));
    verifyHalfWidthFixedLength(value,
        () -> new IllegalArgumentException("value cannot be a half width"),
        LENGTH);
    return new CompanyCode(value);

  }

  @Override
  public String toString() {
    return String.valueOf(value);
    
  }
}
