package undecided.shared.value;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import undecided.shared.dateProvider.DateProvider;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class WhenOccured implements DateTimeValue {
  private final LocalDateTime value;

  public WhenOccured() {
    value = null;
  }

  public static WhenOccured current() {
    return new WhenOccured(DateProvider.currentLocalDateTime());
  }

  public static WhenOccured empty() {
    return new WhenOccured();
  }

  @Override
  public LocalDateTime value() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  /** WhenOccuredとデータベースのLocalDateTimeを相互に変換するためのJPAコンバーターです。 */
  @Converter(autoApply = true)
  public static class WhenOccuredConverter
      implements AttributeConverter<WhenOccured, LocalDateTime> {

    @Override
    public LocalDateTime convertToDatabaseColumn(WhenOccured attribute) {
      return attribute != null ? attribute.value() : null;
    }

    @Override
    public WhenOccured convertToEntityAttribute(@Nullable LocalDateTime dbData) {
      if (dbData == null) {
        return WhenOccured.empty();
      }
      return new WhenOccured(dbData);
    }
  }
}
