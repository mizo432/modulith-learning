package undecided.erp.projectActivity.domain.model.product;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class ProductAttribute {

  public static final ProductAttribute EMPTY = new ProductAttribute();
  private final ProductName name;
  private final ProductCode code;

  ProductAttribute() {
    this.name = ProductName.EMPTY;
    this.code = ProductCode.EMPTY;

  }

  public static ProductAttribute of(@NonNull ProductName name, @NonNull ProductCode code) {
    return new ProductAttribute(name, code);

  }

}
