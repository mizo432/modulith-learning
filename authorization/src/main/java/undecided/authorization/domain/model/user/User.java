package undecided.authorization.domain.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import undecided.authorization.domain.model.role.Role;

/**
 * ユーザーエンティティ
 * <p>
 * システムのユーザーを表すエンティティクラスです。 ユーザー名、パスワード、メールアドレスなどの基本情報と、 ユーザーに割り当てられたロールのコレクションを持ちます。
 * <p>
 * ユーザーは以下の3種類のタイプに分類されます：
 * <ul>
 *   <li>社員（EMPLOYEE）</li>
 *   <li>ビジネスパートナー社員（BUSINESS_PARTNER_EMPLOYEE）</li>
 *   <li>ビジネスパートナー（個人）（INDIVIDUAL_BUSINESS_PARTNER）</li>
 * </ul>
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class User {

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  private final Set<Role> roles = new HashSet<>();
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

  /**
   * ユーザータイプを表す変数。
   * <p>
   * ユーザーの種類（社員、ビジネスパートナー社員、ビジネスパートナー（個人））を示します。 データベース上の "user_type" カラムに対応し、デフォルト値は
   * EMPLOYEE（社員）です。
   */
  @Enumerated(EnumType.STRING)
  @Column(name = "user_type", nullable = false)
  @Builder.Default
  private UserType userType = UserType.EMPLOYEE;

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

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    Class<?> oEffectiveClass = o instanceof HibernateProxy
        ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
        : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer()
        .getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) {
      return false;
    }
    User user = (User) o;
    return getId() != null && Objects.equals(getId(), user.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
        .getPersistentClass().hashCode() : getClass().hashCode();
  }
}
