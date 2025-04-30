package undecided.erp.scrum.application.query.poduct;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.scrum.domain.model.product.Product;
import undecided.erp.scrum.domain.model.product.ProductRepository;

/**
 * プロダクトに関連するコマンド操作を提供するサービスクラス。
 * <p>
 * このクラスはプロダクトの作成、更新、削除などの操作を担当します。
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ProductQuery {

  private final ProductRepository productRepository;

  /**
   * すべてのプロダクトを取得します。
   *
   * @return すべてのプロダクトのリスト
   */
  @Transactional(readOnly = true)
  public List<Product> selectAllProducts() {
    return productRepository.findAll();
  }

  /**
   * 指定されたIDのプロダクトを取得します。
   *
   * @param productId 取得するプロダクトのID
   * @return 指定されたIDのプロダクト（存在しない場合は空のOptional）
   */
  @Transactional(readOnly = true)
  public Optional<Product> findProductById(SnowflakeId productId) {
    return productRepository.findById(productId);
  }

  /**
   * 指定された名前を含むプロダクトを検索します。
   *
   * @param name 検索対象の名前の一部
   * @return 指定された名前を含むプロダクトのリスト
   */
  @Transactional(readOnly = true)
  public List<Product> selectProductsByName(String name) {
    return productRepository.findByNameContaining(name);
  }
}
