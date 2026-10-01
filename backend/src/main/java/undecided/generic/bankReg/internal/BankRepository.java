package undecided.generic.bankReg.internal;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import undecided.generic.bankReg.spi.Bank;

/** 金融機関エンティティに対するデータベースアクセスを提供するリポジトリインターフェース。 */
@Repository
public interface BankRepository extends CrudRepository<Bank, String> {

  /**
   * すべての金融機関を金融機関コードの昇順で取得します。
   *
   * @return 金融機関のリスト
   */
  List<Bank> findAllByOrderByBankCodeAsc();

  /**
   * 指定したデータセットID以外で最後に更新された金融機関を削除します。
   *
   * <p>最新のデータセットに含まれなくなった金融機関を取り除くために使用します。
   *
   * @param datasetId 最新のデータセットID
   * @return 削除件数
   */
  @Modifying
  @Query("DELETE FROM Bank b WHERE b.datasetId <> :datasetId")
  int deleteByDatasetIdNot(@Param("datasetId") String datasetId);
}
