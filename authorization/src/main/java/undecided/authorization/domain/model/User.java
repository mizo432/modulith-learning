package undecided.authorization.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ユーザーエンティティ
 * <p>
 * システムのユーザーを表すエンティティクラスです。 ユーザー名、パスワード、メールアドレスなどの基本情報と、 ユーザーに割り当てられたロールのコレクションを持ちます。
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(nullable = false)
  private boolean enabled;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "last_login_at")
  private LocalDateTime lastLoginAt;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  private Set<Role> roles = new HashSet<>();

  /**
   * ユーザーにロールを追加します。
   *
   * @param role 追加するロール
   */
  public void addRole(Role role) {
    this.roles.add(role);
  }

  /**
   * ユーザーからロールを削除します。
   *
   * @param role 削除するロール
   */
  public void removeRole(Role role) {
    this.roles.remove(role);
  }

  /**
   * ユーザーが特定のロールを持っているかチェックします。
   *
   * @param roleName チェックするロール名
   * @return ロールを持っている場合はtrue、そうでない場合はfalse
   */
  public boolean hasRole(String roleName) {
    return this.roles.stream()
        .anyMatch(role -> role.getName().equals(roleName));
  }

  /**
   * ユーザーが特定の権限を持っているかチェックします。
   *
   * @param permissionName チェックする権限名
   * @return 権限を持っている場合はtrue、そうでない場合はfalse
   */
  public boolean hasPermission(String permissionName) {
    return this.roles.stream()
        .flatMap(role -> role.getPermissions().stream())
        .anyMatch(permission -> permission.getName().equals(permissionName));
  }
}
