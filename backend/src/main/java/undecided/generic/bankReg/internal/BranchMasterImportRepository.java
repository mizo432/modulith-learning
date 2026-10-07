package undecided.generic.bankReg.internal;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/** 支店マスタ取り込みイベントに対するデータベースアクセスを提供するリポジトリインターフェース。 */
@Repository
public interface BranchMasterImportRepository extends CrudRepository<BranchMasterImport, String> {

  /**
   * 最後に完了した取り込みイベントを取得します。
   *
   * @return 最新の取り込みイベントの Optional
   */
  Optional<BranchMasterImport> findTopByOrderByOccurredAtDesc();
}
