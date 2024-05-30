package undecided.erp.relMgmt.model.orgRole.company;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import undecided.erp.shared.value.MaxLengthString;
import undecided.erp.shared.value.NonNullString;

@RequiredArgsConstructor
@Getter
public class CompanyName {

  private static final int LENGTH = 128;
  private final String value;

  public static CompanyName of(@NonNull String value) {
    NonNullString.of(value);
    MaxLengthString.of(value, LENGTH);
    return new CompanyName(value);

  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

}
