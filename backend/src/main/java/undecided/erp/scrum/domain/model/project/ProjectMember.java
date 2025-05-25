package undecided.erp.scrum.domain.model.project;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import undecided.erp.scrum.domain.model.team.Member;

/**
 * ProjectMemberクラスは、エンティティとしてプロジェクトのメンバーシップを表現します。
 *
 * <p>このクラスは以下の特性を持ちます:
 *
 * <ul>
 *   <li>`projectMemberId`: プロジェクトメンバー固有の識別子を表す。SnowflakeIdを使用。
 *   <li>`project`: 所属するプロジェクト。
 *   <li>`member`: メンバー。
 *   <li>`role`: プロジェクト内のロール。
 *   <li>`status`: メンバーシップの状態（招待中、アクティブなど）。
 * </ul>
 */
@AllArgsConstructor
@Entity
@Table(name = "project_members")
@NoArgsConstructor
public class ProjectMember extends PptEntity<ProjectMember> implements Serializable {

  /**
   * ユニークなプロジェクトメンバー識別子を表す変数。
   *
   * <p>アプリケーションにおける各プロジェクトメンバーを一意に識別するために使用されます。 データベース上の "project_member_id" カラムに対応し、null
   * 値は許可されていません。
   */
  @Id
  @Column(name = "project_member_id", columnDefinition = "BIGINT", nullable = false)
  @Convert(converter = SnowflakeId.SnowflakeIdConverter.class)
  private SnowflakeId projectMemberId;

  /**
   * このメンバーが所属するプロジェクトを表す変数。
   *
   * <p>メンバーが属するプロジェクトへの参照です。 データベース上の "project_id" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @ManyToOne
  @JoinColumn(name = "project_id", nullable = false)
  private Project project;

  /**
   * プロジェクトに所属するメンバーを表す変数。
   *
   * <p>プロジェクトに参加しているチームメンバーへの参照です。 データベース上の "member_id" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @ManyToOne
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  /**
   * メンバーのプロジェクト内のロールを表す変数。
   *
   * <p>プロジェクト内でのメンバーのロールへの参照です。 データベース上の "role_id" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @ManyToOne
  @JoinColumn(name = "role_id", nullable = false)
  private ProjectRole role;

  /**
   * メンバーシップの状態を表す変数。
   *
   * <p>メンバーシップの現在の状態（招待中、アクティブなど）を示します。 データベース上の "status" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private ProjectMemberStatus status;

  /**
   * 新しいプロジェクトメンバーを作成します。
   *
   * @param project プロジェクト
   * @param member メンバー
   * @param role プロジェクト内のロール
   * @return 新しいプロジェクトメンバーインスタンス
   */
  public static ProjectMember create(Project project, Member member, ProjectRole role) {
    ProjectMember projectMember = new ProjectMember();
    projectMember.projectMemberId = SnowflakeId.newInstance();
    projectMember.project = project;
    projectMember.member = member;
    projectMember.role = role;
    projectMember.status = ProjectMemberStatus.ACTIVE;
    return projectMember;
  }

  /**
   * メンバーを招待します。
   *
   * @param project プロジェクト
   * @param member メンバー
   * @param role プロジェクト内のロール
   * @return 招待されたプロジェクトメンバーインスタンス
   */
  public static ProjectMember invite(Project project, Member member, ProjectRole role) {
    ProjectMember projectMember = new ProjectMember();
    projectMember.projectMemberId = SnowflakeId.newInstance();
    projectMember.project = project;
    projectMember.member = member;
    projectMember.role = role;
    projectMember.status = ProjectMemberStatus.INVITED;
    return projectMember;
  }

  /**
   * 招待を承諾します。
   *
   * @return アクティブ化されたプロジェクトメンバーインスタンス
   */
  public ProjectMember acceptInvitation() {
    if (this.status == ProjectMemberStatus.INVITED) {
      this.status = ProjectMemberStatus.ACTIVE;
    }
    return this;
  }

  /**
   * 招待を拒否します。
   *
   * @return 拒否されたプロジェクトメンバーインスタンス
   */
  public ProjectMember declineInvitation() {
    if (this.status == ProjectMemberStatus.INVITED) {
      this.status = ProjectMemberStatus.DECLINED;
    }
    return this;
  }

  /**
   * メンバーのロールを変更します。
   *
   * @param role 新しいロール
   * @return 更新されたプロジェクトメンバーインスタンス
   */
  public ProjectMember changeRole(ProjectRole role) {
    this.role = role;
    return this;
  }

  /**
   * このメソッドはProjectMemberクラスの文字列表現を生成します。
   *
   * <p>各フィールドの値を連結して構成された文字列を返します。
   *
   * @return ProjectMemberオブジェクトのフィールド情報を含む文字列表現
   */
  @Override
  public String toString() {
    return "ProjectMember{"
        + "projectMemberId="
        + projectMemberId
        + ", project="
        + project
        + ", member="
        + member
        + ", role="
        + role
        + ", status="
        + status
        + '}';
  }
}
