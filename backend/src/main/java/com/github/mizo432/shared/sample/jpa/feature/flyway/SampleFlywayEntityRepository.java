package com.github.mizo432.shared.sample.jpa.feature.flyway;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Flyway・DDL連携サンプル用のリポジトリです。
 *
 * <p>このリポジトリは、Flywayマイグレーションで作成されたテーブルにアクセスするための
 * Spring Data JPAリポジトリインターフェースです。
 */
@Repository
public interface SampleFlywayEntityRepository extends JpaRepository<SampleFlywayEntity, Long> {

  /**
   * ステータスでエンティティを検索します。
   *
   * @param status 検索するステータス
   * @return 一致するエンティティのリスト
   */
  List<SampleFlywayEntity> findByStatus(SampleFlywayEntity.ItemStatus status);

  /**
   * アイテム名でエンティティを検索します（部分一致）。
   *
   * @param itemName 検索するアイテム名（部分一致）
   * @return 一致するエンティティのリスト
   */
  @Query("SELECT e FROM SampleFlywayEntity e WHERE e.itemName LIKE %:itemName%")
  List<SampleFlywayEntity> findByItemNameContaining(@Param("itemName") String itemName);
}
