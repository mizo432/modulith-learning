package undecided.supporting.databaseDef.spi;

/** カラム情報を表すDTOです。 */
public record ColumnInfo(
    String columnName,
    String dataType,
    Boolean nullable,
    Boolean primaryKey,
    String defaultValue,
    String comment) {}
