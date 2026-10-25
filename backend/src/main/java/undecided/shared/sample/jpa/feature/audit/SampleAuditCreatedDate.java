package undecided.shared.sample.jpa.feature.audit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * @CreatedDateパターンを使用した監査フィールドのサンプルエンティティです。
 *
 * <p>Spring Data
 * JPAの監査機能を使用して、作成日時を自動設定します。 @EntityListeners(AuditingEntityListener.class)を付与することで、 @CreatedDateアノテーションが機能します。
 */
@Entity
@Table(name = "sample_audit_created_date")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleAuditCreatedDate {

  @Id private SnowflakeId id;

  @Column(name = "name", nullable = false, length = 50)
  private String name;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "created_by", nullable = false, length = 50)
  private String createdBy;

  public SampleAuditCreatedDate(SnowflakeId id, String name) {
    this.id = id;
    this.name = name;
    this.createdBy = "system";
  }
}
