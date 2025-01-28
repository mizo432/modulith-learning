package undecided.erp.relationship.domain.model.orgRole.company;

import static undecided.erp.common.precondition.ObjectPrecondition.checkNotNull;
import static undecided.erp.common.precondition.StringPrecondition.checkHalfWidthFixedLength;
import static undecided.erp.common.precondition.StringPrecondition.checkNonEmpty;
import static undecided.erp.common.precondition.StringPrecondition.verifyAllDecimal;

import jakarta.persistence.Embeddable;
import undecided.erp.common.exception.BusinessException;

@Embeddable
public record CompanyCode(String value) {

  public static final int LENGTH = 8;

  public static CompanyCode of(String value) {
    checkNotNull(value, () -> new BusinessException("Company code cannot be null"));
    checkNonEmpty(value, () -> new BusinessException("Company code cannot be empty"));
    checkHalfWidthFixedLength(value,
        () -> new BusinessException("Company code must be length is %s".formatted(LENGTH)), LENGTH);
    verifyAllDecimal(value,
        () -> new BusinessException("Company code must be all decimal"));

    return new CompanyCode(value);
  }

}
