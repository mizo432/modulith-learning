package undecided.erp.shared.supporting.snowflake.internal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(schema = "idMgmt", name = "snowflakeNodes")
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SnowflakeNode {

  @Id
  @Getter
  private Integer nodeId;
  @Getter

  private String instanceName;
  @Getter
  private LocalDateTime leaseUntil;

  public void renewP(LocalDateTime leaseUntil) {
    this.leaseUntil = leaseUntil;
  }

  public SnowflakeNode renew(LocalDateTime leaseUntil) {
    return new SnowflakeNode(nodeId, instanceName, leaseUntil);
  }
}
