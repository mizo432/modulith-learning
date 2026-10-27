package undecided.generic.addressReg.spi;

import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;

/** 市区町村情報を取得するためのクエリインターフェース。 */
public interface CityQuery {

  /**
   * 指定された法定コード（全国地方公共団体コード）に一致するCityエンティティを検索します。
   *
   * @param lgCode 市区町村を識別するための法定コード
   * @return 該当するCityエンティティのOptional
   */
  @NonNull Optional<City> findByLgCode(@NonNull String lgCode);

  /**
   * 指定された都道府県IDに属する市区町村一覧を検索します。
   *
   * @param prefectureId 都道府県ID
   * @return 市区町村エンティティのリスト
   */
  @NonNull List<City> findByPrefectureId(@NonNull Long prefectureId);
}
