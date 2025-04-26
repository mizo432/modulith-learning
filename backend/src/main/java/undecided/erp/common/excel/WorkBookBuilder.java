package undecided.erp.common.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
   * ファイルからExcelテンプレートを使用して新しいWorkBookBuilderを作成します。
   *
   * @param file テンプレートとして使用するExcelファイル
   * @return 新しいWorkBookBuilderインスタンス
   * @throws IOException I/Oエラーが発生した場合
   * @throws IllegalArgumentException ファイルが有効なXLSX拡張子を持っていない場合
   */
  public static WorkBookBuilder create(File file) throws IOException {
    if (!ExcelUtils.hasValidExtension(file)) {
      throw new IllegalArgumentException(
          "File must have " + ExcelFileType.EXTENSION_WITH_DOT + " extension");
    }

    try (FileInputStream is = new FileInputStream(file)) {
      Workbook workbook = new XSSFWorkbook(is);
      return new WorkBookBuilder(workbook);
    }
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
   * ワークブックに新しいシートを作成し、そのシートを操作するためのWorkSheetBuilderを返します。
   *
   * @param sheetName シートの名前
   * @return 新しいWorkSheetBuilderインスタンス
   */
  public WorkSheetBuilder worksheet(String sheetName) {
    WorkSheetBuilder builder = new WorkSheetBuilder(this, sheetName);
    currentSheet = builder.getSheet();
    currentRow = null;
    return builder;
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
   * 既存のシートを操作するためのWorkSheetBuilderを返します。
   *
   * @param sheetIndex シートのインデックス
   * @return 指定されたシートに対するWorkSheetBuilderインスタンス
   */
  public WorkSheetBuilder worksheet(int sheetIndex) {
    Sheet sheet = workbook.getSheetAt(sheetIndex);
    currentSheet = sheet;
    currentRow = null;
    return new WorkSheetBuilder(this, sheet);
  }

  /**
   * 現在のシートに対するWorkSheetBuilderを返します。
   *
   * @return 現在のシートに対するWorkSheetBuilderインスタンス
   * @throws IllegalStateException シートが作成または選択されていない場合
   */
  public WorkSheetBuilder worksheet() {
    if (currentSheet == null) {
      throw new IllegalStateException("No sheet has been created or selected");
    }
    return new WorkSheetBuilder(this, currentSheet);
  }

  /**
   * テンプレートシートのヘッダー行を使用して新しいワークシートを作成します。 作成されたワークシートは一覧形式となり、1行目はテンプレートからコピーされたヘッダー行、
   * 2行目以降にデータを配置することができます。
   *
   * @param sheetName 作成するシートの名前
   * @param templateSheet テンプレートとなるシート（1行目にヘッダー行が必要）
   * @return 新しいWorkSheetBuilderインスタンス
   */
  public WorkSheetBuilder worksheetWithTemplateHeader(String sheetName, Sheet templateSheet) {
    WorkSheetBuilder builder = new WorkSheetBuilder(this, sheetName);
    builder.initHeaderFromTemplate(templateSheet);
    currentSheet = builder.getSheet();
    currentRow = null;
    return builder;
  }

  /**
   * テンプレートシートのヘッダー行と2行目の書式を使用して新しいワークシートを作成します。 作成されたワークシートは一覧形式となり、1行目はテンプレートからコピーされたヘッダー行、
   * 2行目以降にデータを配置する際にテンプレートの2行目の書式が適用されます。 テンプレートの2行目は書式を設定してあり、データ部はその書式をコピーしてデータのみをセルに設定します。
   *
   * @param sheetName 作成するシートの名前
   * @param templateSheet テンプレートとなるシート（1行目にヘッダー行、2行目に書式行が必要）
   * @return 新しいWorkSheetBuilderインスタンス
   */
  public WorkSheetBuilder worksheetWithTemplateHeaderAndFormat(String sheetName,
      Sheet templateSheet) {
    WorkSheetBuilder builder = new WorkSheetBuilder(this, sheetName);
    builder.initHeaderFromTemplate(templateSheet);
    builder.initDataFormatFromTemplate(templateSheet);
    currentSheet = builder.getSheet();
    currentRow = null;
    return builder;
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

  /**
   * 設定されたワークブックを指定されたOutputStreamに書き込みます。
   *
   * @param outputStream ワークブックを書き込むOutputStream
   * @throws IOException I/Oエラーが発生した場合
   */
  public void build(OutputStream outputStream) throws IOException {
    workbook.write(outputStream);
  }

  /**
   * 現在のワークブックを取得します。
   *
   * @return 現在のワークブック
   */
  Workbook getWorkbook() {
    return workbook;
  }

  /**
   * 名前付きスタイルのマップを取得します。
   *
   * @return 名前付きスタイルのマップ
   */
  Map<String, CellStyle> getNamedStyles() {
    return namedStyles;
  }

  /**
   * 名前付きスタイルを追加します。
   *
   * @param styleName スタイルの名前
   * @param cellStyle 追加するセルスタイル
   * @return メソッドチェーン用のこのビルダーインスタンス
   */
  WorkBookBuilder addNamedStyle(String styleName, CellStyle cellStyle) {
    namedStyles.put(styleName, cellStyle);
    return this;
  }
}
