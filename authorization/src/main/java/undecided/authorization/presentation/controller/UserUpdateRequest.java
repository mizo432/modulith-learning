package undecided.authorization.presentation.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ユーザー情報更新リクエスト
 * <p>
 * ユーザーが自身の情報を更新する際に使用するリクエストクラスです。 更新可能なフィールドは、氏名、イニシャル、メールアドレスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

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
   * メールアドレス
   */
  private String email;
}
