package undecided.generic.accountMgmt.spi;

/** パスワード変更リクエストを表す値オブジェクトです。 */
public record ChangePasswordRequest(
    String currentPassword, String newPassword, String newPasswordConfirm) {

  /**
   * 新しいパスワードと確認用パスワードが一致するか検証します。
   *
   * @return 一致する場合はtrue
   */
  public boolean isNewPasswordMatched() {
    return newPassword != null && newPassword.equals(newPasswordConfirm);
  }

  /**
   * 新しいパスワードが有効な長さかどうかを検証します。
   *
   * @return 8文字以上64文字以下の場合はtrue
   */
  public boolean isValidPasswordLength() {
    return newPassword != null && newPassword.length() >= 8 && newPassword.length() <= 64;
  }
}
