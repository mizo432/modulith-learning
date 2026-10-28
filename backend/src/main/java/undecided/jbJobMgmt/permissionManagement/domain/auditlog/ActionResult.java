package undecided.jbJobMgmt.permissionManagement.domain.auditlog;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 操作結果を表す値オブジェクトです。
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActionResult {

  @Column(name = "result", nullable = false, length = 20)
  private String result;

  @Column(name = "message", length = 500)
  private String message;

  public ActionResult(String result) {
    if (result == null || result.isBlank()) {
      throw new IllegalArgumentException("Result must not be null or blank");
    }
    this.result = result;
  }

  public ActionResult(String result, String message) {
    this(result);
    this.message = message;
  }

  public static ActionResult success() {
    return new ActionResult("success");
  }

  public static ActionResult failure(String message) {
    return new ActionResult("failure", message);
  }
}
