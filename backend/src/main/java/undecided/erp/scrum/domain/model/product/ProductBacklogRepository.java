package undecided.erp.scrum.domain.model.product;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.erp.common.entity.SnowflakeId;

/**
 * プロダクトバックログに関連するデータアクセス操作を提供するリポジトリインターフェース。
 *
 * <p>このインターフェースはSpring Data JPAのJpaRepositoryを拡張し、 プロダクトバックログエンティティに対する標準的なCRUD操作と、
 * カスタムクエリメソッドを提供します。
 */
@Repository
public interface ProductBacklogRepository extends JpaRepository<ProductBacklog, SnowflakeId> {

  /**
   * 指定された名前を持つプロダクトバックログを検索します。
   *
   * @param name 検索対象の名前
   * @return 指定された名前を持つプロダクトバックログのOptional
   */
  Optional<ProductBacklog> findByName(String name);

  /**
   * 指定された名前を含むプロダクトバックログを検索します。
   *
   * @param name 検索対象の名前の一部
   * @return 指定された名前を含むプロダクトバックログのリスト
   */
  List<ProductBacklog> findByNameContaining(String name);

  /**
   * 指定されたプロダクトに関連するすべてのプロダクトバックログを取得します。
   *
   * @param product 検索対象のプロダクト
   * @return 指定されたプロダクトに関連するプロダクトバックログのリスト
   */
  List<ProductBacklog> findByProduct(Product product);

  /**
   * 指定されたプロダクトIDに関連するすべてのプロダクトバックログを取得します。
   *
   * @param productId 検索対象のプロダクトID
   * @return 指定されたプロダクトIDに関連するプロダクトバックログのリスト
   */
  List<ProductBacklog> findByProductProductId(SnowflakeId productId);
}
