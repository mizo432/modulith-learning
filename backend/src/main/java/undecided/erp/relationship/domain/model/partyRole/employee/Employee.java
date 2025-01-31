package undecided.erp.relationship.domain.model.partyRole.employee;

import static undecided.erp.common.precondition.ObjectPrecondition.checkNotNull;
import static undecided.erp.common.precondition.StringPrecondition.checkHalfWidthLengthClosed;
import static undecided.erp.common.precondition.StringPrecondition.checkNonEmpty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import undecided.erp.common.exception.BusinessException;
import undecided.erp.common.snowflake.SnowflakeIdProvider;

/**
 * システム内の従業員エンティティを表します。
 * <p>
 * このクラスは「relationship」スキーマ内の「employees」テーブルにマッピングされています。
 */
@Entity
@Table(name = "employees")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

  /**
   * 従業員の一意の識別子を表します。このフィールドは従業員エンティティの主キーとして使用されます。
   */
  @Id
  private Long employeeId;

  /**
   * 従業員の名前を表します。
   */
  @Column(nullable = false, length = 100)
  private String name;

  /**
   * 従業員の名前のイニシャルを表します。
   * <p>
   * 主に従業員のフルネームを省略して表記するために使用されます。
   */
  @Column(nullable = false, length = 3)
  private String initials;

  /**
   * 従業員の名前をカナで表したものを格納します。
   * <p>
   * 主に従業員のフルネームの日本語カナ表記を保存するために使用されます。
   */
  @Column(nullable = false, length = 100)
  private String kanaName;

  /**
   * 指定された名前およびカナ名を使用して、新しい{@code Employee}インスタンスを作成します。
   * <p>
   * 入力パラメータがnullまたは空でないこと、およびカナ名が特定のフォーマット要件を満たしていることを検証します。
   *
   * @param name 従業員の名前。nullまたは空であってはなりません。
   * @param kanaName 従業員のカナ名。nullおよび空であってはならず、半角幅の範囲内でなければなりません。
   * @return {@code Employee}クラスの新しいインスタンス
   * @throws BusinessException パラメータが検証基準を満たしていない場合にスローされます
   */
  public static Employee newInstance(String name, String kanaName) {
    checkNotNull(name, () -> new BusinessException("name must not be null"));
    checkNonEmpty(name, () -> new BusinessException("name must not be empty"));
    checkNotNull(kanaName, () -> new BusinessException("kanaName must not be null"));
    checkNonEmpty(kanaName, () -> new BusinessException("kanaName must not be empty"));
    checkHalfWidthLengthClosed(kanaName, () -> new BusinessException("kanaName must be half width"),
        1, 100);

    return new Employee(SnowflakeIdProvider.generateId(), name, String.valueOf(kanaName.charAt(0)),
        kanaName);
  }

}
