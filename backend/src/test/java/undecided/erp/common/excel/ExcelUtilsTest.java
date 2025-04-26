package undecided.erp.common.excel;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for the ExcelUtils class.
 */
class ExcelUtilsTest {

  @TempDir
  Path tempDir;

  @Test
  void testCreateWorkbook() {
    Workbook workbook = ExcelUtils.createWorkbook();
    assertNotNull(workbook, "Workbook should not be null");
    assertTrue(workbook.getClass().getName().contains("XSSF"),
        "Workbook should be XSSF (XLSX format)");
  }

  @Test
  void testHasValidExtension() {
    File validFile = new File("test.xlsx");
    File invalidFile = new File("test.xls");
    File invalidFile2 = new File("test.csv");

    assertTrue(ExcelUtils.hasValidExtension(validFile),
        "File with .xlsx extension should be valid");
    assertFalse(ExcelUtils.hasValidExtension(invalidFile),
        "File with .xls extension should be invalid");
    assertFalse(ExcelUtils.hasValidExtension(invalidFile2),
        "File with .csv extension should be invalid");
  }

  @Test
  void testEnsureCorrectExtension() {
    assertEquals("test.xlsx",
        ExcelUtils.ensureCorrectExtension("test"),
        "Should add .xlsx extension");
    assertEquals("test.xlsx",
        ExcelUtils.ensureCorrectExtension("test.xls"),
        "Should replace .xls with .xlsx");
    assertEquals("test.xlsx",
        ExcelUtils.ensureCorrectExtension("test.xlsx"),
        "Should keep .xlsx extension");
    assertEquals("path/to/file.xlsx",
        ExcelUtils.ensureCorrectExtension("path/to/file.csv"),
        "Should replace .csv with .xlsx in path");
  }

  @Test
  void testWriteAndReadExcel() throws IOException {
    // Create a test file path
    Path filePath = tempDir.resolve("test-write-read.xlsx");
    String filePathStr = filePath.toString();

    // Write test data to Excel
    ExcelUtils.writeExcel(filePathStr, workbook -> {
      Sheet sheet = workbook.createSheet("TestSheet");
      Row headerRow = sheet.createRow(0);
      headerRow.createCell(0).setCellValue("Column1");
      headerRow.createCell(1).setCellValue("Column2");

      Row dataRow1 = sheet.createRow(1);
      dataRow1.createCell(0).setCellValue("Value1");
      dataRow1.createCell(1).setCellValue("Value2");

      Row dataRow2 = sheet.createRow(2);
      dataRow2.createCell(0).setCellValue("Value3");
      dataRow2.createCell(1).setCellValue("Value4");
    });

    // Verify file exists and has correct extension
    File excelFile = filePath.toFile();
    assertTrue(excelFile.exists(), "Excel file should exist");
    assertTrue(ExcelUtils.hasValidExtension(excelFile), "File should have .xlsx extension");

    // Read and verify data
    List<String[]> data = ExcelUtils.extractDataFromExcel(filePathStr);
    assertEquals(3, data.size(), "Should have 3 rows (header + 2 data rows)");

    // Verify header row
    assertArrayEquals(new String[]{"Column1", "Column2"}, data.get(0),
        "Header row should match expected values");

    // Verify data rows
    assertArrayEquals(new String[]{"Value1", "Value2"}, data.get(1),
        "First data row should match expected values");
    assertArrayEquals(new String[]{"Value3", "Value4"}, data.get(2),
        "Second data row should match expected values");
  }

  @Test
  void testCreateTempExcelFile() throws IOException {
    Path tempFile = ExcelUtils.createTempExcelFile("test-temp", workbook -> {
      Sheet sheet = workbook.createSheet("TempSheet");
      Row row = sheet.createRow(0);
      row.createCell(0).setCellValue("TempValue");
    });

    assertTrue(tempFile.toFile().exists(), "Temp file should exist");
    assertTrue(tempFile.toString().endsWith(ExcelFileType.EXTENSION_WITH_DOT),
        "Temp file should have .xlsx extension");
  }

  @Test
  void testGetCellStringValue() throws IOException {
    // Create a test file with different cell types
    Path filePath = tempDir.resolve("test-cell-values.xlsx");
    String filePathStr = filePath.toString();

    ExcelUtils.writeExcel(filePathStr, workbook -> {
      Sheet sheet = workbook.createSheet("CellTypes");
      Row row = sheet.createRow(0);
      row.createCell(0).setCellValue("String Value");
      row.createCell(1).setCellValue(123.45);
      row.createCell(2).setCellValue(true);
      row.createCell(3).setCellFormula("SUM(B1:B2)");
    });

    // Read and verify cell values
    List<String[]> data = ExcelUtils.extractDataFromExcel(filePathStr);
    assertEquals(1, data.size(), "Should have 1 row");

    String[] rowData = data.get(0);
    assertEquals("String Value", rowData[0], "String cell value should match");
    assertEquals("123.45", rowData[1], "Numeric cell value should match");
    assertEquals("true", rowData[2], "Boolean cell value should match");
    assertEquals("SUM(B1:B2)", rowData[3], "Formula cell value should match");
  }

  @Test
  void testReadExcelWithInvalidExtension() {
    Path filePath = tempDir.resolve("invalid-file.xls");
    String filePathStr = filePath.toString();

    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      ExcelUtils.readExcel(filePathStr, row -> {
      });
    });

    assertTrue(exception.getMessage().contains(".xlsx"),
        "Exception message should mention .xlsx extension");
  }
}
