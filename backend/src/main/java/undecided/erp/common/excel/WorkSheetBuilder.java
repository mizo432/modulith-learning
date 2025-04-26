package undecided.erp.common.excel;

import java.util.Map;
import java.util.function.BiConsumer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * WorkSheetBuilder は、Excel ワークシートの構築および管理を簡易化するビルダークラスです。 行、セルの構成やスタイルの設定をチェーンメソッドの形式で提供します。
 * WorkBookBuilder と連携して使用することで、より直感的なワークシート操作が可能になります。
 * <p>
 * 一覧形式のワークシートでは、1行目は必ず各カラムの名称とし、2行目からデータを出力します。 各カラムの名称はテンプレートのシートに事前に記述しておきます。
 */
public final class WorkSheetBuilder {

  private final WorkBookBuilder parent;
  private final Sheet sheet;
  private final Map<String, CellStyle> namedStyles;
  private Row currentRow;
  private boolean hasHeaderRow = false;

  /**
   * WorkSheetBuilder のコンストラクタ。新しいシートを作成します。
   *
   * @param parent 親となる WorkBookBuilder
   * @param sheetName 作成するシートの名前
   */
  WorkSheetBuilder(WorkBookBuilder parent, String sheetName) {
    this.parent = parent;
    this.sheet = parent.getWorkbook().createSheet(sheetName);
    this.namedStyles = parent.getNamedStyles();
  }

  /**
   * WorkSheetBuilder のコンストラクタ。既存のシートを使用します。
   *
   * @param parent 親となる WorkBookBuilder
   * @param sheet 操作対象の既存シート
   */
  WorkSheetBuilder(WorkBookBuilder parent, Sheet sheet) {
    this.parent = parent;
    this.sheet = sheet;
    this.namedStyles = parent.getNamedStyles();

    // 既存のシートの場合、1行目（ヘッダー行）が存在するかチェック
    if (sheet.getRow(0) != null) {
      hasHeaderRow = true;
    }
  }

  /**
   * 現在のシートに新しい行を作成し、それを現在の行として設定します。 一覧形式のワークシートでは、1行目は必ずヘッダー行として扱われます。
   *
   * @param rowIndex 行のインデックス
   * @return メソッドチェーン用のこのビルダーインスタンス
   */
  public WorkSheetBuilder row(int rowIndex) {
    if (rowIndex == 0) {
      hasHeaderRow = true;
    }
    currentRow = sheet.createRow(rowIndex);
    return this;
  }

  /**
   * データ行を作成します。一覧形式のワークシートでは、データは2行目から始まります。 ヘッダー行が存在しない場合は、先にヘッダー行を作成する必要があります。
   *
   * @param dataRowIndex データ行のインデックス（0から始まる）
   * @return メソッドチェーン用のこのビルダーインスタンス
   * @throws IllegalStateException ヘッダー行が存在しない場合
   */
  public WorkSheetBuilder dataRow(int dataRowIndex) {
    if (!hasHeaderRow) {
      throw new IllegalStateException("Header row must be created first");
    }
    // データ行は2行目（インデックス1）から始まるため、dataRowIndex + 1 を使用
    currentRow = sheet.createRow(dataRowIndex + 1);
    return this;
  }

  /**
   * データ行を作成し、そのデータ行を操作するためのDataRowBuilderを返します。 一覧形式のワークシートでは、データは2行目から始まります。
   * ヘッダー行が存在しない場合は、先にヘッダー行を作成する必要があります。
   *
   * @param dataRowIndex データ行のインデックス（0から始まる）
   * @return 新しいDataRowBuilderインスタンス
   * @throws IllegalStateException ヘッダー行が存在しない場合
   */
  public DataRowBuilder dataRowBuilder(int dataRowIndex) {
    if (!hasHeaderRow) {
      throw new IllegalStateException("Header row must be created first");
    }
    // データ行は2行目（インデックス1）から始まるため、dataRowIndex + 1 を使用
    Row row = sheet.createRow(dataRowIndex + 1);
    currentRow = row;
    return new DataRowBuilder(this, row, namedStyles);
  }

