package undecided.supporting.databaseDef.internal;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;
import org.springframework.stereotype.Service;
import undecided.supporting.databaseDef.spi.ColumnInfo;
import undecided.supporting.databaseDef.spi.DatabaseInfo;
import undecided.supporting.databaseDef.spi.IndexInfo;
import undecided.supporting.databaseDef.spi.SchemaInfo;
import undecided.supporting.databaseDef.spi.TableDefinitionResponse;
import undecided.supporting.databaseDef.spi.TableInfo;
import undecided.supporting.databaseDef.spi.TablesAndViewsResponse;
import undecided.supporting.databaseDef.spi.ViewDefinitionResponse;
import undecided.supporting.databaseDef.spi.ViewInfo;

/**
 * データベースメタデータを取得するサービスです。 JDBCのDatabaseMetaData APIを使用して、データベース・スキーマ・テーブル・Viewの情報を取得します。
 * 非機能要件NFR-DB-001, NFR-DB-002に従い、Caffeineキャッシュを使用して5分間有効なキャッシュを提供します。
 */
@Service
public class DatabaseMetadataService {

  /** キャッシュ有効期間: 5分 (NFR-DB-002) */
  private static final long CACHE_TTL_MINUTES = 5;

  private final DataSource dataSource;
  private final Cache<String, Object> metadataCache;

