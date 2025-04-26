package undecided.erp.common.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Utility class for Excel operations. This class provides methods for reading and writing Excel
 * files in XLSX format.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExcelUtils {

  /**
   * Creates a new Excel workbook in XLSX format.
   *
   * @return a new workbook instance
   */
  public static Workbook createWorkbook() {
    return new XSSFWorkbook();
  }

  /**
   * Checks if the file has a valid Excel XLSX extension.
   *
   * @param file the file to check
   * @return true if the file has an XLSX extension, false otherwise
   */
  public static boolean hasValidExtension(File file) {
    return file != null && file.getName().toLowerCase().endsWith(ExcelFileType.EXTENSION_WITH_DOT);
  }

  /**
   * Ensures the file has the correct XLSX extension, adding it if necessary.
   *
   * @param filePath the file path to check and possibly modify
   * @return the file path with the correct extension
   */
  public static String ensureCorrectExtension(String filePath) {
    if (filePath == null || filePath.isEmpty()) {
      throw new IllegalArgumentException("File path cannot be null or empty");
    }

    if (!filePath.toLowerCase().endsWith(ExcelFileType.EXTENSION_WITH_DOT)) {
      // Remove any existing extension
      int dotIndex = filePath.lastIndexOf('.');
      if (dotIndex > 0) {
        filePath = filePath.substring(0, dotIndex);
      }
      // Add the correct extension
      filePath += ExcelFileType.EXTENSION_WITH_DOT;
    }

    return filePath;
  }

  /**
   * Reads an Excel file and processes each row with the provided consumer.
   *
   * @param filePath the path to the Excel file
   * @param rowConsumer the consumer to process each row
   * @throws IOException if an I/O error occurs
   * @throws IllegalArgumentException if the file does not have a valid XLSX extension
   */
  public static void readExcel(String filePath, Consumer<Row> rowConsumer) throws IOException {
    File file = new File(filePath);
    if (!hasValidExtension(file)) {
      throw new IllegalArgumentException(
          "File must have " + ExcelFileType.EXTENSION_WITH_DOT + " extension");
    }

    try (InputStream is = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(is)) {

      Sheet sheet = workbook.getSheetAt(0);
      for (Row row : sheet) {
        rowConsumer.accept(row);
      }
    }
  }

  /**
   * Writes data to an Excel file.
   *
   * @param filePath the path to the Excel file
   * @param workbookConsumer the consumer to populate the workbook
   * @throws IOException if an I/O error occurs
   */
  public static void writeExcel(String filePath, Consumer<Workbook> workbookConsumer)
      throws IOException {
    String correctedPath = ensureCorrectExtension(filePath);
    File file = new File(correctedPath);

    try (Workbook workbook = createWorkbook()) {
      workbookConsumer.accept(workbook);

      try (OutputStream os = new FileOutputStream(file)) {
        workbook.write(os);
      }
    }
  }

  /**
   * Creates a temporary Excel file.
   *
   * @param prefix the prefix for the temporary file
   * @param workbookConsumer the consumer to populate the workbook
   * @return the path to the created temporary file
   * @throws IOException if an I/O error occurs
   */
  public static Path createTempExcelFile(String prefix, Consumer<Workbook> workbookConsumer)
      throws IOException {
    Path tempFile = Files.createTempFile(prefix, ExcelFileType.EXTENSION_WITH_DOT);

    try (Workbook workbook = createWorkbook();
        OutputStream os = Files.newOutputStream(tempFile)) {

      workbookConsumer.accept(workbook);
      workbook.write(os);
    }

    return tempFile;
  }

  /**
   * Gets the string value from a cell, handling different cell types.
   *
   * @param cell the cell to get the value from
   * @return the string value of the cell, or empty string if the cell is null
   */
  public static String getCellStringValue(Cell cell) {
    if (cell == null) {
      return "";
    }

    switch (cell.getCellType()) {
      case STRING:
        return cell.getStringCellValue();
      case NUMERIC:
        return String.valueOf(cell.getNumericCellValue());
      case BOOLEAN:
        return String.valueOf(cell.getBooleanCellValue());
      case FORMULA:
        return cell.getCellFormula();
      default:
        return "";
    }
  }

  /**
   * Extracts all data from an Excel file as a list of string arrays.
   *
   * @param filePath the path to the Excel file
   * @return a list of string arrays, each representing a row of data
   * @throws IOException if an I/O error occurs
   */
  public static List<String[]> extractDataFromExcel(String filePath) throws IOException {
    List<String[]> data = new ArrayList<>();

    readExcel(filePath, row -> {
      int lastCellNum = row.getLastCellNum();
      String[] rowData = new String[lastCellNum];

      for (int i = 0; i < lastCellNum; i++) {
        Cell cell = row.getCell(i);
        rowData[i] = getCellStringValue(cell);
      }

      data.add(rowData);
    });

    return data;
  }
}
