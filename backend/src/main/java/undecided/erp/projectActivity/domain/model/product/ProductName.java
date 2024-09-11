package undecided.erp.projectActivity.domain.model.product;

import static undecided.erp.common.primitive.Objects2.isNull;
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
public class ProductName implements SingleValue<String> {

  public static final ProductName EMPTY = new ProductName();
  private final String value;

  ProductName() {
    this(null);
  }

  public static ProductName of(@NonNull String value) {
    verifyNonEmpty(value,
        () -> new IllegalArgumentException(
            "ProductName cannot be empty. Please provide a valid code."));
    return new ProductName(value);
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
