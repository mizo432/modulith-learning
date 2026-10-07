package undecided.generic.bankReg.internal.batch.reader;

import static undecided.supporting.precondition.ObjectPrecondition.checkNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
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
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import undecided.generic.bankReg.internal.batch.dto.BankCsvDto;

/**
 * ダウンロードした Master Export ZIP 内の banks.csv を Super CSV で読み込む {@link ItemStreamReader} 実装。
 *
 * <p>ZIP ファイルのパスは {@link #setZipFile(Path)} で設定します。
 */
public class BankCsvItemReader implements ItemStreamReader<BankCsvDto> {

  private static final String CSV_ENTRY_NAME = "banks.csv";
  private static final String[] NAME_MAPPING =
      new String[] {
        "bankCode",
        "bankName",
        "bankHalfKana",
        "bankFullKana",
        "bankFullHira",
        "businessTypeCode",
        "businessType"
      };

  @Setter @Getter private java.nio.file.Path zipFile;

  private ZipFile zipFileHandle;
  private ICsvBeanReader csvBeanReader;

  public BankCsvItemReader() {}

  public BankCsvItemReader(java.nio.file.Path zipFile) {
    this.zipFile = zipFile;
  }

  @Override
  public void open(@NonNull ExecutionContext executionContext) throws ItemStreamException {
    checkNotNull(zipFile, () -> new ItemStreamException("zipFile must not be null"));
    try {
      zipFileHandle = new ZipFile(zipFile.toFile());
      ZipEntry entry = zipFileHandle.getEntry(CSV_ENTRY_NAME);
      if (entry == null) {
        throw new ItemStreamException(
            "ZIP file does not contain '" + CSV_ENTRY_NAME + "': " + zipFile);
      }
      BufferedReader reader =
          new BufferedReader(
              new InputStreamReader(zipFileHandle.getInputStream(entry), StandardCharsets.UTF_8));
      csvBeanReader = new CsvBeanReader(reader, CsvPreference.STANDARD_PREFERENCE);
      // ヘッダー行を読み捨てる
      csvBeanReader.getHeader(true);
    } catch (IOException e) {
      throw new ItemStreamException(
          "Failed to open " + CSV_ENTRY_NAME + " from ZIP: " + zipFile, e);
    }
  }

  @Override
  public @Nullable BankCsvDto read()
      throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    if (csvBeanReader == null) {
      throw new ItemStreamException("Reader is not open. Call open() first.");
    }
    return csvBeanReader.read(BankCsvDto.class, NAME_MAPPING);
  }

  @Override
  public void update(@NonNull ExecutionContext executionContext) throws ItemStreamException {
    // No-op
  }

  @Override
  public void close() throws ItemStreamException {
    IOException firstException = null;
    if (csvBeanReader != null) {
      try {
        csvBeanReader.close();
      } catch (IOException e) {
        firstException = e;
      } finally {
        csvBeanReader = null;
      }
    }
    if (zipFileHandle != null) {
      try {
        zipFileHandle.close();
      } catch (IOException e) {
        if (firstException == null) {
          firstException = e;
        }
      } finally {
        zipFileHandle = null;
      }
    }
    if (firstException != null) {
      throw new ItemStreamException("Failed to close reader", firstException);
    }
  }
}
