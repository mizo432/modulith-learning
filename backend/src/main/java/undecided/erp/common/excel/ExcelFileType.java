package undecided.erp.common.excel;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Excel file type constants. This class defines the file extension to be used for Excel files.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExcelFileType {

  /**
   * The file extension for Excel files (.xlsx). This project uses the Office Open XML format (XLSX)
   * for Excel files.
   */
  public static final String EXTENSION = "xlsx";

  /**
   * The file extension for Excel files with dot prefix (.xlsx).
   */
  public static final String EXTENSION_WITH_DOT = "." + EXTENSION;

  /**
   * The MIME type for Excel XLSX files.
   */
  public static final String MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
}
