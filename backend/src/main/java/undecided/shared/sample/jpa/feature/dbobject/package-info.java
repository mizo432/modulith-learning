/**
 * DBオブジェクト（シーケンス、シノニム、ストアドプロシージャ、ストアドファンクション）のパターンを示すサンプルパッケージです。
 *
 * <p>このパッケージには、データベースのオブジェクトをJPAで操作する方法をデモンストレーションするための
 * クラスが含まれています。
 *
 * <p>主な構成要素:
 * <ul>
 *   <li>{@link undecided.shared.sample.jpa.feature.dbobject.SampleSequence} - データベースシーケンスのサンプル</li>
 *   <li>{@link undecided.shared.sample.jpa.feature.dbobject.SampleSynonym} - データベースシノニムのサンプル</li>
 *   <li>{@link undecided.shared.sample.jpa.feature.dbobject.SampleProcedure} - ストアドプロシージャのサンプル</li>
 *   <li>{@link undecided.shared.sample.jpa.feature.dbobject.SampleFunction} - ストアドファンクションのサンプル</li>
 * </ul>
 *
 * <p>関連するFlywayマイグレーション:
 * <ul>
 *   <li>V20261025140000__add_sample_db_objects.sql</li>
 * </ul>
 *
 * <p>DBオブジェクトの種類:
 * <ul>
 *   <li>シーケンス: 一意の番号を生成するためのデータベースオブジェクト</li>
 *   <li>シノニム: オブジェクトの別名を提供するためのデータベースオブジェクト</li>
 *   <li>ストアドプロシージャ: 事前にコンパイルされたSQL文の集合</li>
 *   <li>ストアドファンクション: 値を返すストアドプロシージャ</li>
 * </ul>
 */
package undecided.shared.sample.jpa.feature.dbobject;
