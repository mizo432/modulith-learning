package undecided.erp.common.excel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * WorkBookBuilder は、Excel ワークブックの構築および管理を簡易化するビルダークラスです。 シート、行、セルの構成やスタイルの設定をチェーンメソッドの形式で提供します。
 * 使用者は POI ライブラリによる手作業を減らし、直感的に Excel ファイルを作成・操作できます。
 */
public final class WorkBookBuilder {

  private final Workbook workbook;
  private final Map<String, CellStyle> namedStyles = new HashMap<>();
  private Sheet currentSheet;
  private Row currentRow;

  private WorkBookBuilder(Workbook workbook) {
    this.workbook = workbook;
  }

  /**
   * 新しいワークブックで新しいWorkBookBuilderを作成します。
   *
   * @return 新しいWorkBookBuilderインスタンス
   */
  public static WorkBookBuilder create() {
    return new WorkBookBuilder(ExcelUtils.createWorkbook());
  }

  /**
   * 提供されたワークブックで新しいWorkBookBuilderを作成します。
   *
   * @param workbook 使用するワークブック
   * @return 新しいWorkBookBuilderインスタンス
   */
  public static WorkBookBuilder withWorkbook(Workbook workbook) {
    return new WorkBookBuilder(workbook);
  }

  /**
   * 既存のExcelファイルをテンプレートとして新しいWorkBookBuilderを作成します。
   *
   * @param filePath テンプレートとして使用するExcelファイルへのパス
   * @return 新しいWorkBookBuilderインスタンス
   * @throws IOException I/Oエラーが発生した場合
   * @throws IllegalArgumentException ファイルが有効なXLSX拡張子を持っていない場合
   */
  public static WorkBookBuilder fromTemplate(String filePath) throws IOException {
    Workbook workbook = ExcelUtils.loadWorkbook(filePath);
    return new WorkBookBuilder(workbook);
  }

  /**
   * JARファイル内のリソースからExcelテンプレートを使用して新しいWorkBookBuilderを作成します。
   *
   * @param resourcePath JAR内のリソースへのパス
   * @return 新しいWorkBookBuilderインスタンス
   * @throws IOException I/Oエラーが発生した場合
   * @throws IllegalArgumentException リソースが有効なXLSX拡張子を持っていないか、見つからない場合
   */
  public static WorkBookBuilder fromResource(String resourcePath) throws IOException {
    Workbook workbook = ExcelUtils.loadWorkbookFromResource(resourcePath,
        WorkBookBuilder.class.getClassLoader());
    return new WorkBookBuilder(workbook);
  }

  /**
   * JARファイル内のリソースからExcelテンプレートを使用して新しいWorkBookBuilderを作成します。
   *
   * @param resourcePath JAR内のリソースへのパス
   * @param classLoader リソースの読み込みに使用するClassLoader
   * @return 新しいWorkBookBuilderインスタンス
   * @throws IOException I/Oエラーが発生した場合
   * @throws IllegalArgumentException リソースが有効なXLSX拡張子を持っていないか、見つからない場合
   */
  public static WorkBookBuilder fromResource(String resourcePath, ClassLoader classLoader)
      throws IOException {
    Workbook workbook = ExcelUtils.loadWorkbookFromResource(resourcePath, classLoader);
    return new WorkBookBuilder(workbook);
  }

  /**
   * ワークブックに新しいシートを作成し、それを現在のシートとして設定します。
   *
   * @param sheetName シートの名前
   * @return メソッドチェーン用のこのビルダーインスタンス
   */
  public WorkBookBuilder sheet(String sheetName) {
    currentSheet = workbook.createSheet(sheetName);
    currentRow = null;
    return this;
  }

  /**
   * 既存のシートを現在のシートとして設定します。
   *
   * @param sheetIndex シートのインデックス
   * @return メソッドチェーン用のこのビルダーインスタンス
   */
  public WorkBookBuilder sheet(int sheetIndex) {
    currentSheet = workbook.getSheetAt(sheetIndex);
    currentRow = null;
    return this;
  }

  /**
   * 現在のシートに新しい行を作成し、それを現在の行として設定します。
   *
   * @param rowIndex 行のインデックス
   * @return メソッドチェーン用のこのビルダーインスタンス
   * @throws IllegalStateException シートが作成または選択されていない場合
   */
  public WorkBookBuilder row(int rowIndex) {
    if (currentSheet == null) {
      throw new IllegalStateException("No sheet has been created or selected");
    }
    currentRow = currentSheet.createRow(rowIndex);
    return this;
  }

  /**
   * 現在の行に文字列値を持つセルを作成します。
   *
   * @param columnIndex セルのインデックス
   * @param value 設定する文字列値
   * @return メソッドチェーン用のこのビルダーインスタンス
   * @throws IllegalStateException 行が作成または選択されていない場合
   */
  public WorkBookBuilder cell(int columnIndex, String value) {
    if (currentRow == null) {
      throw new IllegalStateException("No row has been created or selected");
    }
    Cell cell = currentRow.createCell(columnIndex);
    cell.setCellValue(value);
    return this;
  }

