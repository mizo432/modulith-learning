package undecided.erp.projectActivity.domain.model.product;

import static undecided.erp.common.primitive.Objects2.isNull;
import static undecided.erp.common.verifier.StringVerifiers.verifyHalfWidthFixedLength;
import static undecided.erp.common.verifier.StringVerifiers.verifyNonEmpty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.annotation.ValueObject;
import undecided.erp.shared.entity.SingleValue;

@ValueObject
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class ProductCode implements SingleValue<String> {

  public static final ProductCode EMPTY = new ProductCode();
  private static final int LENGTH = 6;

  private final String value;

  ProductCode() {
    this(null);
  }

  public static ProductCode of(@NonNull String value) {
    verifyNonEmpty(value,
        () -> new IllegalArgumentException(
            "ProductCode cannot be empty. Please provide a valid code."));
    verifyHalfWidthFixedLength(value,
        () -> new IllegalArgumentException("ProductCode must be exactly " + LENGTH
            + " characters long and use half-width characters."), LENGTH);
    return new ProductCode(value);
  }

  @Override
  public boolean isEmpty() {
    return isNull(value);
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
