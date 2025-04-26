package undecided.erp.common.excel;

import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

/**
 * DataRowBuilder は、Excel ワークシートのデータ行の構築および管理を簡易化するビルダークラスです。 セルの構成やスタイルの設定をチェーンメソッドの形式で提供します。
 * WorkSheetBuilder と連携して使用することで、より直感的なデータ行操作が可能になります。
 */
public final class DataRowBuilder {

  private final WorkSheetBuilder parent;
  private final Row row;
  private final Map<String, CellStyle> namedStyles;

  /**
   * DataRowBuilder のコンストラクタ。
   *
   * @param parent 親となる WorkSheetBuilder
   * @param row 操作対象の行
   * @param namedStyles 名前付きスタイルのマップ
   */
  DataRowBuilder(WorkSheetBuilder parent, Row row, Map<String, CellStyle> namedStyles) {
    this.parent = parent;
    this.row = row;
    this.namedStyles = namedStyles;
  }

  /**
   * データセルを作成します。テンプレートの2行目の書式が適用されます。
   *
   * @param columnIndex セルのインデックス
   * @param value 設定する文字列値
   * @return メソッドチェーン用のこのビルダーインスタンス
   */
  public DataRowBuilder cell(int columnIndex, String value) {
    Cell cell = row.createCell(columnIndex);
    cell.setCellValue(value);

    // テンプレートから取得した書式を適用
    String styleName = "_template_col_" + columnIndex + "_style";
    if (namedStyles.containsKey(styleName)) {
      cell.setCellStyle(namedStyles.get(styleName));
    }

    return this;
  }

  /**
   * データセルを作成します。テンプレートの2行目の書式が適用されます。
   *
   * @param columnIndex セルのインデックス
   * @param value 設定する数値
   * @return メソッドチェーン用のこのビルダーインスタンス
   */
  public DataRowBuilder cell(int columnIndex, double value) {
    Cell cell = row.createCell(columnIndex);
    cell.setCellValue(value);

    // テンプレートから取得した書式を適用
    String styleName = "_template_col_" + columnIndex + "_style";
    if (namedStyles.containsKey(styleName)) {
      cell.setCellStyle(namedStyles.get(styleName));
    }

    return this;
  }

  /**
   * データセルを作成します。テンプレートの2行目の書式が適用されます。
   *
   * @param columnIndex セルのインデックス
   * @param value 設定する真偽値
   * @return メソッドチェーン用のこのビルダーインスタンス
   */
  public DataRowBuilder cell(int columnIndex, boolean value) {
    Cell cell = row.createCell(columnIndex);
    cell.setCellValue(value);

    // テンプレートから取得した書式を適用
    String styleName = "_template_col_" + columnIndex + "_style";
    if (namedStyles.containsKey(styleName)) {
      cell.setCellStyle(namedStyles.get(styleName));
    }

    return this;
  }

  /**
   * 数式を持つデータセルを作成します。テンプレートの2行目の書式が適用されます。
   *
   * @param columnIndex セルのインデックス
   * @param formula 設定する数式
   * @return メソッドチェーン用のこのビルダーインスタンス
   */
  public DataRowBuilder cellFormula(int columnIndex, String formula) {
    Cell cell = row.createCell(columnIndex);
    cell.setCellFormula(formula);

    // テンプレートから取得した書式を適用
    String styleName = "_template_col_" + columnIndex + "_style";
    if (namedStyles.containsKey(styleName)) {
      cell.setCellStyle(namedStyles.get(styleName));
    }

    return this;
  }

  /**
   * 文字列値を持つデータセルを作成し、名前付きスタイルを適用します。
   *
   * @param columnIndex セルのインデックス
   * @param value 設定する文字列値
   * @param styleName 適用するスタイルの名前
   * @return メソッドチェーン用のこのビルダーインスタンス
   * @throws IllegalStateException スタイルが存在しない場合
   */
  public DataRowBuilder styledCell(int columnIndex, String value, String styleName) {
    if (!namedStyles.containsKey(styleName)) {
      throw new IllegalStateException("Style '" + styleName + "' does not exist");
    }
    Cell cell = row.createCell(columnIndex);
    cell.setCellValue(value);
    cell.setCellStyle(namedStyles.get(styleName));
    return this;
  }

  /**
   * 親のWorkSheetBuilderに戻ります。
   *
   * @return 親のWorkSheetBuilderインスタンス
   */
  public WorkSheetBuilder end() {
    return parent;
  }
}