  public DatabaseMetadataService(DataSource dataSource) {
    this.dataSource = dataSource;
    this.metadataCache =
        Caffeine.newBuilder()
            .expireAfterWrite(CACHE_TTL_MINUTES, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();
  }

  /**
   * 接続可能なデータベースの一覧を取得します (F-01)。 PostgreSQLではカタログ名をデータベース名として扱います。
   *
   * @return データベース情報のリスト
   */
  public List<DatabaseInfo> listDatabases() {
    String cacheKey = "databases";
    @SuppressWarnings("unchecked")
    List<DatabaseInfo> cached = (List<DatabaseInfo>) metadataCache.getIfPresent(cacheKey);
    if (cached != null) {
      return cached;
    }

    List<DatabaseInfo> result;
    try (Connection conn = dataSource.getConnection()) {
      DatabaseMetaData metaData = conn.getMetaData();
      result = new ArrayList<>();

      // PostgreSQLではConnection.getCatalog()で現在のデータベース名を取得
      String currentCatalog = conn.getCatalog();
      if (currentCatalog != null && !currentCatalog.isEmpty()) {
        result.add(
            new DatabaseInfo(
                currentCatalog,
                metaData.getDatabaseProductName() + " " + metaData.getDatabaseProductVersion()));
      }

      // PostgreSQLのpg_databaseから利用可能なデータベース一覧を取得
      try (PreparedStatement ps =
              conn.prepareStatement(
                  "SELECT datname, datdba::regrole AS owner FROM pg_database WHERE datistemplate = false ORDER BY datname");
          ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          String dbName = rs.getString("datname");
          String owner = rs.getString("owner");
          String desc = (owner != null) ? "Owner: " + owner : "";
          result.add(new DatabaseInfo(dbName, desc));
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("データベース一覧の取得に失敗しました: " + e.getMessage(), e);
    }

    metadataCache.put(cacheKey, result);
    return result;
  }

  /**
   * 指定されたデータベースのスキーマ一覧を取得します (F-02)。
   *
   * @param databaseName データベース名
   * @return スキーマ情報のリスト
   */
  public List<SchemaInfo> listSchemas(String databaseName) {
    String cacheKey = "schemas:" + databaseName;
    @SuppressWarnings("unchecked")
    List<SchemaInfo> cached = (List<SchemaInfo>) metadataCache.getIfPresent(cacheKey);
    if (cached != null) {
      return cached;
    }

    List<SchemaInfo> result;
    try (Connection conn = dataSource.getConnection()) {
      DatabaseMetaData metaData = conn.getMetaData();
      result = new ArrayList<>();

      try (ResultSet rs = metaData.getSchemas()) {
        while (rs.next()) {
          String schemaName = rs.getString("TABLE_SCHEM");
          if (schemaName != null
              && !schemaName.startsWith("pg_")
              && !schemaName.equals("information_schema")) {
            // PostgreSQLのコメントを取得
            String description = getSchemaDescription(conn, schemaName);
            String[] parsed = parseDescription(description);
            result.add(
                new SchemaInfo(
                    schemaName,
                    parsed[0], // schemaKanaName: 最初の";"まで
                    parsed[1] // description: ";"以降
                    ));
          }
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("スキーマ一覧の取得に失敗しました: " + e.getMessage(), e);
    }

    metadataCache.put(cacheKey, result);
    return result;
  }

  /**
   * 指定されたスキーマのテーブル一覧とView一覧を取得します (F-03)。
   *
   * @param databaseName データベース名
   * @param schemaName スキーマ名
   * @return テーブル一覧とView一覧を含むレスポンス
   */
  public TablesAndViewsResponse listTablesAndViews(String databaseName, String schemaName) {
    String cacheKey = "tablesViews:" + databaseName + ":" + schemaName;
    @SuppressWarnings("unchecked")
    TablesAndViewsResponse cached = (TablesAndViewsResponse) metadataCache.getIfPresent(cacheKey);
    if (cached != null) {
      return cached;
    }

    TablesAndViewsResponse result;
    try (Connection conn = dataSource.getConnection()) {
      DatabaseMetaData metaData = conn.getMetaData();
      String catalog = conn.getCatalog();
      List<TableInfo> tables = new ArrayList<>();
      List<ViewInfo> views = new ArrayList<>();

      // テーブル一覧を取得
      try (ResultSet rs = metaData.getTables(catalog, schemaName, null, new String[] {"TABLE"})) {
        while (rs.next()) {
          String tableName = rs.getString("TABLE_NAME");
          String description = getTableDescription(conn, schemaName, tableName);
          String[] parsed = parseDescription(description);
          tables.add(
              new TableInfo(
                  tableName,
                  parsed[0], // tableKanaName: 最初の";"まで
                  parsed[1], // tableType: 1番目の";"から2番目の";"まで
                  parsed[2] // description: 2番目の";"から最後
                  ));
        }
      }

      // View一覧を取得 (通常View)
      try (ResultSet rs = metaData.getTables(catalog, schemaName, null, new String[] {"VIEW"})) {
        while (rs.next()) {
          String viewName = rs.getString("TABLE_NAME");
          String description = getViewDescription(conn, schemaName, viewName);
          String[] parsed = parseDescription(description);
          views.add(
              new ViewInfo(
                  viewName,
                  parsed[0], // viewKanaName: 最初の";"まで
                  parsed[1], // viewType: 1番目の";"から2番目の";"まで
                  parsed[2] // description: 2番目の";"から最後
                  ));
        }
      }

      // PostgreSQLのMaterialized Viewも取得
      try (PreparedStatement ps =
              conn.prepareStatement(
                  "SELECT matviewname FROM pg_matviews WHERE schemaname = ? ORDER BY matviewname");
          ResultSet rs = ps.executeQuery()) {
        ps.setString(1, schemaName);
        while (rs.next()) {
          String viewName = rs.getString("matviewname");
          String description = getMatViewDescription(conn, schemaName, viewName);
          String[] parsed = parseDescription(description);
          views.add(new ViewInfo(viewName, parsed[0], parsed[1], parsed[2]));
        }
      }

      result = new TablesAndViewsResponse(tables, views);
    } catch (SQLException e) {
      throw new RuntimeException("テーブル・View一覧の取得に失敗しました: " + e.getMessage(), e);
    }

    metadataCache.put(cacheKey, result);
    return result;
  }

  /**
   * 指定されたテーブルの定義とインデックス定義を取得します (F-04)。
   *
   * @param databaseName データベース名
   * @param schemaName スキーマ名
   * @param tableName テーブル名
   * @return テーブル定義レスポンス
   */
  public TableDefinitionResponse getTableDefinition(
      String databaseName, String schemaName, String tableName) {
    String cacheKey = "tableDef:" + databaseName + ":" + schemaName + ":" + tableName;
    @SuppressWarnings("unchecked")
    TableDefinitionResponse cached = (TableDefinitionResponse) metadataCache.getIfPresent(cacheKey);
    if (cached != null) {
      return cached;
    }

    TableDefinitionResponse result;
    try (Connection conn = dataSource.getConnection()) {
      DatabaseMetaData metaData = conn.getMetaData();
      String catalog = conn.getCatalog();
      List<ColumnInfo> columns = getColumns(metaData, catalog, schemaName, tableName);
      List<IndexInfo> indexes = getIndexes(conn, schemaName, tableName);
      result = new TableDefinitionResponse(columns, indexes);
    } catch (SQLException e) {
      throw new RuntimeException("テーブル定義の取得に失敗しました: " + e.getMessage(), e);
    }

    metadataCache.put(cacheKey, result);
    return result;
  }

  /**
   * 指定されたViewの定義とインデックス定義を取得します (F-05)。 マテリアライズドViewの場合はインデックス定義も取得します。
   *
   * @param databaseName データベース名
   * @param schemaName スキーマ名
   * @param viewName View名
   * @return View定義レスポンス
   */
  public ViewDefinitionResponse getViewDefinition(
      String databaseName, String schemaName, String viewName) {
    String cacheKey = "viewDef:" + databaseName + ":" + schemaName + ":" + viewName;
    @SuppressWarnings("unchecked")
    ViewDefinitionResponse cached = (ViewDefinitionResponse) metadataCache.getIfPresent(cacheKey);
    if (cached != null) {
      return cached;
    }

    ViewDefinitionResponse result;
    try (Connection conn = dataSource.getConnection()) {
      DatabaseMetaData metaData = conn.getMetaData();
      String catalog = conn.getCatalog();
      List<ColumnInfo> columns = getViewColumns(metaData, catalog, schemaName, viewName);

      // マテリアライズドViewの場合はインデックスを取得
      List<IndexInfo> indexes = new ArrayList<>();
      if (isMaterializedView(conn, schemaName, viewName)) {
        indexes = getIndexes(conn, schemaName, viewName);
      }

      result = new ViewDefinitionResponse(columns, indexes);
    } catch (SQLException e) {
      throw new RuntimeException("View定義の取得に失敗しました: " + e.getMessage(), e);
    }

    metadataCache.put(cacheKey, result);
    return result;
  }

  /** テーブルのカラム情報を取得します。 */
  private List<ColumnInfo> getColumns(
      DatabaseMetaData metaData, String catalog, String schemaName, String tableName)
      throws SQLException {
    List<ColumnInfo> columns = new ArrayList<>();

    // 主キー情報を取得
    Map<String, Boolean> primaryKeyMap = new ConcurrentHashMap<>();
    try (ResultSet pkRs = metaData.getPrimaryKeys(catalog, schemaName, tableName)) {
      while (pkRs.next()) {
        primaryKeyMap.put(pkRs.getString("COLUMN_NAME"), true);
      }
    }

    try (ResultSet rs = metaData.getColumns(catalog, schemaName, tableName, null)) {
      while (rs.next()) {
        String columnName = rs.getString("COLUMN_NAME");
        String dataType = rs.getString("TYPE_NAME");
        int nullability = rs.getInt("NULLABLE");
        String defaultValue = rs.getString("COLUMN_DEF");
        int ordinalPosition = rs.getInt("ORDINAL_POSITION");

        // カラムコメントを取得 (PostgreSQL)
        String comment = rs.getString("REMARKS");

        columns.add(
            new ColumnInfo(
                columnName,
                dataType,
                nullability == DatabaseMetaData.columnNullable,
                primaryKeyMap.getOrDefault(columnName, false),
                defaultValue != null && !defaultValue.isEmpty() ? defaultValue : null,
                comment));
      }
    }
    return columns;
  }

  /** Viewのカラム情報を取得します。 */
  private List<ColumnInfo> getViewColumns(
      DatabaseMetaData metaData, String catalog, String schemaName, String viewName)
      throws SQLException {
    List<ColumnInfo> columns = new ArrayList<>();

    try (ResultSet rs = metaData.getColumns(catalog, schemaName, viewName, null)) {
      while (rs.next()) {
        String columnName = rs.getString("COLUMN_NAME");
        String dataType = rs.getString("TYPE_NAME");
        int ordinalPosition = rs.getInt("ORDINAL_POSITION");
        String comment = rs.getString("REMARKS");

        columns.add(new ColumnInfo(columnName, dataType, null, null, null, comment));
      }
    }
    return columns;
  }

  /** テーブル/Viewのインデックス情報を取得します。 */
  private List<IndexInfo> getIndexes(Connection conn, String schemaName, String tableName)
      throws SQLException {
    List<IndexInfo> indexes = new ArrayList<>();

    // PostgreSQLではgetIndexesが不完全な場合があるため、pg_indexesから直接取得
    try (PreparedStatement ps =
            conn.prepareStatement(
                "SELECT i.relname AS index_name, ix.indisunique, "
                    + "       pg_get_indexdef(ix.indexrelid) AS index_def, "
                    + "       c.attname AS column_name, "
                    + "       array_position(ix.indkey, c.attnum) AS ordinal_position "
                    + "FROM pg_index ix "
                    + "JOIN pg_class t ON t.oid = ix.indrelid "
                    + "JOIN pg_class i ON i.oid = ix.indexrelid "
                    + "JOIN pg_namespace n ON n.oid = t.relnamespace "
                    + "CROSS JOIN LATERAL unnest(ix.indkey) WITH ORDINALITY AS k(attnum, ordinality) "
                    + "JOIN pg_attribute c ON c.attrelid = t.oid AND c.attnum = k.attnum "
                    + "WHERE n.nspname = ? AND t.relname = ? "
                    + "ORDER BY i.relname, k.ordinality");
        ResultSet rs = ps.executeQuery()) {
      ps.setString(1, schemaName);
      ps.setString(2, tableName);

      while (rs.next()) {
        String indexName = rs.getString("index_name");
        boolean isUnique = rs.getBoolean("indis_unique");
        String columnName = rs.getString("column_name");
        int ordinalPosition = rs.getInt("ordinality");

        indexes.add(
            new IndexInfo(
                indexName, isUnique ? "UNIQUE" : "NONUNIQUE", columnName, ordinalPosition));
      }
    }
    return indexes;
  }

  /** 指定されたViewがマテリアライズドViewかどうかを確認します。 */
  private boolean isMaterializedView(Connection conn, String schemaName, String viewName)
      throws SQLException {
    try (PreparedStatement ps =
            conn.prepareStatement(
                "SELECT COUNT(*) > 0 FROM pg_matviews WHERE schemaname = ? AND matviewname = ?");
        ResultSet rs = ps.executeQuery()) {
      ps.setString(1, schemaName);
      ps.setString(2, viewName);
      if (rs.next()) {
        return rs.getBoolean(1);
      }
    }
    return false;
  }

  /** スキーマのコメントを取得します (PostgreSQL用)。 */
  private String getSchemaDescription(Connection conn, String schemaName) throws SQLException {
    try (PreparedStatement ps =
            conn.prepareStatement(
                "SELECT obj_description(oid) AS description FROM pg_namespace WHERE nspname = ?");
        ResultSet rs = ps.executeQuery()) {
      ps.setString(1, schemaName);
      if (rs.next()) {
        return rs.getString("description");
      }
    }
    return null;
  }

  /** テーブルのコメントを取得します (PostgreSQL用)。 */
  private String getTableDescription(Connection conn, String schemaName, String tableName)
      throws SQLException {
    try (PreparedStatement ps =
            conn.prepareStatement(
                "SELECT obj_description(oid) AS description FROM pg_class "
                    + "WHERE relname = ? AND relnamespace = (SELECT oid FROM pg_namespace WHERE nspname = ?)");
        ResultSet rs = ps.executeQuery()) {
      ps.setString(1, tableName);
      ps.setString(2, schemaName);
      if (rs.next()) {
        return rs.getString("description");
      }
    }
    return null;
  }

  /** Viewのコメントを取得します (PostgreSQL用)。 */
  private String getViewDescription(Connection conn, String schemaName, String viewName)
      throws SQLException {
    try (PreparedStatement ps =
            conn.prepareStatement(
                "SELECT obj_description(oid) AS description FROM pg_class "
                    + "WHERE relname = ? AND relnamespace = (SELECT oid FROM pg_namespace WHERE nspname = ?)");
        ResultSet rs = ps.executeQuery()) {
      ps.setString(1, viewName);
      ps.setString(2, schemaName);
      if (rs.next()) {
        return rs.getString("description");
      }
    }
    return null;
  }

  /** マテリアライズドViewのコメントを取得します (PostgreSQL用)。 */
  private String getMatViewDescription(Connection conn, String schemaName, String viewName)
      throws SQLException {
    try (PreparedStatement ps =
            conn.prepareStatement(
                "SELECT obj_description(oid) AS description FROM pg_class "
                    + "WHERE relname = ? AND relnamespace = (SELECT oid FROM pg_namespace WHERE nspname = ?)");
        ResultSet rs = ps.executeQuery()) {
      ps.setString(1, viewName);
      ps.setString(2, schemaName);
      if (rs.next()) {
        return rs.getString("description");
      }
    }
    return null;
  }

  /**
   * セミコロン区切りの説明文字列を解析します。 仕様: 最初の";"までがkanaName、1番目の";"から2番目の";"までがtype、2番目の";"以降がdescription
   *
   * @param description セミコロン区切りの説明文字列
   * @return [kanaName, type, description] の配列
   */
  private String[] parseDescription(String description) {
    if (description == null || description.isEmpty()) {
      return new String[] {"", "", ""};
    }

    String[] parts = description.split(";", -1);
    String kanaName = parts.length > 0 ? parts[0].trim() : "";
    String type = parts.length > 1 ? parts[1].trim() : "";
    String desc =
        parts.length > 2
            ? String.join(";", java.util.Arrays.copyOfRange(parts, 2, parts.length)).trim()
            : "";

    return new String[] {kanaName, type, desc};
  }
}
