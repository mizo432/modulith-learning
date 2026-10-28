package undecided.jbJobMgmt.jobDefinition.spi.jobdefinition;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.List;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * パラメータの仕様定義を表す値オブジェクトです。
 *
 * <p>JSON文字列としてデータベースに格納されます。
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParameterSpec {

  @Column(name = "parameter_spec", columnDefinition = "text")
  private String parametersJson;

  public ParameterSpec(List<ParameterItem> parameters) {
    if (parameters == null) {
      throw new IllegalArgumentException("Parameters must not be null");
    }
    this.parametersJson = toJson(parameters);
  }

  public static ParameterSpec empty() {
    return new ParameterSpec(List.of());
  }

  private String toJson(List<ParameterItem> parameters) {
    if (parameters.isEmpty()) {
      return "[]";
    }
    StringBuilder sb = new StringBuilder("[");
    for (int i = 0; i < parameters.size(); i++) {
      if (i > 0) sb.append(",");
      ParameterItem p = parameters.get(i);
      sb.append("{")
          .append("\"name\":\"").append(escape(p.getName())).append("\",")
          .append("\"type\":\"").append(escape(p.getType() != null ? p.getType() : "")).append("\",")
          .append("\"required\":").append(p.isRequired())
          .append("}");
    }
    sb.append("]");
    return sb.toString();
  }

  private String escape(String value) {
    if (value == null) return "";
    return value.replace("\\", "\\\\").replace("\"", "\\\"");
  }
}
