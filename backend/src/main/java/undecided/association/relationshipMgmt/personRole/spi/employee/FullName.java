package undecided.association.relationshipMgmt.personRole.spi.employee;

import com.github.mizo432.shared.common.precondition.ObjectPrecondition;
import com.github.mizo432.shared.common.precondition.StringPrecondition;
import com.github.mizo432.shared.common.primitive.Strings2;
import com.github.mizo432.shared.exception.BusinessException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
public final class FullName {

  /** 氏名(全角文字) */
  public static final String REGEX =
      "^[\u3000-\u303F\u3040-\u309F\u30A0-\u30FF\u4E00-\u9FFF\uFF01-\uFF5E]+$";

  public static final int MAX_LENGTH = 255;
  private final String value;

  // JPA用
  protected FullName() {
    this.value = null;
  }

  FullName(String value) {
    this.value = value;
  }

  public static FullName of(String value) {
    ObjectPrecondition.checkNotNull(
        value, () -> new BusinessException("FullName cannot be null"));
    StringPrecondition.checkMaxLength(
        value,
        MAX_LENGTH,
        () -> new BusinessException("FullName cannot exceed 255 characters"));
    StringPrecondition.checkRegex(
        value,
        REGEX,
        () -> new BusinessException("FullName must match the pattern " + REGEX));
    return new FullName(value);
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
    FullName that = (FullName) o;
    return java.util.Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(value);
  }
}
