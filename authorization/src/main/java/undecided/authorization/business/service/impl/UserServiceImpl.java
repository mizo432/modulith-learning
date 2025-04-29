package undecided.authorization.business.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.authorization.business.service.UserService;
import undecided.authorization.domain.model.role.Role;
import undecided.authorization.domain.model.role.RoleRepository;
import undecided.authorization.domain.model.user.User;
import undecided.authorization.domain.model.user.UserRepository;

/**
 * ユーザーサービス実装
 * <p>
 * ユーザー管理に関するビジネスロジックを提供するサービス実装クラスです。
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional(readOnly = true)
  public List<User> findAllUsers() {
    return userRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findUserById(Long id) {
    return userRepository.findById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public User createUser(User user) {
    // パスワードをハッシュ化
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    // 作成日時を設定
    user.setCreatedAt(LocalDateTime.now());
    // 有効フラグを設定
    user.setEnabled(true);
    return userRepository.save(user);
  }

  @Override
  public User updateUser(User user) {
    // 更新日時を設定
    user.setUpdatedAt(LocalDateTime.now());
    return userRepository.save(user);
  }

  @Override
  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }

  @Override
  public User addRoleToUser(Long userId, String roleName) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

    Role role = roleRepository.findByName(roleName)
        .orElseThrow(() -> new IllegalArgumentException("Role not found with name: " + roleName));

    user.addRole(role);
    return userRepository.save(user);
  }

  @Override
  public User removeRoleFromUser(Long userId, String roleName) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

    Role role = roleRepository.findByName(roleName)
        .orElseThrow(() -> new IllegalArgumentException("Role not found with name: " + roleName));

    user.removeRole(role);
    return userRepository.save(user);
  }

  @Override
  public User updateUserProfile(String username, String firstName, String lastName, String initials,
      String email) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(
            () -> new IllegalArgumentException("User not found with username: " + username));

    // メールアドレスが変更される場合、重複チェック
    if (email != null && !email.equals(user.getEmail())) {
      userRepository.findByEmail(email).ifPresent(existingUser -> {
        if (!existingUser.getId().equals(user.getId())) {
          throw new IllegalArgumentException("Email already in use: " + email);
        }
      });
      user.setEmail(email);
    }

    // 各フィールドを更新（nullでない場合のみ）
    if (firstName != null) {
      user.setFirstName(firstName);
    }
    if (lastName != null) {
      user.setLastName(lastName);
    }
    if (initials != null) {
      user.setInitials(initials);
    }

    // 更新日時を設定
    user.setUpdatedAt(LocalDateTime.now());

    return userRepository.save(user);
  }
}
