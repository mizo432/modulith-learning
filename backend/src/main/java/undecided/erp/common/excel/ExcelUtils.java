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
 * Excel操作のためのユーティリティクラス。このクラスはXLSX形式のExcelファイルの読み書きのためのメソッドを提供します。
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExcelUtils {

  /**
   * XLSX形式の新しいExcelワークブックを作成します。
   *
   * @return 新しいワークブックインスタンス
   */
  public static Workbook createWorkbook() {
    return new XSSFWorkbook();
  }

  /**
   * ファイルから既存のExcelワークブックを読み込みます。
   *
   * @param filePath Excelファイルへのパス
   * @return 読み込まれたワークブック
   * @throws IOException I/Oエラーが発生した場合
   * @throws IllegalArgumentException ファイルが有効なXLSX拡張子を持っていない場合
   */
  public static Workbook loadWorkbook(String filePath) throws IOException {
    File file = new File(filePath);
    if (!hasValidExtension(file)) {
      throw new IllegalArgumentException(
          "File must have " + ExcelFileType.EXTENSION_WITH_DOT + " extension");
    }

    try (InputStream is = new FileInputStream(file)) {
      return new XSSFWorkbook(is);
    }
  }

  /**
   * JARファイル内のリソースから既存のExcelワークブックを読み込みます。
   *
   * @param resourcePath JAR内のリソースへのパス
   * @param classLoader リソースの読み込みに使用するClassLoader
   * @return 読み込まれたワークブック
   * @throws IOException I/Oエラーが発生した場合
   * @throws IllegalArgumentException リソースが有効なXLSX拡張子を持っていないか、見つからない場合
   */
  public static Workbook loadWorkbookFromResource(String resourcePath, ClassLoader classLoader)
      throws IOException {
    if (!resourcePath.toLowerCase().endsWith(ExcelFileType.EXTENSION_WITH_DOT)) {
      throw new IllegalArgumentException(
          "Resource must have " + ExcelFileType.EXTENSION_WITH_DOT + " extension");
    }

    try (InputStream is = classLoader.getResourceAsStream(resourcePath)) {
      if (is == null) {
        throw new IllegalArgumentException("Resource not found: " + resourcePath);
      }
      return new XSSFWorkbook(is);
    }
  }

  /**
   * ファイルが有効なExcel XLSX拡張子を持っているかチェックします。
   *
   * @param file チェックするファイル
   * @return ファイルがXLSX拡張子を持っている場合はtrue、そうでない場合はfalse
   */
  public static boolean hasValidExtension(File file) {
    return file != null && file.getName().toLowerCase().endsWith(ExcelFileType.EXTENSION_WITH_DOT);
  }

  /**
   * ファイルが正しいXLSX拡張子を持っていることを確認し、必要に応じて追加します。
   *
   * @param filePath チェックして修正する可能性のあるファイルパス
   * @return 正しい拡張子を持つファイルパス
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
   * Excelファイルを読み込み、提供されたコンシューマで各行を処理します。
   *
   * @param filePath Excelファイルへのパス
   * @param rowConsumer 各行を処理するコンシューマ
   * @throws IOException I/Oエラーが発生した場合
   * @throws IllegalArgumentException ファイルが有効なXLSX拡張子を持っていない場合
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
   * データをExcelファイルに書き込みます。
   *
   * @param filePath Excelファイルへのパス
   * @param workbookConsumer ワークブックにデータを設定するコンシューマ
   * @throws IOException I/Oエラーが発生した場合
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
   * 一時的なExcelファイルを作成します。
   *
   * @param prefix 一時ファイルのプレフィックス
   * @param workbookConsumer ワークブックにデータを設定するコンシューマ
   * @return 作成された一時ファイルへのパス
   * @throws IOException I/Oエラーが発生した場合
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
   * セルから文字列値を取得し、異なるセルタイプを処理します。
   *
   * @param cell 値を取得するセル
   * @return セルの文字列値、セルがnullの場合は空文字列
   */
  public static String getCellStringValue(Cell cell) {
    if (cell == null) {
      return "";
    }

    return switch (cell.getCellType()) {
      case STRING -> {
        // For string cells, return the string value directly
        yield cell.getStringCellValue();
      }
      case NUMERIC -> {
        // For numeric cells, convert the numeric value to a string
        yield String.valueOf(cell.getNumericCellValue());
      }
      case BOOLEAN -> {
        // For boolean cells, convert the boolean value to a string
        yield String.valueOf(cell.getBooleanCellValue());
      }
      case FORMULA -> {
        // For formula cells, return the formula string
        yield cell.getCellFormula();
      }
      default -> {
        // For all other cell types (blank, error, etc.), return an empty string
        yield "";
      }
    };
  }

  /**
   * Excelファイルからすべてのデータを文字列配列のリストとして抽出します。
   *
   * @param filePath Excelファイルへのパス
   * @return 文字列配列のリスト、各配列はデータの1行を表す
   * @throws IOException I/Oエラーが発生した場合
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
