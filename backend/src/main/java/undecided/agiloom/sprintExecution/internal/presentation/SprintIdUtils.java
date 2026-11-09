package undecided.agiloom.sprintExecution.internal.presentation;

import com.github.mizo432.shared.exception.BusinessException;
import com.github.mizo432.shared.message.ResultMessage;
import com.github.mizo432.shared.message.ResultMessages;
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
     * @throws BusinessException IDが数値でない場合
     */
    public static SnowflakeId parseSnowflakeId(String id) {
        try {
            long value = Long.parseLong(id);
            return SnowflakeId.of(value);
        } catch (NumberFormatException e) {
            throw new BusinessException(ResultMessages.error().add(ResultMessage.fromText("Sprint IDは数値である必要があります: " + id)), e);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ResultMessages.error().add(ResultMessage.fromText("Sprint IDは正の値である必要があります: " + id)), e);
        }
    }
}
