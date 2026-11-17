package com.github.mizo432.shared.sample.jpa.entity.hierarchy;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import lombok.Getter;
import com.github.mizo432.shared.common.dateProvider.DateProvider;

/**
 * サンプル用の監査リソース基底クラスです。
 *
 * <p>{@code @PrePersist} / {@code @PreUpdate}ライフサイクルコールバックを使用して、 手動で監査フィールドを設定するパターンを示します。
 *
 * <p>特徴:
 *
 * <ul>
 *   <li>{@code @PrePersist} / {@code @PreUpdate}で作成・更新の両方を追跡
 *   <li>{@link DateProvider}を使用して、テスト時に日時をモック可能に
 *   <li>作成者・更新者は固定値 "system"を使用
 * </ul>
 */
@MappedSuperclass
@Getter
public abstract class SampleAuditResource {

  /** エンティティの作成者を表します。 */
  protected String createdBy;

  /** エンティティの生成日時を保持します。 */
  protected LocalDateTime createdAt;

  /** エンティティの更新者を表します。 */
  protected String updatedBy;

  /** エンティティの更新日時を保持します。 */
  protected LocalDateTime updatedAt;

  /** エンティティが永続化される前に呼び出されるコールバックメソッドです。 作成日時と作成者を自動的に設定します。 */
  @PrePersist
  public void prePersist() {
    createdAt = DateProvider.currentLocalDateTime();
    createdBy = "system";
  }

  /** エンティティの更新が行われる直前に呼び出されるコールバックメソッドです。 更新日時および更新者を自動的に設定します。 */
  @PreUpdate
  public void preUpdate() {
    updatedAt = DateProvider.currentLocalDateTime();
    updatedBy = "system";
  }
}
