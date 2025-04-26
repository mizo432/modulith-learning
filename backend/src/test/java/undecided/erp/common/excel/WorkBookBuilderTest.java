package undecided.erp.common.excel;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * WorkBookBuilderクラスのテスト。
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

  @Test
  void testFromResource() throws IOException {
    // Create a workbook with some data
    Workbook originalWorkbook = WorkBookBuilder.create()
        .sheet("ResourceSheet")
        .row(0)
        .cell(0, "ResourceHeader1")
        .cell(1, "ResourceHeader2")
        .row(1)
        .cell(0, "ResourceData1")
        .cell(1, 987.65)
        .build();

    // Save the workbook to a file in the test resources directory
    Path resourceDir = tempDir.resolve("resources");
    resourceDir.toFile().mkdir();
    Path resourcePath = resourceDir.resolve("test-resource.xlsx");
    try (FileOutputStream fos = new FileOutputStream(resourcePath.toFile())) {
      originalWorkbook.write(fos);
    }

    // Create a custom ClassLoader that can load from our temporary directory
    ClassLoader testClassLoader = new ClassLoader(WorkBookBuilderTest.class.getClassLoader()) {
      @Override
      public InputStream getResourceAsStream(String name) {
        if ("test-resource.xlsx".equals(name)) {
          try {
            return new FileInputStream(resourcePath.toFile());
          } catch (IOException e) {
            return null;
          }
        }
        return super.getResourceAsStream(name);
      }
    };

    // Load the file as a resource using our custom ClassLoader
    WorkBookBuilder builder = WorkBookBuilder.fromResource("test-resource.xlsx",
        testClassLoader);
    Workbook loadedWorkbook = builder.build();

    // Verify the loaded workbook has the same content as the original
    assertEquals(1, loadedWorkbook.getNumberOfSheets(), "Should have 1 sheet");
    Sheet sheet = loadedWorkbook.getSheetAt(0);
    assertEquals("ResourceSheet", sheet.getSheetName(), "Sheet name should match");

    // Verify header row
    Row headerRow = sheet.getRow(0);
    assertEquals("ResourceHeader1", headerRow.getCell(0).getStringCellValue(),
        "ResourceHeader1 should match");
    assertEquals("ResourceHeader2", headerRow.getCell(1).getStringCellValue(),
        "ResourceHeader2 should match");

    // Verify data row
    Row dataRow = sheet.getRow(1);
    assertEquals("ResourceData1", dataRow.getCell(0).getStringCellValue(),
        "ResourceData1 should match");
    assertEquals(987.65, dataRow.getCell(1).getNumericCellValue(), 0.001,
        "Numeric value should match");

    // Test adding more data to the template
    Workbook enhancedWorkbook = builder
        .sheet("ResourceSheet") // Select the existing sheet
        .row(2) // Add a new row
        .cell(0, "ResourceData2")
        .cell(1, 543.21)
        .build();

    // Verify the enhanced workbook
    sheet = enhancedWorkbook.getSheetAt(0);
    assertEquals(3, sheet.getLastRowNum() + 1, "Should have 3 rows");

    // Verify the new row
    Row newRow = sheet.getRow(2);
    assertEquals("ResourceData2", newRow.getCell(0).getStringCellValue(),
        "ResourceData2 should match");
    assertEquals(543.21, newRow.getCell(1).getNumericCellValue(), 0.001,
        "Numeric value should match");
  }

  @Test
  void testWorkSheetBuilder() {
    // Test creating a new sheet with WorkSheetBuilder
    Workbook workbook = WorkBookBuilder.create()
        .worksheet("SheetWithBuilder")
        .row(0)
        .cell(0, "Header1")
        .cell(1, "Header2")
        .row(1)
        .cell(0, "Data1")
        .cell(1, 123.45)
        .end() // Return to WorkBookBuilder
        .build();

    // Verify the sheet was created correctly
    assertEquals(1, workbook.getNumberOfSheets(), "Should have 1 sheet");
    Sheet sheet = workbook.getSheetAt(0);
    assertEquals("SheetWithBuilder", sheet.getSheetName(), "Sheet name should match");

    // Verify header row
    Row headerRow = sheet.getRow(0);
    assertEquals("Header1", headerRow.getCell(0).getStringCellValue(), "Header1 should match");
    assertEquals("Header2", headerRow.getCell(1).getStringCellValue(), "Header2 should match");

    // Verify data row
    Row dataRow = sheet.getRow(1);
    assertEquals("Data1", dataRow.getCell(0).getStringCellValue(), "Data1 should match");
    assertEquals(123.45, dataRow.getCell(1).getNumericCellValue(), 0.001,
        "Numeric value should match");
  }

  @Test
  void testMultipleWorkSheets() {
    // Test creating multiple sheets with WorkSheetBuilder
    Workbook workbook = WorkBookBuilder.create()
        .worksheet("Sheet1")
        .row(0)
        .cell(0, "Sheet1 Data")
        .end()
        .worksheet("Sheet2")
        .row(0)
        .cell(0, "Sheet2 Data")
        .end()
        .build();

    // Verify both sheets were created correctly
    assertEquals(2, workbook.getNumberOfSheets(), "Should have 2 sheets");

    // Verify Sheet1
    Sheet sheet1 = workbook.getSheetAt(0);
    assertEquals("Sheet1", sheet1.getSheetName(), "Sheet1 name should match");
    assertEquals("Sheet1 Data", sheet1.getRow(0).getCell(0).getStringCellValue(),
        "Sheet1 data should match");

    // Verify Sheet2
    Sheet sheet2 = workbook.getSheetAt(1);
    assertEquals("Sheet2", sheet2.getSheetName(), "Sheet2 name should match");
    assertEquals("Sheet2 Data", sheet2.getRow(0).getCell(0).getStringCellValue(),
        "Sheet2 data should match");
  }

  @Test
  void testWorkSheetWithExistingSheet() {
    // Create a workbook with a sheet first
    WorkBookBuilder builder = WorkBookBuilder.create()
        .sheet("ExistingSheet")
        .row(0)
        .cell(0, "Existing Data");

    // Now use WorkSheetBuilder with the existing sheet
    Workbook workbook = builder
        .worksheet(0) // Get WorkSheetBuilder for the existing sheet
        .row(1)
        .cell(0, "New Data")
        .end()
        .build();

    // Verify the sheet was modified correctly
    Sheet sheet = workbook.getSheetAt(0);
    assertEquals("ExistingSheet", sheet.getSheetName(), "Sheet name should match");
    assertEquals("Existing Data", sheet.getRow(0).getCell(0).getStringCellValue(),
        "Existing data should match");
    assertEquals("New Data", sheet.getRow(1).getCell(0).getStringCellValue(),
        "New data should match");
  }

  @Test
  void testWorkSheetWithCurrentSheet() {
    // Create a workbook with a sheet first
    WorkBookBuilder builder = WorkBookBuilder.create()
        .sheet("CurrentSheet");

    // Now use WorkSheetBuilder with the current sheet
    Workbook workbook = builder
        .worksheet() // Get WorkSheetBuilder for the current sheet
        .row(0)
        .cell(0, "Current Sheet Data")
        .end()
        .build();

    // Verify the sheet was modified correctly
    Sheet sheet = workbook.getSheetAt(0);
    assertEquals("CurrentSheet", sheet.getSheetName(), "Sheet name should match");
    assertEquals("Current Sheet Data", sheet.getRow(0).getCell(0).getStringCellValue(),
        "Data should match");
  }

  @Test
  void testWorkSheetWithTemplateHeader() {
    // Create a template workbook with headers
    Workbook templateWorkbook = WorkBookBuilder.create()
        .sheet("TemplateSheet")
        .row(0)
        .cell(0, "ID")
        .cell(1, "Name")
        .cell(2, "Value")
        .build();

    Sheet templateSheet = templateWorkbook.getSheetAt(0);

    // Create a new workbook and use the template headers
    Workbook workbook = WorkBookBuilder.create()
        .worksheetWithTemplateHeader("DataSheet", templateSheet)
        .firstDataRow() // Start with the first data row (row index 1)
        .cell(0, "001")
        .cell(1, "Item 1")
        .cell(2, 100.0)
        .dataRow(1) // Add second data row (row index 2)
        .cell(0, "002")
        .cell(1, "Item 2")
        .cell(2, 200.0)
        .end()
        .build();

    // Verify the sheet was created correctly
    Sheet sheet = workbook.getSheetAt(0);
    assertEquals("DataSheet", sheet.getSheetName(), "Sheet name should match");

    // Verify header row
    Row headerRow = sheet.getRow(0);
    assertNotNull(headerRow, "Header row should exist");
    assertEquals("ID", headerRow.getCell(0).getStringCellValue(), "Header 1 should match");
    assertEquals("Name", headerRow.getCell(1).getStringCellValue(), "Header 2 should match");
    assertEquals("Value", headerRow.getCell(2).getStringCellValue(), "Header 3 should match");

    // Verify first data row
    Row dataRow1 = sheet.getRow(1);
    assertNotNull(dataRow1, "First data row should exist");
    assertEquals("001", dataRow1.getCell(0).getStringCellValue(), "ID should match");
    assertEquals("Item 1", dataRow1.getCell(1).getStringCellValue(), "Name should match");
    assertEquals(100.0, dataRow1.getCell(2).getNumericCellValue(), 0.001, "Value should match");

    // Verify second data row
    Row dataRow2 = sheet.getRow(2);
    assertNotNull(dataRow2, "Second data row should exist");
    assertEquals("002", dataRow2.getCell(0).getStringCellValue(), "ID should match");
    assertEquals("Item 2", dataRow2.getCell(1).getStringCellValue(), "Name should match");
    assertEquals(200.0, dataRow2.getCell(2).getNumericCellValue(), 0.001, "Value should match");
  }

  @Test
  void testDataRowRequiresHeaderRow() {
    // Create a workbook
    WorkBookBuilder builder = WorkBookBuilder.create()
        .sheet("TestSheet");

    // Try to create a data row without a header row
    WorkSheetBuilder worksheetBuilder = builder.worksheet();
    Exception exception = assertThrows(IllegalStateException.class, () -> {
      worksheetBuilder.dataRow(0);
    });

    assertTrue(exception.getMessage().contains("Header row must be created first"),
        "Exception message should mention header row requirement");
  }

  @Test
  void testDatasourceMethod() {
    // Create a test data class
    class TestItem {

      private final String id;
      private final String name;
      private final double value;

      TestItem(String id, String name, double value) {
        this.id = id;
        this.name = name;
        this.value = value;
      }

      String getId() {
        return id;
      }

      String getName() {
        return name;
      }

      double getValue() {
        return value;
      }
    }

    // Create a list of test items
    List<TestItem> testItems = Arrays.asList(
        new TestItem("001", "Item 1", 100.0),
        new TestItem("002", "Item 2", 200.0),
        new TestItem("003", "Item 3", 300.0)
    );

    // Create a workbook with a template header
    Workbook templateWorkbook = WorkBookBuilder.create()
        .sheet("TemplateSheet")
        .row(0)
        .cell(0, "ID")
        .cell(1, "Name")
        .cell(2, "Value")
        .build();

    Sheet templateSheet = templateWorkbook.getSheetAt(0);

    // Create a new workbook and use the datasource method
    Workbook workbook = WorkBookBuilder.create()
        .worksheetWithTemplateHeader("DataSheet", templateSheet)
        .datasource(testItems, (builder, item) -> {
          builder.cell(0, item.getId())
              .cell(1, item.getName())
              .cell(2, item.getValue());
        })
        .end()
        .build();

    // Verify the sheet was created correctly
    Sheet sheet = workbook.getSheetAt(0);
    assertEquals("DataSheet", sheet.getSheetName(), "Sheet name should match");

    // Verify header row
    Row headerRow = sheet.getRow(0);
    assertNotNull(headerRow, "Header row should exist");
    assertEquals("ID", headerRow.getCell(0).getStringCellValue(), "Header 1 should match");
    assertEquals("Name", headerRow.getCell(1).getStringCellValue(), "Header 2 should match");
    assertEquals("Value", headerRow.getCell(2).getStringCellValue(), "Header 3 should match");

    // Verify data rows
    for (int i = 0; i < testItems.size(); i++) {
      TestItem item = testItems.get(i);
      Row dataRow = sheet.getRow(i + 1);
      assertNotNull(dataRow, "Data row " + (i + 1) + " should exist");
      assertEquals(item.getId(), dataRow.getCell(0).getStringCellValue(), "ID should match");
      assertEquals(item.getName(), dataRow.getCell(1).getStringCellValue(),
          "Name should match");
      assertEquals(item.getValue(), dataRow.getCell(2).getNumericCellValue(), 0.001,
          "Value should match");
    }
  }

  @Test
  void testDatasourceRequiresHeaderRow() {
    // Create a workbook
    WorkBookBuilder builder = WorkBookBuilder.create()
        .sheet("TestSheet");

    // Create a simple data source
    List<String> dataSource = Arrays.asList("Item 1", "Item 2", "Item 3");

    // Try to use datasource method without a header row
    WorkSheetBuilder worksheetBuilder = builder.worksheet();
    Exception exception = assertThrows(IllegalStateException.class, () -> {
      worksheetBuilder.datasource(dataSource, (wsBuilder, item) -> {
        wsBuilder.cell(0, item);
      });
    });

    assertTrue(exception.getMessage().contains("Header row must be created first"),
        "Exception message should mention header row requirement");
  }

  @Test
  void testWorkSheetWithTemplateHeaderAndFormat() {
    // Create a template workbook with headers and formatted second row
    Workbook templateWorkbook = WorkBookBuilder.create()
        .sheet("TemplateSheet")
        .row(0)
        .cell(0, "ID")
        .cell(1, "Name")
        .cell(2, "Value")
        .build();

    // Get the template sheet
    Sheet templateSheet = templateWorkbook.getSheetAt(0);

    // Add a formatted second row to the template
    Row formatRow = templateSheet.createRow(1);

    // Create a cell style for the ID column (column 0)
    CellStyle idStyle = templateWorkbook.createCellStyle();
    Font boldFont = templateWorkbook.createFont();
    boldFont.setBold(true);
    idStyle.setFont(boldFont);

    // Create a cell style for the Name column (column 1)
    CellStyle nameStyle = templateWorkbook.createCellStyle();
    Font italicFont = templateWorkbook.createFont();
    italicFont.setItalic(true);
    nameStyle.setFont(italicFont);

    // Create a cell style for the Value column (column 2)
    CellStyle valueStyle = templateWorkbook.createCellStyle();
    valueStyle.setDataFormat(templateWorkbook.createDataFormat().getFormat("#,##0.00"));

    // Apply the styles to the format row
    Cell idFormatCell = formatRow.createCell(0);
    idFormatCell.setCellStyle(idStyle);
    idFormatCell.setCellValue("001"); // Example value

    Cell nameFormatCell = formatRow.createCell(1);
    nameFormatCell.setCellStyle(nameStyle);
    nameFormatCell.setCellValue("Format Example"); // Example value

    Cell valueFormatCell = formatRow.createCell(2);
    valueFormatCell.setCellStyle(valueStyle);
    valueFormatCell.setCellValue(1234.56); // Example value

    // Create a new workbook and use the template headers and format
    Workbook workbook = WorkBookBuilder.create()
        .worksheetWithTemplateHeaderAndFormat("DataSheet", templateSheet)
        .firstDataRow() // Start with the first data row (row index 1)
        .dataCell(0, "001")
        .dataCell(1, "Item 1")
        .dataCell(2, 100.0)
        .dataRow(1) // Add second data row (row index 2)
        .dataCell(0, "002")
        .dataCell(1, "Item 2")
        .dataCell(2, 200.0)
        .end()
        .build();

    // Verify the sheet was created correctly
    Sheet sheet = workbook.getSheetAt(0);
    assertEquals("DataSheet", sheet.getSheetName(), "Sheet name should match");

    // Verify header row
    Row headerRow = sheet.getRow(0);
    assertNotNull(headerRow, "Header row should exist");
    assertEquals("ID", headerRow.getCell(0).getStringCellValue(), "Header 1 should match");
    assertEquals("Name", headerRow.getCell(1).getStringCellValue(), "Header 2 should match");
    assertEquals("Value", headerRow.getCell(2).getStringCellValue(), "Header 3 should match");

    // Verify first data row
    Row dataRow1 = sheet.getRow(1);
    assertNotNull(dataRow1, "First data row should exist");
    assertEquals("001", dataRow1.getCell(0).getStringCellValue(), "ID should match");
    assertEquals("Item 1", dataRow1.getCell(1).getStringCellValue(), "Name should match");
    assertEquals(100.0, dataRow1.getCell(2).getNumericCellValue(), 0.001, "Value should match");

    // Verify second data row
    Row dataRow2 = sheet.getRow(2);
    assertNotNull(dataRow2, "Second data row should exist");
    assertEquals("002", dataRow2.getCell(0).getStringCellValue(), "ID should match");
    assertEquals("Item 2", dataRow2.getCell(1).getStringCellValue(), "Name should match");
    assertEquals(200.0, dataRow2.getCell(2).getNumericCellValue(), 0.001, "Value should match");

    // Verify that the styles were applied correctly
    // ID column should have bold font
    assertTrue(workbook.getFontAt(dataRow1.getCell(0).getCellStyle().getFontIndex()).getBold(),
        "ID cell should have bold font");

    // Name column should have italic font
    assertTrue(
        workbook.getFontAt(dataRow1.getCell(1).getCellStyle().getFontIndex()).getItalic(),
        "Name cell should have italic font");

    // Value column should have number format
    assertEquals(valueStyle.getDataFormat(), dataRow1.getCell(2).getCellStyle().getDataFormat(),
        "Value cell should have the same data format");
  }
}
