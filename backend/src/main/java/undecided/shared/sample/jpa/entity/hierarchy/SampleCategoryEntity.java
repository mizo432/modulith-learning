package undecided.shared.sample.jpa.entity.hierarchy;

import jakarta.persistence.MappedSuperclass;

/**
 * 分類用抽象クラスのサンプルです。
 *
 * <p>具体的なフィールドを持たず、型分類のみを行う抽象クラスを使用して、 エンティティ階層を整理するパターンを示します。
 *
 * <p>継承階層:
 *
 * <pre>
 * SampleBusinessEntity
 *   └── SampleCategoryEntity (分類用抽象クラス)
 *       └── SampleProductEntity (具体的なエンティティ)
 * </pre>
 */
@MappedSuperclass
public abstract class SampleCategoryEntity<E extends SampleCategoryEntity<E>>
    extends SampleBusinessEntity<E> {

  // 具体的なフィールドを持たず、型分類のみを行う
}
