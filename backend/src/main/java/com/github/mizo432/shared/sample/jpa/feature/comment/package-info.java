/**
 * テーブルコメントとカラムコメントのサンプルパッケージです。
 *
 * <p>{@code @Table.comment} と {@code @Column.comment} を使用して、「和名;情報種類」の書式で
 * テーブルとカラムの目的を自己文書化するパターンを示します。
 *
 * <p>情報種類は以下の4種類から選択します：
 * <ul>
 *   <li>リソース: 独立したエンティティ</li>
 *   <li>対応表: 2つ以上のリソース間の関係性</li>
 *   <li>対照表: コードと名称の対応（マスターデータ）</li>
 *   <li>イベント: システム内で発生する出来事</li>
 * </ul>
 */
package com.github.mizo432.shared.sample.jpa.feature.comment;
