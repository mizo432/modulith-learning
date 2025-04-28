package undecided.erp.scrum.domain.model.team;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.erp.common.entity.PptEntity;
import undecided.erp.common.entity.SnowflakeId;

/**
 * Memberクラスは、エンティティとしてスクラム開発におけるチームメンバーを表現します。
 * <p>
 * このクラスは以下の特性を持ちます:
 * <ul>
 *   <li>`memberId`: メンバー固有の識別子を表す。SnowflakeIdを使用。</li>
 *   <li>`team`: 所属するチーム。</li>
 *   <li>`name`: メンバーの名前。</li>
 *   <li>`email`: メンバーのメールアドレス。</li>
 *   <li>`role`: メンバーのロール（プロダクトオーナー、スクラムマスター、開発者など）。</li>
 * </ul>
 */
@AllArgsConstructor
@Entity
@Table(name = "team_members")
@NoArgsConstructor
public class Member extends PptEntity<Member> implements Serializable {

  /**
   * ユニークなメンバー識別子を表す変数。
   * <p>
   * アプリケーションにおける各チームメンバーを一意に識別するために使用されます。 データベース上の "member_id" カラムに対応し、null 値は許可されていません。
   */
  @Id
  @Column(name = "member_id", columnDefinition = "BIGINT", nullable = false)
  @Convert(converter = SnowflakeId.SnowflakeIdConverter.class)
  private SnowflakeId memberId;

  /**
   * このメンバーが所属するチームを表す変数。
   * <p>
   * メンバーが属するチームへの参照です。 データベース上の "team_id" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @ManyToOne
  @JoinColumn(name = "team_id", nullable = false)
  private Team team;

  /**
   * メンバーの名前を表す変数。
   * <p>
   * チームメンバーの名前です。 データベース上の "name" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  /**
   * メンバーのメールアドレスを表す変数。
   * <p>
   * チームメンバーの連絡先メールアドレスです。 データベース上の "email" カラムに対応します。
   */
  @Getter
  @Column(name = "email", length = 100)
  private String email;

  /**
   * メンバーのロールを表す変数。
   * <p>
   * チームメンバーのスクラムにおけるロール（プロダクトオーナー、スクラムマスター、開発者など）です。 データベース上の "role" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @ManyToOne
  @JoinColumn(name = "role_id", nullable = false)
  private Role role;

  /**
   * このメソッドはMemberクラスの文字列表現を生成します。
   * <p>
   * 各フィールドの値を連結して構成された文字列を返します。
   *
   * @return Memberオブジェクトのフィールド情報を含む文字列表現
   */
  @Override
  public String toString() {
    return "Member{" +
        "memberId=" + memberId +
        ", team=" + team +
        ", name='" + name + '\'' +
        ", email='" + email + '\'' +
        ", role=" + role +
        '}';
  }
}
