package undecided.erp.scrum.domain.model.team;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.erp.common.entity.PptEntity;
import undecided.erp.common.entity.SnowflakeId;

/**
 * Roleクラスは、エンティティとしてスクラム開発におけるロールを表現します。
 * <p>
 * このクラスは以下の特性を持ちます:
 * <ul>
 *   <li>`roleId`: ロール固有の識別子を表す。SnowflakeIdを使用。</li>
 *   <li>`name`: ロールの名前（プロダクトオーナー、スクラムマスター、開発者など）。</li>
 *   <li>`description`: ロールの詳細説明。</li>
 * </ul>
 */
@AllArgsConstructor
@Entity
@Table(name = "roles")
@NoArgsConstructor
public class Role extends PptEntity<Role> implements Serializable {

  /**
   * ユニークなロール識別子を表す変数。
   * <p>
   * アプリケーションにおける各ロールを一意に識別するために使用されます。 データベース上の "role_id" カラムに対応し、null 値は許可されていません。
   */
  @Id
  @Column(name = "role_id", columnDefinition = "BIGINT", nullable = false)
  @Convert(converter = SnowflakeId.SnowflakeIdConverter.class)
  private SnowflakeId roleId;

  /**
   * ロールの名前を表す変数。
   * <p>
   * ロールの識別に使用される名前です（プロダクトオーナー、スクラムマスター、開発者など）。 データベース上の "name" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "name", nullable = false, length = 50)
  private String name;

  /**
   * ロールの詳細説明を表す変数。
   * <p>
   * ロールの詳細な説明や責任を提供します。 データベース上の "description" カラムに対応します。
   */
  @Getter
  @Column(name = "description", length = 2000)
  private String description;

  /**
   * このメソッドはRoleクラスの文字列表現を生成します。
   * <p>
   * 各フィールドの値を連結して構成された文字列を返します。
   *
   * @return Roleオブジェクトのフィールド情報を含む文字列表現
   */
  @Override
  public String toString() {
    return "Role{" +
        "roleId=" + roleId +
        ", name='" + name + '\'' +
        ", description='" + description + '\'' +
        '}';
  }
}
