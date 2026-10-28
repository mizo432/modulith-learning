package undecided.jobMgmt.jobDefinition.spi.jobdefinition;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import java.util.List;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * パラメータ項目を表す値オブジェクトです。
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParameterItem {

  private String name;
  private String type;
  private boolean required;
  private String defaultValue;
  private String description;

  public ParameterItem(String name, String type, boolean required) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Parameter name must not be null or blank");
    }
    this.name = name;
    this.type = type;
    this.required = required;
  }

  public ParameterItem(String name, String type, boolean required, String defaultValue, String description) {
    this(name, type, required);
    this.defaultValue = defaultValue;
    this.description = description;
  }
}
