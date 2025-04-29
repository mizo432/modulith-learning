package undecided.erp.scrum.domain.model.sprint;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import undecided.erp.common.entity.SnowflakeId;

/**
 * スプリントに関連するデータアクセス操作を提供するリポジトリインターフェース。
 * <p>
 * このインターフェースはSpring Data JPAのJpaRepositoryを拡張し、 スプリントエンティティに対する標準的なCRUD操作と、 カスタムクエリメソッドを提供します。
 */
@Repository
public interface SprintRepository extends JpaRepository<Sprint, SnowflakeId> {

  /**
   * 指定されたプロダクトIDに関連するすべてのスプリントを取得します。
   *
   * @param productId 検索対象のプロダクトID
   * @return 指定されたプロダクトIDに関連するスプリントのリスト
   */
  List<Sprint> findByProductProductId(SnowflakeId productId);

  /**
   * 指定された名前を持つスプリントを検索します。
   *
   * @param name 検索対象の名前
   * @return 指定された名前を持つスプリントのOptional
   */
  Optional<Sprint> findByName(String name);

  /**
   * 指定された名前を含むスプリントを検索します。
   *
   * @param name 検索対象の名前の一部
   * @return 指定された名前を含むスプリントのリスト
   */
  List<Sprint> findByNameContaining(String name);

  /**
   * 指定された開始日と終了日の間に実施されるスプリントを検索します。
   *
   * @param startDate 検索対象の開始日
   * @param endDate 検索対象の終了日
   * @return 指定された期間内のスプリントのリスト
   */
  List<Sprint> findByStartDateGreaterThanEqualAndEndDateLessThanEqual(
      LocalDate startDate, LocalDate endDate);

  /**
   * 現在進行中のスプリントを検索します。
   *
   * @param currentDate 現在の日付
   * @return 現在進行中のスプリントのリスト
   */
  List<Sprint> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(
      LocalDate currentDate, LocalDate currentDate2);

  /**
   * 指定されたプロダクトIDに関連する現在進行中のスプリントを検索します。
   *
   * @param productId 検索対象のプロダクトID
   * @param currentDate 現在の日付
   * @return 指定されたプロダクトIDに関連する現在進行中のスプリントのリスト
   */
  List<Sprint> findByProductProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
      SnowflakeId productId, LocalDate currentDate, LocalDate currentDate2);

  /**
   * 指定されたエピックIDに関連するスプリントを検索します。
   * <p>
   * このクエリは、指定されたエピックに関連付けられたユーザーストーリーのタスクが 割り当てられているスプリントを検索します。
   *
   * @param epicId 検索対象のエピックID
   * @return 指定されたエピックIDに関連するスプリントのリスト
   */
  @Query("SELECT DISTINCT s FROM Sprint s JOIN Task t ON t.sprint.sprintId = s.sprintId " +
      "JOIN t.userStory us JOIN us.epic e WHERE e.epicId = :epicId")
  List<Sprint> findByEpicId(@Param("epicId") SnowflakeId epicId);

  /**
   * 複数のエピックにまたがるスプリントを検索します。
   * <p>
   * このクエリは、2つ以上の異なるエピックに関連付けられたユーザーストーリーのタスクが 割り当てられているスプリントを検索します。
   *
   * @return 複数のエピックにまたがるスプリントのリスト
   */
  @Query("SELECT DISTINCT s FROM Sprint s JOIN Task t ON t.sprint.sprintId = s.sprintId " +
      "JOIN t.userStory us JOIN us.epic e " +
      "GROUP BY s.sprintId " +
      "HAVING COUNT(DISTINCT e.epicId) > 1")
  List<Sprint> findSprintsSpanningMultipleEpics();
}
