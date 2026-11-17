package undecided.generic.largeRecordBatch.internal.batch.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jspecify.annotations.Nullable;

/** 大量レコード処理バッチサンプル用のCSVデータDTOクラス。 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class LargeRecordCsvDto implements Serializable {

  /** レコードコード (例: "REC000001") */
  private @Nullable String recordCode;

  /** レコード名 (例: "サンプルレコード1") */
  private @Nullable String recordName;

  /** カテゴリ (例: "カテゴリA") */
  private @Nullable String category;

  /** サブカテゴリ (例: "サブA1") */
  private @Nullable String subCategory;

  /** 金額 (例: "10000") */
  private @Nullable String amount;

  /** 数量 (例: "100") */
  private @Nullable String quantity;

  /** ステータス (例: "ACTIVE") */
  private @Nullable String status;
}
