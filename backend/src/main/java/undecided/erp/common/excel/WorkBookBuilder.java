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
 * Builder class for creating Excel workbooks. This class provides a fluent API for creating and
 * configuring Excel workbooks, sheets, rows, and cells.
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
   * Creates a new WorkBookBuilder with a new workbook.
   *
   * @return a new WorkBookBuilder instance
   */
  public static WorkBookBuilder create() {
    return new WorkBookBuilder(ExcelUtils.createWorkbook());
  }

  /**
   * Creates a new WorkBookBuilder with the provided workbook.
   *
   * @param workbook the workbook to use
   * @return a new WorkBookBuilder instance
   */
  public static WorkBookBuilder withWorkbook(Workbook workbook) {
    return new WorkBookBuilder(workbook);
  }

  /**
   * Creates a new WorkBookBuilder with an existing Excel file as a template.
   *
   * @param filePath the path to the Excel file to use as a template
   * @return a new WorkBookBuilder instance
   * @throws IOException if an I/O error occurs
   * @throws IllegalArgumentException if the file does not have a valid XLSX extension
   */
  public static WorkBookBuilder fromTemplate(String filePath) throws IOException {
    Workbook workbook = ExcelUtils.loadWorkbook(filePath);
    return new WorkBookBuilder(workbook);
  }

  /**
   * Creates a new sheet in the workbook and makes it the current sheet.
   *
   * @param sheetName the name of the sheet
   * @return this builder instance for method chaining
   */
  public WorkBookBuilder sheet(String sheetName) {
    currentSheet = workbook.createSheet(sheetName);
    currentRow = null;
    return this;
  }

  /**
   * Sets an existing sheet as the current sheet.
   *
   * @param sheetIndex the index of the sheet
   * @return this builder instance for method chaining
   */
  public WorkBookBuilder sheet(int sheetIndex) {
    currentSheet = workbook.getSheetAt(sheetIndex);
    currentRow = null;
    return this;
  }

  /**
   * Creates a new row in the current sheet and makes it the current row.
   *
   * @param rowIndex the index of the row
   * @return this builder instance for method chaining
   * @throws IllegalStateException if no sheet has been created or selected
   */
  public WorkBookBuilder row(int rowIndex) {
    if (currentSheet == null) {
      throw new IllegalStateException("No sheet has been created or selected");
    }
    currentRow = currentSheet.createRow(rowIndex);
    return this;
  }

  /**
   * Creates a cell in the current row with a string value.
   *
   * @param columnIndex the index of the cell
   * @param value the string value to set
   * @return this builder instance for method chaining
   * @throws IllegalStateException if no row has been created or selected
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
   * Creates a cell in the current row with a numeric value.
   *
   * @param columnIndex the index of the cell
   * @param value the numeric value to set
   * @return this builder instance for method chaining
   * @throws IllegalStateException if no row has been created or selected
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
   * Creates a cell in the current row with a boolean value.
   *
   * @param columnIndex the index of the cell
   * @param value the boolean value to set
   * @return this builder instance for method chaining
   * @throws IllegalStateException if no row has been created or selected
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
   * Creates a cell in the current row with a formula.
   *
   * @param columnIndex the index of the cell
   * @param formula the formula to set
   * @return this builder instance for method chaining
   * @throws IllegalStateException if no row has been created or selected
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
   * Creates a cell in the current row with a string value and applies a named style.
   *
   * @param columnIndex the index of the cell
   * @param value the string value to set
   * @param styleName the name of the style to apply
   * @return this builder instance for method chaining
   * @throws IllegalStateException if no row has been created or selected or if the style does not
   * exist
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
   * Creates a named cell style that can be reused.
   *
   * @param styleName the name of the style
   * @param styleConfigurer a consumer that configures the style
   * @return this builder instance for method chaining
   */
  public WorkBookBuilder createStyle(String styleName,
      java.util.function.Consumer<CellStyle> styleConfigurer) {
    CellStyle style = workbook.createCellStyle();
    styleConfigurer.accept(style);
    namedStyles.put(styleName, style);
    return this;
  }

  /**
   * Creates a bold font style.
   *
   * @param styleName the name of the style
   * @return this builder instance for method chaining
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
   * Sets the column width for a specific column in the current sheet.
   *
   * @param columnIndex the index of the column
   * @param width the width in characters
   * @return this builder instance for method chaining
   * @throws IllegalStateException if no sheet has been created or selected
   */
  public WorkBookBuilder columnWidth(int columnIndex, int width) {
    if (currentSheet == null) {
      throw new IllegalStateException("No sheet has been created or selected");
    }
    currentSheet.setColumnWidth(columnIndex, width * 256);
    return this;
  }

  /**
   * Auto-sizes a column in the current sheet.
   *
   * @param columnIndex the index of the column
   * @return this builder instance for method chaining
   * @throws IllegalStateException if no sheet has been created or selected
   */
  public WorkBookBuilder autoSizeColumn(int columnIndex) {
    if (currentSheet == null) {
      throw new IllegalStateException("No sheet has been created or selected");
    }
    currentSheet.autoSizeColumn(columnIndex);
    return this;
  }

  /**
   * Builds and returns the configured workbook.
   *
   * @return the configured workbook
   */
  public Workbook build() {
    return workbook;
  }
}
