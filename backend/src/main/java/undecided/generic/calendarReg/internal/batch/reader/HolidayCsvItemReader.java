package undecided.generic.calendarReg.internal.batch.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.item.ItemStreamException;
import org.springframework.batch.infrastructure.item.ItemStreamReader;
import org.springframework.batch.infrastructure.item.NonTransientResourceException;
import org.springframework.batch.infrastructure.item.ParseException;
import org.springframework.batch.infrastructure.item.UnexpectedInputException;
import org.springframework.core.io.Resource;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import undecided.generic.calendarReg.internal.batch.dto.HolidayCsvDto;

/** Super CSV を使用してデジタル庁 / 内閣府の祝日 CSV (syukujitsu.csv) を読み込む {@link ItemStreamReader} 実装。 */
public class HolidayCsvItemReader implements ItemStreamReader<HolidayCsvDto> {

  private static final String[] DEFAULT_NAME_MAPPING = new String[] {"holidayDate", "holidayName"};

  private static final String HEADER_HOLIDAY_DATE_JP1 = "国民の祝日・休日月日";
  private static final String HEADER_HOLIDAY_DATE_JP2 = "祝日・休日月日";
  private static final String HEADER_HOLIDAY_DATE_JP3 = "国民の祝日月日";
  private static final String HEADER_HOLIDAY_DATE_JP4 = "祝日月日";
  private static final String HEADER_HOLIDAY_DATE_JP5 = "日付";

  private static final String HEADER_HOLIDAY_NAME_JP1 = "国民の祝日・休日名称";
  private static final String HEADER_HOLIDAY_NAME_JP2 = "祝日・休日名称";
  private static final String HEADER_HOLIDAY_NAME_JP3 = "国民の祝日名称";
  private static final String HEADER_HOLIDAY_NAME_JP4 = "祝日名称";
  private static final String HEADER_HOLIDAY_NAME_JP5 = "祝日名";
  private static final String HEADER_HOLIDAY_NAME_JP6 = "名称";

  private static final String HEADER_REMARKS_JP = "備考";

  @Setter @Getter private Resource resource;
  @Setter @Getter private Charset charset = StandardCharsets.UTF_8;

  private ICsvBeanReader csvBeanReader;
  private String[] nameMapping;

  public HolidayCsvItemReader() {}

  public HolidayCsvItemReader(Resource resource) {
    this.resource = resource;
  }

  public HolidayCsvItemReader(Resource resource, Charset charset) {
    this.resource = resource;
    this.charset = charset != null ? charset : StandardCharsets.UTF_8;
  }

  @Override
  public void open(@NonNull ExecutionContext executionContext) throws ItemStreamException {
    if (resource == null) {
      throw new ItemStreamException("Resource must not be null");
    }
    try {
      BufferedReader reader =
          new BufferedReader(new InputStreamReader(resource.getInputStream(), charset));
      csvBeanReader = new CsvBeanReader(reader, CsvPreference.STANDARD_PREFERENCE);

      String[] headers = csvBeanReader.getHeader(true);
      if (headers == null || headers.length == 0) {
        nameMapping = DEFAULT_NAME_MAPPING;
      } else {
        nameMapping = resolveNameMapping(headers);
      }
    } catch (IOException e) {
      throw new ItemStreamException("Failed to open CSV resource: " + resource.getDescription(), e);
    }
  }

  private String[] resolveNameMapping(String[] headers) {
    String[] mapping = new String[headers.length];
    for (int i = 0; i < headers.length; i++) {
      String h = headers[i] != null ? headers[i].trim() : "";
      if (HEADER_HOLIDAY_DATE_JP1.equals(h)
          || HEADER_HOLIDAY_DATE_JP2.equals(h)
          || HEADER_HOLIDAY_DATE_JP3.equals(h)
          || HEADER_HOLIDAY_DATE_JP4.equals(h)
          || HEADER_HOLIDAY_DATE_JP5.equals(h)
          || "holidayDate".equalsIgnoreCase(h)
          || "holiday_date".equalsIgnoreCase(h)
          || "date".equalsIgnoreCase(h)) {
        mapping[i] = "holidayDate";
      } else if (HEADER_HOLIDAY_NAME_JP1.equals(h)
          || HEADER_HOLIDAY_NAME_JP2.equals(h)
          || HEADER_HOLIDAY_NAME_JP3.equals(h)
          || HEADER_HOLIDAY_NAME_JP4.equals(h)
          || HEADER_HOLIDAY_NAME_JP5.equals(h)
          || HEADER_HOLIDAY_NAME_JP6.equals(h)
          || "holidayName".equalsIgnoreCase(h)
          || "holiday_name".equalsIgnoreCase(h)
          || "name".equalsIgnoreCase(h)) {
        mapping[i] = "holidayName";
      } else if (HEADER_REMARKS_JP.equals(h)
          || "remarks".equalsIgnoreCase(h)
          || "remark".equalsIgnoreCase(h)) {
        mapping[i] = "remarks";
      } else {
        mapping[i] = null;
      }
    }
    return mapping;
  }

  @Override
  public @Nullable HolidayCsvDto read()
      throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    if (csvBeanReader == null) {
      throw new ItemStreamException("Reader is not open. Call open() first.");
    }
    return csvBeanReader.read(HolidayCsvDto.class, nameMapping);
  }

  @Override
  public void update(@NonNull ExecutionContext executionContext) throws ItemStreamException {
    // No-op
  }

  @Override
  public void close() throws ItemStreamException {
    if (csvBeanReader != null) {
      try {
        csvBeanReader.close();
      } catch (IOException e) {
        throw new ItemStreamException("Failed to close CSV reader", e);
      } finally {
        csvBeanReader = null;
      }
    }
  }
}
