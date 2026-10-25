/**
 * ValueObjectとIdValueパターンのサンプルです。
 *
 * <p>値オブジェクトの共通インターフェースや、 分散ID生成パターン（SnowflakeId, UUID v7）の使用方法を示します。
 *
 * <p>実際の実装は以下のパッケージに存在します:
 *
 * <ul>
 *   <li>{@link undecided.shared.entity.ValueObject} - 値オブジェクト共通インターフェース
 *   <li>{@link undecided.shared.entity.IdValue} - ID値オブジェクト基底インターフェース
 *   <li>{@link undecided.supporting.snowflake.SnowflakeId} - Snowflake ID実装
 *   <li>{@link undecided.shared.entity.UuidV7IdValue} - UUID v7 ID実装
 * </ul>
 */
package undecided.shared.sample.jpa.value;
