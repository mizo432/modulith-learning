package undecided.erp.common.excel;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for the WorkBookBuilder class.
 */
class WorkBookBuilderTest {

  @TempDir
  Path tempDir;

  @Test
  void testCreateWorkbook() {
    Workbook workbook = WorkBookBuilder.create().build();
    assertNotNull(workbook, "Workbook should not be null");
    assertTrue(workbook.getClass().getName().contains("XSSF"),
        "Workbook should be XSSF (XLSX format)");
  }

  @Test
  void testCreateSheet() {
    Workbook workbook = WorkBookBuilder.create()
        .sheet("TestSheet")
        .build();

    assertEquals(1, workbook.getNumberOfSheets(), "Workbook should have 1 sheet");
    assertEquals("TestSheet", workbook.getSheetAt(0).getSheetName(),
        "Sheet name should match");
  }

  @Test
  void testCreateMultipleSheets() {
    Workbook workbook = WorkBookBuilder.create()
        .sheet("Sheet1")
        .sheet("Sheet2")
        .sheet("Sheet3")
        .build();

    assertEquals(3, workbook.getNumberOfSheets(), "Workbook should have 3 sheets");
    assertEquals("Sheet1", workbook.getSheetAt(0).getSheetName(),
        "First sheet name should match");
    assertEquals("Sheet2", workbook.getSheetAt(1).getSheetName(),
        "Second sheet name should match");
    assertEquals("Sheet3", workbook.getSheetAt(2).getSheetName(),
        "Third sheet name should match");
  }

  @Test
  void testCreateRowsAndCells() {
    Workbook workbook = WorkBookBuilder.create()
        .sheet("DataSheet")
        .row(0)
        .cell(0, "Header1")
        .cell(1, "Header2")
        .row(1)
        .cell(0, "Value1")
        .cell(1, 123.45)
        .row(2)
        .cell(0, "Value3")
        .cell(1, true)
        .build();

    Sheet sheet = workbook.getSheetAt(0);
    assertEquals(3, sheet.getPhysicalNumberOfRows(), "Sheet should have 3 rows");

    // Check header row
    Row headerRow = sheet.getRow(0);
    assertEquals("Header1", headerRow.getCell(0).getStringCellValue(),
        "Header1 value should match");
    assertEquals("Header2", headerRow.getCell(1).getStringCellValue(),
        "Header2 value should match");

    // Check data row 1
    Row dataRow1 = sheet.getRow(1);
    assertEquals("Value1", dataRow1.getCell(0).getStringCellValue(),
        "Value1 should match");
    assertEquals(123.45, dataRow1.getCell(1).getNumericCellValue(), 0.001,
        "Numeric value should match");

    // Check data row 2
    Row dataRow2 = sheet.getRow(2);
    assertEquals("Value3", dataRow2.getCell(0).getStringCellValue(),
        "Value3 should match");
    assertTrue(dataRow2.getCell(1).getBooleanCellValue(),
        "Boolean value should be true");
  }

  @Test
  void testCellFormula() {
    Workbook workbook = WorkBookBuilder.create()
        .sheet("FormulaSheet")
        .row(0)
        .cell(0, 10.0)
        .cell(1, 20.0)
        .row(1)
        .cellFormula(0, "SUM(A1:B1)")
        .build();

    Sheet sheet = workbook.getSheetAt(0);
    Row formulaRow = sheet.getRow(1);
    Cell formulaCell = formulaRow.getCell(0);

    assertEquals("SUM(A1:B1)", formulaCell.getCellFormula(),
        "Formula should match");
  }

  @Test
  void testCellStyles() {
    // Create the builder first with bold style
    Workbook workbook = WorkBookBuilder.create()
        .sheet("StyleSheet")
        .createBoldStyle("headerStyle")
        .build();

    // Create a final reference to the workbook for use in the lambda
    final Workbook finalWorkbook = workbook;

    // Create a new builder with the existing workbook and add the custom style
    workbook = WorkBookBuilder.withWorkbook(workbook)
        .createStyle("customStyle", style -> {
          Font font = finalWorkbook.createFont();
          font.setItalic(true);
          style.setFont(font);
        })
        .row(0)
        .styledCell(0, "Bold Header", "headerStyle")
        .row(1)
        .styledCell(0, "Italic Text", "customStyle")
        .build();

    Sheet sheet = workbook.getSheetAt(0);

    // Check bold style
    Cell boldCell = sheet.getRow(0).getCell(0);
    CellStyle boldStyle = boldCell.getCellStyle();
    Font boldFont = workbook.getFontAt(boldStyle.getFontIndex());
    assertTrue(boldFont.getBold(), "Font should be bold");

    // Check italic style
    Cell italicCell = sheet.getRow(1).getCell(0);
    CellStyle italicStyle = italicCell.getCellStyle();
    Font italicFont = workbook.getFontAt(italicStyle.getFontIndex());
    assertTrue(italicFont.getItalic(), "Font should be italic");
  }

