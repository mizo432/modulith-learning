package undecided.erp.shared.supporting.snowflake.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
class NodeId {

  public static final int MAX = 1023;
  @Getter
  private Integer value;

  @Override
  public String toString() {
    if (value == null) {
      return "null";
    }

    return value.toString();
  }
}
