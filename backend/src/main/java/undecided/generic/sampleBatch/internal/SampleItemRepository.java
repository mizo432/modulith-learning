package undecided.generic.sampleBatch.internal;

import org.jspecify.annotations.Nullable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import undecided.generic.sampleBatch.spi.SampleItem;

/** サンプルアイテムの永続化を担うリポジトリインターフェース。 */
@Repository
public interface SampleItemRepository extends CrudRepository<SampleItem, Long> {

  /**
   * アイテムコードで既存データを検索します。
   *
   * @param itemCode アイテムコード
   * @return 該当するエンティティ、不存在の場合は null
   */
  @Nullable
  SampleItem findByItemCode(String itemCode);
}
