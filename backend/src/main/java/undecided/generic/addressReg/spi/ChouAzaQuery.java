package undecided.generic.addressReg.spi;

import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;

/** 町字情報を取得するためのクエリインターフェース。 */
public interface ChouAzaQuery {

  /**
   * 指定された法定コードおよび町字コードに一致するChouAzaエンティティを検索します。
   *
   * @param lgCode 市区町村を識別するための法定コード
   * @param machiazaCode 町字コード
   * @return 該当するChouAzaエンティティのOptional
   */
  @NonNull Optional<ChouAza> findByLgCodeAndMachiazaCode(
      @NonNull String lgCode, @NonNull String machiazaCode);

  /**
   * 指定された市区町村IDに属する町字一覧を検索します。
   *
   * @param cityId 市区町村ID
   * @return 町字エンティティのリスト
   */
  @NonNull List<ChouAza> findByCityId(@NonNull Long cityId);

  /**
   * 指定された法定コードに属する町字一覧を検索します。
   *
   * @param lgCode 法定コード
   * @return 町字エンティティのリスト
   */
  @NonNull List<ChouAza> findByLgCode(@NonNull String lgCode);
}
