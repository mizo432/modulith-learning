package undecided.authorization.presentation.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
   * ユーザーを作成します。
   *
   * @param user 作成するユーザー
   * @return 作成されたユーザー
   */
  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody User user) {
    return ResponseEntity.ok(userService.createUser(user));
  }

  /**
   * ユーザーを更新します。
   *
   * @param id ユーザーID
   * @param user 更新するユーザー
   * @return 更新されたユーザー
   */
  @PutMapping("/{id}")
  public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
    return userService.findUserById(id)
        .map(existingUser -> {
          user.setId(id);
          return ResponseEntity.ok(userService.updateUser(user));
        })
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * ユーザーを削除します。
   *
   * @param id 削除するユーザーのID
   * @return レスポンス
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    return userService.findUserById(id)
        .map(user -> {
          userService.deleteUser(id);
          return ResponseEntity.ok().<Void>build();
        })
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * ユーザーにロールを追加します。
   *
   * @param userId ユーザーID
   * @param roleName ロール名
   * @return 更新されたユーザー
   */
  @PostMapping("/{userId}/roles/{roleName}")
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
   * ユーザーからロールを削除します。
   *
   * @param userId ユーザーID
   * @param roleName ロール名
   * @return 更新されたユーザー
   */
  @DeleteMapping("/{userId}/roles/{roleName}")
  public ResponseEntity<User> removeRoleFromUser(
      @PathVariable Long userId,
      @PathVariable String roleName) {
    try {
      return ResponseEntity.ok(userService.removeRoleFromUser(userId, roleName));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }
}
