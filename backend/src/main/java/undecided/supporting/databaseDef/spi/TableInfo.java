package undecided.supporting.databaseDef.spi;

/** テーブル情報を表すDTOです。 */
public record TableInfo(
    String tableName, String tableKanaName, String tableType, String description) {}
