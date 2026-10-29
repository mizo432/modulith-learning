package com.github.mizo432.shared.sample.jpa.entity.hierarchy;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * Entity階層構造の具体的なサンプルエンティティです。
 *
 * <p>継承階層:
 *
 * <pre>
 * SampleBusinessEntity (基底クラス - @MappedSuperclass)
 *   └── SampleCategoryEntity (分類用抽象クラス - @MappedSuperclass)
 *       └── SampleProductEntity (具体的なエンティティ - @Entity)
 * </pre>
 *
 * <p>このエンティティは {@code @MappedSuperclass}による階層構造の例として、 製品情報を表します。
 */
@Entity
@Table(name = "sample_products")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleProductEntity extends SampleCategoryEntity<SampleProductEntity> {

  @Id private SnowflakeId productId;

  /** 製品名 */
  private String productName;

  /** 製品価格 */
  private Long price;

  public SampleProductEntity(SnowflakeId productId, String productName, Long price) {
    this.productId = productId;
    this.productName = productName;
    this.price = price;
  }
}
