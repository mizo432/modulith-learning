package undecided.generic.sampleBatch.internal.batch.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jspecify.annotations.Nullable;

/** バッチ基本構成サンプル用のCSVデータDTOクラス。 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SampleItemCsvDto implements Serializable {

  /** アイテムコード (例: "ITEM001") */
  private @Nullable String itemCode;

  /** アイテム名 (例: "サンプル商品A") */
  private @Nullable String itemName;

  /** カテゴリ (例: "食品") */
  private @Nullable String category;

  /** 価格 (例: "1000") */
  private @Nullable String price;
}
