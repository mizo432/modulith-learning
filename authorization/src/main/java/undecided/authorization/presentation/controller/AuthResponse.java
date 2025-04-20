package undecided.authorization.presentation.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 認証レスポンスDTO
 * <p>
 * ユーザー認証レスポンスのデータを保持するDTOクラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

  private boolean success;
  private String username;
  private String message;
}
