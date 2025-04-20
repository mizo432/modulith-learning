package undecided.authorization.business.service;

import undecided.authorization.domain.model.user.User;

/**
 * 認証サービス
 * <p>
 * ユーザー認証に関するビジネスロジックを提供するサービスインターフェースです。
 */
public interface AuthenticationService {

  /**
   * ユーザーを認証します。
   *
   * @param username ユーザー名
   * @param password パスワード
   * @return 認証されたユーザー、認証に失敗した場合はnull
   */
  User authenticate(String username, String password);

  /**
   * ユーザーのパスワードを変更します。
   *
   * @param userId ユーザーID
   * @param currentPassword 現在のパスワード
   * @param newPassword 新しいパスワード
   * @return パスワードが変更されたユーザー
   * @throws IllegalArgumentException 現在のパスワードが一致しない場合
   */
  User changePassword(Long userId, String currentPassword, String newPassword);

  /**
   * ユーザーのログイン情報を更新します。
   *
   * @param userId ユーザーID
   * @return 更新されたユーザー
   */
  User updateLoginInfo(Long userId);
}
