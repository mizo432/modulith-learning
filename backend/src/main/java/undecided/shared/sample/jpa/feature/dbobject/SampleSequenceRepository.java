package undecided.shared.sample.jpa.feature.dbobject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * SampleSequenceエンティティのリポジトリインターフェースです。
 *
 * <p>シーケンスで生成されたIDを持つエンティティのCRUD操作を提供します。
 *
 * @see <a href="https://github.com/mizo432/modulith-learning/issues/277">Issue #277</a>
 */
@Repository
public interface SampleSequenceRepository extends JpaRepository<SampleSequence, Long> {
}
