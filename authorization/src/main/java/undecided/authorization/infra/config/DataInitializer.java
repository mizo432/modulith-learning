package undecided.authorization.infra.config;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import undecided.authorization.domain.model.Permission;
import undecided.authorization.domain.model.Role;
import undecided.authorization.domain.model.User;
import undecided.authorization.infra.repository.PermissionRepository;
import undecided.authorization.infra.repository.RoleRepository;
import undecided.authorization.infra.repository.UserRepository;

/**
 * データ初期化
 * <p>
 * アプリケーション起動時に初期データを作成するクラスです。
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PermissionRepository permissionRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) {
    // 既存のデータがある場合は初期化をスキップ
    if (userRepository.count() > 0) {
      return;
    }

    System.out.println("Initializing data...");

    // 権限の作成
    List<Permission> permissions = createPermissions();

    // ロールの作成
    List<Role> roles = createRoles(permissions);

    // ユーザーの作成
    createUsers(roles);

    System.out.println("Data initialization completed.");
  }

  private List<Permission> createPermissions() {
    List<Permission> permissions = Arrays.asList(
        Permission.builder().name("user:read").description("ユーザー情報の読み取り権限").build(),
        Permission.builder().name("user:write").description("ユーザー情報の書き込み権限").build(),
        Permission.builder().name("user:delete").description("ユーザー情報の削除権限").build(),
        Permission.builder().name("role:read").description("ロール情報の読み取り権限").build(),
        Permission.builder().name("role:write").description("ロール情報の書き込み権限").build(),
        Permission.builder().name("role:delete").description("ロール情報の削除権限").build()
    );

    return permissionRepository.saveAll(permissions);
  }

  private List<Role> createRoles(List<Permission> permissions) {
    // 管理者ロール（すべての権限を持つ）
    Role adminRole = Role.builder()
        .name("ROLE_ADMIN")
        .description("管理者ロール")
        .build();
    permissions.forEach(adminRole::addPermission);

    // ユーザーロール（読み取り権限のみ）
    Role userRole = Role.builder()
        .name("ROLE_USER")
        .description("一般ユーザーロール")
        .build();
    permissions.stream()
        .filter(p -> p.getName().endsWith(":read"))
        .forEach(userRole::addPermission);

    return roleRepository.saveAll(Arrays.asList(adminRole, userRole));
  }

  private void createUsers(List<Role> roles) {
    Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow();
    Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow();

    // 管理者ユーザー
    User adminUser = User.builder()
        .username("admin")
        .password(passwordEncoder.encode("admin"))
        .email("admin@example.com")
        .firstName("Admin")
        .lastName("User")
        .enabled(true)
        .createdAt(LocalDateTime.now())
        .build();
    adminUser.addRole(adminRole);

    // 一般ユーザー
    User normalUser = User.builder()
        .username("user")
        .password(passwordEncoder.encode("user"))
        .email("user@example.com")
        .firstName("Normal")
        .lastName("User")
        .enabled(true)
        .createdAt(LocalDateTime.now())
        .build();
    normalUser.addRole(userRole);

    userRepository.saveAll(Arrays.asList(adminUser, normalUser));
  }
}
