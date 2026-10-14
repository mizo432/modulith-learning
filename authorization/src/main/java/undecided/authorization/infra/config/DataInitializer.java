package undecided.authorization.infra.config;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import undecided.authorization.domain.model.permission.Permission;
import undecided.authorization.domain.model.permission.PermissionRepository;
import undecided.authorization.domain.model.role.Role;
import undecided.authorization.domain.model.role.RoleRepository;
import undecided.authorization.domain.model.tenant.Tenant;
import undecided.authorization.domain.model.tenant.TenantRepository;
import undecided.authorization.domain.model.user.User;
import undecided.authorization.domain.model.user.UserRepository;
import undecided.authorization.domain.model.user.UserType;

/**
 * データ初期化
 *
 * <p>アプリケーション起動時に初期データを作成するクラスです。
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PermissionRepository permissionRepository;
  private final TenantRepository tenantRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) {
    // 既存のユーザーがある場合は初期化をスキップ
    if (userRepository.count() > 0) {
      return;
    }

    System.out.println("Initializing data...");

    // テナントの作成（Flywayによってdefaultテナントが既に存在する場合はpartnerのみ作成）
    List<Tenant> tenants = createTenants();

    // 権限の作成
    List<Permission> permissions = createPermissions();

    // ロールの作成
    List<Role> roles = createRoles(permissions);

    // ユーザーの作成
    createUsers(roles, tenants);

    System.out.println("Data initialization completed.");
  }

  private List<Tenant> createTenants() {
    // Flywayによってdefaultテナントが既に存在する場合は、partnerのみ作成
    List<Tenant> tenants = new ArrayList<>();

    if (tenantRepository.findByTenantCode("partner").isEmpty()) {
      Tenant partnerTenant =
          Tenant.builder()
              .tenantCode("partner")
              .name("パートナーテナント")
              .enabled(true)
              .createdAt(LocalDateTime.now())
              .build();
      tenants.add(partnerTenant);
    }

    return tenantRepository.saveAll(tenants);
  }

  private List<Permission> createPermissions() {
    List<Permission> permissions =
        Arrays.asList(
            Permission.builder().name("user:read").description("ユーザー情報の読み取り権限").build(),
            Permission.builder().name("user:write").description("ユーザー情報の書き込み権限").build(),
            Permission.builder().name("user:delete").description("ユーザー情報の削除権限").build(),
            Permission.builder().name("role:read").description("ロール情報の読み取り権限").build(),
            Permission.builder().name("role:write").description("ロール情報の書き込み権限").build(),
            Permission.builder().name("role:delete").description("ロール情報の削除権限").build());

    return permissionRepository.saveAll(permissions);
  }

  private List<Role> createRoles(List<Permission> permissions) {
    // 管理者ロール（すべての権限を持つ）
    Role adminRole = Role.builder().name("ROLE_ADMIN").description("管理者ロール").build();
    permissions.forEach(adminRole::addPermission);

    // ユーザーロール（読み取り権限のみ）
    Role userRole = Role.builder().name("ROLE_USER").description("一般ユーザーロール").build();
    permissions.stream()
        .filter(p -> p.getName().endsWith(":read"))
        .forEach(userRole::addPermission);

    return roleRepository.saveAll(Arrays.asList(adminRole, userRole));
  }

  private void createUsers(List<Role> roles, List<Tenant> tenants) {
    // 引数からROLE_ADMINとROLE_USERを取得
    Role adminRole =
        roles.stream().filter(r -> "ROLE_ADMIN".equals(r.getName())).findFirst().orElseThrow();
    Role userRole =
        roles.stream().filter(r -> "ROLE_USER".equals(r.getName())).findFirst().orElseThrow();

    // defaultテナントはFlywayで既に存在するため、DBから取得
    Tenant defaultTenant = tenantRepository.findByTenantCode("default").orElseThrow();
    // partnerテナントは引数から取得（存在しない場合はDBから取得）
    Tenant partnerTenant =
        tenants.stream()
            .filter(t -> "partner".equals(t.getTenantCode()))
            .findFirst()
            .orElse(tenantRepository.findByTenantCode("partner").orElseThrow());

    // 管理者ユーザー（社員）を作成 - 初回ログイン時にパスワード設定を強制
    User adminUser =
        User.builder()
            .username("admin")
            .password("")
            .email("admin@example.com")
            .firstName("Admin")
            .lastName("User")
            .initials("AU")
            .enabled(true)
            .createdAt(LocalDateTime.now())
            .userType(UserType.EMPLOYEE)
            .firstLogin(true)
            .tenant(defaultTenant)
            .build();
    adminUser.addRole(adminRole);
    userRepository.save(adminUser);

    // ビジネスパートナー社員ユーザーを作成
    User bpEmployeeUser =
        User.builder()
            .username("partner_employee")
            .password(passwordEncoder.encode("password"))
            .email("partner_employee@example.com")
            .firstName("Partner")
            .lastName("Employee")
            .initials("PE")
            .enabled(true)
            .createdAt(LocalDateTime.now())
            .userType(UserType.BUSINESS_PARTNER_EMPLOYEE)
            .tenant(partnerTenant)
            .build();
    bpEmployeeUser.addRole(userRole);
    userRepository.save(bpEmployeeUser);

    // ビジネスパートナー（個人）ユーザーを作成
    User individualBpUser =
        User.builder()
            .username("individual_partner")
            .password(passwordEncoder.encode("password"))
            .email("individual_partner@example.com")
            .firstName("Individual")
            .lastName("Partner")
            .initials("IP")
            .enabled(true)
            .createdAt(LocalDateTime.now())
            .userType(UserType.INDIVIDUAL_BUSINESS_PARTNER)
            .tenant(partnerTenant)
            .build();
    individualBpUser.addRole(userRole);
    userRepository.save(individualBpUser);
  }
}
