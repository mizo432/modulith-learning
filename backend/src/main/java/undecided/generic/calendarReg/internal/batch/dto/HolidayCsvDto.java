package undecided.generic.calendarReg.internal.batch.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jspecify.annotations.Nullable;

/** デジタル庁 / 内閣府 国民の祝日 CSV (syukujitsu.csv) データ用 DTO クラス。 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class HolidayCsvDto implements Serializable {

  /** 祝日・休日月日 (例: "2024/01/01", "2024/1/1", "2024-01-01") */
  private @Nullable String holidayDate;

  /** 祝日・休日名称 (例: "元日", "成人の日") */
  private @Nullable String holidayName;

  /** 備考 */
  private @Nullable String remarks;

  /**
   * 祝日日付文字列を解析して {@link LocalDate} を返します。
   *
   * @return 解析された LocalDate。解析できない場合は null
   */
  public @Nullable LocalDate getParsedHolidayDate() {
    if (holidayDate == null || holidayDate.isBlank()) {
      return null;
    }
    String trimmed = holidayDate.trim();
    // 形式変換: "2024/1/1" -> "2024-01-01"
    String normalized = trimmed.replace('/', '-');
    try {
      return LocalDate.parse(normalized, DateTimeFormatter.ISO_LOCAL_DATE);
    } catch (DateTimeParseException ignored) {
      // ISOフォーマット以外のパターン（例: "2024-1-1" や "20240101" 等）に対応
      try {
        DateTimeFormatter flexibleFormatter = DateTimeFormatter.ofPattern("[yyyy-M-d][yyyyMMdd]");
        return LocalDate.parse(normalized, flexibleFormatter);
      } catch (DateTimeParseException e) {
        return null;
      }
    }
  }

  /**
   * 祝日日付から一意なID (例: 20240101L) を生成します。
   *
   * @return 祝日ID。日付が無効な場合は null
   */
  public @Nullable Long getHolidayId() {
    LocalDate parsed = getParsedHolidayDate();
    if (parsed == null) {
      return null;
    }
    long id =
        (long) parsed.getYear() * 10000
            + (long) parsed.getMonthValue() * 100
            + parsed.getDayOfMonth();
    return id;
  }
}
