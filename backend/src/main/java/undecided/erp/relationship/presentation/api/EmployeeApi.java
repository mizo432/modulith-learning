package undecided.erp.relationship.presentation.api;

import static undecided.erp.common.primitive.Lists2.newArrayList;

import org.springdoc.core.parsers.ReturnTypeParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import undecided.erp.relationship.business.command.employee.CreateEmployeeCommand;
import undecided.erp.relationship.business.command.employee.DropEmployeeCommand;
import undecided.erp.relationship.business.command.employee.UpdateEmployeeCommand;
import undecided.erp.relationship.business.query.employee.EmployeeQuery;
import undecided.erp.relationship.domain.model.partyRole.employee.Employee;

/**
 * 従業員関連の操作を管理するためのREST APIコントローラークラスです。 このクラスは、従業員に対するCRUD（作成、読み取り、更新、削除）操作を実行するためのエンドポイントを提供します。
 */
@RestController
@RequestMapping("relationship/api/v1/employees")
public class EmployeeApi {

  private final EmployeeQuery employeeQuery;

  private final CreateEmployeeCommand createEmployeeCommand;

  private final UpdateEmployeeCommand updateEmployeeCommand;

  private final DropEmployeeCommand dropEmployeeCommand;
  private final ReturnTypeParser returnTypeParser;

  /**
   * 指定されたクエリおよびコマンドハンドラーを使用して、EmployeeApiのインスタンスを構築します。
   *
   * @param employeeQuery 従業員データを取得するために使用されるクエリコンポーネント。
   * @param createEmployeeCommand 従業員データを作成するためのコマンドコンポーネント。
   * @param updateEmployeeCommand 従業員データを更新するためのコマンドコンポーネント。
   * @param dropEmployeeCommand 従業員データを削除するためのコマンドコンポーネント。
   */
  public EmployeeApi(EmployeeQuery employeeQuery, CreateEmployeeCommand createEmployeeCommand,
      UpdateEmployeeCommand updateEmployeeCommand, DropEmployeeCommand dropEmployeeCommand,
      ReturnTypeParser returnTypeParser) {
    this.employeeQuery = employeeQuery;
    this.createEmployeeCommand = createEmployeeCommand;
    this.updateEmployeeCommand = updateEmployeeCommand;
    this.dropEmployeeCommand = dropEmployeeCommand;
    this.returnTypeParser = returnTypeParser;
  }

  /**
   * 指定された従業員IDに基づいて従業員の詳細情報を取得します。
   *
   * @param employeeId 取得対象の従業員の一意の識別子。
   * @return 従業員オブジェクトを含むResponseEntity（従業員が見つかった場合）、 または適切なHTTPレスポンス（従業員が見つからない場合）。
   */
  @GetMapping("/{id}")
  public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Long employeeId) {
    return ResponseEntity.of(employeeQuery.findByEmployeeId(employeeId));

  }

  /**
   * 指定された検索条件に基づいて従業員の一覧を取得します。
   *
   * @param employeeCriteria 従業員をフィルタリングするために使用される検索条件。 この条件には、検索の種類や関連するパラメーターが含まれます。
   * @return 指定された条件に一致する従業員オブジェクトの反復可能なコレクションを含むResponseEntity。
   */
  @GetMapping
  public ResponseEntity<Iterable<Employee>> getEmployees(
      @RequestParam EmployeeCriteria employeeCriteria) {
    return switch (employeeCriteria.searchType()) {
      case BY_NAME, BY_ID -> ResponseEntity.ok(newArrayList());
      case BY_INITIALS -> ResponseEntity.ok(
          employeeQuery.findByInitials("Initials"));
    };


  }

  /**
   * 新しい従業員リソースを作成します。
   *
   * @param employee 作成する従業員オブジェクト。
   * @param uriComponentsBuilder 作成されたリソースのURIを構築するために使用されるUriComponentsBuilder。
   * @return 作成されたリソースの場所情報を含むHTTPレスポンスを表すResponseEntity。
   */
  @PostMapping
  public ResponseEntity<Employee> postEmployee(@RequestBody Employee employee,
      UriComponentsBuilder uriComponentsBuilder) {
    Employee created = createEmployeeCommand.execute(employee);
    return ResponseEntity.created(uriComponentsBuilder.path("/{id}").build(created.getEmployeeId()))
        .build();

  }

  /**
   * 既存の従業員リソースの詳細を更新します。
   *
   * @param employeeId 更新対象の従業員のID。
   * @param employee 更新後の従業員の詳細。
   * @return 操作が成功したことを示すResponseEntity（レスポンスボディには内容が含まれません）。
   */
  @PutMapping("/{id}")
  public ResponseEntity<Employee> putEmployee(@PathVariable("id") Long employeeId,
      @RequestBody Employee employee) {
    updateEmployeeCommand.execute(employeeId, employee);
    return ResponseEntity.noContent().build();

  }

  /**
   * 指定されたIDに基づいて従業員リソースを削除します。
   *
   * @param employeeId 削除対象の従業員のID。
   * @return 操作が成功したことを示す内容なしのResponseEntity（レスポンスボディは含まれません）。
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Long employeeId) {
    dropEmployeeCommand.execute(employeeId);
    return ResponseEntity.noContent().build();

  }

}
