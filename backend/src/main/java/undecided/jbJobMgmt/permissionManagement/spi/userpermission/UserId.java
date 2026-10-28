package undecided.jbJobMgmt.permissionManagement.spi.userpermission;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ユーザーIDを表す値オブジェクトです。
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserId {

  @Column(name = "user_id", nullable = false, length = 50)
  private String value;

  public UserId(String value) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("User ID must not be null or blank");
    }
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}
