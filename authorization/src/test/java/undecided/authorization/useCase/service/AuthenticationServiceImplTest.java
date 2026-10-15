package undecided.authorization.useCase.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import undecided.authorization.domain.model.tenant.Tenant;
import undecided.authorization.domain.model.tenant.TenantRepository;
import undecided.authorization.domain.model.user.User;
import undecided.authorization.domain.model.user.UserRepository;
import undecided.authorization.useCase.service.impl.AuthenticationServiceImpl;

@Tag("small")
@DisplayName("AuthenticationServiceImplのテスト")
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

  @Mock private UserRepository userRepository;

  @Mock private TenantRepository tenantRepository;

  @Mock private PasswordEncoder passwordEncoder;

  private AuthenticationServiceImpl authenticationService;

  private Tenant testTenant;

  @BeforeEach
  void setUp() {
    authenticationService =
        new AuthenticationServiceImpl(userRepository, tenantRepository, passwordEncoder);
    testTenant =
        Tenant.builder()
            .id(1L)
            .tenantCode("test")
            .name("Test Tenant")
            .enabled(true)
            .createdAt(LocalDateTime.now())
            .build();
  }

  @Nested
  @DisplayName("changePasswordOnFirstLoginメソッドのテスト")
  class ChangePasswordOnFirstLoginTest {

    @Test
    @DisplayName("初回ログイン時にパスワードを変更できること")
    void shouldChangePasswordOnFirstLogin() {
      User user =
          User.builder()
              .id(1L)
              .username("admin")
              .password("")
              .email("admin@example.com")
              .firstLogin(true)
              .enabled(true)
              .createdAt(LocalDateTime.now())
              .tenant(testTenant)
              .build();

      String newPassword = "newPassword123";
      String hashedPassword = "$2a$10$hashedPassword";

      when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
      when(passwordEncoder.encode(newPassword)).thenReturn(hashedPassword);
      when(userRepository.save(any(User.class))).thenReturn(user);

      User result = authenticationService.changePasswordOnFirstLogin("admin", newPassword);

      assertThat(result.getPassword()).isEqualTo(hashedPassword);
      assertThat(result.isFirstLogin()).isFalse();
      verify(passwordEncoder).encode(newPassword);
      verify(userRepository).save(user);
    }

    @Test
    @DisplayName("初回ログインでない場合は例外がスローされること")
    void shouldThrowExceptionWhenNotFirstLogin() {
      User user =
          User.builder()
              .id(1L)
              .username("admin")
              .password("$2a$10$hashed")
              .email("admin@example.com")
              .firstLogin(false)
              .enabled(true)
              .createdAt(LocalDateTime.now())
              .tenant(testTenant)
              .build();

      when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

      assertThatThrownBy(() -> authenticationService.changePasswordOnFirstLogin("admin", "newPass"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("not on first login");

      verify(passwordEncoder, never()).encode(anyString());
      verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("ユーザーが見つからない場合は例外がスローされること")
    void shouldThrowExceptionWhenUserNotFound() {
      when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

      assertThatThrownBy(
              () -> authenticationService.changePasswordOnFirstLogin("unknown", "newPass"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("User not found");

      verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    @DisplayName("パスワードが短い場合は例外がスローされること")
    void shouldThrowExceptionWhenPasswordTooShort() {
      User user =
          User.builder()
              .id(1L)
              .username("admin")
              .password("")
              .email("admin@example.com")
              .firstLogin(true)
              .enabled(true)
              .createdAt(LocalDateTime.now())
              .tenant(testTenant)
              .build();

      when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

      assertThatThrownBy(() -> authenticationService.changePasswordOnFirstLogin("admin", "short"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("8 and 64 characters");

      verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    @DisplayName("パスワードが長い場合は例外がスローされること")
    void shouldThrowExceptionWhenPasswordTooLong() {
      User user =
          User.builder()
              .id(1L)
              .username("admin")
              .password("")
              .email("admin@example.com")
              .firstLogin(true)
              .enabled(true)
              .createdAt(LocalDateTime.now())
              .tenant(testTenant)
              .build();

      when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

      String longPassword = "a".repeat(65);

      assertThatThrownBy(
              () -> authenticationService.changePasswordOnFirstLogin("admin", longPassword))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("8 and 64 characters");

      verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    @DisplayName("パスワードがnullの場合は例外がスローされること")
    void shouldThrowExceptionWhenPasswordIsNull() {
      User user =
          User.builder()
              .id(1L)
              .username("admin")
              .password("")
              .email("admin@example.com")
              .firstLogin(true)
              .enabled(true)
              .createdAt(LocalDateTime.now())
              .tenant(testTenant)
              .build();

      when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

      assertThatThrownBy(() -> authenticationService.changePasswordOnFirstLogin("admin", null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("8 and 64 characters");

      verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    @DisplayName("パスワード変更成功后firstLoginフラグがfalseになること")
    void shouldSetFirstLoginToFalseAfterPasswordChange() {
      User user =
          User.builder()
              .id(1L)
              .username("admin")
              .password("")
              .email("admin@example.com")
              .firstLogin(true)
              .enabled(true)
              .createdAt(LocalDateTime.now())
              .tenant(testTenant)
              .build();

      when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
      when(passwordEncoder.encode("newPassword123")).thenReturn("$2a$10$hashed");
      when(userRepository.save(any(User.class))).thenReturn(user);

      authenticationService.changePasswordOnFirstLogin("admin", "newPassword123");

      assertThat(user.isFirstLogin()).isFalse();
    }
  }

  @Nested
  @DisplayName("authenticateメソッドのテスト")
  class AuthenticateTest {

    @Test
    @DisplayName("有効なテナントのユーザーは認証に成功すること")
    void shouldAuthenticateUserWithEnabledTenant() {
      User user =
          User.builder()
              .id(1L)
              .username("admin")
              .password("$2a$10$hashed")
              .email("admin@example.com")
              .firstLogin(false)
              .enabled(true)
              .createdAt(LocalDateTime.now())
              .tenant(testTenant)
              .build();

      when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
      when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);
      when(userRepository.save(any(User.class))).thenReturn(user);

      User result = authenticationService.authenticate("admin", "password");

      assertThat(result).isNotNull();
      assertThat(result.getUsername()).isEqualTo("admin");
    }

    @Test
    @DisplayName("無効なテナントのユーザーは認証に失敗すること")
    void shouldFailAuthenticateUserWithDisabledTenant() {
      Tenant disabledTenant =
          Tenant.builder()
              .id(2L)
              .tenantCode("disabled")
              .name("Disabled Tenant")
              .enabled(false)
              .createdAt(LocalDateTime.now())
              .build();

      User user =
          User.builder()
              .id(1L)
              .username("admin")
              .password("$2a$10$hashed")
              .email("admin@example.com")
              .firstLogin(false)
              .enabled(true)
              .createdAt(LocalDateTime.now())
              .tenant(disabledTenant)
              .build();

      when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
      when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);

      User result = authenticationService.authenticate("admin", "password");

      assertThat(result).isNull();
    }

    @Test
    @DisplayName("テナントがnullのユーザーは認証に失敗すること")
    void shouldFailAuthenticateUserWithNullTenant() {
      User user =
          User.builder()
              .id(1L)
              .username("admin")
              .password("$2a$10$hashed")
              .email("admin@example.com")
              .firstLogin(false)
              .enabled(true)
              .createdAt(LocalDateTime.now())
              .tenant(null)
              .build();

      when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
      when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);

      User result = authenticationService.authenticate("admin", "password");

      assertThat(result).isNull();
    }
  }
}
