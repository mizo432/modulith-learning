package undecided.authorization.presentation.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 初回ログイン時のパスワード変更リクエストDTO
 *
 * <p>初回ログイン時のパスワード変更リクエストのデータを保持するDTOクラスです。 現在のパスワードの確認は不要です。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirstLoginPasswordChangeRequest {

  private String newPassword;
  private String confirmPassword;
}
