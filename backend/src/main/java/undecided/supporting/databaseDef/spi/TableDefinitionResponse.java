package undecided.supporting.databaseDef.spi;

import java.util.List;

/** テーブル定義のレスポンスDTOです。 */
public record TableDefinitionResponse(List<ColumnInfo> columns, List<IndexInfo> indexes) {}
