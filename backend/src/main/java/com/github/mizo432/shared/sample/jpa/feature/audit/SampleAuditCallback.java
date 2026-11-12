package com.github.mizo432.shared.sample.jpa.feature.audit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.github.mizo432.shared.common.dateProvider.DateProvider;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * @PrePersist/@PreUpdateパターンを使用した監査フィールドのサンプルエンティティです。
 *
 * <p>ライフサイクルコールバックを使用して、手動で監査フィールドを設定します。 DateProviderを使用することで、テスト時に日時をモック可能になります。
 */
@Entity
@Table(name = "sample_audit_callback")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleAuditCallback {

  @Column(name = "created_at", nullable = false)
  protected LocalDateTime createdAt;

  @Column(name = "created_by", nullable = false, length = 50)
  protected String createdBy;

  @Column(name = "updated_at")
  protected LocalDateTime updatedAt;

  @Column(name = "updated_by", length = 50)
  protected String updatedBy;

  @Id private SnowflakeId id;

  @Column(name = "name", nullable = false, length = 50)
  private String name;

  public SampleAuditCallback(SnowflakeId id, String name) {
    this.id = id;
    this.name = name;
  }

  @PrePersist
  public void prePersist() {
    createdAt = DateProvider.currentLocalDateTime();
    createdBy = "system";
  }

  @PreUpdate
  public void preUpdate() {
    updatedAt = DateProvider.currentLocalDateTime();
    updatedBy = "system";
  }
}
