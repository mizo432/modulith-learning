package undecided.association.relationshipMgmt.partyRole.internal.role;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.association.relationshipMgmt.partyRole.spi.dto.RoleDTO;

/**
 * RoleApiは、ロール情報を管理するためのREST APIコントローラーです。
 *
 * <p>主に以下の機能を提供します: - 指定されたエンドポイントへのリクエストに対して、ロール情報を返却します。
 *
 * <p>アノテーション: - `@RestController`: Spring FrameworkにおけるREST APIコントローラーとしての役割を示します。 -
 * `@RequestMapping`: 基本URLを設定し、このコントローラーが処理するリクエストを定義します。 - `@GetMapping`: HTTP
 * GETリクエストをこのメソッドにマッピングします。
 *
 * <p>エンドポイント: - `/api/roles`: 基本URLにマッピングされており、ロールデータを返却するために使用されます。
 *
 * <p>戻り値: - `RoleDTO`: システムで管理されるロールを表すDTO。
 */
@RestController
@RequestMapping("/api/roles")
public class RoleApi {
  @GetMapping
  RoleDTO get() {
    return new RoleDTO(
        null,
        "",
        "",
        "",
        null,
        null,
        null);
  }
}
