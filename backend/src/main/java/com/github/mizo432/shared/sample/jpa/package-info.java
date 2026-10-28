/**
 * JPA Entity基本パターンのサンプル実装を提供するパッケージです。
 *
 * <p>このパッケージは、JPAガイドライン第1部に準拠した主要なJPAパターンを サンプルとして実装し、参照コードを提供することを目的としています。
 *
 * <p>カバーするパターン:
 *
 * <ul>
 *   <li>{@link com.github.mizo432.shared.sample.jpa.entity.hierarchy} Entity階層構造 (@MappedSuperclass)
 *   <li>{@link com.github.mizo432.shared.sample.jpa.entity.relationship} 関連付け
 *       (@OneToMany, @ManyToOne, @ManyToMany, @OneToOne)
 *   <li>{@link com.github.mizo432.shared.sample.jpa.entity.inheritance} 継承戦略 (SINGLE_TABLE, JOINED,
 *       TABLE_PER_CLASS)
 *   <li>{@link com.github.mizo432.shared.sample.jpa.entity.embeddable} 埋め込み可能クラス (@Embeddable/@Embedded)
 *   <li>{@link com.github.mizo432.shared.sample.jpa.entity.converter} カスタム変換 (@Converter)
 *   <li>{@link com.github.mizo432.shared.sample.jpa.value} ValueObjectとIdValueパターン
 * </ul>
 */
package com.github.mizo432.shared.sample.jpa;
