package undecided.supporting.databaseDef.spi;

import java.util.List;

/** テーブル・View一覧のレスポンスDTOです。 */
public record TablesAndViewsResponse(List<TableInfo> tables, List<ViewInfo> views) {}
