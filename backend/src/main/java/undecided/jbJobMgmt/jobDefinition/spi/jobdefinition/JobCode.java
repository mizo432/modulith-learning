package undecided.jbJobMgmt.jobDefinition.spi.jobdefinition;

import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ジョブを一意に識別するコードを表す値オブジェクトです。
 *
 * <p>半角英数字とアンダースコアのみ使用可能で、最大50文字です。
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JobCode {

  private static final int MAX_LENGTH = 50;
  private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9_]+$");

  @jakarta.persistence.Column(name = "job_code", nullable = false, length = MAX_LENGTH, unique = true)
  private String value;

  public JobCode(String value) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("JobCode must not be null or blank");
    }
    if (value.length() > MAX_LENGTH) {
      throw new IllegalArgumentException("JobCode must not exceed " + MAX_LENGTH + " characters");
    }
    if (!PATTERN.matcher(value).matches()) {
      throw new IllegalArgumentException("JobCode must contain only alphanumeric characters and underscores");
    }
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}
