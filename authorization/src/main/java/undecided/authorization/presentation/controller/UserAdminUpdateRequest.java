package undecided.authorization.presentation.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import undecided.authorization.domain.model.user.UserType;

/**
 * 管理者用ユーザー更新リクエスト
 * <p>
 * 管理者がユーザー情報を更新する際に使用するリクエストクラスです。 管理者は通常のユーザー情報に加えて、ユーザータイプや有効/無効状態も更新できます。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAdminUpdateRequest {

  /**
   * ユーザー名
   */
  private String username;

  /**
   * メールアドレス
   */
  private String email;

  /**
   * 名前
   */
  private String firstName;

  /**
   * 姓
   */
  private String lastName;

  /**
   * イニシャル（短い表記で表示する場合に使用）
   */
  private String initials;

  /**
   * ユーザータイプ
   */
  private UserType userType;

  /**
   * 有効フラグ
   */
  private Boolean enabled;

  /**
   * パスワード（変更する場合のみ設定）
   */
  private String password;

  /**
   * ロール名のリスト
   */
  private java.util.List<String> roles;

  /**
   * 従業員ID（SnowflakeId）。EMPLOYEEタイプのユーザーに設定。
   * 省略時は既存値を保持し、明示的なnullは関連付けの解除として扱うため、指定有無を別に保持する。
   */
  private Long employeeId;

  /** employeeIdがリクエストに含まれていたかどうか（省略と明示的なnullの区別用）。 */
  @lombok.Getter(lombok.AccessLevel.NONE)
  @lombok.Setter(lombok.AccessLevel.NONE)
  @com.fasterxml.jackson.annotation.JsonIgnore
  private boolean employeeIdSpecified;

  /**
   * 従業員IDを設定します。JSONにemployeeIdが含まれていた場合に呼ばれ、指定有無を記録します。
   *
   * @param employeeId 従業員ID（nullは関連付けの解除）
   */
  @com.fasterxml.jackson.annotation.JsonSetter("employeeId")
  public void setEmployeeId(Long employeeId) {
    this.employeeId = employeeId;
    this.employeeIdSpecified = true;
  }

  /**
   * employeeIdがリクエストで指定されたかどうかを返します。
   *
   * @return 指定された場合はtrue
   */
  @com.fasterxml.jackson.annotation.JsonIgnore
  public boolean isEmployeeIdSpecified() {
    return employeeIdSpecified;
  }
}
