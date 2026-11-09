package undecided.agiloom.sprintExecution.internal.presentation;

import undecided.supporting.snowflake.SnowflakeId;

/**
 * Sprint関連の共通ユーティリティです。
 */
public final class SprintIdUtils {
    private SprintIdUtils() {
    }

    /**
     * 文字列IDをSnowflakeIdに変換します。
     *
     * @param id 文字列ID
     * @return SnowflakeId
     * @throws IllegalArgumentException IDが数値でない場合
     */
    public static SnowflakeId parseSnowflakeId(String id) {
        try {
            long value = Long.parseLong(id);
            return SnowflakeId.of(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Sprint IDは数値である必要があります: " + id, e);
        }
    }
}
