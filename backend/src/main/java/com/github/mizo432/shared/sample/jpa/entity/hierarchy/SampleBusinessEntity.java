package com.github.mizo432.shared.sample.jpa.entity.hierarchy;

import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Immutable;

/**
 * サンプル用のビジネスエンティティ基底クラスです。
 *
 * <p>{@code @MappedSuperclass}を使用して、サブクラスで共通のフィールドを継承します。 自己参照型ジェネリクスを使用して、サブクラスでの型安全性を確保します。
 *
 * <p>特徴:
 *
 * <ul>
 *   <li>自己参照型ジェネリクス ({@code E extends SampleBusinessEntity<E>})
 *   <li>{@code @Immutable}で不変性を保証
 *   <li>{@code @CreatedDate}で作成日時の自動設定
 * </ul>
 *
 * @param <E> このクラスを拡張するエンティティのタイプ
 */
@MappedSuperclass
@Getter
@Setter
@Immutable
public abstract class SampleBusinessEntity<E extends SampleBusinessEntity<E>> {

  /** エンティティが作成された時刻を示すタイムスタンプです。 {@code @CreatedDate}アノテーションにより、エンティティ作成時に自動的に設定されます。 */
  @CreatedDate private LocalDateTime createdAt;

  /** エンティティを作成したユーザーの識別子です。 */
  private Long createdBy;
}
