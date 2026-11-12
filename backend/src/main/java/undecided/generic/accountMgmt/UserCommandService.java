package undecided.generic.accountMgmt;

/** ユーザー操作用のコマンドインターフェースです。 */
public interface UserCommandService {

  /**
   * パスワードを変更します。
   *
   * @param username ユーザー名
   * @param request パスワード変更リクエスト
   */
  void changePassword(String username, ChangePasswordRequest request);
}
