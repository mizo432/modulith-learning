package undecided.authorization.business.service;

/**
 * 認可サービス
 * <p>
 * ユーザー認可に関するビジネスロジックを提供するサービスインターフェースです。
 */
public interface AuthorizationService {

  /**
   * ユーザーが特定のロールを持っているかチェックします。
   *
   * @param username ユーザー名
   * @param roleName ロール名
   * @return ロールを持っている場合はtrue、そうでない場合はfalse
   */
  boolean hasRole(String username, String roleName);

  /**
   * ユーザーが特定の権限を持っているかチェックします。
   *
   * @param username ユーザー名
   * @param permissionName 権限名
   * @return 権限を持っている場合はtrue、そうでない場合はfalse
   */
  boolean hasPermission(String username, String permissionName);

  /**
   * ユーザーが特定のリソースに対して特定のアクションを実行する権限を持っているかチェックします。
   *
   * @param username ユーザー名
   * @param resource リソース
   * @param action アクション
   * @return 権限を持っている場合はtrue、そうでない場合はfalse
   */
  boolean isAuthorized(String username, String resource, String action);
}
