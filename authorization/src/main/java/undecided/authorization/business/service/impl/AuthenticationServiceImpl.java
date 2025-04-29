package undecided.authorization.business.service.impl;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.authorization.business.service.AuthenticationService;
import undecided.authorization.domain.model.user.User;
import undecided.authorization.domain.model.user.UserRepository;

/**
 * 認証サービス実装
 * <p>
 * ユーザー認証に関するビジネスロジックを提供するサービス実装クラスです。
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public User authenticate(String username, String password) {
    return userRepository.findByUsername(username)
        .filter(user -> passwordEncoder.matches(password, user.getPassword()) && user.isEnabled())
        .map(this::updateLoginInfo)
        .orElse(null);
  }

  @Override
  public User changePassword(Long userId, String currentPassword, String newPassword) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

    if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
      throw new IllegalArgumentException("Current password is incorrect");
    }

    user.setPassword(passwordEncoder.encode(newPassword));
    user.setUpdatedAt(LocalDateTime.now());
    return userRepository.save(user);
  }

  @Override
  public User updateLoginInfo(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

    return updateLoginInfo(user);
  }

  private User updateLoginInfo(User user) {
    user.setLastLoginAt(LocalDateTime.now());
    return userRepository.save(user);
  }

  @Override
  public User changePasswordByUsername(String username, String currentPassword,
      String newPassword) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(
            () -> new IllegalArgumentException("User not found with username: " + username));

    if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
      throw new IllegalArgumentException("Current password is incorrect");
    }

    user.setPassword(passwordEncoder.encode(newPassword));
    user.setUpdatedAt(LocalDateTime.now());
    return userRepository.save(user);
  }
}
