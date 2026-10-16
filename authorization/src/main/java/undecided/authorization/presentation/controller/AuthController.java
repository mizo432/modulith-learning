package undecided.authorization.presentation.controller;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import undecided.authorization.domain.model.user.User;
import undecided.authorization.useCase.service.AuthenticationService;
import undecided.authorization.useCase.service.AuthorizationService;

/**
 * 認証・認可コントローラー
 *
 * <p>認証・認可に関するAPIエンドポイントを提供するコントローラークラスです。
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationService authenticationService;
  private final AuthorizationService authorizationService;

  /**
   * ユーザーを認証します。
   *
   * @param request 認証リクエスト
   * @return 認証レスポンス
   */
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
    User user = authenticationService.authenticate(request.getUsername(), request.getPassword());

    if (user == null) {
      return ResponseEntity.badRequest()
          .body(new AuthResponse(false, null, "Invalid username or password", false));
    }

    return ResponseEntity.ok(
        new AuthResponse(
            true, user.getUsername(), "Authentication successful", user.isFirstLogin()));
  }

  /**
   * ユーザーのパスワードを変更します。
   *
   * @param userId ユーザーID
   * @param request パスワード変更リクエスト
   * @return 認証レスポンス
   */
  @PostMapping("/{userId}/change-password")
  public ResponseEntity<AuthResponse> changePassword(
      @PathVariable Long userId, @RequestBody PasswordChangeRequest request) {
    try {
      User user =
          authenticationService.changePassword(
              userId, request.getCurrentPassword(), request.getNewPassword());
      return ResponseEntity.ok(
          new AuthResponse(
              true, user.getUsername(), "Password changed successfully", user.isFirstLogin()));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(new AuthResponse(false, null, e.getMessage(), false));
    }
  }

  /**
   * ユーザーが特定のロールを持っているかチェックします。
   *
   * @param username ユーザー名
   * @param roleName ロール名
   * @return 認可レスポンス
   */
  @GetMapping("/check-role")
  public ResponseEntity<AuthResponse> checkRole(
      @RequestParam String username, @RequestParam String roleName) {
    boolean hasRole = authorizationService.hasRole(username, roleName);

    if (hasRole) {
      return ResponseEntity.ok(
          new AuthResponse(true, username, "User has the required role", false));
    } else {
      return ResponseEntity.ok(
          new AuthResponse(false, username, "User does not have the required role", false));
    }
  }

  /**
   * ユーザーが特定の権限を持っているかチェックします。
   *
   * @param username ユーザー名
   * @param permissionName 権限名
   * @return 認可レスポンス
   */
  @GetMapping("/check-permission")
  public ResponseEntity<AuthResponse> checkPermission(
      @RequestParam String username, @RequestParam String permissionName) {
    boolean hasPermission = authorizationService.hasPermission(username, permissionName);

    if (hasPermission) {
      return ResponseEntity.ok(
          new AuthResponse(true, username, "User has the required permission", false));
    } else {
      return ResponseEntity.ok(
          new AuthResponse(false, username, "User does not have the required permission", false));
    }
  }

  /**
   * ユーザーが特定のリソースに対して特定のアクションを実行する権限を持っているかチェックします。
   *
   * @param username ユーザー名
   * @param resource リソース
   * @param action アクション
   * @return 認可レスポンス
   */
  @GetMapping("/is-authorized")
  public ResponseEntity<AuthResponse> isAuthorized(
      @RequestParam String username, @RequestParam String resource, @RequestParam String action) {
    boolean isAuthorized = authorizationService.isAuthorized(username, resource, action);

    if (isAuthorized) {
      return ResponseEntity.ok(
          new AuthResponse(true, username, "User is authorized for the requested action", false));
    } else {
      return ResponseEntity.ok(
          new AuthResponse(
              false, username, "User is not authorized for the requested action", false));
    }
  }

  /**
   * ユーザーが自身のパスワードを変更します。
   *
   * @param request パスワード変更リクエスト
   * @param principal 認証済みユーザー
   * @return 認証レスポンス
   */
  @PostMapping("/change-password")
  public ResponseEntity<AuthResponse> changeOwnPassword(
      @RequestBody PasswordChangeRequest request, Principal principal) {
    try {
      User user =
          authenticationService.changePasswordByUsername(
              principal.getName(), request.getCurrentPassword(), request.getNewPassword());
      return ResponseEntity.ok(
          new AuthResponse(
              true, user.getUsername(), "Password changed successfully", user.isFirstLogin()));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(new AuthResponse(false, null, e.getMessage(), false));
    }
  }

  /**
   * 初回ログイン時のパスワード変更を行います。 現在のパスワードの確認は不要です。
   *
   * @param request 初回ログインパスワード変更リクエスト
   * @param principal 認証済みユーザー
   * @return 認証レスポンス
   */
  @PostMapping("/first-login/change-password")
  public ResponseEntity<AuthResponse> changePasswordOnFirstLogin(
      @RequestBody FirstLoginPasswordChangeRequest request, Principal principal) {
    try {
      // パスワード一致チェック
      if (request.getNewPassword() == null
          || !request.getNewPassword().equals(request.getConfirmPassword())) {
        return ResponseEntity.badRequest()
            .body(new AuthResponse(false, null, "Passwords do not match", false));
      }

      User user =
          authenticationService.changePasswordOnFirstLogin(
              principal.getName(), request.getNewPassword());
      return ResponseEntity.ok(
          new AuthResponse(
              true, user.getUsername(), "Password changed successfully", user.isFirstLogin()));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(new AuthResponse(false, null, e.getMessage(), false));
    }
  }

  /**
   * ユーザーの初回ログイン状態を確認します。 backendサービスから呼び出されるエンドポイントです。
   *
   * @param username ユーザー名
   * @return 初回ログイン状態
   */
  @GetMapping("/users/{username}/first-login")
  public ResponseEntity<UserFirstLoginStatus> getFirstLoginStatus(@PathVariable String username) {
    boolean isFirstLogin = authenticationService.isFirstLogin(username);
    return ResponseEntity.ok(new UserFirstLoginStatus(username, isFirstLogin));
  }

  /**
   * ユーザー名でパスワードを変更します。 backendサービスから呼び出されるエンドポイントです。
   *
   * @param username ユーザー名
   * @param request パスワード変更リクエスト
   * @return 204 No Content
   */
  @PostMapping("/users/{username}/password")
  public ResponseEntity<Void> changePasswordByUsername(
      @PathVariable String username, @RequestBody PasswordChangeRequestDto request) {
    try {
      authenticationService.changePasswordByUsername(
          username, request.currentPassword(), request.newPassword());
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * 初回ログイン時のパスワードを変更します。 backendサービスから呼び出されるエンドポイントです。
   *
   * @param username ユーザー名
   * @param request パスワード変更リクエスト
   * @return 204 No Content
   */
  @PostMapping("/users/{username}/first-login-password")
  public ResponseEntity<Void> changeFirstLoginPassword(
      @PathVariable String username, @RequestBody PasswordChangeRequestDto request) {
    try {
      authenticationService.changePasswordOnFirstLogin(username, request.newPassword());
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  /** 初回ログインステータスレスポンスです。 */
  public record UserFirstLoginStatus(String username, boolean isFirstLogin) {}

  /** パスワード変更リクエストDTOです。 */
  public record PasswordChangeRequestDto(String currentPassword, String newPassword) {}
}
