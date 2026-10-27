package undecided.generic.largeRecordBatch.internal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import undecided.generic.largeRecordBatch.spi.LargeRecord;

/** 大量レコード処理バッチサンプル用のリポジトリインターフェース。 */
@Repository
public interface LargeRecordRepository extends CrudRepository<LargeRecord, Long> {
}
