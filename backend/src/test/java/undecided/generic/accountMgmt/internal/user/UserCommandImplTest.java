package undecided.generic.accountMgmt.internal.user;

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
import undecided.generic.accountMgmt.spi.ChangePasswordRequest;
import undecided.generic.accountMgmt.spi.User;
import undecided.shared.exception.BusinessException;
import undecided.supporting.snowflake.spi.SnowflakeId;

@Tag("small")
@DisplayName("UserCommandImplのテスト")
@ExtendWith(MockitoExtension.class)
class UserCommandImplTest {

  @Mock private UserRepository userRepository;

  @Mock private PasswordEncoder passwordEncoder;

  private UserCommandImpl userCommandImpl;

  @BeforeEach
  void setUp() {
    userCommandImpl = new UserCommandImpl(userRepository, passwordEncoder);
  }

  @Nested
  @DisplayName("changePasswordメソッドのテスト")
  class ChangePasswordTest {

    @Test
    @DisplayName("初回ログイン時にパスワードを変更できる")
    void shouldChangePasswordOnFirstLogin() {
      User user =
          new User(SnowflakeId.of(1L), "testuser", "default", true, LocalDateTime.now(), null);
      ChangePasswordRequest request =
          new ChangePasswordRequest(null, "newPassword123", "newPassword123");
      String hashedPassword = "$2a$10$hashedPassword123";

      when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
      when(passwordEncoder.encode("newPassword123")).thenReturn(hashedPassword);

      userCommandImpl.changePassword("testuser", request);

      assertThat(user.getPassword()).isEqualTo(hashedPassword);
      assertThat(user.needsPasswordChange()).isFalse();
      verify(passwordEncoder).encode("newPassword123");
      verify(userRepository).save(user);
    }

    @Test
    @DisplayName("初回ログイン以外で現在のパスワードが正しい場合に変更できる")
    void shouldChangePasswordWhenCurrentPasswordIsCorrect() {
      String hashedCurrentPassword = "$2a$10$hashedCurrentPassword";
      String hashedNewPassword = "$2a$10$hashedNewPassword123";
      User user =
          new User(
              SnowflakeId.of(1L),
              "testuser",
              hashedCurrentPassword,
              false,
              LocalDateTime.now(),
              null);
      ChangePasswordRequest request =
          new ChangePasswordRequest("currentPass", "newPassword123", "newPassword123");

      when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
      when(passwordEncoder.matches("currentPass", hashedCurrentPassword)).thenReturn(true);
      when(passwordEncoder.encode("newPassword123")).thenReturn(hashedNewPassword);

      userCommandImpl.changePassword("testuser", request);

      assertThat(user.getPassword()).isEqualTo(hashedNewPassword);
      verify(passwordEncoder).matches("currentPass", hashedCurrentPassword);
      verify(passwordEncoder).encode("newPassword123");
      verify(userRepository).save(user);
    }

    @Test
    @DisplayName("初回ログイン以外で現在のパスワードが正しくない場合は例外がスローされる")
    void shouldThrowExceptionWhenCurrentPasswordIsIncorrect() {
      String hashedCurrentPassword = "$2a$10$hashedCurrentPassword";
      User user =
          new User(
              SnowflakeId.of(1L),
              "testuser",
              hashedCurrentPassword,
              false,
              LocalDateTime.now(),
              null);
      ChangePasswordRequest request =
          new ChangePasswordRequest("wrongPass", "newPassword123", "newPassword123");

      when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
      when(passwordEncoder.matches("wrongPass", hashedCurrentPassword)).thenReturn(false);

      assertThatThrownBy(() -> userCommandImpl.changePassword("testuser", request))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("現在のパスワードが正しくありません");

      verify(passwordEncoder, never()).encode(anyString());
      verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("新しいパスワードが短い場合は例外がスローされる")
    void shouldThrowExceptionWhenNewPasswordIsTooShort() {
      ChangePasswordRequest request = new ChangePasswordRequest(null, "short", "short");

      assertThatThrownBy(() -> userCommandImpl.changePassword("testuser", request))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("8文字以上");
    }

    @Test
    @DisplayName("新しいパスワードが長い場合は例外がスローされる")
    void shouldThrowExceptionWhenNewPasswordIsTooLong() {
      String longPassword = "a".repeat(65);
      ChangePasswordRequest request = new ChangePasswordRequest(null, longPassword, longPassword);

      assertThatThrownBy(() -> userCommandImpl.changePassword("testuser", request))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("64文字以下");
    }

    @Test
    @DisplayName("新しいパスワードと確認用パスワードが一致しない場合は例外がスローされる")
    void shouldThrowExceptionWhenPasswordsDoNotMatch() {
      ChangePasswordRequest request =
          new ChangePasswordRequest(null, "newPassword123", "differentPass");

      assertThatThrownBy(() -> userCommandImpl.changePassword("testuser", request))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("一致しません");
    }

    @Test
    @DisplayName("ユーザーが見つからない場合は例外がスローされる")
    void shouldThrowExceptionWhenUserNotFound() {
      ChangePasswordRequest request =
          new ChangePasswordRequest(null, "newPassword123", "newPassword123");

      when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

      assertThatThrownBy(() -> userCommandImpl.changePassword("unknown", request))
          .isInstanceOf(jakarta.persistence.EntityNotFoundException.class)
          .hasMessageContaining("ユーザーが見つかりません");
    }

    @Test
    @DisplayName("パスワードはハッシュ化して保存される")
    void shouldSaveHashedPassword() {
      User user =
          new User(SnowflakeId.of(1L), "testuser", "default", true, LocalDateTime.now(), null);
      ChangePasswordRequest request =
          new ChangePasswordRequest(null, "plainPassword", "plainPassword");
      String hashedPassword = "$2a$10$hashedPlainPassword";

      when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
      when(passwordEncoder.encode("plainPassword")).thenReturn(hashedPassword);

      userCommandImpl.changePassword("testuser", request);

      assertThat(user.getPassword()).isNotEqualTo("plainPassword");
      assertThat(user.getPassword()).isEqualTo(hashedPassword);
      verify(passwordEncoder).encode("plainPassword");
    }
  }
}
