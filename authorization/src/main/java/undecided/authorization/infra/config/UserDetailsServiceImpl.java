package undecided.authorization.infra.config;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import undecided.authorization.domain.model.user.User;
import undecided.authorization.domain.model.user.UserRepository;

/**
 * UserDetailsService実装
 *
 * <p>OAuth 2.0認可サーバーがユーザー認証を行うために必要なUserDetailsServiceを実装します。 ユーザー名からUserエンティティを検索し、Spring
 * SecurityのUserDetailsに変換します。
 *
 * <p>REQ-00010: 認証はOAuth 2.0 Authorization Code with PKCEフローを使用すること。
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> userOpt = userRepository.findByUsername(username);
    if (userOpt.isEmpty()) {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }

    User user = userOpt.get();

    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getUsername())
        .password(user.getPassword())
        .disabled(!user.isEnabled())
        .roles(user.getRoles().stream().map(role -> role.getName()).toArray(String[]::new))
        .build();
  }
}
