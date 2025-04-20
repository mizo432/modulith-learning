package undecided.authorization.presentation.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 認証リクエストDTO
 * <p>
 * ユーザー認証リクエストのデータを保持するDTOクラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

  private String username;
  private String password;
}
