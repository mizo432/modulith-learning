package undecided.generic.addressReg.internal;

import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import undecided.generic.addressReg.spi.City;

/** 市区町村リポジトリインターフェース。 */
@Repository
public interface CityRepository extends CrudRepository<City, Long> {

  /**
   * 法定コード（全国地方公共団体コード）で市区町村を検索します。
   *
   * @param lgCode 法定コード (6桁)
   * @return 該当する市区町村エンティティ、存在しない場合は null
   */
  City findByLgCode(@NonNull String lgCode);

  /**
   * 都道府県IDで市区町村一覧を検索します。
   *
   * @param prefectureId 都道府県ID
   * @return 市区町村エンティティのリスト
   */
  List<City> findByPrefectureId(@NonNull Long prefectureId);
}
