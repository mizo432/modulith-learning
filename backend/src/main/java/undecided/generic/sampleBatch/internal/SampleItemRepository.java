package undecided.generic.sampleBatch.internal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import undecided.generic.sampleBatch.spi.SampleItem;

/** サンプルアイテムの永続化を担うリポジトリインターフェース。 */
@Repository
public interface SampleItemRepository extends CrudRepository<SampleItem, Long> {
}
