package undecided.shared.sample.jpa.value;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import undecided.shared.value.ValueObject;
import undecided.shared.sample.jpa.entity.converter.SampleCurrencyCode;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

/**
 * 商品（Product）を表すエンティティで、ValueObjectとIdValueの使用例です。
 *
 * <p>このエンティティは以下のパターンを示します:
 *
 * <ul>
 *   <li>{@link SnowflakeId}を@Idとして使用
 *   <li>{@link ValueObject}を実装する値オブジェクト（SampleCurrencyCode）を使用
 *   <li>{@link SnowflakeIdGenerator}でIDを生成するパターン
 * </ul>
 *
 * <p>ID生成の使用例:
 *
 * <pre>{@code
 * @Service
 * public class ProductService {
 *   private final SnowflakeIdGenerator snowflakeIdGenerator;
 *
 *   public SampleProduct createProduct(String name, Long price, String currency) {
 *     SnowflakeId id = snowflakeIdGenerator.nextIdValue();
 *     return new SampleProduct(id, name, price, SampleCurrencyCode.of(currency));
 *   }
 * }
 * }</pre>
 */
@Entity
@Table(name = "sample_products_value")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleProduct {

  @Id private SnowflakeId productId;

  /** 商品名 */
  @Column(nullable = false, length = 100)
  private String productName;

  /** 価格 */
  @Column(nullable = false)
  private Long price;

  /** 通貨コード */
  @Convert(converter = SampleCurrencyCode.SampleCurrencyCodeConverter.class)
  @Column(name = "currency_code", length = 3)
  private SampleCurrencyCode currencyCode;

  public SampleProduct(
      SnowflakeId productId, String productName, Long price, SampleCurrencyCode currencyCode) {
    this.productId = productId;
    this.productName = productName;
    this.price = price;
    this.currencyCode = currencyCode;
  }

  /**
   * ValueObjectのisEmpty()の使用例:
   *
   * <pre>{@code
   * if (ValueObject.ValueObjects.isEmpty(currencyCode)) {
   *   throw new IllegalStateException("Currency code is required");
   * }
   * }</pre>
   */
  public boolean hasCurrencyCode() {
    return currencyCode != null && !currencyCode.isEmpty();
  }
}
