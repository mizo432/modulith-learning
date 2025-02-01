package undecided.erp.relationship.domain.model.partyRole.orgRole.myCompany;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import undecided.erp.common.entity.SnowflakeId;

/**
 * MyCompanyエンティティを管理するためのリポジトリインターフェース。
 * <p>
 * このインターフェースは、MyCompanyエンティティに対するCRUD（作成、読み取り、更新、削除）操作を提供し、
 * また、会社の一意の識別子を用いて会社を削除するためのカスタム操作も提供します。
 */
@Repository
public interface MyCompanyRepository extends CrudRepository<MyCompany, SnowflakeId> {

}
