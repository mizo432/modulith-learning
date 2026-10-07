package undecided.generic.bankReg.internal;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import undecided.generic.bankReg.spi.branch.Branch;

/** 支店エンティティに対するデータベースアクセスを提供するリポジトリインターフェース。 */
@Repository
public interface BranchRepository extends CrudRepository<Branch, Branch.BranchId> {

  /**
   * 指定した金融機関コードに属するすべての支店を支店コードの昇順で取得します。
   *
   * @param bankCode 金融機関コード（4桁）
   * @return 支店のリスト
   */
  List<Branch> findByBankCodeOrderByBranchCodeAsc(@Param("bankCode") String bankCode);

  /**
   * 指定したデータセットID以外で最後に更新された支店を削除します。
   *
   * <p>最新のデータセットに含まれなくなった支店を取り除くために使用します。
   *
   * @param datasetId 最新のデータセットID
   * @return 削除件数
   */
  @Modifying
  @Query("DELETE FROM Branch b WHERE b.datasetId <> :datasetId")
  int deleteByDatasetIdNot(@Param("datasetId") String datasetId);
}
