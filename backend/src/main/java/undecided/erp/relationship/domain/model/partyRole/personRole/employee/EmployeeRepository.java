package undecided.erp.relationship.domain.model.partyRole.personRole.employee;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Employeeエンティティのデータベース操作を処理するためのリポジトリインターフェースです。
 * <p>
 * このインターフェースは、基本的なCRUD操作（作成、読み取り、更新、削除）に加え、 Employeeエンティティに対するページングやソート機能を提供します。 Spring Data
 * JPAが提供するPagingAndSortingRepositoryを継承しており、 データベースとのやり取りを行うための汎用メソッドを利用することができます。
 * <p>
 * このリポジトリが管理するエンティティはEmployeeクラスに対応しており、 このクラスは「relationship」スキーマ内の「employees」テーブルにマッピングされています。
 * <p>
 * このリポジトリは、Long型のユニークIDで識別されるEmployeeエンティティを操作します。
 */
@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

  Employee deleteEmployeeByEmployeeId(Long employeeId);
}
