package undecided.generic.largeRecordBatch.spi;

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
 * 大量レコード処理バッチサンプル用のエンティティクラス。
 *
 * <p>このクラスは、大量データ処理パターン（チャンク処理、エラー処理、フォールトトレランス）を
 * 学ぶためのサンプルデータとして使用されます。
 */
@Getter
@Setter
@ToString
@Entity
@Table(
    schema = "undecided_ef",
    name = "large_records",
    comment = "large record table for batch processing demo"
)
public class LargeRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", comment = "Unique identifier", nullable = false)
  private Long id;

  @Size(max = 50)
  @NotNull
  @Column(name = "record_code", comment = "Record code", nullable = false, length = 50)
  private String recordCode;

  @Size(max = 200)
  @NotNull
  @Column(name = "record_name", comment = "Record name", nullable = false, length = 200)
  private String recordName;

  @Size(max = 50)
  @Column(name = "category", comment = "Category", length = 50)
  private String category;

  @Size(max = 50)
  @Column(name = "sub_category", comment = "Sub category", length = 50)
  private String subCategory;

  @Column(name = "amount", comment = "Amount")
  private Long amount;

  @Column(name = "quantity", comment = "Quantity")
  private Integer quantity;

  @Size(max = 10)
  @Column(name = "status", comment = "Status (ACTIVE/INACTIVE)", length = 10)
  private String status;
}
