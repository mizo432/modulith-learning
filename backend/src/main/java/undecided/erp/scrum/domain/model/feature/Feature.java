package undecided.erp.scrum.domain.model.feature;

import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Transient;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import undecided.erp.scrum.domain.model.product.Product;
import undecided.erp.shared.entity.SnowflakeId;

/**
 * これは製品の特徴を表しています。
 */
@AllArgsConstructor
@Getter
public class Feature {

  @EmbeddedId
  private SnowflakeId<Feature> id;
  @Embedded
  private SnowflakeId<Product> productId;

  private String name;
  private String description;
  @Transient
  private Product product;

  public static Feature create(SnowflakeId<Product> productId, String name, String description,
      Product product) {
    return new Feature(SnowflakeId.newInstance(), productId, name, description, product);

  }

  public Feature update(String name, String description) {
    return new Feature(id, this.productId, name, description, this.product);
  }

  class Features {

    private List<Feature> features;

  }
}
