/**
 * FlywayとJPAの連携パターンを示すサンプルパッケージです。
 *
 * <p>このパッケージには、FlywayマイグレーションスクリプトとJPAエンティティの連携方法を
 * デモンストレーションするためのクラスが含まれています。
 *
 * <p>主な構成要素:
 * <ul>
 *   <li>{@link SchemaSampleFlyway} - スキーマ名定義</li>
 *   <li>{@link SampleFlywayEntity} - Flywayで作成されたテーブルにマッピングするエンティティ</li>
 *   <li>{@link SampleFlywayEntityRepository} - Spring Data JPAリポジトリ</li>
 * </ul>
 *
 * <p>関連するFlywayマイグレーション:
 * <ul>
 *   <li>V20261025132200__add_flyway_sample_table.sql</li>
 * </ul>
 */
package undecided.shared.sample.jpa.feature.flyway;
