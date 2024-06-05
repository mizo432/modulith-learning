package undecided.erp.scrum.model;

import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import undecided.erp.scrum.model.product.Product;
import undecided.erp.shared.entity.SnowflakeId;

/**
 * これは製品の特徴を表しています。
 */
@AllArgsConstructor
@Getter
public class Feature {

  private SnowflakeId<Feature> id;

  private String name;
  private String description;
  @Transient
  private Product product;

  public static Feature create(String name, String description, Product product) {
    return new Feature(SnowflakeId.newInstance(), name, description, product);
  }
}
