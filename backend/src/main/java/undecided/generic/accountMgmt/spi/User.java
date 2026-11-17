package undecided.generic.accountMgmt.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * ユーザーアカウントを表すエンティティです。
 *
 * <p>機微情報（パスワード、初回ログインフラグ）は専用DB（authorization）に保管するため、
 * このエンティティには非機微情報のみを含みます。
 *
 * <p>C8修正により{@code @Data}は{@code @Getter}に置き換えられ、{@code create()}ファクトリメソッドを使用して
 * インスタンスを作成します。
 */
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

  /** ユーザーの一意な識別子（Snowflake ID）。 */
  @Id
  @Column(columnDefinition = "bigint")
  protected SnowflakeId id = SnowflakeId.EMPTY;

  /** ユーザー名。ログインに使用される一意な識別子です。 */
  @Column(unique = true, nullable = false, length = 50)
  protected String username;

  /** ユーザーの有効開始日。この日時からユーザーアカウントが有効となります。 */
  @Column(nullable = false)
  protected LocalDateTime validFrom;

  /** ユーザーの有効終了日。この日時までユーザーアカウントが有効となります。nullの場合は現在も有効です。 */
  protected LocalDateTime validTo;

  /**
   * ユーザーを新規作成します。
   *
   * @param id ID
   * @param username ユーザー名
   * @param validFrom 有効開始日
   * @param validTo 有効終了日
   * @return 新規作成されたUserインスタンス
   */
  public static User create(
          @NonNull SnowflakeId id,
          @NonNull String username,
          @NonNull LocalDateTime validFrom,
          @Nullable LocalDateTime validTo) {
    User user = new User();
    user.id = id;
    user.username = username;
    user.validFrom = validFrom;
    user.validTo = validTo;
    return user;
  }
}