  /**
   * テンプレートからヘッダー行を初期化します。 テンプレートシートの1行目をヘッダー行として使用します。
   *
   * @param templateSheet テンプレートとなるシート
   * @return メソッドチェーン用のこのビルダーインスタンス
   */
  public WorkSheetBuilder initHeaderFromTemplate(Sheet templateSheet) {
    Row templateHeaderRow = templateSheet.getRow(0);
    if (templateHeaderRow == null) {
      throw new IllegalArgumentException("Template sheet does not have a header row");
    }

    // 現在のシートにヘッダー行を作成
    Row headerRow = sheet.createRow(0);

    // テンプレートのヘッダー行からセルをコピー
    for (int i = 0; i <= templateHeaderRow.getLastCellNum(); i++) {
      Cell templateCell = templateHeaderRow.getCell(i);
      if (templateCell != null) {
        Cell headerCell = headerRow.createCell(i);
        headerCell.setCellValue(templateCell.getStringCellValue());
        // スタイルもコピーする場合
        if (templateCell.getCellStyle() != null) {
          headerCell.setCellStyle(templateCell.getCellStyle());
        }
      }
    }

    hasHeaderRow = true;
    return this;
  }

  /**
   * テンプレートの2行目の書式を取得します。 テンプレートシートの2行目は書式を設定してあり、データ部はその書式をコピーしてデータのみをセルに設定します。
   *
   * @param templateSheet テンプレートとなるシート
   * @return メソッドチェーン用のこのビルダーインスタンス
   */
  public WorkSheetBuilder initDataFormatFromTemplate(Sheet templateSheet) {
    Row templateFormatRow = templateSheet.getRow(1);
    if (templateFormatRow == null) {
      throw new IllegalArgumentException("Template sheet does not have a format row (second row)");
    }

    // テンプレートの2行目の書式を保存
    for (int i = 0; i <= templateFormatRow.getLastCellNum(); i++) {
      Cell templateCell = templateFormatRow.getCell(i);
      if (templateCell != null && templateCell.getCellStyle() != null) {
        // 列インデックスをキーとして、その列のセルスタイルを保存
        String styleName = "_template_col_" + i + "_style";
        CellStyle cellStyle = templateCell.getCellStyle();
        parent.addNamedStyle(styleName, cellStyle);
      }
    }

    return this;
  }

  /**
   * テンプレートの2行目の書式を使用してデータセルを作成します。
   *
   * @param columnIndex セルのインデックス
   * @param value 設定する文字列値
   * @return メソッドチェーン用のこのビルダーインスタンス
   * @throws IllegalStateException 行が作成または選択されていない場合
   */
  public WorkSheetBuilder dataCell(int columnIndex, String value) {
    if (currentRow == null) {
      throw new IllegalStateException("No row has been created or selected");
    }

    Cell cell = currentRow.createCell(columnIndex);
    cell.setCellValue(value);

    // テンプレートから取得した書式を適用
    String styleName = "_template_col_" + columnIndex + "_style";
    if (namedStyles.containsKey(styleName)) {
      cell.setCellStyle(namedStyles.get(styleName));
    }

    return this;
  }

  /**
   * テンプレートの2行目の書式を使用して数値データセルを作成します。
   *
   * @param columnIndex セルのインデックス
   * @param value 設定する数値
   * @return メソッドチェーン用のこのビルダーインスタンス
   * @throws IllegalStateException 行が作成または選択されていない場合
   */
  public WorkSheetBuilder dataCell(int columnIndex, double value) {
    if (currentRow == null) {
      throw new IllegalStateException("No row has been created or selected");
    }

    Cell cell = currentRow.createCell(columnIndex);
    cell.setCellValue(value);

    // テンプレートから取得した書式を適用
    String styleName = "_template_col_" + columnIndex + "_style";
    if (namedStyles.containsKey(styleName)) {
      cell.setCellStyle(namedStyles.get(styleName));
    }

    return this;
  }

