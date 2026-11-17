package undecided.generic.addressReg.internal;

import java.util.List;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import undecided.generic.addressReg.spi.ChouAza;

/** 町字リポジトリインターフェース。 */
@Repository
public interface ChouAzaRepository extends CrudRepository<ChouAza, Long> {

  /**
   * 法定コードと町字コードで町字を検索します。
   *
   * @param lgCode 法定コード (6桁)
   * @param machiazaCode 町字コード (7桁)
   * @return 該当する町字エンティティ、存在しない場合は null
   */
  @Nullable ChouAza findByLgCodeAndMachiazaCode(
      @NonNull String lgCode, @NonNull String machiazaCode);

  /**
   * 市区町村IDで町字一覧を検索します。
   *
   * @param cityId 市区町村ID
   * @return 町字エンティティのリスト
   */
  @NonNull List<ChouAza> findByCityId(@NonNull Long cityId);

  /**
   * 法定コードで町字一覧を検索します。
   *
   * @param lgCode 法定コード (6桁)
   * @return 町字エンティティのリスト
   */
  @NonNull List<ChouAza> findByLgCode(@NonNull String lgCode);
}
