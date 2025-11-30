package undecided.erp.shared.address.spi;

import java.util.Optional;
import org.jspecify.annotations.NonNull;

public interface PrefectureQuery {

  /**
   * 指定された都道府県コードに一致するPrefectureエンティティを検索します。
   *
   * @param code 都道府県を識別するための一意のコード
   * @return 検索されたPrefectureエンティティをOptionalでラップしたものを返します。 該当するエンティティがない場合は空のOptionalを返します。
   */
  @NonNull Optional<Prefecture> findByCode(@NonNull String code);
}
