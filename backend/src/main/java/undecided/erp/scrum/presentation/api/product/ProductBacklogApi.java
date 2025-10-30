package undecided.erp.scrum.presentation.api.product;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.scrum.application.command.product.ProductBacklogCommand;
import undecided.erp.scrum.application.query.product.ProductBacklogQuery;
import undecided.erp.scrum.domain.model.product.ProductBacklog;

/** プロダクトバックログに関連するAPIエンドポイントを提供するコントローラークラス。 */
@RestController
@RequestMapping("/api/product-backlogs")
@RequiredArgsConstructor
public class ProductBacklogApi {

  private final ProductBacklogCommand productBacklogCommand;
  private final ProductBacklogQuery productBacklogQuery;

  /**
   * 新しいプロダクトバックログを作成します。
   *
   * @param request プロダクトバックログ作成リクエスト
   * @return 作成されたプロダクトバックログ
   */
  @PostMapping
  public ResponseEntity<ProductBacklog> createProductBacklog(
      @RequestBody CreateProductBacklogRequest request) {
    ProductBacklog productBacklog =
        productBacklogCommand.createProductBacklog(
            request.getProductId(), request.getName(), request.getDescription());
    return ResponseEntity.status(HttpStatus.CREATED).body(productBacklog);
  }

  /**
   * 既存のプロダクトバックログを更新します。
   *
   * @param backlogId プロダクトバックログID
   * @param request プロダクトバックログ更新リクエスト
   * @return 更新されたプロダクトバックログ
   */
  @PutMapping("/{backlogId}")
  public ResponseEntity<ProductBacklog> updateProductBacklog(
      @PathVariable("backlogId") SnowflakeId backlogId,
      @RequestBody UpdateProductBacklogRequest request) {
    ProductBacklog productBacklog =
        productBacklogCommand.updateProductBacklog(
            backlogId, request.getName(), request.getDescription());
    return ResponseEntity.ok(productBacklog);
  }

  /**
   * 既存のプロダクトバックログを削除します。
   *
   * @param backlogId プロダクトバックログID
   * @return 空のレスポンス
   */
  @DeleteMapping("/{backlogId}")
  public ResponseEntity<Void> deleteProductBacklog(
      @PathVariable("backlogId") SnowflakeId backlogId) {
    productBacklogCommand.deleteProductBacklog(backlogId);
    return ResponseEntity.noContent().build();
  }

  /**
   * 指定されたプロダクトバックログIDに対応するプロダクトバックログを取得します。
   *
   * @param backlogId プロダクトバックログID
   * @return プロダクトバックログ
   */
  @GetMapping("/{backlogId}")
  public ResponseEntity<ProductBacklog> getProductBacklog(
      @PathVariable("backlogId") SnowflakeId backlogId) {
    return productBacklogQuery
        .findById(backlogId)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * 指定されたプロダクトに関連するすべてのプロダクトバックログを取得します。
   *
   * @param productId プロダクトID
   * @return プロダクトバックログのリスト
   */
  @GetMapping("/by-product/{productId}")
  public ResponseEntity<List<ProductBacklog>> getProductBacklogsByProduct(
      @PathVariable("productId") SnowflakeId productId) {
    List<ProductBacklog> productBacklogs = productBacklogQuery.findByProductId(productId);
    return ResponseEntity.ok(productBacklogs);
  }

  /**
   * すべてのプロダクトバックログを取得します。
   *
   * @return プロダクトバックログのリスト
   */
  @GetMapping
  public ResponseEntity<List<ProductBacklog>> getAllProductBacklogs() {
    List<ProductBacklog> productBacklogs = productBacklogQuery.findAll();
    return ResponseEntity.ok(productBacklogs);
  }

  /** プロダクトバックログ作成リクエスト。 */
  @Setter
  @Getter
  public static class CreateProductBacklogRequest {
    private SnowflakeId productId;
    private String name;
    private String description;
  }

  /** プロダクトバックログ更新リクエスト。 */
  @Setter
  @Getter
  public static class UpdateProductBacklogRequest {
    private String name;
    private String description;
  }
}
