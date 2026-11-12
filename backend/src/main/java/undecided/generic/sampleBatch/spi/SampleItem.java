package undecided.generic.sampleBatch.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Objects;
import lombok.Getter;
import lombok.ToString;

/**
 * バッチ基本構成サンプル用のエンティティクラス。
 *
 * <p>このクラスは、バッチ処理の基本パターン（Job/Step/Launcher）を学ぶためのサンプルデータとして使用されます。
 */
@Getter
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

  // JPAおよびバッチ処理用パッケージプライベートセッター
  public void setId(Long id) {
    this.id = id;
  }

  public void setItemCode(String itemCode) {
    this.itemCode = itemCode;
  }

  public void setItemName(String itemName) {
    this.itemName = itemName;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public void setPrice(Long price) {
    this.price = price;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SampleItem that = (SampleItem) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
