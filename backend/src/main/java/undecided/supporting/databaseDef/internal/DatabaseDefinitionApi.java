package undecided.supporting.databaseDef.internal;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.supporting.databaseDef.DatabaseInfo;
import undecided.supporting.databaseDef.SchemaInfo;
import undecided.supporting.databaseDef.TableDefinitionResponse;
import undecided.supporting.databaseDef.TablesAndViewsResponse;
import undecided.supporting.databaseDef.ViewDefinitionResponse;

/**
 * データベース定義に関するREST APIエンドポイントを提供するコントローラーです。
 *
 * <p>以下の機能を提供します:
 *
 * <ul>
 *   <li>F-01: データベース一覧取得 (GET /do/databases)
 *   <li>F-02: スキーマ一覧取得 (GET /do/databases/{databaseName})
 *   <li>F-03: テーブル・View一覧取得 (GET /do/databases/{databaseName}/schemas/{schemaName})
 *   <li>F-04: テーブル定義取得 (GET /do/databases/{databaseName}/schemas/{schemaName}/tables/{tableName})
 *   <li>F-05: View定義取得 (GET /do/databases/{databaseName}/schemas/{schemaName}/views/{viewName})
 * </ul>
 */
@RestController
@RequestMapping("/do/databases")
public class DatabaseDefinitionApi {

  private final DatabaseMetadataService metadataService;

  public DatabaseDefinitionApi(DatabaseMetadataService metadataService) {
    this.metadataService = metadataService;
  }

  /**
   * F-01: 接続可能なデータベースの一覧を取得します。
   *
   * @return データベース情報のリスト
   */
  @GetMapping
  public ResponseEntity<List<DatabaseInfo>> listDatabases() {
    List<DatabaseInfo> databases = metadataService.listDatabases();
    return ResponseEntity.ok(databases);
  }

  /**
   * F-02: 指定されたデータベースのスキーマ一覧を取得します。
   *
   * @param databaseName データベース名
   * @return スキーマ情報のリスト
   */
  @GetMapping("/{databaseName}")
  public ResponseEntity<List<SchemaInfo>> listSchemas(@PathVariable String databaseName) {
    List<SchemaInfo> schemas = metadataService.listSchemas(databaseName);
    return ResponseEntity.ok(schemas);
  }

  /**
   * F-03: 指定されたスキーマのテーブル一覧とView一覧を取得します。
   *
   * @param databaseName データベース名
   * @param schemaName スキーマ名
   * @return テーブル一覧とView一覧を含むレスポンス
   */
  @GetMapping("/{databaseName}/schemas/{schemaName}")
  public ResponseEntity<TablesAndViewsResponse> listTablesAndViews(
      @PathVariable String databaseName, @PathVariable String schemaName) {
    TablesAndViewsResponse response = metadataService.listTablesAndViews(databaseName, schemaName);
    return ResponseEntity.ok(response);
  }

  /**
   * F-04: 指定されたテーブルの定義とインデックス定義を取得します。
   *
   * @param databaseName データベース名
   * @param schemaName スキーマ名
   * @param tableName テーブル名
   * @return テーブル定義レスポンス
   */
  @GetMapping("/{databaseName}/schemas/{schemaName}/tables/{tableName}")
  public ResponseEntity<TableDefinitionResponse> getTableDefinition(
      @PathVariable String databaseName,
      @PathVariable String schemaName,
      @PathVariable String tableName) {
    TableDefinitionResponse response =
        metadataService.getTableDefinition(databaseName, schemaName, tableName);
    return ResponseEntity.ok(response);
  }

  /**
   * F-05: 指定されたViewの定義とインデックス定義を取得します。 マテリアライズドViewの場合はインデックス定義も取得します。
   *
   * @param databaseName データベース名
   * @param schemaName スキーマ名
   * @param viewName View名
   * @return View定義レスポンス
   */
  @GetMapping("/{databaseName}/schemas/{schemaName}/views/{viewName}")
  public ResponseEntity<ViewDefinitionResponse> getViewDefinition(
      @PathVariable String databaseName,
      @PathVariable String schemaName,
      @PathVariable String viewName) {
    ViewDefinitionResponse response =
        metadataService.getViewDefinition(databaseName, schemaName, viewName);
    return ResponseEntity.ok(response);
  }
}
