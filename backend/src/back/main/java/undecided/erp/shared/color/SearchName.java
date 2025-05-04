package undecided.erp.shared.color;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NonNull;
import undecided.erp.common.precondition.StringPrecondition;
import undecided.erp.common.primitive.Objects2;
import undecided.erp.common.primitive.Strings2;
import undecided.erp.shared.entity.StringValue;

/**
 * 検索用名称
 */
@Getter
public class SearchName implements StringValue<SearchName> {

  public static final SearchName EMPTY = new SearchName();
  @JsonValue
  private final String value;

  private SearchName() {
    value = null;
  }

  private SearchName(@NonNull @NotEmpty final String value) {
    this.value = value;
  }

  @JsonCreator
  public static SearchName of(@NonNull @NotEmpty final String value) {
    StringPrecondition.checkNonEmpty(value,
        () -> new IllegalArgumentException("value can't be empty."));
    return new SearchName(value);
  }

  public static SearchName reconstruct(String value) {
    if (Strings2.isEmpty(value)) {
      return EMPTY;
    }

    return new SearchName(value);
  }

  @Override
  public String toString() {
    return String.valueOf(value);

  }

  @Override
  public boolean isEmpty() {
    return Objects2.isNull(value);
  }

}
