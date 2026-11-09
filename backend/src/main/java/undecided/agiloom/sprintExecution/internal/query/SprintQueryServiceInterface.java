package undecided.agiloom.sprintExecution.internal.query;

import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Pageable;
import undecided.agiloom.sprintExecution.spi.SprintDTO;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * Sprintクエリサービスのインターフェースです。
 */
public interface SprintQueryServiceInterface {

  /**
   * IDでSprintを検索します。
   *
   * @param id SprintのID
   * @return 検索結果
   */
  Optional<SprintDTO> findById(@NonNull SnowflakeId id);

  /**
   * ページネーション付きで全Sprintを取得します。
   *
   * @param pageable ページネーション情報
   * @return Sprintリスト
   */
  List<SprintDTO> findAll(Pageable pageable);

  /**
   * ステータスでSprintを検索します。
   *
   * @param status ステータス
   * @return Sprintリスト
   */
  List<SprintDTO> findByStatus(@NonNull SprintStatus status);
}
