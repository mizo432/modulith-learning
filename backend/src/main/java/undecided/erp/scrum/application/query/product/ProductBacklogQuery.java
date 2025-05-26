package undecided.erp.scrum.application.query.product;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.scrum.domain.model.product.Product;
import undecided.erp.scrum.domain.model.product.ProductBacklog;
import undecided.erp.scrum.domain.model.product.ProductBacklogRepository;
import undecided.erp.scrum.domain.model.product.ProductRepository;

/**
 * プロダクトバックログに関連するクエリ操作を提供するサービスクラス。
 *
 * <p>このクラスはプロダクトバックログの検索や取得などの操作を提供します。
 */
@Service
@RequiredArgsConstructor
public class ProductBacklogQuery {

  private final ProductBacklogRepository productBacklogRepository;
  private final ProductRepository productRepository;

  /**
   * 指定されたプロダクトバックログIDに対応するプロダクトバックログを取得します。
   *
   * @param backlogId プロダクトバックログID
   * @return プロダクトバックログのOptional
   */
  @Transactional(readOnly = true)
  public Optional<ProductBacklog> findById(SnowflakeId backlogId) {
    return productBacklogRepository.findById(backlogId);
  }

  /**
   * 指定された名前を持つプロダクトバックログを検索します。
   *
   * @param name 検索対象の名前
   * @return プロダクトバックログのOptional
   */
  @Transactional(readOnly = true)
  public Optional<ProductBacklog> findByName(String name) {
    return productBacklogRepository.findByName(name);
  }

  /**
   * 指定された名前を含むプロダクトバックログを検索します。
   *
   * @param name 検索対象の名前の一部
   * @return プロダクトバックログのリスト
   */
  @Transactional(readOnly = true)
  public List<ProductBacklog> findByNameContaining(String name) {
    return productBacklogRepository.findByNameContaining(name);
  }

  /**
   * 指定されたプロダクトに関連するすべてのプロダクトバックログを取得します。
   *
   * @param productId プロダクトID
   * @return プロダクトバックログのリスト
   */
  @Transactional(readOnly = true)
  public List<ProductBacklog> findByProductId(SnowflakeId productId) {
    Product product =
        productRepository
            .findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
    return productBacklogRepository.findByProduct(product);
  }

  /**
   * すべてのプロダクトバックログを取得します。
   *
   * @return プロダクトバックログのリスト
   */
  @Transactional(readOnly = true)
  public List<ProductBacklog> findAll() {
    return productBacklogRepository.findAll();
  }
}
