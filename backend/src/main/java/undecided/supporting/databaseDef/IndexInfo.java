package undecided.supporting.databaseDef;

/** インデックス情報を表すDTOです。 */
public record IndexInfo(
    String indexName, String indexType, String columnName, Integer ordinalPosition) {}
