package com.github.mizo432.shared.sample.jpa.feature.flyway;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * FlywayとJPAの連携パターンを示すサンプルエンティティです。
 *
 * <p>このエンティティは、Flywayマイグレーションスクリプト（V20261025132200__add_flyway_sample_table.sql）
 * で作成されたテーブルにマッピングされます。
 *
 * <p>主な連携パターン:
 * <ul>
 *   <li>FlywayがDDL（テーブル作成、カラム定義、インデックス、コメント）を管理</li>
 *   <li>JPAエンティティがFlywayで作成されたテーブルにマッピング</li>
 *   <li>ddl-auto: none でJPAによるDDL生成を無効化</li>
 *   <li>デフォルト値はFlyway側で定義（DEFAULT 0, DEFAULT CURRENT_TIMESTAMP）</li>
 * </ul>
 */
@Entity
@Table(schema = SchemaSampleFlyway.NAME, name = "sample_flyway_entities")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleFlywayEntity {

  /** アイテムのステータスを表す列挙型 */
  public enum ItemStatus {
    ACTIVE,
    INACTIVE
  }

  @Id
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "item_name", nullable = false, length = 100)
  private String itemName;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  @Column(name = "status", nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  private ItemStatus status;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  /**
   * サンプルエンティティを作成します。
   *
   * @param id エンティティの一意の識別子
   * @param itemName アイテム名
   * @param quantity 数量（nullの場合、データベースのデフォルト値0が適用されます）
   * @param status ステータス
   */
  public SampleFlywayEntity(Long id, String itemName, Integer quantity, ItemStatus status) {
    this.id = id;
    this.itemName = itemName;
    this.quantity = quantity;
    this.status = status;
  }
}
