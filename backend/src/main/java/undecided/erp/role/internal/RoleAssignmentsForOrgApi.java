package undecided.erp.role.internal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.erp.role.spi.RoleAssignmentForOrg;

/**
 * RoleAssignmentsForOrgApiは、組織に関連付けられたロールの割り当て情報を取得するためのREST APIコントローラーです。
 *
 * <p>主に以下の機能を提供します: - 指定されたエンドポイントへのリクエストに対して、組織に関連するロール割り当て情報を返却する。
 *
 * <p>アノテーション: - `@RestController`: このクラスがSpring FrameworkにおいてREST APIコントローラーとして動作することを示します。 -
 * `@RequestMapping`: このコントローラーが処理するリクエストの基本URLを定義します。 - `@GetMapping`: HTTP
 * GETリクエストをこのメソッドにマッピングし、そのリクエストに対応する処理を定義します。
 *
 * <p>エンドポイント: - `/api/roleAssignmentsForOrg`: ロール割り当てデータを提供する基本エンドポイントです。
 *
 * <p>戻り値: - `RoleAssignmentForOrg`: 組織に対するロール割り当てを表すオブジェクト。
 */
@RestController
@RequestMapping("/api/roleAssignmentsForOrg")
public class RoleAssignmentsForOrgApi {
  @GetMapping
  RoleAssignmentForOrg get() {
    return new RoleAssignmentForOrg();
  }
}
