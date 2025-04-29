package undecided.erp.scrum.domain.model.product;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.erp.common.entity.SnowflakeId;

/**
 * プロダクトに関連するデータアクセス操作を提供するリポジトリインターフェース。
 * <p>
 * このインターフェースはSpring Data JPAのJpaRepositoryを拡張し、 プロダクトエンティティに対する標準的なCRUD操作と、 カスタムクエリメソッドを提供します。
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, SnowflakeId> {

  /**
   * 指定された名前を持つプロダクトを検索します。
   *
   * @param name 検索対象の名前
   * @return 指定された名前を持つプロダクトのOptional
   */
  Optional<Product> findByName(String name);

  /**
   * 指定された名前を含むプロダクトを検索します。
   *
   * @param name 検索対象の名前の一部
   * @return 指定された名前を含むプロダクトのリスト
   */
  List<Product> findByNameContaining(String name);

  /**
   * 指定されたビジョンを含むプロダクトを検索します。
   *
   * @param vision 検索対象のビジョンの一部
   * @return 指定されたビジョンを含むプロダクトのリスト
   */
  List<Product> findByVisionContaining(String vision);

  /**
   * 指定された説明を含むプロダクトを検索します。
   *
   * @param description 検索対象の説明の一部
   * @return 指定された説明を含むプロダクトのリスト
   */
  List<Product> findByDescriptionContaining(String description);

  /**
   * 指定されたプロダクトIDとプロダクト名を持つプロダクトを検索します。
   *
   * @param productId 検索対象のプロダクトID
   * @param name 検索対象のプロダクト名
   * @return 指定されたプロダクトIDとプロダクト名を持つプロダクトのOptional
   */
  Optional<Product> findByProductIdAndName(SnowflakeId productId, String name);
}
