package undecided.shared.sample.jpa.feature.dbobject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * SampleSynonymエンティティのリポジトリインターフェースです。
 *
 * <p>シノニム経由でアクセスする読み取り専用エンティティの操作を提供します。
 *
 * @see <a href="https://github.com/mizo432/modulith-learning/issues/277">Issue #277</a>
 */
@Repository
public interface SampleSynonymRepository extends JpaRepository<SampleSynonym, Long> {
}
