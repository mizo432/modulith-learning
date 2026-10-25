package undecided.generic.largeRecordBatch.internal.batch.reader;

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
import undecided.generic.largeRecordBatch.internal.batch.dto.LargeRecordCsvDto;

/**
 * Super CSVを使用して大量レコードのCSVをストリーミング方式で読み込む{@link ItemStreamReader}実装。
 *
 * <p>大量データ処理パターンとして、メモリ効率の高いストリーミング読み込みを示します。
 * 全データをメモリに読み込むことなく、1行ずつ処理します。
 */
public class LargeRecordCsvItemReader implements ItemStreamReader<LargeRecordCsvDto> {

  private static final String[] DEFAULT_NAME_MAPPING =
      new String[] {
        "recordCode", "recordName", "category", "subCategory", "amount", "quantity", "status"
      };

  @Setter private Resource resource;

  private ICsvBeanReader csvBeanReader;
  private String[] nameMapping;

  public LargeRecordCsvItemReader() {}

  public LargeRecordCsvItemReader(Resource resource) {
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
      throw new ItemStreamException(
          "Failed to open CSV resource: " + resource.getDescription(), e);
    }
  }

  private String[] resolveNameMapping(String[] headers) {
    String[] mapping = new String[headers.length];
    for (int i = 0; i < headers.length; i++) {
      String h = headers[i] != null ? headers[i].trim() : "";
      if ("recordCode".equalsIgnoreCase(h) || "record_code".equalsIgnoreCase(h)) {
        mapping[i] = "recordCode";
      } else if ("recordName".equalsIgnoreCase(h) || "record_name".equalsIgnoreCase(h)) {
        mapping[i] = "recordName";
      } else if ("category".equalsIgnoreCase(h)) {
        mapping[i] = "category";
      } else if ("subCategory".equalsIgnoreCase(h) || "sub_category".equalsIgnoreCase(h)) {
        mapping[i] = "subCategory";
      } else if ("amount".equalsIgnoreCase(h)) {
        mapping[i] = "amount";
      } else if ("quantity".equalsIgnoreCase(h)) {
        mapping[i] = "quantity";
      } else if ("status".equalsIgnoreCase(h)) {
        mapping[i] = "status";
      } else {
        mapping[i] = null;
      }
    }
    return mapping;
  }

  @Override
  public @Nullable LargeRecordCsvDto read()
      throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    if (csvBeanReader == null) {
      throw new ItemStreamException("Reader is not open. Call open() first.");
    }
    return csvBeanReader.read(LargeRecordCsvDto.class, nameMapping);
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
