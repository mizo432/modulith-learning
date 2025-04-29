package undecided.authorization.presentation.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import undecided.authorization.domain.model.user.UserType;

/**
 * ユーザー作成リクエスト
 * <p>
 * 管理者がユーザーを作成する際に使用するリクエストクラスです。 必須フィールドは、ユーザー名、パスワード、メールアドレスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {

  /**
   * ユーザー名
   */
  private String username;

  /**
   * パスワード
   */
  private String password;

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
   * ロール名のリスト
   */
  private java.util.List<String> roles;
}
