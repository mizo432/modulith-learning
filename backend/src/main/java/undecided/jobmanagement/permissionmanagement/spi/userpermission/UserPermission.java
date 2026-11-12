package undecided.jobmanagement.permissionmanagement.spi.userpermission;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import undecided.supporting.snowflake.SnowflakeId;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_permission", schema = "job_management")
@Getter
public class UserPermission {

  @Id
  private SnowflakeId id;

  @Column(name = "user_id", nullable = false, length = 50, unique = true)
  private String userId;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false, length = 20)
  private Role role;

  @Column(name = "company_code", length = 20)
  private String companyCode;

  @Column(name = "store_code", length = 20)
  private String storeCode;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive = true;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "created_by", length = 50)
  private String createdBy;

  // JPA用パッケージプライベートセッター
  void setId(SnowflakeId id) {
    this.id = id;
  }

  void setUserId(String userId) {
    this.userId = userId;
  }

  void setRole(Role role) {
    this.role = role;
  }

  void setCompanyCode(String companyCode) {
    this.companyCode = companyCode;
  }

  void setStoreCode(String storeCode) {
    this.storeCode = storeCode;
  }

  void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }
}
