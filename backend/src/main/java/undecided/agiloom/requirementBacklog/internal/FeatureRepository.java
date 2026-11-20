package undecided.agiloom.requirementBacklog.internal;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import undecided.agiloom.requirementBacklog.spi.Feature;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * フィーチャーリポジトリ。
 */
public interface FeatureRepository extends JpaRepository<Feature, SnowflakeId> {

  /**
   * スクラムチームIDでフィーチャー一覧を取得します。
   *
   * @param scrumTeamId スクラムチームID
   * @return フィーチャー一覧
   */
  @Query("SELECT f FROM Feature f WHERE f.scrumTeamId = :scrumTeamId")
  List<Feature> findByScrumTeamId(@Param("scrumTeamId") SnowflakeId scrumTeamId);
}
