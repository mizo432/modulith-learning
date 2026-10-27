package undecided.generic.accountMgmt.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * ユーザーアカウントを表すエンティティです。
 *
 * <p>機微情報（パスワード、初回ログインフラグ）は専用DB（authorization）に保管するため、 このエンティティには非機微情報のみを含みます。
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @Column(columnDefinition = "bigint")
  private SnowflakeId id = SnowflakeId.EMPTY;

  @Column(unique = true, nullable = false, length = 50)
  private String username;

  @Column(nullable = false)
  private LocalDateTime validFrom;

  private LocalDateTime validTo;
}
