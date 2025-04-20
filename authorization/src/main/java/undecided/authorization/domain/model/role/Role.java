package undecided.authorization.domain.model.role;

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
import undecided.authorization.domain.model.permission.Permission;

/**
 * ロールエンティティ
 * <p>
 * システム内のユーザーロールを表すエンティティクラスです。 ロール名と説明、および関連する権限のコレクションを持ちます。
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column
  private String description;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "role_permissions",
      joinColumns = @JoinColumn(name = "role_id"),
      inverseJoinColumns = @JoinColumn(name = "permission_id")
  )
  private Set<Permission> permissions = new HashSet<>();

  /**
   * ロールに権限を追加します。
   *
   * @param permission 追加する権限
   */
  public void addPermission(Permission permission) {
    this.permissions.add(permission);
  }

  /**
   * ロールから権限を削除します。
   *
   * @param permission 削除する権限
   */
  public void removePermission(Permission permission) {
    this.permissions.remove(permission);
  }

  /**
   * ロールが特定の権限を持っているかチェックします。
   *
   * @param permissionName チェックする権限名
   * @return 権限を持っている場合はtrue、そうでない場合はfalse
   */
  public boolean hasPermission(String permissionName) {
    return this.permissions.stream()
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
    Role role = (Role) o;
    return getId() != null && Objects.equals(getId(), role.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
        .getPersistentClass().hashCode() : getClass().hashCode();
  }
}
