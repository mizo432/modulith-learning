package undecided.authorization.business.service;

import undecided.authorization.domain.model.user.User;

/**
 * 認証サービス
 *
 * <p>ユーザー認証に関するビジネスロジックを提供するサービスインターフェースです。
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

  /**
   * ユーザー名を使用してユーザーのパスワードを変更します。
   *
   * @param username ユーザー名
   * @param currentPassword 現在のパスワード
   * @param newPassword 新しいパスワード
   * @return パスワードが変更されたユーザー
   * @throws IllegalArgumentException 現在のパスワードが一致しない場合、またはユーザーが見つからない場合
   */
  User changePasswordByUsername(String username, String currentPassword, String newPassword);

  /**
   * ユーザーが初回ログインかどうかを確認します。
   *
   * @param username ユーザー名
   * @return 初回ログインの場合はtrue、そうでない場合はfalse
   */
  boolean isFirstLogin(String username);

  /**
   * 初回ログイン時のパスワード変更を行います。 現在のパスワードの確認は不要です。
   *
   * @param username ユーザー名
   * @param newPassword 新しいパスワード
   * @return パスワードが変更されたユーザー
   * @throws IllegalArgumentException ユーザーが見つからない場合
   */
  User changePasswordOnFirstLogin(String username, String newPassword);
}
