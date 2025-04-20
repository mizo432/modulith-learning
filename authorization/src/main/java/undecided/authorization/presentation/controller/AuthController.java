package undecided.authorization.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import undecided.authorization.business.service.AuthenticationService;
import undecided.authorization.business.service.AuthorizationService;
import undecided.authorization.domain.model.user.User;

/**
 * 認証・認可コントローラー
 * <p>
 * 認証・認可に関するAPIエンドポイントを提供するコントローラークラスです。
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
          .body(new AuthResponse(false, null, "Invalid username or password"));
    }

    return ResponseEntity.ok(
        new AuthResponse(true, user.getUsername(), "Authentication successful"));
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
      @PathVariable Long userId,
      @RequestBody PasswordChangeRequest request) {
    try {
      User user = authenticationService.changePassword(
          userId,
          request.getCurrentPassword(),
          request.getNewPassword()
      );
      return ResponseEntity.ok(
          new AuthResponse(true, user.getUsername(), "Password changed successfully"));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(new AuthResponse(false, null, e.getMessage()));
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
      @RequestParam String username,
      @RequestParam String roleName) {
    boolean hasRole = authorizationService.hasRole(username, roleName);

    if (hasRole) {
      return ResponseEntity.ok(new AuthResponse(true, username, "User has the required role"));
    } else {
      return ResponseEntity.ok(
          new AuthResponse(false, username, "User does not have the required role"));
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
      @RequestParam String username,
      @RequestParam String permissionName) {
    boolean hasPermission = authorizationService.hasPermission(username, permissionName);

    if (hasPermission) {
      return ResponseEntity.ok(
          new AuthResponse(true, username, "User has the required permission"));
    } else {
      return ResponseEntity.ok(
          new AuthResponse(false, username, "User does not have the required permission"));
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
      @RequestParam String username,
      @RequestParam String resource,
      @RequestParam String action) {
    boolean isAuthorized = authorizationService.isAuthorized(username, resource, action);

    if (isAuthorized) {
      return ResponseEntity.ok(
          new AuthResponse(true, username, "User is authorized for the requested action"));
    } else {
      return ResponseEntity.ok(
          new AuthResponse(false, username, "User is not authorized for the requested action"));
    }
  }
}
