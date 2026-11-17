package undecided.generic.{module}.internal.batch.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.Nullable;

/** CSV から読み込む {Entity} データの DTO。 */
@Getter
@Setter
public class {Entity}CsvDto implements Serializable {

  private static final long serialVersionUID = 1L;

  // TODO: CSV の各列に対応するフィールドを定義
  // 例:
  // private Long id;
  // private String name;
  // private String code;

  /**
   * CSV ヘッダー名とフィールド名のマッピングは、{@link undecided.generic.{module}.internal.batch.reader.{Entity}CsvItemReader}
   * の {@code resolveNameMapping} メソッドで定義します。
   */
}
