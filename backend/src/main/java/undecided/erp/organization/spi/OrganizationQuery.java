package undecided.erp.organization.spi;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** 組織情報を取得するためのクエリインターフェースです。 このインターフェースを実装することで、組織のデータにアクセスするための 機能を提供します。 */
public interface OrganizationQuery {

  /**
   * 全ての組織情報を取得します。
   *
   * @return 現在利用可能な全ての組織データのリスト
   */
  List<Organization> findAll();

  /**
   * 指定された UUID に対応する組織情報を検索します。
   *
   * @param id 検索対象の組織を一意に識別するための UUID
   * @return 指定された UUID に対応する組織の情報を含む Optional 存在しない場合は Optional.empty() を返します
   */
  Optional<Organization> findById(UUID id);
}
