package undecided.generic.bankReg.internal.batch.reader;

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
import undecided.generic.bankReg.internal.batch.dto.BranchCsvDto;

/** Super CSV を使用して BankcodeJP Master Export の branches.csv を読み込む {@link ItemStreamReader} 実装。 */
public class BranchCsvItemReader implements ItemStreamReader<BranchCsvDto> {

  private static final String[] DEFAULT_NAME_MAPPING =
      new String[] {
        "bankCode", "branchCode", "branchName", "branchHalfKana", "branchFullKana", "branchHiragana"
      };

  @Setter private Resource resource;

  private ICsvBeanReader csvBeanReader;
  private String[] nameMapping;

  public BranchCsvItemReader() {}

  public BranchCsvItemReader(Resource resource) {
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
      String h = headers[i] != null ? headers[i].trim().toLowerCase() : "";
      if (h.contains("bank") && h.contains("code")) {
        mapping[i] = "bankCode";
      } else if (h.contains("branch") && h.contains("code")) {
        mapping[i] = "branchCode";
      } else if (h.contains("branch")
          && h.contains("name")
          && !h.contains("kana")
          && !h.contains("hira")) {
        mapping[i] = "branchName";
      } else if (h.contains("branch") && h.contains("half") && h.contains("kana")) {
        mapping[i] = "branchHalfKana";
      } else if (h.contains("branch") && h.contains("full") && h.contains("kana")) {
        mapping[i] = "branchFullKana";
      } else if (h.contains("branch") && h.contains("hira")) {
        mapping[i] = "branchHiragana";
      } else {
        mapping[i] = null;
      }
    }
    return mapping;
  }

  @Override
  public @Nullable BranchCsvDto read()
      throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    if (csvBeanReader == null) {
      throw new ItemStreamException("Reader is not open. Call open() first.");
    }
    return csvBeanReader.read(BranchCsvDto.class, nameMapping);
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
