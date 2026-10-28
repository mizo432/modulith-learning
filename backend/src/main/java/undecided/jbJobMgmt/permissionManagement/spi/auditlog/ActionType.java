package undecided.jbJobMgmt.permissionManagement.spi.auditlog;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 操作タイプを表す値オブジェクトです。
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActionType {

  @Column(name = "action_type", nullable = false, length = 50)
  private String type;

  public ActionType(String type) {
    if (type == null || type.isBlank()) {
      throw new IllegalArgumentException("Action type must not be null or blank");
    }
    this.type = type;
  }
}
