package com.github.mizo432.shared.entity;

/**
 * 物（Thing）のロールを表すエンティティの基底クラスです。
 *
 * <p>この抽象クラスは PPT（Party, Place, Thing）モデルにおける「物」に付与される
 * ロール（役割）を表すエンティティの基底として機能します。物が持つ役割をモデル化するために
 * 使用されます。
 *
 * <p>{@link RoleEntity} を継承するため、作成日時や作成者などの監査情報を保持します。
 *
 * @param <R> {@code ThingRoleEntity} を拡張する実際のクラス型
 * @see RoleEntity
 */
public abstract class ThingRoleEntity<R extends ThingRoleEntity<R>> extends RoleEntity<R> {}
