/**
 * ValueObjectとIdValueパターンのサンプルです。
 *
 * <p>値オブジェクトの共通インターフェースや、 分散ID生成パターン（SnowflakeId, UUID v7）の使用方法を示します。
 *
 * <p>実際の実装は以下のパッケージに存在します:
 *
 * <ul>
 *   <li>{@link com.github.mizo432.shared.entity.ValueObject} - 値オブジェクト共通インターフェース
 *   <li>{@link com.github.mizo432.shared.entity.IdValue} - ID値オブジェクト基底インターフェース
 *   <li>{@link undecided.supporting.snowflake.SnowflakeId} - Snowflake ID実装
 *   <li>{@link com.github.mizo432.shared.entity.UuidV7IdValue} - UUID v7 ID実装
 * </ul>
 */
package com.github.mizo432.shared.sample.jpa.value;
