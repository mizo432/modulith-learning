package undecided.generic.addressReg.internal.batch.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
import undecided.generic.addressReg.internal.batch.dto.PrefectureCsvDto;

/** Super CSV を使用してデジタル庁の都道府県マスター CSV を読み込む {@link ItemStreamReader} 実装。 */
public class PrefectureCsvItemReader implements ItemStreamReader<PrefectureCsvDto> {

  private static final String[] DEFAULT_NAME_MAPPING =
      new String[] {
        "lgCode", "prefName", "prefKana", "prefRoma", "effectiveDate", "abolitionDate", "remarks"
      };

  private static final String HEADER_LG_CODE_JP = "全国地方公共団体コード";
  private static final String HEADER_PREF_NAME_JP = "都道府県名";
  private static final String HEADER_PREF_KANA_JP = "都道府県名_カナ";
  private static final String HEADER_PREF_ROMA_JP = "都道府県名_英字";
  private static final String HEADER_EFFECTIVE_DATE_JP = "効力発生日";
  private static final String HEADER_ABOLITION_DATE_JP = "廃止日";
  private static final String HEADER_REMARKS_JP = "備考";

  @Setter private Resource resource;

  private ICsvBeanReader csvBeanReader;
  private String[] nameMapping;

  public PrefectureCsvItemReader() {}

  public PrefectureCsvItemReader(Resource resource) {
    this.resource = resource;
  }

  @Override
  public void open(@NonNull ExecutionContext executionContext) throws ItemStreamException {
    if (resource == null) {
      throw new ItemStreamException("Resource must not be null");
    }
    try {
      BufferedReader reader =
          new BufferedReader(
              new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
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
      if (HEADER_LG_CODE_JP.equals(h)
          || "lgCode".equalsIgnoreCase(h)
          || "lg_code".equalsIgnoreCase(h)) {
        mapping[i] = "lgCode";
      } else if (HEADER_PREF_NAME_JP.equals(h)
          || "prefName".equalsIgnoreCase(h)
          || "pref_name".equalsIgnoreCase(h)) {
        mapping[i] = "prefName";
      } else if (HEADER_PREF_KANA_JP.equals(h)
          || "prefKana".equalsIgnoreCase(h)
          || "pref_kana".equalsIgnoreCase(h)) {
        mapping[i] = "prefKana";
      } else if (HEADER_PREF_ROMA_JP.equals(h)
          || "prefRoma".equalsIgnoreCase(h)
          || "pref_roma".equalsIgnoreCase(h)) {
        mapping[i] = "prefRoma";
      } else if (HEADER_EFFECTIVE_DATE_JP.equals(h)
          || "effectiveDate".equalsIgnoreCase(h)
          || "effective_date".equalsIgnoreCase(h)) {
        mapping[i] = "effectiveDate";
      } else if (HEADER_ABOLITION_DATE_JP.equals(h)
          || "abolitionDate".equalsIgnoreCase(h)
          || "abolition_data".equalsIgnoreCase(h)
          || "abolition_date".equalsIgnoreCase(h)) {
        mapping[i] = "abolitionDate";
      } else if (HEADER_REMARKS_JP.equals(h) || "remarks".equalsIgnoreCase(h)) {
        mapping[i] = "remarks";
      } else {
        mapping[i] = null;
      }
    }
    return mapping;
  }

  @Override
  public @Nullable PrefectureCsvDto read()
      throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    if (csvBeanReader == null) {
      throw new ItemStreamException("Reader is not open. Call open() first.");
    }
    return csvBeanReader.read(PrefectureCsvDto.class, nameMapping);
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
