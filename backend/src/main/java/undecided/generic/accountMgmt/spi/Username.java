package undecided.generic.accountMgmt.spi;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/** ユーザー名を表す値オブジェクトです。 */
public record Username(@NonNull String value) {
  public static final Username EMPTY = new Username(null);

  public Username(@Nullable String value) {
    if (value == null || value.isBlank()) {
      this.value = "";
      return;
    }
    if (value.length() > 50) {
      throw new IllegalArgumentException("Username must be 50 characters or less");
    }
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}
