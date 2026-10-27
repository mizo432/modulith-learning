package undecided.supporting.databaseDef;

import java.util.List;

/** View定義のレスポンスDTOです。 */
public record ViewDefinitionResponse(List<ColumnInfo> columns, List<IndexInfo> indexes) {}
