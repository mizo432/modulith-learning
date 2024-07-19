package undecided.erp.scrum.domain.model.product;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import undecided.erp.relationship.domain.model.personRole.productOwner.ProductOwner;
import undecided.erp.scrum.domain.model.feature.Feature;
import undecided.erp.shared.entity.SnowflakeId;
import undecided.erp.shared.value.MaxLengthString;
import undecided.erp.shared.value.NonNullObject;

/**
 * Represents a product.
 */
@Entity
@Table(schema = "scrum", name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Product {

  private static final Boolean DEFAULT_IS_ARCHIVED = Boolean.FALSE;
  @Id
  @EmbeddedId
  @Column(name = "product_id", nullable = false)
  private SnowflakeId<Product> id;
  @Column(name = "product_code", length = 3, nullable = false)
  private String code;

  @Column(name = "product_name", length = 128, nullable = false)
  private String name;
  @Column(length = 1024, nullable = false)
  private String description;

  private Boolean isArchived;
  @Transient
  private List<Feature> features = new ArrayList<>();
  @Transient
  private ProductOwner assignedOwner = ProductOwner.EMPTY;

  /**
   * 製品に製品オーナーを割り当てます。
   *
   * @param productOwner 割り当てる製品オーナー
   * @return オーナーが割り当てられた製品
   */
  public Product assignOwner(ProductOwner productOwner) {
    return new Product(id, code, name, description, isArchived, features, productOwner);

  }

  /**
   * 製品に機能を追加します。
   *
   * @param feature 製品に追加する機能。
   * @return 機能が追加された製品。
   * @throws IllegalArgumentException 製品に既に存在する機能が追加された場合。
   */
  public Product addFeature(@NonNull Feature feature) {
    if (this.features.contains(feature)) {
      throw new IllegalArgumentException("Feature already exists");
    }
    List<Feature> newFeatures = new ArrayList<>(features);
    newFeatures.add(feature);

    return new Product(id, code, name, description, isArchived, newFeatures, assignedOwner);

  }

  /**
   * 製品から機能を削除します。
   *
   * @param featureId 削除する機能のID。
   * @return 機能が削除された製品。
   * @throws IllegalArgumentException 製品に存在しない機能が指定された場合。
   */
  public Product removeFeature(@NonNull SnowflakeId<Feature> featureId) {
    List<Feature> newFeatures = new ArrayList<>(features);
    if (newFeatures.stream().noneMatch(new Predicate<Feature>() {
      @Override
      public boolean test(Feature feature) {
        return false;
      }
    })) {
      throw new IllegalArgumentException("Feature does not exist");
    }

    newFeatures.removeIf(f -> f.getId().equals(featureId));
    return new Product(id, code, name, description, isArchived, newFeatures, assignedOwner);

  }

  /**
   * 与えられたコード、名前、説明で新しいプロダクトを作成します。
   *
   * @param code プロダクトのコード。
   * @param name プロダクトの名前。
   * @param description プロダクトの説明。
   * @return 新しいプロダクトインスタンス。
   */
  public static Product create(String code, String name, String description
  ) {
    NonNullObject.of(code);
    MaxLengthString.of(code, 3);
    NonNullObject.of(name);
    MaxLengthString.of(name, 128);
    MaxLengthString.of(description, 1024);
    return new Product(SnowflakeId.newInstance(), code, name, description, DEFAULT_IS_ARCHIVED,
        new ArrayList<>(),
        ProductOwner.EMPTY);

  }

  /**
   * 与えられたコード、名前、説明で新しいプロダクトを作成します。
   *
   * @param name プロダクトの名前。
   * @param description プロダクトの説明。
   * @return 新しいプロダクトインスタンス。
   */
  public Product update(String name, String description) {
    return new Product(SnowflakeId.newInstance(), code, name, description, DEFAULT_IS_ARCHIVED,
        new ArrayList<>(),
        ProductOwner.EMPTY);

  }

  public Product archive() {
    return new Product(id, code, name, description, true, features, assignedOwner);

  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    Class<?> oEffectiveClass = o instanceof HibernateProxy
        ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
        : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer()
        .getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) {
      return false;
    }
    Product product = (Product) o;
    return getId() != null && Objects.equals(getId(), product.getId());
  }

  @Override
  public final int hashCode() {
    return Objects.hash(id);
  }
}
