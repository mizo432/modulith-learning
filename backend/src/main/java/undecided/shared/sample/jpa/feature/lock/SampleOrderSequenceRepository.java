package undecided.shared.sample.jpa.feature.lock;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @Lock（ペシミスティックロック）の使用パターンを示すサンプルリポジトリです。
 *
 * <p>並行アクセス時のデータ整合性を保証するために、 @Lock(LockModeType.PESSIMISTIC_WRITE)で排他ロックを取得します。
 */
@Repository
public interface SampleOrderSequenceRepository extends JpaRepository<SampleOrderSequence, Long> {

  /**
   * 順序名でペシミスティックロック付きで順序を取得します。
   *
   * <p>このメソッドはトランザクション内で呼び出す必要があります。 ロックにより、他のトランザクションが同じ行を更新できなくなります。
   *
   * @param sequenceName 順序名
   * @return 一致する順序が存在する場合、そのOptional
   */
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT s FROM SampleOrderSequence s WHERE s.sequenceName = :sequenceName")
  Optional<SampleOrderSequence> findBySequenceNameWithLock(
      @Param("sequenceName") String sequenceName);

  /**
   * IDでペシミスティックロック付きで順序を取得します。
   *
   * @param id 順序ID
   * @return 一致する順序が存在する場合、そのOptional
   */
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT s FROM SampleOrderSequence s WHERE s.id = :id")
  Optional<SampleOrderSequence> findByIdWithLock(@Param("id") Long id);
}
