package undecided.agiloom.sprintExecution.spi;

import java.util.Optional;
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * スプリント0を作成するためのファクトリインターフェースです。
 * 他のモジュールからスプリント0を作成する際に使用します。
 */
public interface SprintZeroFactory {

    /**
     * スプリント0を作成します。
     * 既にスプリント0が存在する場合は作成しません。
     *
     * @param productId 関連する製品のID
     * @param teamId スクラムチームのID
     * @param teamName スクラムチームの名前
     * @return 作成されたスプリント0（既に存在する場合は既存のスプリント0）
     */
    Optional<Sprint> createSprintZero(@NonNull SnowflakeId productId, @NonNull SnowflakeId teamId, @NonNull String teamName);
}
