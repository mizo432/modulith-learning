package undecided.generic.sampleBatch.internal.batch.reader;

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
import undecided.generic.sampleBatch.internal.batch.dto.SampleItemCsvDto;

/**
 * Super CSVを使用してサンプルアイテムのCSVを読み込む{@link ItemStreamReader}実装。
 *
 * <p>バッチ基本構成サンプルとして、Readerの標準パターンを示します。
 */
public class SampleItemCsvItemReader implements ItemStreamReader<SampleItemCsvDto> {

  private static final String[] DEFAULT_NAME_MAPPING =
      new String[] {"itemCode", "itemName", "category", "price"};

  @Setter private Resource resource;

  private ICsvBeanReader csvBeanReader;
  private String[] nameMapping;

  public SampleItemCsvItemReader() {}

  public SampleItemCsvItemReader(Resource resource) {
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
      if ("itemCode".equalsIgnoreCase(h) || "item_code".equalsIgnoreCase(h)) {
        mapping[i] = "itemCode";
      } else if ("itemName".equalsIgnoreCase(h) || "item_name".equalsIgnoreCase(h)) {
        mapping[i] = "itemName";
      } else if ("category".equalsIgnoreCase(h)) {
        mapping[i] = "category";
      } else if ("price".equalsIgnoreCase(h)) {
        mapping[i] = "price";
      } else {
        mapping[i] = null;
      }
    }
    return mapping;
  }

  @Override
  public @Nullable SampleItemCsvDto read()
      throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    if (csvBeanReader == null) {
      throw new ItemStreamException("Reader is not open. Call open() first.");
    }
    return csvBeanReader.read(SampleItemCsvDto.class, nameMapping);
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