  @Test
  void testColumnWidth() {
    Workbook workbook = WorkBookBuilder.create()
        .sheet("WidthSheet")
        .columnWidth(0, 20)
        .columnWidth(1, 30)
        .build();

    Sheet sheet = workbook.getSheetAt(0);
    assertEquals(20 * 256, sheet.getColumnWidth(0),
        "Column 0 width should match");
    assertEquals(30 * 256, sheet.getColumnWidth(1),
        "Column 1 width should match");
  }

  @Test
  void testExceptionWhenNoSheet() {
    WorkBookBuilder builder = WorkBookBuilder.create();
    Exception exception = assertThrows(IllegalStateException.class, () -> {
      builder.row(0);
    });
    assertTrue(exception.getMessage().contains("No sheet"),
        "Exception message should mention no sheet");
  }

  @Test
  void testExceptionWhenNoRow() {
    WorkBookBuilder builder = WorkBookBuilder.create().sheet("TestSheet");
    Exception exception = assertThrows(IllegalStateException.class, () -> {
      builder.cell(0, "Value");
    });
    assertTrue(exception.getMessage().contains("No row"),
        "Exception message should mention no row");
  }

  @Test
  void testExceptionWhenStyleNotFound() {
    WorkBookBuilder builder = WorkBookBuilder.create()
        .sheet("TestSheet")
        .row(0);
    Exception exception = assertThrows(IllegalStateException.class, () -> {
      builder.styledCell(0, "Value", "nonExistentStyle");
    });
    assertTrue(exception.getMessage().contains("Style"),
        "Exception message should mention style not existing");
  }

  @Test
  void testIntegrationWithExcelUtils() throws IOException {
    // Create a workbook using the builder
    Workbook workbook = WorkBookBuilder.create()
        .sheet("IntegrationSheet")
        .row(0)
        .cell(0, "Name")
        .cell(1, "Value")
        .row(1)
        .cell(0, "Item1")
        .cell(1, 100.0)
        .row(2)
        .cell(0, "Item2")
        .cell(1, 200.0)
        .build();

    // Save the workbook to a file
    Path filePath = tempDir.resolve("integration-test.xlsx");
    try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
      workbook.write(fos);
    }

    // Read the file using ExcelUtils
    File excelFile = filePath.toFile();
    assertTrue(excelFile.exists(), "Excel file should exist");
    assertTrue(ExcelUtils.hasValidExtension(excelFile),
        "File should have .xlsx extension");

    // Extract and verify data
    java.util.List<String[]> data = ExcelUtils.extractDataFromExcel(filePath.toString());
    assertEquals(3, data.size(), "Should have 3 rows");

    // Verify header row
    assertArrayEquals(new String[]{"Name", "Value"}, data.get(0),
        "Header row should match expected values");

    // Verify data rows
    assertArrayEquals(new String[]{"Item1", "100.0"}, data.get(1),
        "First data row should match expected values");
    assertArrayEquals(new String[]{"Item2", "200.0"}, data.get(2),
        "Second data row should match expected values");
  }

  @Test
  void testFromTemplate() throws IOException {
    // Create a workbook with some data
    Workbook originalWorkbook = WorkBookBuilder.create()
        .sheet("TemplateSheet")
        .row(0)
        .cell(0, "Header1")
        .cell(1, "Header2")
        .row(1)
        .cell(0, "Data1")
        .cell(1, 123.45)
        .build();

    // Save the workbook to a file
    Path templatePath = tempDir.resolve("template-test.xlsx");
    try (FileOutputStream fos = new FileOutputStream(templatePath.toFile())) {
      originalWorkbook.write(fos);
    }

    // Load the file as a template
    WorkBookBuilder builder = WorkBookBuilder.fromTemplate(templatePath.toString());
    Workbook loadedWorkbook = builder.build();

    // Verify the loaded workbook has the same content as the original
    assertEquals(1, loadedWorkbook.getNumberOfSheets(), "Should have 1 sheet");
    Sheet sheet = loadedWorkbook.getSheetAt(0);
    assertEquals("TemplateSheet", sheet.getSheetName(), "Sheet name should match");

    // Verify header row
    Row headerRow = sheet.getRow(0);
    assertEquals("Header1", headerRow.getCell(0).getStringCellValue(), "Header1 should match");
    assertEquals("Header2", headerRow.getCell(1).getStringCellValue(), "Header2 should match");

    // Verify data row
    Row dataRow = sheet.getRow(1);
    assertEquals("Data1", dataRow.getCell(0).getStringCellValue(), "Data1 should match");
    assertEquals(123.45, dataRow.getCell(1).getNumericCellValue(), 0.001,
        "Numeric value should match");

    // Test adding more data to the template
    Workbook enhancedWorkbook = builder
        .sheet("TemplateSheet") // Select the existing sheet
        .row(2) // Add a new row
        .cell(0, "Data2")
        .cell(1, 678.90)
        .build();

    // Verify the enhanced workbook
    sheet = enhancedWorkbook.getSheetAt(0);
    assertEquals(3, sheet.getLastRowNum() + 1, "Should have 3 rows");

    // Verify the new row
    Row newRow = sheet.getRow(2);
    assertEquals("Data2", newRow.getCell(0).getStringCellValue(), "Data2 should match");
    assertEquals(678.90, newRow.getCell(1).getNumericCellValue(), 0.001,
        "Numeric value should match");
  }
}
