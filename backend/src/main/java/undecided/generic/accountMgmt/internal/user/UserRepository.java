package undecided.generic.accountMgmt.internal.user;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import undecided.generic.accountMgmt.spi.User;
import undecided.supporting.snowflake.spi.SnowflakeId;

/** ユーザーリポジトリインターフェースです。 */
public interface UserRepository extends CrudRepository<User, SnowflakeId> {

  Optional<User> findByUsername(String username);
}