  /**
   * 現在の行に数値を持つセルを作成します。
   *
   * @param columnIndex セルのインデックス
   * @param value 設定する数値
   * @return メソッドチェーン用のこのビルダーインスタンス
   * @throws IllegalStateException 行が作成または選択されていない場合
   */
  public WorkBookBuilder cell(int columnIndex, double value) {
    if (currentRow == null) {
      throw new IllegalStateException("No row has been created or selected");
    }
    Cell cell = currentRow.createCell(columnIndex);
    cell.setCellValue(value);
    return this;
  }

  /**
   * 現在の行に真偽値を持つセルを作成します。
   *
   * @param columnIndex セルのインデックス
   * @param value 設定する真偽値
   * @return メソッドチェーン用のこのビルダーインスタンス
   * @throws IllegalStateException 行が作成または選択されていない場合
   */
  public WorkBookBuilder cell(int columnIndex, boolean value) {
    if (currentRow == null) {
      throw new IllegalStateException("No row has been created or selected");
    }
    Cell cell = currentRow.createCell(columnIndex);
    cell.setCellValue(value);
    return this;
  }

  /**
   * 現在の行に数式を持つセルを作成します。
   *
   * @param columnIndex セルのインデックス
   * @param formula 設定する数式
   * @return メソッドチェーン用のこのビルダーインスタンス
   * @throws IllegalStateException 行が作成または選択されていない場合
   */
  public WorkBookBuilder cellFormula(int columnIndex, String formula) {
    if (currentRow == null) {
      throw new IllegalStateException("No row has been created or selected");
    }
    Cell cell = currentRow.createCell(columnIndex);
    cell.setCellFormula(formula);
    return this;
  }

  /**
   * 現在の行に文字列値を持つセルを作成し、名前付きスタイルを適用します。
   *
   * @param columnIndex セルのインデックス
   * @param value 設定する文字列値
   * @param styleName 適用するスタイルの名前
   * @return メソッドチェーン用のこのビルダーインスタンス
   * @throws IllegalStateException 行が作成または選択されていない場合、またはスタイルが存在しない場合
   */
  public WorkBookBuilder styledCell(int columnIndex, String value, String styleName) {
    if (currentRow == null) {
      throw new IllegalStateException("No row has been created or selected");
    }
    if (!namedStyles.containsKey(styleName)) {
      throw new IllegalStateException("Style '" + styleName + "' does not exist");
    }
    Cell cell = currentRow.createCell(columnIndex);
    cell.setCellValue(value);
    cell.setCellStyle(namedStyles.get(styleName));
    return this;
  }

  /**
   * 再利用可能な名前付きセルスタイルを作成します。
   *
   * @param styleName スタイルの名前
   * @param styleConfigurer スタイルを設定するコンシューマ
   * @return メソッドチェーン用のこのビルダーインスタンス
   */
  public WorkBookBuilder createStyle(String styleName,
      java.util.function.Consumer<CellStyle> styleConfigurer) {
    CellStyle style = workbook.createCellStyle();
    styleConfigurer.accept(style);
    namedStyles.put(styleName, style);
    return this;
  }

  /**
   * 太字フォントスタイルを作成します。
   *
   * @param styleName スタイルの名前
   * @return メソッドチェーン用のこのビルダーインスタンス
   */
  public WorkBookBuilder createBoldStyle(String styleName) {
    CellStyle style = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setBold(true);
    style.setFont(font);
    namedStyles.put(styleName, style);
    return this;
  }

  /**
   * 現在のシートの特定の列の幅を設定します。
   *
   * @param columnIndex 列のインデックス
   * @param width 文字単位の幅
   * @return メソッドチェーン用のこのビルダーインスタンス
   * @throws IllegalStateException シートが作成または選択されていない場合
   */
  public WorkBookBuilder columnWidth(int columnIndex, int width) {
    if (currentSheet == null) {
      throw new IllegalStateException("No sheet has been created or selected");
    }
    currentSheet.setColumnWidth(columnIndex, width * 256);
    return this;
  }

  /**
   * 現在のシートの列を自動サイズ調整します。
   *
   * @param columnIndex 列のインデックス
   * @return メソッドチェーン用のこのビルダーインスタンス
   * @throws IllegalStateException シートが作成または選択されていない場合
   */
  public WorkBookBuilder autoSizeColumn(int columnIndex) {
    if (currentSheet == null) {
      throw new IllegalStateException("No sheet has been created or selected");
    }
    currentSheet.autoSizeColumn(columnIndex);
    return this;
  }

  /**
   * 設定されたワークブックを構築して返します。
   *
   * @return 設定されたワークブック
   */
  public Workbook build() {
    return workbook;
  }
}
