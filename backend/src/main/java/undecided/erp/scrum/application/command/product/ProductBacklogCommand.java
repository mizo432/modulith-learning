package undecided.erp.scrum.application.command.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.scrum.domain.model.product.Product;
import undecided.erp.scrum.domain.model.product.ProductBacklog;
import undecided.erp.scrum.domain.model.product.ProductBacklogRepository;
import undecided.erp.scrum.domain.model.product.ProductRepository;

/**
 * プロダクトバックログに関連するコマンド操作を提供するサービスクラス。
 *
 * <p>このクラスはプロダクトバックログの作成、更新、削除などの操作を提供します。
 */
@Service
@RequiredArgsConstructor
public class ProductBacklogCommand {

  private final ProductBacklogRepository productBacklogRepository;
  private final ProductRepository productRepository;

  /**
   * 新しいプロダクトバックログを作成します。
   *
   * @param productId プロダクトID
   * @param name プロダクトバックログ名
   * @param description プロダクトバックログの説明
   * @return 作成されたプロダクトバックログ
   */
  @Transactional
  public ProductBacklog createProductBacklog(
      SnowflakeId productId, String name, String description) {
    Product product =
        productRepository
            .findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

    ProductBacklog productBacklog = new ProductBacklog();
    productBacklog.setBacklogId(SnowflakeId.newInstance());
    productBacklog.setProduct(product);
    productBacklog.setName(name);
    productBacklog.setDescription(description);

    return productBacklogRepository.save(productBacklog);
  }

  /**
   * 既存のプロダクトバックログを更新します。
   *
   * @param backlogId プロダクトバックログID
   * @param name プロダクトバックログ名
   * @param description プロダクトバックログの説明
   * @return 更新されたプロダクトバックログ
   */
  @Transactional
  public ProductBacklog updateProductBacklog(
      SnowflakeId backlogId, String name, String description) {
    ProductBacklog productBacklog =
        productBacklogRepository
            .findById(backlogId)
            .orElseThrow(
                () -> new IllegalArgumentException("Product backlog not found: " + backlogId));

    productBacklog.setName(name);
    productBacklog.setDescription(description);

    return productBacklogRepository.save(productBacklog);
  }

  /**
   * 既存のプロダクトバックログを削除します。
   *
   * @param backlogId プロダクトバックログID
   */
  @Transactional
  public void deleteProductBacklog(SnowflakeId backlogId) {
    ProductBacklog productBacklog =
        productBacklogRepository
            .findById(backlogId)
            .orElseThrow(
                () -> new IllegalArgumentException("Product backlog not found: " + backlogId));

    productBacklogRepository.delete(productBacklog);
  }
}
