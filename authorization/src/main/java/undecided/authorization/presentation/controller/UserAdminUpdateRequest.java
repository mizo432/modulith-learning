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
}
