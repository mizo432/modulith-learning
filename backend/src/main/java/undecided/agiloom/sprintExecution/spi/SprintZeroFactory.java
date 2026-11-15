package undecided.agiloom.sprintExecution.spi;

import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.NonNull;

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
    Optional<Sprint> createSprintZero(@NonNull UUID productId, @NonNull UUID teamId, @NonNull String teamName);
}
