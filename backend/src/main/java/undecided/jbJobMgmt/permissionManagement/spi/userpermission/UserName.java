package undecided.jbJobMgmt.permissionManagement.spi.userpermission;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ユーザー名を表す値オブジェクトです。
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserName {

  @Column(name = "user_name", nullable = false, length = 100)
  private String value;

  public UserName(String value) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("User name must not be null or blank");
    }
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}
