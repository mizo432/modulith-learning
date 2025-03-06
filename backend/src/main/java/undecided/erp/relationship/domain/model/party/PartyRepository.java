package undecided.erp.relationship.domain.model.party;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.erp.common.entity.SnowflakeId;

/**
 * PartyRepositoryは、Partyエンティティに対するデータアクセス操作を提供するリポジトリインターフェースです。
 * JpaRepositoryを拡張しており、CRUD操作やページング、ソートなどの基本的なデータアクセス機能を利用できます。
 * <p>
 * このリポジトリインターフェースでは、標準的なCRUD機能に加えて、特定の検索基準に基づくカスタムクエリメソッドを提供します。
 */
@Repository
public interface PartyRepository extends JpaRepository<Party, SnowflakeId> {

  /**
   * 指定された名前以前の検索条件に一致するPartyオブジェクトのリストを取得します。
   *
   * @param searchName 検索条件として使用される名前。この名前より前に一致するデータを検索します。
   * @return 条件に一致するPartyオブジェクトのリスト。条件に一致する結果がない場合は空のリストを返します。
   */
  List<Party> findBySearchNameBefore(String searchName);


}
