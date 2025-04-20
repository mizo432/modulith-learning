package undecided.authorization.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * パスワード変更リクエストDTO
 * <p>
 * ユーザーパスワード変更リクエストのデータを保持するDTOクラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeRequest {

  private String currentPassword;
  private String newPassword;
}
