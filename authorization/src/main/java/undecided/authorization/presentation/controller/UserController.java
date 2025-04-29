package undecided.authorization.presentation.controller;

import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.authorization.business.service.UserService;
import undecided.authorization.domain.model.user.User;
import undecided.authorization.domain.model.user.UserType;

/**
 * ユーザーコントローラー
 * <p>
 * ユーザー管理に関するAPIエンドポイントを提供するコントローラークラスです。
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  /**
   * すべてのユーザーを取得します。
   *
   * @return ユーザーのリスト
   */
  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    return ResponseEntity.ok(userService.findAllUsers());
  }

  /**
   * IDでユーザーを検索します。
   *
   * @param id ユーザーID
   * @return ユーザー
   */
  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable Long id) {
    return userService.findUserById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * ユーザーを作成します。 管理者権限を持つユーザーのみがこの操作を実行できます。
   *
   * @param request 作成するユーザーの情報
   * @return 作成されたユーザー
   */
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<User> createUser(@RequestBody UserCreateRequest request) {
    try {
      // リクエストからユーザーエンティティを作成
      User user = User.builder()
          .username(request.getUsername())
          .password(request.getPassword())
          .email(request.getEmail())
          .firstName(request.getFirstName())
          .lastName(request.getLastName())
          .initials(request.getInitials())
          .userType(request.getUserType() != null ? request.getUserType() : UserType.EMPLOYEE)
          .build();

      // ユーザーを作成
      User createdUser = userService.createUser(user);

      // ロールを追加（指定されている場合）
      if (request.getRoles() != null && !request.getRoles().isEmpty()) {
        for (String roleName : request.getRoles()) {
          userService.addRoleToUser(createdUser.getId(), roleName);
        }
        // 最新のユーザー情報を取得
        createdUser = userService.findUserById(createdUser.getId()).orElse(createdUser);
      }

      return ResponseEntity.ok(createdUser);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * ユーザーを更新します。 管理者権限を持つユーザーのみがこの操作を実行できます。
   *
   * @param id ユーザーID
   * @param request 更新するユーザーの情報
   * @return 更新されたユーザー
   */
  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<User> updateUser(@PathVariable Long id,
      @RequestBody UserAdminUpdateRequest request) {
    try {
      return userService.findUserById(id)
          .map(existingUser -> {
            // 基本情報の更新
            if (request.getUsername() != null) {
              existingUser.setUsername(request.getUsername());
            }
            if (request.getEmail() != null) {
              existingUser.setEmail(request.getEmail());
            }
            if (request.getFirstName() != null) {
              existingUser.setFirstName(request.getFirstName());
            }
            if (request.getLastName() != null) {
              existingUser.setLastName(request.getLastName());
            }
            if (request.getInitials() != null) {
              existingUser.setInitials(request.getInitials());
            }
            if (request.getUserType() != null) {
              existingUser.setUserType(request.getUserType());
            }
            if (request.getEnabled() != null) {
              existingUser.setEnabled(request.getEnabled());
            }

            // パスワードの更新（指定されている場合のみ）
            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
              // パスワードの更新はUserServiceに任せる（ハッシュ化などが必要なため）
              // 一時的に設定しておき、UserServiceでハッシュ化される
              existingUser.setPassword(request.getPassword());
            }

            // ユーザーを更新
            User updatedUser = userService.updateUser(existingUser);

            // ロールの更新（指定されている場合）
            if (request.getRoles() != null && !request.getRoles().isEmpty()) {
              // 既存のロールをクリア
              updatedUser.getRoles().clear();
              userService.updateUser(updatedUser);

              // 新しいロールを追加
              for (String roleName : request.getRoles()) {
                userService.addRoleToUser(updatedUser.getId(), roleName);
              }
              // 最新のユーザー情報を取得
              updatedUser = userService.findUserById(updatedUser.getId()).orElse(updatedUser);
            }

            return ResponseEntity.ok(updatedUser);
          })
          .orElse(ResponseEntity.notFound().build());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * ユーザーを削除します。 管理者権限を持つユーザーのみがこの操作を実行できます。
   *
   * @param id 削除するユーザーのID
   * @return レスポンス
   */
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    return userService.findUserById(id)
        .map(user -> {
          userService.deleteUser(id);
          return ResponseEntity.ok().<Void>build();
        })
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * ユーザーにロールを追加します。 管理者権限を持つユーザーのみがこの操作を実行できます。
   *
   * @param userId ユーザーID
   * @param roleName ロール名
   * @return 更新されたユーザー
   */
  @PostMapping("/{userId}/roles/{roleName}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<User> addRoleToUser(
      @PathVariable Long userId,
      @PathVariable String roleName) {
    try {
      return ResponseEntity.ok(userService.addRoleToUser(userId, roleName));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * ユーザーからロールを削除します。 管理者権限を持つユーザーのみがこの操作を実行できます。
   *
   * @param userId ユーザーID
   * @param roleName ロール名
   * @return 更新されたユーザー
   */
  @DeleteMapping("/{userId}/roles/{roleName}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<User> removeRoleFromUser(
      @PathVariable Long userId,
      @PathVariable String roleName) {
    try {
      return ResponseEntity.ok(userService.removeRoleFromUser(userId, roleName));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * ユーザーが自身の情報を更新します。 更新可能なフィールドは、氏名、イニシャル、メールアドレスです。
   *
   * @param request 更新リクエスト
   * @param principal 認証済みユーザー
   * @return 更新されたユーザー
   */
  @PutMapping("/profile")
  public ResponseEntity<?> updateProfile(
      @RequestBody UserUpdateRequest request,
      Principal principal) {
    try {
      User updatedUser = userService.updateUserProfile(
          principal.getName(),
          request.getFirstName(),
          request.getLastName(),
          request.getInitials(),
          request.getEmail()
      );
      return ResponseEntity.ok(updatedUser);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
