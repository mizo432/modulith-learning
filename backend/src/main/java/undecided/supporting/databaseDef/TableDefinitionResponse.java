package undecided.supporting.databaseDef;

import java.util.List;

/** テーブル定義のレスポンスDTOです。 */
public record TableDefinitionResponse(List<ColumnInfo> columns, List<IndexInfo> indexes) {}
