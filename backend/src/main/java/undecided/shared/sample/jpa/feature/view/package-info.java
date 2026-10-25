/**
 * ViewとMaterialized Viewのパターンを示すサンプルパッケージです。
 *
 * <p>このパッケージには、データベースのViewとMaterialized ViewをJPAエンティティとして
 * マッピングする方法をデモンストレーションするためのクラスが含まれています。
 *
 * <p>主な構成要素:
 * <ul>
 *   <li>{@link SampleEmployeeSummaryView} - Viewをマッピングするサンプルエンティティ</li>
 *   <li>{@link SamplePartySummaryMV} - Materialized Viewをマッピングするサンプルエンティティ</li>
 * </ul>
 *
 * <p>関連するFlywayマイグレーション:
 * <ul>
 *   <li>V20261025133800__add_sample_views.sql</li>
 * </ul>
 *
 * <p>ViewとMaterialized Viewの違い:
 * <ul>
 *   <li>View: 仮想テーブル。都度クエリを実行し、常に最新のデータを返す</li>
 *   <li>Materialized View: 物理的にデータを保存。REFRESHで更新し、高速な読み取りを提供</li>
 * </ul>
 */
package undecided.shared.sample.jpa.feature.view;
