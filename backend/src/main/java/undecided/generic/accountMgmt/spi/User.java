package undecided.generic.accountMgmt.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.spi.SnowflakeId;

/** ユーザーアカウントを表すエンティティです。 初回ログイン時のパスワード変更状態を管理します。 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id private SnowflakeId id = SnowflakeId.EMPTY;

  @Column(unique = true, nullable = false, length = 50)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private boolean isFirstLogin = true;

  @Column(nullable = false)
  private LocalDateTime validFrom;

  private LocalDateTime validTo;

  /**
   * パスワードを変更し、初回ログインフラグをクリアします。
   *
   * @param newPassword 新しいパスワード
   * @return 更新されたUserインスタンス
   */
  public User changePassword(@NonNull String newPassword) {
    this.password = newPassword;
    this.isFirstLogin = false;
    return this;
  }

  /**
   * 初回ログイン状態かどうかを返します。
   *
   * @return 初回ログイン状態の場合はtrue
   */
  public boolean needsPasswordChange() {
    return isFirstLogin;
  }
}
