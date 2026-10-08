package undecided.generic.accountMgmt.spi;

/** ユーザー操作用のコマンドインターフェースです。 */
public interface UserCommand {

  /**
   * パスワードを変更します。
   *
   * @param username ユーザー名
   * @param request パスワード変更リクエスト
   */
  void changePassword(String username, ChangePasswordRequest request);
}
