package undecided.supporting.databaseDef;

/** テーブル情報を表すDTOです。 */
public record TableInfo(
    String tableName, String tableKanaName, String tableType, String description) {}
