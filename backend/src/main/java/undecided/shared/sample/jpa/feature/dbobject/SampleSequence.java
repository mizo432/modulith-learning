package undecided.shared.sample.jpa.feature.dbobject;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * データベースシーケンスのサンプルエンティティです。
 *
 * <p>このエンティティは、JPAの@SequenceGeneratorアノテーションを使用して
 * データベースシーケンスからIDを生成するパターンを示します。
 *
 * <p>主な特徴:
 * <ul>
 *   <li>{@code @SequenceGenerator} でシーケンス名を指定</li>
 *   <li>{@code @GeneratedValue(strategy = GenerationType.SEQUENCE)} でシーケンス生成戦略を使用</li>
 *   <li>データベースのシーケンスオブジェクト（seq_sample_order）を参照</li>
 * </ul>
 *
 * <p>使用例:
 * <pre>{@code
 * @Entity
 * @Table(name = "orders")
 * public class Order {
 *     @Id
 *     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderSeq")
 *     @SequenceGenerator(name = "orderSeq", sequenceName = "seq_sample_order", allocationSize = 1)
 *     private Long orderId;
 * }
 * }</pre>
 *
 * @see <a href="https://github.com/mizo432/modulith-learning/issues/277">Issue #277</a>
 */
@Entity
@Table(name = "sample_sequence_demo")
@SequenceGenerator(
    name = "sampleOrderSeq",
    sequenceName = "seq_sample_order",
    schema = "undecided_as",
    allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleSequence {

  @Id
  @jakarta.persistence.GeneratedValue(
      strategy = jakarta.persistence.GenerationType.SEQUENCE,
      generator = "sampleOrderSeq"
  )
  private Long id;

  @Column(name = "sequence_name", nullable = false, length = 50)
  private String sequenceName;

  @Column(name = "description", length = 200)
  private String description;

  public SampleSequence(String sequenceName, String description) {
    this.sequenceName = sequenceName;
    this.description = description;
  }
}
