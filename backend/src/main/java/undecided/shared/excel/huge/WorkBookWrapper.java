package undecided.shared.excel.huge;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import undecided.shared.common.exception.IORuntimeException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class WorkBookWrapper {
    private SXSSFWorkbook workbook = null;

    public WorkBookWrapper() {
        workbook = new SXSSFWorkbook();
    }

    public WorkBookWrapper(XSSFWorkbook workbook) {
        this.workbook = new SXSSFWorkbook(workbook);
        this.workbook.setActiveSheet(0);
    }

    public void saveAs(String fileName) {
        try (FileOutputStream out = new FileOutputStream(fileName)) {
            workbook.write(out);
        } catch (IOException e) {
            throw new IORuntimeException("", "IoException", e);

        }
    }

    public void saveAs(OutputStream stream) {
        try {
            this.workbook.write(stream);
        } catch (IOException e) {
            throw new IORuntimeException("", "IoException", e);
        }
    }

    public boolean setActiveSheet(String sheetName) {
        for (int i = 0; i < workbook.getNumberOfSheets(); ++i) {
            workbook.getSheetAt(i).setSelected(false);
        }

        int idx = workbook.getSheetIndex(sheetName);
        workbook.setActiveSheet(idx);
        workbook.getSheetAt(idx).setSelected(true);
        return true;

    }

    public boolean setActiveSheet(int sheetIdx) {
        for (int i = 0; i < this.workbook.getNumberOfSheets(); ++i) {
            this.workbook.getSheetAt(i).setSelected(false);
        }

        this.workbook.setActiveSheet(sheetIdx);
        this.workbook.getSheetAt(sheetIdx).setSelected(true);
        return true;
    }

    public String getActiveSheetName() {
        int idx = workbook.getActiveSheetIndex();
        return workbook.getSheetName(idx);
    }

    public SXSSFSheet addBlankSheet(String sheetName) {
        SXSSFSheet sheet = workbook.createSheet(sheetName);
        int idx = workbook.getSheetIndex(sheet);
        workbook.setActiveSheet(idx);
        return sheet;
    }

    public SXSSFRow insertRow(int rowNo) {
        int idx = workbook.getActiveSheetIndex();
        SXSSFSheet sheet = workbook.getSheetAt(idx);
        SXSSFRow row = sheet.createRow(rowNo - 1);
        return row;
    }

    public SXSSFCell insertCell(SXSSFRow row, int colNo, CellStyle style) throws Exception {
        SXSSFCell cell = row.createCell(colNo - 1);
        if (style != null) {
            cell.setCellStyle(style);
        }

        return cell;
    }

    public CellStyle createCellStyle() {
        return workbook.createCellStyle();
    }

    public SXSSFWorkbook getWorkbook() {
        return workbook;
    }
}