  /**
   * テンプレートの2行目の書式を使用して真偽値データセルを作成します。
   *
   * @param columnIndex セルのインデックス
   * @param value 設定する真偽値
   * @return メソッドチェーン用のこのビルダーインスタンス
   * @throws IllegalStateException 行が作成または選択されていない場合
   */
  public WorkSheetBuilder dataCell(int columnIndex, boolean value) {
    if (currentRow == null) {
      throw new IllegalStateException("No row has been created or selected");
    }

    Cell cell = currentRow.createCell(columnIndex);
    cell.setCellValue(value);

    // テンプレートから取得した書式を適用
    String styleName = "_template_col_" + columnIndex + "_style";
    if (namedStyles.containsKey(styleName)) {
      cell.setCellStyle(namedStyles.get(styleName));
    }

    return this;
  }

  /**
   * 最初のデータ行（2行目、インデックス1）を作成します。
   *
   * @return メソッドチェーン用のこのビルダーインスタンス
   * @throws IllegalStateException ヘッダー行が存在しない場合
   */
  public WorkSheetBuilder firstDataRow() {
    return dataRow(0);
  }

  /**
   * 最初のデータ行（2行目、インデックス1）を作成し、そのデータ行を操作するためのDataRowBuilderを返します。
   *
   * @return 新しいDataRowBuilderインスタンス
   * @throws IllegalStateException ヘッダー行が存在しない場合
   */
  public DataRowBuilder firstDataRowBuilder() {
    return dataRowBuilder(0);
  }

  /**
   * 現在の行に文字列値を持つセルを作成します。
   *
   * @param columnIndex セルのインデックス
   * @param value 設定する文字列値
   * @return メソッドチェーン用のこのビルダーインスタンス
   * @throws IllegalStateException 行が作成または選択されていない場合
   */
  public WorkSheetBuilder cell(int columnIndex, String value) {
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
  public WorkSheetBuilder cell(int columnIndex, double value) {
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
  public WorkSheetBuilder cell(int columnIndex, boolean value) {
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
  public WorkSheetBuilder cellFormula(int columnIndex, String formula) {
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
  public WorkSheetBuilder styledCell(int columnIndex, String value, String styleName) {
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
   * 現在のシートの特定の列の幅を設定します。
   *
   * @param columnIndex 列のインデックス
   * @param width 文字単位の幅
   * @return メソッドチェーン用のこのビルダーインスタンス
   */
  public WorkSheetBuilder columnWidth(int columnIndex, int width) {
    sheet.setColumnWidth(columnIndex, width * 256);
    return this;
  }

  /**
   * 現在のシートの列を自動サイズ調整します。
   *
   * @param columnIndex 列のインデックス
   * @return メソッドチェーン用のこのビルダーインスタンス
   */
  public WorkSheetBuilder autoSizeColumn(int columnIndex) {
    sheet.autoSizeColumn(columnIndex);
    return this;
  }

  /**
   * 親のWorkBookBuilderに戻ります。
   *
   * @return 親のWorkBookBuilderインスタンス
   */
  public WorkBookBuilder end() {
    return parent;
  }

  /**
   * 現在のシートを取得します。
   *
   * @return 現在のシート
   */
  Sheet getSheet() {
    return sheet;
  }

  /**
   * データソースからワークシートにデータを追加します。 各データ項目に対して、指定されたコンシューマーが呼び出されます。 コンシューマーは、DataRowBuilderとデータ項目を受け取り、
   * ワークシートにデータを書き込む方法を定義します。
   *
   * @param <T> データソースの要素の型
   * @param dataSource データソース（Iterable<T>型）
   * @param consumer 各データ項目をワークシートに書き込む方法を定義するBiConsumer
   * @return メソッドチェーン用のこのビルダーインスタンス
   * @throws IllegalStateException ヘッダー行が存在しない場合
   */
  public <T> WorkSheetBuilder datasource(Iterable<T> dataSource,
      BiConsumer<DataRowBuilder, T> consumer) {
    if (!hasHeaderRow) {
      throw new IllegalStateException("Header row must be created first");
    }

    int dataRowIndex = 0;
    for (T item : dataSource) {
      DataRowBuilder rowBuilder = dataRowBuilder(dataRowIndex);
      consumer.accept(rowBuilder, item);
      dataRowIndex++;
    }

    return this;
  }
}
