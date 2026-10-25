package undecided.generic.sampleBatch.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * バッチ基本構成サンプル用のエンティティクラス。
 *
 * <p>このクラスは、バッチ処理の基本パターン（Job/Step/Launcher）を学ぶためのサンプルデータとして使用されます。
 */
@Getter
@Setter
@ToString
@Entity
@Table(schema = "undecided_ef", name = "sample_items", comment = "sample item table for batch demo")
public class SampleItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", comment = "Unique identifier", nullable = false)
  private Long id;

  @Size(max = 50)
  @NotNull
  @Column(name = "item_code", comment = "Item code", nullable = false, length = 50)
  private String itemCode;

  @Size(max = 100)
  @NotNull
  @Column(name = "item_name", comment = "Item name", nullable = false, length = 100)
  private String itemName;

  @Size(max = 20)
  @Column(name = "category", comment = "Category", length = 20)
  private String category;

  @Column(name = "price", comment = "Price")
  private Long price;
}
