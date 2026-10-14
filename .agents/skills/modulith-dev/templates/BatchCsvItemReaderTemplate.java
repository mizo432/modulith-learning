package undecided.generic.{module}.internal.batch.reader;

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
import undecided.generic.{module}.internal.batch.dto.{Entity}CsvDto;

/** Super CSV を使用して {Entity} マスター CSV を読み込む {@link ItemStreamReader} 実装。 */
public class {Entity}CsvItemReader implements ItemStreamReader<{Entity}CsvDto> {

  // TODO: CSV のヘッダー名と DTO フィールドの対応関係を定義
  private static final String[] DEFAULT_NAME_MAPPING =
      new String[] {
        // 例: "id", "name", "code"
      };

  @Setter private Resource resource;

  private ICsvBeanReader csvBeanReader;
  private String[] nameMapping;

  public {Entity}CsvItemReader() {}

  public {Entity}CsvItemReader(Resource resource) {
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
        // TODO: ヘッダー名からフィールドマッピングを解決するロジックを実装
        nameMapping = resolveNameMapping(headers);
      }
    } catch (IOException e) {
      throw new ItemStreamException("Failed to open CSV resource: " + resource.getDescription(), e);
    }
  }

  private String[] resolveNameMapping(String[] headers) {
    // TODO: CSV ヘッダー名と DTO フィールド名のマッピングを定義
    String[] mapping = new String[headers.length];
    for (int i = 0; i < headers.length; i++) {
      String h = headers[i] != null ? headers[i].trim() : "";
      // TODO: ヘッダー名に応じてマッピングを設定
      mapping[i] = null;
    }
    return mapping;
  }

  @Override
  public @Nullable {Entity}CsvDto read()
      throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    if (csvBeanReader == null) {
      throw new ItemStreamException("Reader is not open. Call open() first.");
    }
    return csvBeanReader.read({Entity}CsvDto.class, nameMapping);
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
