package undecided.generic.accountMgmt.internal.user;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import undecided.generic.accountMgmt.spi.User;
import undecided.generic.accountMgmt.spi.UserQuery;

/** ユーザー検索の実装クラスです。 */
@RequiredArgsConstructor
@Service
public class UserQueryImpl implements UserQuery {

  private final UserRepository userRepository;

  @Override
  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }
}
