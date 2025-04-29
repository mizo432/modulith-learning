package undecided.erp.scrum.application.command.product;

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
public class ProductCommand {

  private final ProductRepository productRepository;

  /**
   * すべてのプロダクトを取得します。
   *
   * @return すべてのプロダクトのリスト
   */
  @Transactional(readOnly = true)
  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }

  /**
   * 指定されたIDのプロダクトを取得します。
   *
   * @param productId 取得するプロダクトのID
   * @return 指定されたIDのプロダクト（存在しない場合は空のOptional）
   */
  @Transactional(readOnly = true)
  public Optional<Product> getProductById(SnowflakeId productId) {
    return productRepository.findById(productId);
  }

  /**
   * 指定された名前のプロダクトを取得します。
   *
   * @param name 取得するプロダクトの名前
   * @return 指定された名前のプロダクト（存在しない場合は空のOptional）
   */
  @Transactional(readOnly = true)
  public Optional<Product> getProductByName(String name) {
    return productRepository.findByName(name);
  }

  /**
   * 新しいプロダクトを作成します。 プロダクトオーナーが指定されていない場合は例外をスローします。
   *
   * @param product 作成するプロダクト
   * @return 作成されたプロダクト
   * @throws IllegalArgumentException プロダクトオーナーが指定されていない場合
   */
  public Product createProduct(Product product) {
    if (product.getProductOwner() == null) {
      throw new IllegalArgumentException("プロダクトオーナーを指定する必要があります。");
    }
    return productRepository.save(product);
  }

  /**
   * 指定されたIDのプロダクトを更新します。 Productクラスにはsetterがないため、新しいインスタンスを作成して置き換えます。
   *
   * @param productId 更新するプロダクトのID
   * @param product 更新内容を含むプロダクト
   * @return 更新されたプロダクト（存在しない場合は空のOptional）
   */
  public Optional<Product> updateProduct(SnowflakeId productId, Product product) {
    return productRepository.findById(productId)
        .map(existingProduct -> {
          // 既存のプロダクトを削除して新しいプロダクトを作成
          productRepository.delete(existingProduct);

          // 新しいプロダクトを作成して保存
          Product newProduct = new Product(
              productId,
              product.getName() != null ? product.getName() : existingProduct.getName(),
              product.getVision() != null ? product.getVision() : existingProduct.getVision(),
              product.getDescription() != null ? product.getDescription()
                  : existingProduct.getDescription(),
              product.getProductOwner() != null ? product.getProductOwner()
                  : existingProduct.getProductOwner()
          );

          return productRepository.save(newProduct);
        });
  }

  /**
   * 指定されたIDのプロダクトを削除します。
   *
   * @param productId 削除するプロダクトのID
   */
  public void deleteProduct(SnowflakeId productId) {
    productRepository.deleteById(productId);
  }

  /**
   * 指定された名前を含むプロダクトを検索します。
   *
   * @param name 検索対象の名前の一部
   * @return 指定された名前を含むプロダクトのリスト
   */
  @Transactional(readOnly = true)
  public List<Product> searchProductsByName(String name) {
    return productRepository.findByNameContaining(name);
  }
}
