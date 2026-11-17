/**
 * ViewとMaterialized Viewのパターンを示すサンプルパッケージです。
 *
 * <p>このパッケージには、データベースのViewとMaterialized ViewをJPAエンティティとして
 * マッピングする方法をデモンストレーションするためのクラスが含まれています。
 *
 * <p>ViewとMaterialized Viewのコメントは「和名;情報種類」の書式で設定します。
 * テーブルと同じく、情報種類は以下の4種類から選択します：
 * <ul>
 *   <li>リソース: 独立したエンティティ</li>
 *   <li>対応表: 2つ以上のリソース間の関係性</li>
 *   <li>対照表: コードと名称の対応（マスターデータ）</li>
 *   <li>イベント: システム内で発生する出来事</li>
 * </ul>
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
package com.github.mizo432.shared.sample.jpa.feature.view;
