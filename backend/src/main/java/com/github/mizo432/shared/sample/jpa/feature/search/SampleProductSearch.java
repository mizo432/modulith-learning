package com.github.mizo432.shared.sample.jpa.feature.search;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 検索パターンのサンプルで使用される製品エンティティです。
 *
 * <p>メソッド名派生クエリ、JPQL LIKE、ExampleMatcher、Specificationなどの 検索アプローチの例として使用します。
 */
@Entity
@Table(name = "sample_products_search")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleProductSearch {

  @Id private SnowflakeId productId;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "category", nullable = false, length = 50)
  private String category;

  @Column(name = "price")
  private Long price;

  public SampleProductSearch(SnowflakeId productId, String name, String category, Long price) {
    this.productId = productId;
    this.name = name;
    this.category = category;
    this.price = price;
  }

  /**
   * 製品名で先頭一致検索するSpecificationです。
   *
   * @param namePrefix 先頭文字列
   * @return Specification
   */
  public static Specification<SampleProductSearch> nameStartsWith(String namePrefix) {
    return (root, query, cb) -> cb.like(root.get("name"), namePrefix + "%");
  }

  /**
   * 製品名で部分一致検索するSpecificationです。
   *
   * @param keyword 検索キーワード
   * @return Specification
   */
  public static Specification<SampleProductSearch> nameContains(String keyword) {
    return (root, query, cb) -> cb.like(root.get("name"), "%" + keyword + "%");
  }

  /**
   * カテゴリで完全一致検索するSpecificationです。
   *
   * @param category カテゴリ名
   * @return Specification
   */
  public static Specification<SampleProductSearch> categoryEquals(String category) {
    return (root, query, cb) -> cb.equal(root.get("category"), category);
  }

  /**
   * 価格が指定された範囲内の製品を検索するSpecificationです。
   *
   * @param minPrice 最小価格
   * @param maxPrice 最大価格
   * @return Specification
   */
  public static Specification<SampleProductSearch> priceBetween(Long minPrice, Long maxPrice) {
    return (root, query, cb) -> cb.between(root.get("price"), minPrice, maxPrice);
  }
}
