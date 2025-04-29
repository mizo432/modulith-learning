package undecided.erp.scrum.presentation.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.scrum.application.command.product.ProductCommand;
import undecided.erp.scrum.domain.model.product.Product;

/**
 * プロダクトに関連するREST APIエンドポイントを提供するコントローラークラス。
 * <p>
 * このコントローラーは、プロダクトの作成、取得、更新、削除などの操作に関するエンドポイントを提供します。 プロダクトの作成、更新、削除はアドミンユーザーのみが実行できます。
 * <p>
 * 注意: 現在の実装ではセキュリティチェックは行われていません。実際の環境では、 Spring Securityなどを使用して適切な認証・認可を実装する必要があります。
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductApi {

  private final ProductCommand productCommand;

  /**
   * すべてのプロダクトを取得します。
   *
   * @return すべてのプロダクトのリスト
   */
  @GetMapping
  public ResponseEntity<List<Product>> getAllProducts() {
    return ResponseEntity.ok(productCommand.getAllProducts());
  }

  /**
   * 指定されたIDのプロダクトを取得します。
   *
   * @param productId 取得するプロダクトのID
   * @return 指定されたIDのプロダクト（存在しない場合は404）
   */
  @GetMapping("/{productId}")
  public ResponseEntity<Product> getProductById(@PathVariable String productId) {
    return productCommand.getProductById(SnowflakeId.of(Long.parseLong(productId)))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * 新しいプロダクトを作成します。 このエンドポイントはアドミンユーザーのみが実行できます。 注意: 現在の実装ではセキュリティチェックは行われていません。
   * プロダクトオーナーを指定する必要があります。
   *
   * @param product 作成するプロダクト
   * @return 作成されたプロダクト、またはエラーレスポンス
   */
  @PostMapping
  public ResponseEntity<?> createProduct(@RequestBody Product product) {
    try {
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(productCommand.createProduct(product));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
   * 指定されたIDのプロダクトを更新します。 このエンドポイントはアドミンユーザーのみが実行できます。 注意: 現在の実装ではセキュリティチェックは行われていません。
   *
   * @param productId 更新するプロダクトのID
   * @param product 更新内容を含むプロダクト
   * @return 更新されたプロダクト（存在しない場合は404）
   */
  @PutMapping("/{productId}")
  public ResponseEntity<Product> updateProduct(
      @PathVariable String productId,
      @RequestBody Product product) {

    return productCommand.updateProduct(SnowflakeId.of(Long.parseLong(productId)), product)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * 指定されたIDのプロダクトを削除します。 このエンドポイントはアドミンユーザーのみが実行できます。
   * 注意: 現在の実装ではセキュリティチェックは行われていません。
   *
   * @param productId 削除するプロダクトのID
   * @return 削除成功時は204、存在しない場合は404
   */
  @DeleteMapping("/{productId}")
  public ResponseEntity<Void> deleteProduct(@PathVariable String productId) {
    try {
      productCommand.deleteProduct(SnowflakeId.of(Long.parseLong(productId)));
      return ResponseEntity.noContent().build();
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * 指定された名前を含むプロダクトを検索します。
   *
   * @param name 検索対象の名前の一部
   * @return 指定された名前を含むプロダクトのリスト
   */
  @GetMapping("/search")
  public ResponseEntity<List<Product>> searchProductsByName(@RequestParam String name) {
    return ResponseEntity.ok(productCommand.searchProductsByName(name));
  }
}
