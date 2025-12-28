package undecided.erp.role.internal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.erp.role.spi.RoleAssignmentForEmp;

/**
 * RoleAssignmentsForEmpApiは、従業員に関連付けられたロールの情報を取得するためのREST APIコントローラーです。
 *
 * <p>主に以下の機能を提供します: - 指定されたエンドポイントへのリクエストに対して、従業員のロール割り当て情報を返却する。
 *
 * <p>アノテーション: - `@RestController`: Spring Frameworkにおいて、REST APIコントローラーとしての役割を示します。 -
 * `@RequestMapping`: このコントローラーが処理するリクエストの基本URLを定義します。 - `@GetMapping`: HTTP
 * GETリクエストをマッピングし、そのリクエストに対応する処理を指定します。
 *
 * <p>エンドポイント: - `/api/roleAssignmentsFor`: 基本URLにマッピングされており、ロール割り当てデータを返却します。
 *
 * <p>戻り値: - `RoleAssignmentForEmp`: 従業員に対するロール割り当てを表すオブジェクト。
 */
@RestController
@RequestMapping("/api/roleAssignmentsFor")
public class RoleAssignmentsForEmpApi {
  @GetMapping
  RoleAssignmentForEmp get() {
    return new RoleAssignmentForEmp();
  }
}
