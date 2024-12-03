package undecided.erp.relationship.domain.model.orgRole.company;

import static undecided.erp.common.precondition.ObjectPrecondition.checkNotNull;
import static undecided.erp.common.precondition.StringPrecondition.checkHalfWidthFixedLength;
import static undecided.erp.common.precondition.StringPrecondition.checkNonEmpty;
import static undecided.erp.common.precondition.StringPrecondition.verifyAllDecimal;

import undecided.erp.common.exception.BusinessException;

public record CompanyCode(String value) {

  public static CompanyCode of(String value) {
    checkNotNull(value, () -> new BusinessException("Company code cannot be null"));
    checkNonEmpty(value, () -> new BusinessException("Company code cannot be empty"));
    checkHalfWidthFixedLength(value,
        () -> new BusinessException("Company code must be length is 8"), 8);
    verifyAllDecimal(value,
        () -> new BusinessException("Company code must be all decimal"));

    return new CompanyCode(value);
  }

}
