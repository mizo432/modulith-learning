package undecided.association.relationshipMgmt.personRole.spi.employee;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import undecided.shared.precondition.ObjectPrecondition;
import undecided.shared.precondition.StringPrecondition;
import undecided.shared.primitive.Strings2;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchName {

  /** 検索用名称(全角文字) */
  public static final String REGEX =
      "^[\u3000-\u303F\u3040-\u309F\u30A0-\u30FF\u4E00-\u9FFF\uFF00-\uFF9F]+$";

  public static final int MAX_LENGTH = 50;
  private String value;

  public SearchName(String value) {
    this.value = value;
  }

  public static SearchName of(String value) {
    ObjectPrecondition.checkNotNull(
        value, () -> new IllegalArgumentException("SearchName cannot be null"));
    StringPrecondition.checkMaxLength(
        value,
        MAX_LENGTH,
        () -> new IllegalArgumentException("SearchName cannot exceed 50 characters"));
    StringPrecondition.checkRegex(
        value,
        REGEX,
        () -> new IllegalArgumentException("SearchName must match the pattern " + REGEX));
    return new SearchName(value);
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return Strings2.toString(value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SearchName that = (SearchName) o;
    return java.util.Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(value);
  }
}
