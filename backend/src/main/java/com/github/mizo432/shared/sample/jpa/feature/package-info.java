/**
 * JPA機能パターンサンプル実装を提供するパッケージです。
 *
 * <p>このパッケージは、JPAガイドライン第2部に準拠した主要なJPA機能パターンをサンプルとして実装し、 参照コードを提供することを目的としています。
 *
 * <p>カバーするパターン:
 *
 * <ul>
 *   <li>{@link com.github.mizo432.shared.sample.jpa.feature.comment} テーブル/カラムコメント
 *       (@Table.comment, @Column.comment)
 *   <li>{@link com.github.mizo432.shared.sample.jpa.feature.repository} リポジトリバリエーション (CrudRepository,
 *       JpaRepository)
 *   <li>{@link com.github.mizo432.shared.sample.jpa.feature.audit} 監査フィールドパターン
 *       (@CreatedDate, @PrePersist/@PreUpdate)
 *   <li>{@link com.github.mizo432.shared.sample.jpa.feature.query} @Query/@Modifying (カスタムJPQLクエリと更新クエリ)
 *   <li>{@link com.github.mizo432.shared.sample.jpa.feature.search} 検索パターン
 *       (先頭一致、部分一致、ExampleMatcher、Specification)
 *   <li>{@link com.github.mizo432.shared.sample.jpa.feature.lock} @Lock (ペシミスティックロック)
 *   <li>{@link com.github.mizo432.shared.sample.jpa.feature.transaction} @Transactional (トランザクション管理)
 *   <li>{@link com.github.mizo432.shared.sample.jpa.feature.schema} Schema分離パターン (スキーマ分離の実装)
 * </ul>
 */
package com.github.mizo432.shared.sample.jpa.feature;
