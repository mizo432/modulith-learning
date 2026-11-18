package undecided.association.relationshipMgmt.partyRole.internal.role;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.supporting.event.AdminEmployeeChanged;
import undecided.association.relationshipMgmt.partyRole.spi.role.RoleAssignmentForEmp;
import undecided.supporting.snowflake.SnowflakeId;

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

  private final ApplicationEventPublisher eventPublisher;

  RoleAssignmentsForEmpApi(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  @GetMapping
  RoleAssignmentForEmp get() {
    SnowflakeId employeeId = SnowflakeId.of(1L);
    RoleAssignmentForEmp roleAssignment =
        RoleAssignmentForEmp.create(
            employeeId,
            SnowflakeId.of(2L),
            SnowflakeId.of(3L));

    // ADMINロールの付与を他事業領域に通知する
    eventPublisher.publishEvent(new AdminEmployeeChanged(employeeId, true));

    return roleAssignment;
  }
}
